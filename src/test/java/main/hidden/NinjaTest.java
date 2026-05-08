package main.hidden;

import main.Result;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class NinjaTest {

    @Test
    void focusedThrowUsesUnifiedFormulaAndIdeology300() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.focusedThrow(100, 100, 3, false, true, true, "none", out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertTrue(result.damageDealt() > 0);
        assertEquals(-3, result.manaUsed());
        assertTrue(log.contains("이념 봉인 스킬 적용: 데미지 +300%"));
        assertTrue(log.contains("주사위 보정 배율"));
        assertTrue(log.contains("데미지 계산(스킬)"));
    }
}
