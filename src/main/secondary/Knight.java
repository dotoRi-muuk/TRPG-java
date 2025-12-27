package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Knight {

    /**
     * 기사 기본공격
     */
    public static int plain(int stat, PrintStream out) {
        out.println("기사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 내려치기 기술
     * D8, 스태미나 1 소모
     */
    public static int smashDown(int stat, PrintStream out) {
        out.println("기사-내려치기 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 1 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 후려치기 기술
     * 2D4, 스태미나 1 소모
     */
    public static int sweep(int stat, PrintStream out) {
        out.println("기사-후려치기 사용 (2D4)");
        int defaultDamage = Main.dice(2, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 1 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 머리치기 기술
     * D6, 다음턴 적 공격불가, 스태미나 8 소모
     */
    public static int headStrike(int stat, PrintStream out) {
        out.println("기사-머리치기 사용 (D6)");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 8 소모");
        out.println("※ 다음턴 적 공격불가");
        return defaultDamage + sideDamage;
    }

    /**
     * 수비파괴 기술
     * D6, 다음 턴 적 수비 불가, 스태미나 5 소모
     */
    public static int defenseBreak(int stat, PrintStream out) {
        out.println("기사-수비파괴 사용 (D6)");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 5 소모");
        out.println("※ 다음 턴 적 수비 불가");
        return defaultDamage + sideDamage;
    }

    /**
     * 기절시키기 기술
     * D8, 다음턴 적 행동불가, 스태미나 10 소모
     */
    public static int stun(int stat, PrintStream out) {
        out.println("기사-기절시키기 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 10 소모");
        out.println("※ 다음턴 적 행동불가");
        return defaultDamage + sideDamage;
    }

    /**
     * 일격 기술
     * 3D12, 스태미나 6 소모
     */
    public static int criticalStrike(int stat, PrintStream out) {
        out.println("기사-일격 사용 (3D12)");
        int defaultDamage = Main.dice(3, 12, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 6 소모");
        return defaultDamage + sideDamage;
    }

}

