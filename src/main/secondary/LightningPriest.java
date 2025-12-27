package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 번개의 사제 (Lightning Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 딜서폿, 왕귀형
 */
public class LightningPriest {

    // ===== 패시브 스킬 =====

    /**
     * 축복 (패시브)
     * 자신이 공격 성공 시 이번턴 아군 데미지 150%
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double blessing(PrintStream out) {
        out.println("축복: 공격 성공 → 이번턴 아군 데미지 150%");
        return 1.5;
    }

    // ===== 스킬 =====

    /**
     * 번개의 사제 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("번개의 사제-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 스파크
     * 2D4
     * 마나1
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int spark(int intelligence, PrintStream out) {
        out.println("번개의 사제-스파크 사용 (2D4)");
        int totalDamage = 0;

        for (int i = 1; i <= 2; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 스파크: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 1 소모");
        return totalDamage + sideDamage;
    }

    /**
     * 체인 라이트닝 - 적 데미지
     * 대상 최대 3명
     * 적 2D6
     * 마나3 쿨타임3턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int chainLightningDamage(int intelligence, PrintStream out) {
        out.println("번개의 사제-체인 라이트닝(공격) 사용 (2D6)");
        out.println("※ 대상 최대 3명");
        int totalDamage = 0;

        for (int i = 1; i <= 2; i++) {
            int diceResult = Main.dice(1, 6, out);
            out.printf("%d번째 번개: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 3 소모, 쿨타임 3턴");
        return totalDamage + sideDamage;
    }

    /**
     * 체인 라이트닝 - 아군 보호막
     * 아군 보호막 2D4
     *
     * @param out 출력 스트림
     * @return 보호막 양
     */
    public static int chainLightningShield(PrintStream out) {
        out.println("번개의 사제-체인 라이트닝(보호막) 사용 (2D4)");
        int totalShield = 0;

        for (int i = 1; i <= 2; i++) {
            int shieldRoll = Main.dice(1, 4, out);
            out.printf("%d번째 보호막: %d%n", i, shieldRoll);
            totalShield += shieldRoll;
        }

        out.printf("총 보호막: %d%n", totalShield);
        return totalShield;
    }

    /**
     * 일렉트릭 필드
     * (최대 6턴 영창)
     * (광역)
     * 아군 데미지 50n% / 적 (8-4n)D4
     * 마나8 쿨타임 7턴
     *
     * @param n 영창 턴 수
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int electricField(int n, int intelligence, PrintStream out) {
        // 영창 턴 수 검증 (0-6 사이)
        int validN = Math.max(0, Math.min(n, 6));
        if (n != validN) {
            out.printf("※ 영창 턴 수 조정: %d → %d%n", n, validN);
            n = validN;
        }

        out.println("번개의 사제-일렉트릭 필드 사용");
        out.printf("※ 영창 %d턴 (최대 6턴)%n", n);

        double allyMultiplier = electricFieldAllyBonus(n);
        out.printf("아군 데미지 보너스: +%.0f%%%n", (allyMultiplier - 1.0) * 100);

        int diceCount = 8 - 4 * n;
        if (diceCount <= 0) {
            out.println("※ 영창 완료: 적 데미지 없음");
            out.println("※ 마나 8 소모, 쿨타임 7턴");
            return 0;
        }

        out.printf("적에게 %dD4%n", diceCount);
        int totalDamage = 0;

        for (int i = 1; i <= diceCount; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 전격: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 광역");
        out.println("※ 마나 8 소모, 쿨타임 7턴");
        return totalDamage + sideDamage;
    }

    /**
     * 일렉트릭 필드 - 아군 데미지 보너스
     *
     * @param n 영창 턴 수
     * @return 아군 데미지 배율
     */
    public static double electricFieldAllyBonus(int n) {
        return 1.0 + (n * 0.50);
    }

    /**
     * 스트라이크
     * 2D8
     * 마나2 쿨타임5턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int strike(int intelligence, PrintStream out) {
        out.println("번개의 사제-스트라이크 사용 (2D8)");
        int totalDamage = 0;

        for (int i = 1; i <= 2; i++) {
            int diceResult = Main.dice(1, 8, out);
            out.printf("%d번째 스트라이크: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 2 소모, 쿨타임 5턴");
        return totalDamage + sideDamage;
    }

    /**
     * 일레이스터
     * 아군 1명 다음턴까지 데미지 200%
     * 마나5 쿨타임 5턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double elaster(PrintStream out) {
        out.println("번개의 사제-일레이스터 사용");
        out.println("※ 아군 1명 다음턴까지 데미지 200%");
        out.println("※ 마나 5 소모, 쿨타임 5턴");
        return 2.0;
    }

    /**
     * 신뇌격
     * 3D20
     * 영창2턴 마나7
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int divineLightning(int intelligence, PrintStream out) {
        out.println("번개의 사제-신뇌격 사용 (3D20)");
        out.println("※ 영창 2턴");
        int totalDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 20, out);
            out.printf("%d번째 신뇌: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 7 소모");
        return totalDamage + sideDamage;
    }

    /**
     * 독점
     * 아군 데미지 n% -> 본인 데미지 n+100%
     * 전투 시작 시 자신 외의 아군 1명 이상 존재 시 사용 가능
     * 전투 내 영구 지속, 취소 불가
     * 마나7
     *
     * @param allyDamageBonus 아군의 데미지 보너스 (%)
     * @param out 출력 스트림
     * @return 본인의 데미지 배율
     */
    public static double monopoly(double allyDamageBonus, PrintStream out) {
        out.println("번개의 사제-독점 사용");
        double multiplier = 1.0 + (allyDamageBonus / 100.0) + 1.0;
        out.printf("아군 데미지 %.0f%% → 본인 데미지 x%.2f%n", allyDamageBonus, multiplier);
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 7 소모");
        return multiplier;
    }

    /**
     * 불신
     * [신뢰]효과 제거
     * (전투 시작 시점의 아군 수) - (현재 아군 수) 만큼 아군 스탯 감소, 본인 스탯 증가
     * 전투 시작 시 자신 외의 아군 1명 이상 존재 시 사용 가능
     * 전투 내 영구 지속, 취소 불가
     * 마나8
     *
     * @param initialAllyCount 전투 시작 시 아군 수
     * @param currentAllyCount 현재 아군 수
     * @param out 출력 스트림
     * @return 스탯 변화량
     */
    public static int distrust(int initialAllyCount, int currentAllyCount, PrintStream out) {
        out.println("번개의 사제-불신 사용");
        int statChange = initialAllyCount - currentAllyCount;
        out.printf("아군 수 %d → %d: 스탯 ±%d%n", initialAllyCount, currentAllyCount, statChange);
        out.println("※ 신뢰 효과 제거");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 8 소모");
        return statChange;
    }

    /**
     * 신앙심
     * 최대 3턴동안 자신과 적1명 행동 불가 부여
     * 지속시간 중 모든 아군 데미지 150%
     * 마나6 쿨타임10턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double faith(PrintStream out) {
        out.println("번개의 사제-신앙심 사용");
        out.println("※ 최대 3턴: 자신과 적 1명 행동 불가");
        out.println("※ 지속 중 모든 아군 데미지 150%");
        out.println("※ 마나 6 소모, 쿨타임 10턴");
        return 1.5;
    }
}


