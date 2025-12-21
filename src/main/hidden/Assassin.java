package main.hidden;
}

    }
        return totalDamage;
        out.println("※ 스태미나 3 소모");
        out.printf("총 데미지 : %d%n", totalDamage);

        }
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
            totalDamage *= 4;
        if (isReturnTurn) {

        int totalDamage = defaultDamage + sideDamage;
        int sideDamage = Main.sideDamage(stat, out);
        int defaultDamage = Main.dice(3, 6, out);
        out.println("암살자-후방 공격 사용 (3D6)");
    public static int rearAttack(int stat, boolean isReturnTurn, PrintStream out) {
     */
     * 3D6, 스태미나 3 소모
     * 후방 공격 기술
    /**

    }
        return totalDamage;
        out.println("※ 스태미나 3 소모");
        out.printf("총 데미지 : %d%n", totalDamage);

        }
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
            totalDamage *= 4;
        if (isReturnTurn) {

        int totalDamage = defaultDamage + sideDamage;
        int sideDamage = Main.sideDamage(stat, out);
        int defaultDamage = Main.dice(4, 4, out);
        out.println("암살자-손목 긋기 사용 (4D4)");
    public static int wristSlit(int stat, boolean isReturnTurn, PrintStream out) {
     */
     * 4D4, 스태미나 3 소모
     * 손목 긋기 기술
    /**

    }
        return totalDamage;
        out.println("※ 스태미나 4 소모");
        out.printf("총 데미지 : %d%n", totalDamage);

        }
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
            totalDamage *= 4;
        if (isReturnTurn) {

        int totalDamage = defaultDamage + sideDamage;
        int sideDamage = Main.sideDamage(stat, out);
        int defaultDamage = Main.dice(1, 20, out);
        out.println("암살자-목 긋기 사용 (D20)");
    public static int throatSlit(int stat, boolean isReturnTurn, PrintStream out) {
     */
     * D20, 스태미나 4 소모
     * 목 긋기 기술
    /**

    }
        return totalDamage;
        out.println("※ 스태미나 5 소모");
        out.printf("총 데미지 : %d%n", totalDamage);

        }
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
            totalDamage *= 4;
        if (isReturnTurn) {

        int totalDamage = defaultDamage + sideDamage;
        int sideDamage = Main.sideDamage(stat, out);
        int defaultDamage = Main.dice(2, 10, out);
        out.println("암살자-급소 찌르기 사용 (2D10)");
    public static int criticalStab(int stat, boolean isReturnTurn, PrintStream out) {
     */
     * 2D10, 스태미나 5 소모
     * 급소 찌르기 기술
    /**

    }
        return totalDamage;
        out.println("※ 스태미나 44 소모");
        out.printf("총 데미지 : %d%n", totalDamage);

        }
            out.printf("생사여탈 패시브 적용: x2.0 → %d%n", totalDamage);
            totalDamage *= 2;
        if (isFirstAssault) {

        }
            out.printf("암살 대상 패시브 적용: x4.0 → %d%n", totalDamage);
            totalDamage *= 4;
        if (isReturnTurn) {

        int totalDamage = defaultDamage + sideDamage;
        int sideDamage = Main.sideDamage(stat, out);
        int defaultDamage = Main.dice(4, 20, out);
        out.println("암살자-암살 사용 (4D20)");
    public static int assassinate(int stat, boolean isReturnTurn, boolean isFirstAssault, PrintStream out) {
     */
     * 4D20, 스태미나 44 소모
     * 암살 기술
    /**

    }
        return totalDamage;
        out.printf("총 데미지 : %d%n", totalDamage);

        }
            out.printf("생사여탈 패시브 적용 (전투 시작 암살): x2.0 → %d%n", totalDamage);
            totalDamage *= 2;
        if (isFirstAssault) {
        // 생사여탈 패시브

        }
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            totalDamage *= 4;
        if (isReturnTurn) {
        // 암살 대상 패시브

        int totalDamage = defaultDamage + sideDamage;
        int sideDamage = Main.sideDamage(stat, out);
        int defaultDamage = Main.dice(1, 6, out);
        out.println("암살자-기본공격 사용");
    public static int plain(int stat, boolean isReturnTurn, boolean isFirstAssault, PrintStream out) {
     */
     * @param isFirstAssault 생사여탈 패시브 (전투 시작 암살 200%)
     * @param isReturnTurn 전장 복귀 턴 여부 (암살 대상 패시브 400%)
     * @param stat 사용할 스탯
     * 암살자 기본공격
    /**

public class Assassin {

import java.io.PrintStream;

import main.Main;


