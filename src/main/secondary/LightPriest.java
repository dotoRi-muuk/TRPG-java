package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 빛의 사제 (Light Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 완벽한 힐서폿
 */
public class LightPriest {

    // ===== 패시브 스킬 =====

    /**
     * 신성한 육체 (패시브)
     * 아군 회복량의 10% 만큼 보호막 획득
     *
     * @param healAmount 회복량
     * @param out 출력 스트림
     * @return 보호막 양
     */
    public static int holyBody(int healAmount, PrintStream out) {
        int shield = healAmount / 10;
        out.printf("신성한 육체: 회복량 %d → 보호막 %d%n", healAmount, shield);
        return shield;
    }

    /**
     * 자비 (패시브)
     * 본인이 전투 내에서 공격한 적이 없다면 회복량 150%
     *
     * @param hasAttacked 전투 내에서 공격 여부
     * @param out 출력 스트림
     * @return 회복량 배율
     */
    public static double mercy(boolean hasAttacked, PrintStream out) {
        if (hasAttacked) {
            out.println("자비: 공격 이력 있음 → 회복량 100%");
            return 1.0;
        }
        out.println("자비: 공격 이력 없음 → 회복량 150%");
        return 1.5;
    }

    // ===== 스킬 =====

    /**
     * 빛의 사제 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("빛의 사제-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 힐
     * D6 회복
     * 마나1
     *
     * @param hasAttacked 공격 이력 여부
     * @param out 출력 스트림
     * @return 회복량
     */
    public static int heal(boolean hasAttacked, PrintStream out) {
        out.println("빛의 사제-힐 사용 (D6 회복)");
        int healRoll = Main.dice(1, 6, out);
        double multiplier = mercy(hasAttacked, out);
        int finalHeal = (int)(healRoll * multiplier);
        out.printf("회복량: %d x %.2f = %d%n", healRoll, multiplier, finalHeal);
        out.println("※ 마나 1 소모");
        return finalHeal;
    }

    /**
     * 치유의 바람
     * 2D6
     * 마나2 쿨타임 2턴
     *
     * @param hasAttacked 공격 이력 여부
     * @param out 출력 스트림
     * @return 회복량
     */
    public static int healingWind(boolean hasAttacked, PrintStream out) {
        out.println("빛의 사제-치유의 바람 사용 (2D6 회복)");
        int totalHeal = 0;

        for (int i = 1; i <= 2; i++) {
            int healRoll = Main.dice(1, 6, out);
            out.printf("%d번째 바람: %d%n", i, healRoll);
            totalHeal += healRoll;
        }

        double multiplier = mercy(hasAttacked, out);
        int finalHeal = (int)(totalHeal * multiplier);
        out.printf("회복량: %d x %.2f = %d%n", totalHeal, multiplier, finalHeal);
        out.println("※ 마나 2 소모, 쿨타임 2턴");
        return finalHeal;
    }

    /**
     * 빛의 성배
     * 5D4 회복
     * 마나3 쿨타임4턴
     *
     * @param hasAttacked 공격 이력 여부
     * @param out 출력 스트림
     * @return 회복량
     */
    public static int chaliceOfLight(boolean hasAttacked, PrintStream out) {
        out.println("빛의 사제-빛의 성배 사용 (5D4 회복)");
        int totalHeal = 0;

        for (int i = 1; i <= 5; i++) {
            int healRoll = Main.dice(1, 4, out);
            out.printf("%d번째 성배: %d%n", i, healRoll);
            totalHeal += healRoll;
        }

        double multiplier = mercy(hasAttacked, out);
        int finalHeal = (int)(totalHeal * multiplier);
        out.printf("회복량: %d x %.2f = %d%n", totalHeal, multiplier, finalHeal);
        out.println("※ 마나 3 소모, 쿨타임 4턴");
        return finalHeal;
    }

    /**
     * 기원
     * 다음턴까지 5D8 보호막 획득
     * 마나4 쿨타임6턴
     *
     * @param out 출력 스트림
     * @return 보호막 양
     */
    public static int prayer(PrintStream out) {
        out.println("빛의 사제-기원 사용 (5D8 보호막)");
        int totalShield = 0;

        for (int i = 1; i <= 5; i++) {
            int shieldRoll = Main.dice(1, 8, out);
            out.printf("%d번째 기원: %d%n", i, shieldRoll);
            totalShield += shieldRoll;
        }

        out.printf("보호막: %d%n", totalShield);
        out.println("※ 다음턴까지 지속");
        out.println("※ 마나 4 소모, 쿨타임 6턴");
        return totalShield;
    }

    /**
     * 헤븐즈 도어
     * 1명의 죽음을 1회 극복
     * 4D10 체력으로 부활
     * 마나15 쿨타임 20턴
     *
     * @param out 출력 스트림
     * @return 부활 체력
     */
    public static int heavensDoor(PrintStream out) {
        out.println("빛의 사제-헤븐즈 도어 사용");
        int totalHp = 0;

        for (int i = 1; i <= 4; i++) {
            int hpRoll = Main.dice(1, 10, out);
            out.printf("%d번째 부활: %d%n", i, hpRoll);
            totalHp += hpRoll;
        }

        out.printf("부활 체력: %d%n", totalHp);
        out.println("※ 마나 15 소모, 쿨타임 20턴");
        return totalHp;
    }

    /**
     * 편애
     * [축복] 대상지정 효과 -> 아군 1명
     * 회복량 250%
     * 전투내 영구 지속, 취소 불가
     * 마나6
     *
     * @param out 출력 스트림
     * @return 회복량 배율
     */
    public static double favoritism(PrintStream out) {
        out.println("빛의 사제-편애 사용");
        out.println("※ 축복 대상 → 아군 1명 고정");
        out.println("※ 회복량 250%");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 6 소모");
        return 2.5;
    }

    /**
     * 양도
     * [신성한 육체]효과 제거
     * 본인 회복 불가
     * 회복량 150%
     * 전투 내 영구 지속, 취소 불가
     * 마나4
     *
     * @param out 출력 스트림
     * @return 회복량 배율
     */
    public static double transfer(PrintStream out) {
        out.println("빛의 사제-양도 사용");
        out.println("※ 신성한 육체 효과 제거");
        out.println("※ 본인 회복 불가");
        out.println("※ 회복량 150%");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 4 소모");
        return 1.5;
    }

    /**
     * 증오
     * [자비] 회복량 150% -> 아군 데미지 150%
     * 전투 내 영구 지속, 취소 불가
     * 마나13
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double hatred(PrintStream out) {
        out.println("빛의 사제-증오 사용");
        out.println("※ 자비 효과 변경: 회복량 150% → 아군 데미지 150%");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 13 소모");
        return 1.5;
    }

    /**
     * 이기심
     * [이타심]효과 제거
     * 아군 치유시 (이번턴 치유량) x 10% 해당 아군 데미지 증가
     * 전투 내 영구 지속, 취소 불가
     * 마나12
     *
     * @param healAmount 치유량
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double selfishness(int healAmount, PrintStream out) {
        out.println("빛의 사제-이기심 사용");
        double multiplier = 1.0 + (healAmount * 0.10);
        out.printf("치유량 %d → 데미지 x%.2f%n", healAmount, multiplier);
        out.println("※ 이타심 효과 제거");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 12 소모");
        return multiplier;
    }

    /**
     * 신앙심
     * 다음턴 모든 적 행동 불가
     * 회복량 200%
     * 모든 아군 데미지 150%
     * 마나4 쿨타임7턴
     *
     * @param out 출력 스트림
     */
    public static void faith(PrintStream out) {
        out.println("빛의 사제-신앙심 사용");
        out.println("※ 다음턴 모든 적 행동 불가");
        out.println("※ 회복량 200%");
        out.println("※ 모든 아군 데미지 150%");
        out.println("※ 마나 4 소모, 쿨타임 7턴");
    }

    public static double faithHealMultiplier() {
        return 2.0;
    }

    public static double faithDamageMultiplier() {
        return 1.5;
    }
}

