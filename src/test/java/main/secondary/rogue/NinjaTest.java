package main.secondary.rogue;

import main.Result;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

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
}
