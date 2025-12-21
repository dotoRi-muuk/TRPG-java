package main.hidden;

import main.Main;

import java.io.PrintStream;

public class Assassin {

    /**
     * 암살자 기본공격
     * @param stat 사용할 스탯
     * @param isReturnTurn 암살 대상 패시브 - 전장 복귀 턴 (400%)
     * @param isFirstAssault 생사여탈 패시브 - 전투 시작 암살 (200%)
     * @param out 출력 스트림
     */
    public static int plain(int stat, boolean isReturnTurn, boolean isFirstAssault, PrintStream out) {
        out.println("암살자-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 암살 대상 패시브 (전장 복귀 턴)
        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            out.println("※ 일도양단 패시브: 턴 2회 사용 가능, 스태미나 소모 X");
            out.println("※ 묵음 패시브: 피해 적중 시 다음 턴까지 적 행동불가");
            out.println("※ 생사여탈 패시브: 적 체력 10%% 이하 시 즉시 처형");
        }

        // 생사여탈 패시브 (전투 시작 암살)
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
     * @param stat 사용할 스탯
     * @param isReturnTurn 암살 대상 패시브 (전장 복귀 턴 400%)
     * @param isFirstAssault 생사여탈 패시브 (전투 시작 암살 200%)
     * @param confirmKillActive 확인 사살 스킬 적용 여부 (200%)
     * @param out 출력 스트림
     */
    public static int assassinate(int stat, boolean isReturnTurn, boolean isFirstAssault, boolean confirmKillActive, PrintStream out) {
        out.println("암살자-암살 사용 (4D20)");
        out.println("※ 스태미나 44 소모");

        int defaultDamage = Main.dice(4, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 확인 사살 스킬 (다음 암살 200%)
        if (confirmKillActive) {
            totalDamage *= 2;
            out.printf("확인 사살 스킬 적용: x2.0 → %d%n", totalDamage);
        }

        // 암살 대상 패시브
        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            out.println("※ 일도양단 패시브: 턴 2회 사용 가능, 스태미나 소모 X");
            out.println("※ 묵음 패시브: 피해 적중 시 다음 턴까지 적 행동불가");
            out.println("※ 생사여탈 패시브: 적 체력 10%% 이하 시 즉시 처형");
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
     * 급소 찌르기 기술
     * 2D10, 스태미나 5 소모
     */
    public static int criticalStab(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-급소 찌르기 사용 (2D10)");
        out.println("※ 스태미나 5 소모");

        int defaultDamage = Main.dice(2, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            out.println("※ 일도양단 패시브: 턴 2회 사용 가능, 스태미나 소모 X");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 목 긋기 기술
     * D20, 스태미나 4 소모
     */
    public static int throatSlit(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-목 긋기 사용 (D20)");
        out.println("※ 스태미나 4 소모");

        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            out.println("※ 일도양단 패시브: 턴 2회 사용 가능, 스태미나 소모 X");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 손목 긋기 기술
     * 4D4, 스태미나 3 소모
     */
    public static int wristSlit(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-손목 긋기 사용 (4D4)");
        out.println("※ 스태미나 3 소모");

        int defaultDamage = Main.dice(4, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            out.println("※ 일도양단 패시브: 턴 2회 사용 가능, 스태미나 소모 X");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 후방 공격 기술
     * 3D6, 스태미나 3 소모
     */
    public static int rearAttack(int stat, boolean isReturnTurn, PrintStream out) {
        out.println("암살자-후방 공격 사용 (3D6)");
        out.println("※ 스태미나 3 소모");

        int defaultDamage = Main.dice(3, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isReturnTurn) {
            totalDamage *= 4;
            out.printf("암살 대상 패시브 적용 (전장 복귀 턴): x4.0 → %d%n", totalDamage);
            out.println("※ 일도양단 패시브: 턴 2회 사용 가능, 스태미나 소모 X");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

}

