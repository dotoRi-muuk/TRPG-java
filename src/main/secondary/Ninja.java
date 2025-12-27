package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Ninja {

    /**
     * 닌자 기본공격
     * @param stat 사용할 스탯
     * @param isIllusionTurn 환영 패시브 (3n턴, 데미지 150%)
     * @param isCloneActive 분신 패시브 (데미지 75%)
     * @param isReflexActive 순발력 패시브 (데미지 75%)
     */
    public static int plain(int stat, boolean isIllusionTurn, boolean isCloneActive, boolean isReflexActive, PrintStream out) {
        out.println("닌자-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        double multiplier = 1.0;

        // 환영 패시브
        if (isIllusionTurn) {
            multiplier *= 1.5;
            out.println("환영 패시브 적용 (3n턴): x1.5");
        }

        // 분신 패시브
        if (isCloneActive) {
            multiplier *= 0.75;
            out.println("분신 패시브 적용: x0.75");
        }

        // 순발력 패시브
        if (isReflexActive) {
            multiplier *= 0.75;
            out.println("순발력 패시브 적용: x0.75");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("패시브 적용 후: %d%n", totalDamage);
        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 일격 기술
     * 2D6, 스태미나 2 소모
     */
    public static int strike(int stat, boolean isIllusionTurn, boolean isCloneActive, PrintStream out) {
        out.println("닌자-일격 사용 (2D6)");
        int defaultDamage = Main.dice(2, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        double multiplier = 1.0;

        if (isIllusionTurn) {
            multiplier *= 1.5;
            out.println("환영 패시브 적용: x1.5");
        }

        if (isCloneActive) {
            multiplier *= 0.75;
            out.println("분신 패시브 적용: x0.75");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 난도 기술
     * 3D8, 스태미나 4 소모
     */
    public static int chaos(int stat, boolean isIllusionTurn, boolean isCloneActive, PrintStream out) {
        out.println("닌자-난도 사용 (3D8)");
        int defaultDamage = Main.dice(3, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        double multiplier = 1.0;

        if (isIllusionTurn) {
            multiplier *= 1.5;
            out.println("환영 패시브 적용: x1.5");
        }

        if (isCloneActive) {
            multiplier *= 0.75;
            out.println("분신 패시브 적용: x0.75");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

    /**
     * 투척 표창 기술
     * D8, 턴 소모 X (표창 1개 소모)
     */
    public static int throwShuriken(int stat, boolean isCloneActive, PrintStream out) {
        out.println("닌자-투척 표창 사용 (D8)");
        out.println("※ 표창 1개 소모, 턴 소모 X");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isCloneActive) {
            totalDamage = (int) (totalDamage * 0.75);
            out.println("분신 패시브 적용: x0.75");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 환영난무 기술
     * 8D6, 스태미나 4 소모
     * 3n 턴에 발동 가능, 3n+3 턴까지 [분신] 봉인
     */
    public static int illusionBarrage(int stat, boolean isIllusionTurn, PrintStream out) {
        out.println("닌자-환영난무 사용 (8D6)");
        out.println("※ 3n 턴에 발동 가능");
        out.println("※ 3n+3 턴까지 [분신] 봉인");
        int defaultDamage = Main.dice(8, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 환영난무는 분신 패시브 영향 받지 않음
        if (isIllusionTurn) {
            totalDamage = (int) (totalDamage * 1.5);
            out.println("환영 패시브 적용: x1.5");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

    /**
     * 일점투척 스킬
     * (보유 표창)D10, 모든 표창 소모
     * 마나 3 소모, 쿨타임 3턴
     */
    public static int focusThrow(int stat, int shurikenCount, boolean isCloneActive, PrintStream out) {
        out.println("닌자-일점투척 사용");
        out.printf("보유 표창: %d개 → %dD10%n", shurikenCount, shurikenCount);
        out.println("※ 모든 표창 소모");
        out.println("※ 마나 3 소모");

        int defaultDamage = Main.dice(shurikenCount, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isCloneActive) {
            totalDamage = (int) (totalDamage * 0.75);
            out.println("분신 패시브 적용: x0.75");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

}

