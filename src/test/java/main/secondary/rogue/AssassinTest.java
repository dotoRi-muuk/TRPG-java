package main.secondary.rogue;

import main.Main;
import main.Result;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AssassinTest {

    @Test
    void fatalMomentStartsOnceAndIgnoresControl() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Assassin.FatalMomentState started = Assassin.startFatalMoment(Assassin.FatalMomentState.idle(), out);
        assertTrue(started.active());
        assertTrue(started.usedInBattle());
        assertEquals(4, started.turnsRemaining());
        assertTrue(Assassin.canActInFatalMoment(true, true, started, out));

        Assassin.FatalMomentState second = Assassin.startFatalMoment(started, out);
        assertEquals(started, second);
    }

    @Test
    void fatalMomentTurnSequenceWorks() {
        PrintStream out = System.out;
        Assassin.FatalMomentState state = Assassin.startFatalMoment(Assassin.FatalMomentState.idle(), out);

        Assassin.FatalMomentTurnResult turn1 = Assassin.triggerFatalMomentTurn(state, 0, out);
        assertEquals(4, turn1.additionalAttacks());
        state = turn1.state();

        Assassin.FatalMomentTurnResult turn2 = Assassin.triggerFatalMomentTurn(state, 9, out);
        assertEquals(9, turn2.healAmount());
        state = turn2.state();

        Assassin.FatalMomentTurnResult turn3 = Assassin.triggerFatalMomentTurn(state, 0, out);
        assertEquals(5, turn3.additionalActionsAfterAllActed());
        state = turn3.state();

        state = Assassin.accumulateFatalMomentDamage(state, 37);
        Assassin.FatalMomentTurnResult turn4 = Assassin.triggerFatalMomentTurn(state, 0, out);
        assertEquals(37, turn4.delayedAoEDamage());
        assertFalse(turn4.state().active());
        assertEquals(0, turn4.state().turnsRemaining());
    }

    @Test
    void fatalMomentTechniqueIsRestrictedToActiveState() {
        Result fail = Assassin.unus(Assassin.FatalMomentState.idle(), 100, 0, System.out);
        assertFalse(fail.succeeded());

        Assassin.FatalMomentState state = Assassin.startFatalMoment(Assassin.FatalMomentState.idle(), System.out);
        Result ok = Assassin.decem(state, 100, 0, System.out);
        assertTrue(ok.succeeded());
        assertTrue(ok.damageDealt() > 0);
    }

    @Test
    void throwingMarkFlowSkillsWork() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result throwing = Assassin.throwing(100, 2, out);
        assertTrue(throwing.succeeded());
        assertEquals(3, throwing.manaUsed());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("적 수비 효과를 발동시키지 않음"));

        Result premeditatedFail = Assassin.premeditatedAssassination(false, out);
        assertFalse(premeditatedFail.succeeded());
        Result premeditatedOk = Assassin.premeditatedAssassination(true, out);
        assertTrue(premeditatedOk.succeeded());
        assertTrue(premeditatedOk.damageDealt() >= 1 && premeditatedOk.damageDealt() <= 80);

        Result namelessFail = Assassin.namelessSlash(4, out);
        assertFalse(namelessFail.succeeded());
        Result namelessOk = Assassin.namelessSlash(5, out);
        assertTrue(namelessOk.succeeded());
        assertEquals(6, namelessOk.manaUsed());
        assertEquals(1, namelessOk.staminaUsed());

        Result throwingBoosted = Assassin.throwing(100, 6, namelessOk.staminaUsed(), out);
        assertTrue(throwingBoosted.succeeded());
        assertEquals(8, throwingBoosted.manaUsed());
    }

    @Test
    void finisherRequiresTenMarksAndUsesTotalMarks() {
        Result fail = Assassin.finisher(9, new int[]{9, 1, 0, 0}, System.out);
        assertFalse(fail.succeeded());

        Result ok = Assassin.finisher(10, new int[]{3, 2, 0, 5}, System.out);
        assertTrue(ok.succeeded());
        assertTrue(ok.damageDealt() >= 1);
        assertEquals(0, ok.manaUsed());
    }

    @Test
    void skillDamageFormulaUsesRequiredPercentOrder() {
        int damage = Main.calculateSkillDamage(100, 50, 120, 1.3, System.out);
        assertEquals(234, damage);
    }

    @Test
    void fatalMomentTurnSelectionWorksWithExtraDamageOnTurnFour() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result turn1 = Assassin.fatalMoment(100, 0, 1, 0, out);
        assertTrue(turn1.succeeded());
        assertTrue(turn1.damageDealt() > 0);
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("시간 사이를 꿰뚫어"));

        baos.reset();
        Result turn2 = Assassin.fatalMoment(100, 0, 2, 0, out);
        assertTrue(turn2.succeeded());
        assertEquals(0, turn2.damageDealt());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("뚫린 찰나를 뛰어"));

        baos.reset();
        Result turn3 = Assassin.fatalMoment(100, 0, 3, 0, out);
        assertTrue(turn3.succeeded());
        assertTrue(turn3.damageDealt() > 0);
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("처음부터 아무것도 있지 않았던것 처럼"));

        int extraDamage = 77;
        baos.reset();
        Result turn4 = Assassin.fatalMoment(100, 0, 4, extraDamage, out);
        assertTrue(turn4.succeeded());
        assertTrue(turn4.damageDealt() >= extraDamage);
        String log = baos.toString(StandardCharsets.UTF_8);
        assertTrue(log.contains("죽음으로 매꾸리라"));
        assertTrue(log.contains("추가 데미지 적용: +" + extraDamage));
    }
}
