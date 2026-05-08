package main.secondary.archer;

import main.Main;
import main.Result;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class GunslingerPoacherTest {

    @Test
    public void calculateDamageUsesRequestedFormula() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Main.calculateDamage(10, 30, 2.0, 1.4, out);

        assertEquals(36, damage);
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("데미지 계산"));
    }

    @Test
    public void weightedJudgmentConsumesManaWithoutDamage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.weightedJudgment(out);

        assertTrue(result.succeeded());
        assertEquals(0, result.damageDealt());
        assertEquals(6, result.manaUsed());
        assertEquals(0, result.staminaUsed());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("다음 4회의 공격"));
    }

    @Test
    public void perfectComboShotConsumesStaminaAndAppliesWeightedJudgmentBonus() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.perfectComboShot(100, 2, 0, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertEquals(8, result.staminaUsed());
        assertTrue(result.damageDealt() >= 158);
        assertTrue(result.damageDealt() <= 2092);
        assertTrue(log.contains("가중 심판 적용"));
        assertTrue(log.contains("건슬링거-퍼펙트 콤보샷 사용"));
    }

    @Test
    public void fragmentationShellConsumesManaWithoutDamage() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Poacher.largeCaliberFragmentationShell(out);

        assertTrue(result.succeeded());
        assertEquals(0, result.damageDealt());
        assertEquals(15, result.manaUsed());
        assertEquals(0, result.staminaUsed());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("[대구경파쇄산탄 탄환]"));
    }

    @Test
    public void impactBuckshotBasicAttackUsesSpecialStaminaAndCancelsEnemyAttack() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Poacher.plain(100, false, false, false, false, false, false, true, 0, out);
        String log = baos.toString(StandardCharsets.UTF_8);

        assertTrue(result.succeeded());
        assertEquals(20, result.staminaUsed());
        assertTrue(result.damageDealt() >= 18);
        assertTrue(result.damageDealt() <= 1090);
        assertTrue(log.contains("대구경충격벅샷 탄환 적용"));
        assertTrue(log.contains("적의 공격을 취소시킵니다."));
    }
}
