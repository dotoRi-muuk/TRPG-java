package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Gunslinger {

    /**
     * 건슬링거 기본공격 (호환성을 위한 오버로드)
     */
    public static int plain(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, PrintStream out) {
        return plain(stat, isFirstShot, dodgedLastTurn, isJudgeTurn, false, out);
    }

    /**
     * 건슬링거 기본공격
     * @param stat 사용할 스탯
     * @param isFirstShot 신중함 패시브 (첫 공격 300%)
     * @param dodgedLastTurn 노림수 패시브 (지난 턴 회피 시 200%)
     * @param isJudgeTurn 심판자 패시브 (5n턴, D4% 추가)
     * @param isJudgementTarget 심판 대상 (지정 적 200%)
     */
    public static int plain(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, boolean isJudgementTarget, PrintStream out) {
        out.println("건슬링거-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isFirstShot, dodgedLastTurn, isJudgeTurn, isJudgementTarget, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 더블샷 기술 (호환성을 위한 오버로드)
     */
    public static int doubleShot(int stat, PrintStream out) {
        return doubleShot(stat, false, false, false, false, out);
    }

    /**
     * 더블샷 기술
     * 2D6, 스태미나 2 소모
     */
    public static int doubleShot(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, boolean isJudgementTarget, PrintStream out) {
        out.println("건슬링거-더블샷 사용 (2D6)");
        int defaultDamage = Main.dice(2, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isFirstShot, dodgedLastTurn, isJudgeTurn, isJudgementTarget, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 헤드샷 기술 (호환성을 위한 오버로드)
     */
    public static int headshot(int stat, PrintStream out) {
        return headshot(stat, false, false, false, false, out);
    }

    /**
     * 헤드샷 기술
     * D20, 스태미나 3 소모
     */
    public static int headshot(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, boolean isJudgementTarget, PrintStream out) {
        out.println("건슬링거-헤드샷 사용 (D20)");
        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isFirstShot, dodgedLastTurn, isJudgeTurn, isJudgementTarget, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 퀵드로우 기술 (호환성을 위한 오버로드)
     */
    public static int quickDraw(int stat, boolean isFirstShot, PrintStream out) {
        return quickDraw(stat, isFirstShot, false, false, false, out);
    }

    /**
     * 퀵드로우 기술
     * D8 (신중함 발동 시 4D8 + 300%), 스태미나 1 소모
     */
    public static int quickDraw(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, boolean isJudgementTarget, PrintStream out) {
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
        int totalDamage = defaultDamage + sideDamage;

        // 퀵드로우에도 신중함 300% 배율 적용 (버그 수정)
        totalDamage = applyPassives(totalDamage, isFirstShot, dodgedLastTurn, isJudgeTurn, isJudgementTarget, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * 일점사 기술 (호환성을 위한 오버로드)
     */
    public static int focusFire(int stat, PrintStream out) {
        return focusFire(stat, false, false, false, false, out);
    }

    /**
     * 일점사 기술
     * 6D6, 스태미나 4 소모
     */
    public static int focusFire(int stat, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, boolean isJudgementTarget, PrintStream out) {
        out.println("건슬링거-일점사 사용 (6D6)");
        int defaultDamage = Main.dice(6, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isFirstShot, dodgedLastTurn, isJudgeTurn, isJudgementTarget, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

    /**
     * 백스탭 (버프 스킬)
     * 활성화 시: 공격 회피, 150% 데미지로 반격
     * 스태미나 3 소모
     */
    public static int backstab(int stat, PrintStream out) {
        out.println("건슬링거-백스탭 사용 (버프 스킬)");
        out.println("※ 공격 회피 성공 시 발동");

        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 1.5);

        out.printf("반격 데미지 150%%: (%d + %d) x 1.5 = %d%n", defaultDamage, sideDamage, totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 경고 스킬
     * 다음턴까지 반격 데미지 300%
     * 지속시간 중 반격하지 않을 시 그 다음 턴 공격 불가
     * 턴을 소모하지 않음
     * 마나 5, 쿨타임 8턴
     */
    public static void warning(PrintStream out) {
        out.println("건슬링거-경고 사용");
        out.println("※ 다음턴까지 반격 데미지 300%%");
        out.println("※ 지속시간 중 반격하지 않을 시 그 다음 턴 공격 불가");
        out.println("※ 턴 소모 없음");
        out.println("※ 마나 5 소모, 쿨타임 8턴");
    }

    /**
     * 예고장 스킬
     * 대상에게 반격 데미지 200%
     * 마나 6, 쿨타임 15턴
     * @param stat 사용할 스탯
     */
    public static int notice(int stat, PrintStream out) {
        out.println("건슬링거-예고장 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 2.0);

        out.printf("반격 데미지 200%%: (%d + %d) x 2.0 = %d%n", defaultDamage, sideDamage, totalDamage);
        out.println("※ 마나 6 소모, 쿨타임 15턴");
        return totalDamage;
    }

    /**
     * 활약 기회 (전용 신속)
     * 신속 성공 시 이번 턴 반격 데미지 150%
     * @param swiftness 신속 스탯
     */
    public static boolean activeOpportunity(int swiftness, PrintStream out) {
        out.println("건슬링거-활약 기회 사용 (전용 신속)");
        int roll = Main.dice(1, 20, out);
        int check = swiftness - roll;
        out.printf("신속 판정: %d - %d = %d%n", swiftness, roll, check);

        if (check >= 1) {
            out.println("활약 기회 성공! 이번 턴 반격 데미지 150%%");
            return true;
        } else {
            out.println("활약 기회 실패");
            return false;
        }
    }

    /**
     * 패시브 적용 (곱연산)
     */
    private static int applyPassives(int damage, boolean isFirstShot, boolean dodgedLastTurn, boolean isJudgeTurn, boolean isJudgementTarget, PrintStream out) {
        double multiplier = 1.0;

        // 심판자 패시브
        if (isJudgeTurn) {
            int judgeBonus = Main.dice(1, 4, out);
            multiplier *= (1.0 + judgeBonus / 100.0);
            out.printf("심판자 패시브 적용: +%d%%%n", judgeBonus);
        }

        // 신중함 패시브
        if (isFirstShot) {
            multiplier *= 3.0;
            out.println("신중함 패시브 적용: x3.0");
        }

        // 노림수 패시브
        if (dodgedLastTurn) {
            multiplier *= 2.0;
            out.println("노림수 패시브 적용: x2.0");
        }

        // 심판 대상
        if (isJudgementTarget) {
            multiplier *= 2.0;
            out.println("심판 대상 적용: x2.0");
        }

        return (int) (damage * multiplier);
    }

}

