package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Spearman {

    /**
     * 창술사 기본공격
     * 약점파악 패시브: 기본 기술 50%, [연계]기술 200%
     */
    public static int plain(int stat, PrintStream out) {
        out.println("창술사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 0.5);

        out.printf("약점파악 패시브 적용 (기본 기술 50%%): %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 돌려 찌르기 기술
     * D8, 스태미나 1 소모
     */
    public static int spinThrust(int stat, PrintStream out) {
        out.println("창술사-돌려 찌르기 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 0.5);

        out.printf("약점파악 패시브 적용 (기본 기술 50%%): %d%n", totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * 회전 타격 기술
     * D10, [연계] 획득, 스태미나 2 소모
     */
    public static int spinStrike(int stat, PrintStream out) {
        out.println("창술사-회전 타격 사용 (D10)");
        int defaultDamage = Main.dice(1, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 0.5);

        out.printf("약점파악 패시브 적용 (기본 기술 50%%): %d%n", totalDamage);
        out.println("※ [연계] 획득");
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 하단 베기 기술
     * D6, 이번 턴 상대 수비 불가, [연계] 획득, 스태미나 2 소모
     */
    public static int lowSlash(int stat, PrintStream out) {
        out.println("창술사-하단 베기 사용 (D6)");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = (int) ((defaultDamage + sideDamage) * 0.5);

        out.printf("약점파악 패시브 적용 (기본 기술 50%%): %d%n", totalDamage);
        out.println("※ [연계] 획득");
        out.println("※ 이번 턴 상대 수비 불가");
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * [연계] 정면 찌르기 기술 (호환성을 위한 오버로드)
     */
    public static int comboFrontThrust(int stat, PrintStream out) {
        return comboFrontThrust(stat, false, out);
    }

    /**
     * [연계] 정면 찌르기 기술
     * 2D10, 스태미나 1 소모
     * @param stat 사용할 스탯
     * @param isAdaptationActive 적응 스킬 활성화 여부 (400% 추가)
     * @param out 출력 스트림
     */
    public static int comboFrontThrust(int stat, boolean isAdaptationActive, PrintStream out) {
        out.println("창술사-[연계]정면 찌르기 사용 (2D10)");
        int defaultDamage = Main.dice(2, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 약점파악 패시브: [연계]기술 200%
        double multiplier = 2.0;
        out.println("약점파악 패시브 적용: x2.0");

        // 적응 스킬: 곱연산 800% (200% x 400%)
        if (isAdaptationActive) {
            multiplier *= 4.0;
            out.println("적응 스킬 적용: x4.0");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("[연계]기술 배율 적용: x%.1f = %d%n", multiplier, totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * [연계] 일섬창 기술 (호환성을 위한 오버로드)
     */
    public static int comboFlashSpear(int stat, PrintStream out) {
        return comboFlashSpear(stat, false, out);
    }

    /**
     * [연계] 일섬창 기술
     * 4D8, 스태미나 3 소모
     * @param stat 사용할 스탯
     * @param isAdaptationActive 적응 스킬 활성화 여부 (400% 추가)
     * @param out 출력 스트림
     */
    public static int comboFlashSpear(int stat, boolean isAdaptationActive, PrintStream out) {
        out.println("창술사-[연계]일섬창 사용 (4D8)");
        int defaultDamage = Main.dice(4, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 약점파악 패시브: [연계]기술 200%
        double multiplier = 2.0;
        out.println("약점파악 패시브 적용: x2.0");

        // 적응 스킬: 곱연산 800% (200% x 400%)
        if (isAdaptationActive) {
            multiplier *= 4.0;
            out.println("적응 스킬 적용: x4.0");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("[연계]기술 배율 적용: x%.1f = %d%n", multiplier, totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * [연계] 천뢰격 기술 (호환성을 위한 오버로드)
     */
    public static int comboThunderStrike(int stat, PrintStream out) {
        return comboThunderStrike(stat, false, out);
    }

    /**
     * [연계] 천뢰격 기술
     * 5D12, 스태미나 5 소모
     * @param stat 사용할 스탯
     * @param isAdaptationActive 적응 스킬 활성화 여부 (400% 추가)
     * @param out 출력 스트림
     */
    public static int comboThunderStrike(int stat, boolean isAdaptationActive, PrintStream out) {
        out.println("창술사-[연계]천뢰격 사용 (5D12)");
        int defaultDamage = Main.dice(5, 12, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 약점파악 패시브: [연계]기술 200%
        double multiplier = 2.0;
        out.println("약점파악 패시브 적용: x2.0");

        // 적응 스킬: 곱연산 800% (200% x 400%)
        if (isAdaptationActive) {
            multiplier *= 4.0;
            out.println("적응 스킬 적용: x4.0");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("[연계]기술 배율 적용: x%.1f = %d%n", multiplier, totalDamage);
        out.println("※ 스태미나 5 소모");
        return totalDamage;
    }

}

