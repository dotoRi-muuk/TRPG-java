package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Assassin {

    /**
     * 암살자 기본공격
     * @param stat 사용할 스탯
     * @param isReturnTurn 전장 복귀 턴 여부 (암살 대상 패시브 400%)
     * @param isFirstAssault 생사여탈 패시브 (전투 시작 암살 200%)
     */
    public static int plain(int stat, boolean isReturnTurn, boolean isFirstAssault, PrintStream out) {
        out.println("암살자-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 암살 대상 패시브
        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
        }

        // 생사여탈 패시브
        if (isFirstAssault) {
            totalDamage *= 2;
            out.printf("생사여탈 패시브 적용 (전투 시작 암살): x2.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 암살 기술
     * 4D20, 스태미나 44 소모
     */
    public static int assassinate(int stat, boolean isReturnTurn, boolean isFirstAssault, PrintStream out) {
        out.println("암살자-암살 사용 (4D20)");
        int defaultDamage = Main.dice(4, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
        }

        if (isFirstAssault) {
            totalDamage *= 2;
            out.printf("생사여탈 패시브 적용: x2.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 44 소모");
        return totalDamage;
    }

    /**
     * 급소 찌르기 기술
     * 2D10, 스태미나 5 소모
     */
    public static int criticalStab(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-급소 찌르기 사용 (2D10)");
        int defaultDamage = Main.dice(2, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 5 소모");
        return totalDamage;
    }

    /**
     * 목 긋기 기술
     * D20, 스태미나 4 소모
     */
    public static int throatSlit(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-목 긋기 사용 (D20)");
        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

    /**
     * 손목 긋기 기술
     * 4D4, 스태미나 3 소모
     */
    public static int wristSlit(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-손목 긋기 사용 (4D4)");
        int defaultDamage = Main.dice(4, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 후방 공격 기술
     * 3D6, 스태미나 3 소모
     */
    public static int rearAttack(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-후방 공격 사용 (3D6)");
        int defaultDamage = Main.dice(3, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

}

