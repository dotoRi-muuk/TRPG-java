package main.secondary.rogue;

import main.Result;
import main.Stat;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class NinjaTest {

    @Test
    void strikeUsesUnifiedFormulaWithDiceModifierAndIdeology300() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.strike(100, false, true, "none", 0, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertTrue(result.damageDealt() > 0);
        assertEquals(-2, result.staminaUsed());
        assertTrue(log.contains("이념 봉인 스킬 적용: 데미지 +300%"));
        assertTrue(log.contains("주사위 보정 배율"));
        assertTrue(log.contains("데미지 계산(스킬)"));
    }

    @Test
    void flowCatchReturnsThreeStatBuffBasedOnReflexCount() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.flowCatch(4, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertEquals(-6, result.manaUsed());
        assertEquals(0, result.damageDealt());
        assertEquals(Map.of(
                Stat.STRENGTH, 4,
                Stat.DEXTERITY, 4,
                Stat.SPEED, 4
        ), result.statChanges());
        assertTrue(log.contains("다음 3턴 동안 힘/민첩/신속 +4"));
        assertTrue(log.contains("쿨타임 10턴"));
    }

    @Test
    void cloneEnhanceIsNoTurnBuffWithManaAndCooldownLog() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.cloneEnhance(out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertEquals(-1, result.manaUsed());
        assertEquals(0, result.damageDealt());
        assertTrue(log.contains("!턴 소모 없음!"));
        assertTrue(log.contains("쿨타임 2턴"));
    }

    @Test
    void strikeDiceModifierUsesD20RollRange() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Ninja.strike(100, false, false, "none", 0, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        Matcher matcher = Pattern.compile("주사위 보정 배율 : 1 \\+ max\\(0, (\\d+)\\) \\* 0\\.1").matcher(log);
        assertTrue(matcher.find());
        int diceRoll = Integer.parseInt(matcher.group(1));
        assertTrue(diceRoll >= 1 && diceRoll <= 20);
    }
}
