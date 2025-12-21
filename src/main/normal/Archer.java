package main.normal;

import main.Main;

import java.io.PrintStream;

public class Archer {

    /**
     * 궁수 기본공격 - 명사수 패시브 적용 (단일 스탯 사용, D4)
     * 명사수 패시브: 자신과 상대의 수비효과 발동 X (게임 로직에서 처리)
     * 기본 공격이 원거리로 적용 (게임 로직에서 처리)
     */
    public static int plain(int stat, PrintStream out) {
        out.println("궁수-기본공격 사용 (단일 스탯, D4)");
        int defaultDamage = Main.dice(1, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 궁수 기본공격 - 명사수 패시브 적용 (힘과 민첩 동시 사용, D6)
     */
    public static int plain(int strength, int dexterity, PrintStream out) {
        out.println("궁수-기본공격 사용 (힘과 민첩 동시 사용, D6)");
        int defaultDamage = Main.dice(1, 6, out);
        // 두 스탯 중 더 높은 값으로 사이드 데미지 계산
        int higherStat = Math.max(strength, dexterity);
        int sideDamage = Main.sideDamage(higherStat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 퀵샷 기술
     * 기본 공격 3회(D4로 고정), 주사위당 데미지-1 (최소 1)
     */
    public static int quickShot(int stat, PrintStream out) {
        out.println("궁수-퀵샷 사용");
        int totalDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 4, out);
            int adjustedDamage = Math.max(1, diceResult - 1);
            out.printf("%d번째 공격: %d - 1 = %d%n", i, diceResult, adjustedDamage);
            totalDamage += adjustedDamage;
        }

        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        return totalDamage + sideDamage;
    }

    /**
     * 대쉬 기술 - 단일 스탯 사용 (D4 * 1.5)
     * 명사수 1번 효과(수비 무시) 제거는 게임 로직에서 처리
     */
    public static int dash(int stat, PrintStream out) {
        out.println("궁수-대쉬 사용 (단일 스탯, D4)");
        out.println("※ 명사수 패시브 1번 효과(수비 무시)가 이번 턴 제거됨");
        int defaultDamage = Main.dice(1, 4, out);
        int boostedDamage = (int) (defaultDamage * 1.5);
        out.printf("기본 데미지 150%%: %d * 1.5 = %d%n", defaultDamage, boostedDamage);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", boostedDamage, sideDamage, boostedDamage + sideDamage);
        return boostedDamage + sideDamage;
    }

    /**
     * 대쉬 기술 - 힘과 민첩 동시 사용 (D6 * 1.5)
     * 명사수 1번 효과(수비 무시) 제거는 게임 로직에서 처리
     */
    public static int dash(int strength, int dexterity, PrintStream out) {
        out.println("궁수-대쉬 사용 (힘과 민첩 동시 사용, D6)");
        out.println("※ 명사수 패시브 1번 효과(수비 무시)가 이번 턴 제거됨");
        int defaultDamage = Main.dice(1, 6, out);
        int boostedDamage = (int) (defaultDamage * 1.5);
        out.printf("기본 데미지 150%%: %d * 1.5 = %d%n", defaultDamage, boostedDamage);
        int higherStat = Math.max(strength, dexterity);
        int sideDamage = Main.sideDamage(higherStat, out);
        out.printf("총 데미지 : %d + %d = %d%n", boostedDamage, sideDamage, boostedDamage + sideDamage);
        return boostedDamage + sideDamage;
    }

    /**
     * 사냥감 스킬 - 단일 스탯 사용 (D4)
     * 연속 공격 횟수에 따라 데미지 배율 증가
     * 1회: 100% / 2-3회: 125% / 4-6회: 150% / 7회 이상: 200%
     */
    public static int hunt(int stat, int consecutiveHits, PrintStream out) {
        out.println("궁수-사냥감 스킬 사용 (단일 스탯, D4)");
        out.printf("연속 공격 횟수: %d회%n", consecutiveHits);

        int defaultDamage = Main.dice(1, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        int baseDamage = defaultDamage + sideDamage;

        double multiplier = getHuntMultiplier(consecutiveHits);
        int finalDamage = (int) (baseDamage * multiplier);

        out.printf("기본 데미지: %d, 배율: %.0f%%, 최종 데미지: %d%n",
                   baseDamage, multiplier * 100, finalDamage);

        return finalDamage;
    }

    /**
     * 사냥감 스킬 - 힘과 민첩 동시 사용 (D6)
     * 연속 공격 횟수에 따라 데미지 배율 증가
     * 1회: 100% / 2-3회: 125% / 4-6회: 150% / 7회 이상: 200%
     */
    public static int hunt(int strength, int dexterity, int consecutiveHits, PrintStream out) {
        out.println("궁수-사냥감 스킬 사용 (힘과 민첩 동시 사용, D6)");
        out.printf("연속 공격 횟수: %d회%n", consecutiveHits);

        int defaultDamage = Main.dice(1, 6, out);
        int higherStat = Math.max(strength, dexterity);
        int sideDamage = Main.sideDamage(higherStat, out);
        int baseDamage = defaultDamage + sideDamage;

        double multiplier = getHuntMultiplier(consecutiveHits);
        int finalDamage = (int) (baseDamage * multiplier);

        out.printf("기본 데미지: %d, 배율: %.0f%%, 최종 데미지: %d%n",
                   baseDamage, multiplier * 100, finalDamage);

        return finalDamage;
    }

    /**
     * 사냥감 스킬의 배율 계산
     */
    private static double getHuntMultiplier(int consecutiveHits) {
        if (consecutiveHits == 1) {
            return 1.0;
        } else if (consecutiveHits >= 2 && consecutiveHits <= 3) {
            return 1.25;
        } else if (consecutiveHits >= 4 && consecutiveHits <= 6) {
            return 1.5;
        } else { // 7회 이상
            return 2.0;
        }
    }

}

