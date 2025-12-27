package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Berserker {

    /**
     * 버서커 기본공격
     * @param stat 사용할 스탯
     * @param maxHealth 최대 체력
     * @param currentHealth 현재 체력
     * @param out 출력 스트림
     */
    public static int plain(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 찍어내리기 기술
     * D8, 스태미나 1 소모
     */
    public static int chopDown(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-찍어내리기 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * 부수기 기술
     * 2D6, 스태미나 2 소모
     */
    public static int smash(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-부수기 사용 (2D6)");
        int defaultDamage = Main.dice(2, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 일격 기술
     * D20, 스태미나 5 소모
     */
    public static int strike(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-일격 사용 (D20)");
        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 5 소모");
        return totalDamage;
    }

    /**
     * 무지성 난타 기술
     * 4D8, 스태미나 8 소모
     */
    public static int mindlessBarrage(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-무지성 난타 사용 (4D8)");
        int defaultDamage = Main.dice(4, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 8 소모");
        return totalDamage;
    }

    /**
     * 흉폭한 맹공 기술
     * 5D12, 스태미나 12 소모
     */
    public static int savageAssault(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-흉폭한 맹공 사용 (5D12)");
        int defaultDamage = Main.dice(5, 12, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 12 소모");
        return totalDamage;
    }

    /**
     * 최후의 일격 기술
     * 4D20, 체력이 10% 이하로 남았을 때 사용 가능
     * 모든 스태미나 소모
     */
    public static int lastStrike(int stat, int maxHealth, int currentHealth, PrintStream out) {
        out.println("버서커-최후의 일격 사용 (4D20)");
        out.println("※ 모든 스태미나 소모");
        out.println("※ 체력이 10%% 이하일 때만 사용 가능");

        int defaultDamage = Main.dice(4, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, maxHealth, currentHealth, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 패시브 적용
     * - 분노: 감소한 체력 1당 가하는 데미지 2% 증가 (최대 100%)
     * - 희열: 남은 체력이 20% 이하일때 가하는 데미지 200%
     */
    private static int applyPassives(int damage, int maxHealth, int currentHealth, PrintStream out) {
        double multiplier = 1.0;

        // 분노 패시브
        int lostHealth = maxHealth - currentHealth;
        double rageBonus = Math.min(lostHealth * 0.02, 1.0); // 최대 100%
        multiplier += rageBonus;
        out.printf("분노 패시브 적용: 감소 체력 %d → +%.0f%% 데미지%n", lostHealth, rageBonus * 100);

        // 희열 패시브
        double healthPercent = (double) currentHealth / maxHealth * 100;
        if (healthPercent <= 20) {
            multiplier *= 2.0;
            out.printf("희열 패시브 적용: 체력 %.1f%% → x2.0 데미지%n", healthPercent);
        }

        int finalDamage = (int) (damage * multiplier);
        out.printf("패시브 배율 적용: %d x %.2f = %d%n", damage, multiplier, finalDamage);

        return finalDamage;
    }

}

