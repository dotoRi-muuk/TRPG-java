package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Gunslinger {

    /**
     * 건슬링거 기본공격
     * @param stat 사용할 스탯
     * @param isFirstShot 신중함 패시브 (첫 공격 300%)
     * @param dodgedLastTurn 노림수 패시브 (지난 턴 회피 시 200%)
     * @param isJudgeTurn 심판자 패시브 (5n턴, D4% 추가)
     */
    public static int plain(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, PrintStream out) {
        out.println("건슬링거-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 심판자 패시브 - D4% 추가 데미지
        if (isJudgeTurn) {
            int judgeBonus = Main.dice(1, 4, out);
            int bonusDamage = (int) (totalDamage * judgeBonus / 100.0);
            totalDamage += bonusDamage;
            out.printf("심판자 패시브 적용: +%d%% (+%d) → %d%n", judgeBonus, bonusDamage, totalDamage);
        }

        // 신중함 패시브
        if (isFirstShot) {
            totalDamage *= 3;
            out.printf("신중함 패시브 적용: x3.0 → %d%n", totalDamage);
        }

        // 노림수 패시브
        if (dodgedLastTurn) {
            totalDamage *= 2;
            out.printf("노림수 패시브 적용: x2.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 더블샷 기술
     * 2D6, 스태미나 2 소모
     */
    public static int doubleShot(int stat, PrintStream out) {
        out.println("건슬링거-더블샷 사용 (2D6)");
        int defaultDamage = Main.dice(2, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 2 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 헤드샷 기술
     * D20, 스태미나 3 소모
     */
    public static int headshot(int stat, PrintStream out) {
        out.println("건슬링거-헤드샷 사용 (D20)");
        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 3 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 퀵드로우 기술
     * D8 (신중함 발동 시 4D8), 스태미나 1 소모
     */
    public static int quickDraw(int stat, boolean isFirstShot, PrintStream out) {
        out.println("건슬링거-퀵드로우 사용");
        int defaultDamage;

        if (isFirstShot) {
            out.println("신중함 발동! (4D8)");
            defaultDamage = Main.dice(4, 8, out);
        } else {
            out.println("(D8)");
            defaultDamage = Main.dice(1, 8, out);
        }

        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 1 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 일점사 기술
     * 6D6, 스태미나 4 소모
     */
    public static int focusFire(int stat, PrintStream out) {
        out.println("건슬링거-일점사 사용 (6D6)");
        int defaultDamage = Main.dice(6, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 4 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 백스탭 (전용 수비)
     * 성공 시 공격 회피, 150% 데미지로 반격
     * 스태미나 3 소모
     */
    public static int backstab(int stat, PrintStream out) {
        out.println("건슬링거-백스탭 사용 (전용 수비)");
        out.println("※ 공격 회피 성공");

        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 1.5);

        out.printf("반격 데미지 150%%: (%d + %d) x 1.5 = %d%n", defaultDamage, sideDamage, totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

}

