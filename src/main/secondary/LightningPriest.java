package main.secondary;

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
     */
    public static double blessing() {
        return 1.5;
    }

    // ===== 스킬 =====

    /**
     * 스파크
     * 2D4
     * 마나1
     */
    public static int[] spark(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 체인 라이트닝 - 적 데미지
     * 대상 최대 3명
     * 적 2D6
     * 마나3 쿨타임3턴
     */
    public static int[] chainLightningDamage(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 체인 라이트닝 - 아군 보호막
     * 아군 보호막 2D4
     */
    public static int[] chainLightningShield(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 일렉트릭 필드
     * (최대 6턴 영창)
     * (광역)
     * 아군 데미지 50n% / 적 (8-4n)D4
     * 마나8 쿨타임 7턴
     *
     * @param n 영창 턴 수
     * @return 아군 데미지 배율
     */
    public static double electricFieldAllyBonus(int n) {
        return 1.0 + (n * 0.50);
    }

    /**
     * 일렉트릭 필드 - 적 데미지
     * 적 (8-4n)D4
     *
     * @param n 영창 턴 수
     * @param rolls 주사위 결과들
     * @return 총 데미지
     */
    public static int[] electricFieldEnemyDamage(int n, int[] rolls) {
        System.out.println("일렉트릭 필드: 영창 " + n + "턴, 적에게 " + (8 - 4 * n) + "D4");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 스트라이크
     * 2D8
     * 마나2 쿨타임5턴
     */
    public static int[] strike(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 일레이스터
     * 아군 1명 다음턴까지 데미지 200%
     * 마나5 쿨타임 5턴
     */
    public static double elaster() {
        return 2.0;
    }

    /**
     * 신뇌격
     * 3D20
     * 영창2턴 마나7
     */
    public static int[] divineLightning(int[] rolls) {
        System.out.println("신뇌격: 영창 2턴");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 독점
     * 아군 데미지 n% -> 본인 데미지 n+100%
     * 전투 시작 시 자신 외의 아군 1명 이상 존재 시 사용 가능
     * 전투 내 영구 지속, 취소 불가
     * 마나7
     *
     * @param allyDamageBonus 아군의 데미지 보너스 (%)
     * @return 본인의 데미지 배율
     */
    public static double monopoly(double allyDamageBonus) {
        return 1.0 + (allyDamageBonus / 100.0) + 1.0;
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
     * @return 스탯 변화량
     */
    public static int distrust(int initialAllyCount, int currentAllyCount) {
        return initialAllyCount - currentAllyCount;
    }

    /**
     * 신앙심
     * 최대 3턴동안 자신과 적1명 행동 불가 부여
     * 지속시간 중 모든 아군 데미지 150%
     * 마나6 쿨타임10턴
     */
    public static double faith() {
        return 1.5;
    }
}

