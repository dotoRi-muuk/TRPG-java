package main.secondary.rogue;

import main.Result;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AssassinTest {

    @Test
    void momentaryFatalityAppliesTurnEffectAndImmunityLog() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Assassin.momentaryFatality(2, false, 17, 0, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertEquals(-17, result.damageTaken());
        assertTrue(log.contains("지속 턴: 4턴"));
        assertTrue(log.contains("행동불가/공격불가 면역"));
        assertTrue(log.contains("우누스~데케임"));
    }

    @Test
    void throwDaggerRequiresDefenseTrigger() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Assassin.throwDagger(100, false, 0, out);

        assertFalse(result.succeeded());
    }

    @Test
    void foreseenAssassinationRequiresDaggerThrowHit() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Assassin.foreseenAssassination(100, false, 0, out);

        assertFalse(result.succeeded());
    }

    @Test
    void namelessSlashNeedsAtLeastFiveMarks() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Assassin.namelessSlash(4, out);

        assertFalse(result.succeeded());
    }

    @Test
    void conclusionUsesUnifiedFormulaAndClearsMarksLog() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Assassin.conclusion(100, 10, 12, 0, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertTrue(result.damageDealt() > 0);
        assertTrue(log.contains("데미지 계산(스킬)"));
        assertTrue(log.contains("모든 적의 [단검 표식] 제거"));
    }
}
