package main.secondary;

/**
 * 어둠의 사제 (Dark Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 팀킬, 극딜
 */
public class DarkPriest {

    // ===== 패시브 스킬 =====

    /**
     * 지배 (패시브)
     * 아군 공격 시 다음 턴 데미지 200%
     */
    public static double domination() {
        return 2.0;
    }

    /**
     * 강탈 (패시브)
     * 모든 아군이 받는 효과를 제거, 그 효과의 50%로 자신이 효과를 받음
     * (해제 가능)
     */
    public static double plunder() {
        return 0.5;
    }

    /**
     * 잠식 (패시브)
     * 자신이 공격한 아군은 다음 턴 수비불가
     * 아군 공격 시 데미지만큼 체력 회복
     * 다음 턴동안 받는 피해 75%
     */
    public static double encroachment() {
        return 0.75;
    }

    // ===== 스킬 =====

    /**
     * 어둠의 기운
     * D4
     * 다음 턴까지 데미지 80% 부여
     * 광역
     * 마나2
     */
    public static int[] darkEnergy(int roll) {
        return new int[]{roll};
    }

    public static double darkEnergyDebuff() {
        return 0.8;
    }

    /**
     * 손아귀
     * D8
     * 다음 턴동안 행동불가 부여
     * 마나5 쿨타임 4턴
     */
    public static int[] grip(int roll) {
        return new int[]{roll};
    }

    /**
     * 우즈마니아
     * 4D12
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 2턴 마나8 쿨타임 6턴
     */
    public static int[] uzumania(int[] rolls) {
        System.out.println("우즈마니아: 영창 2턴");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 엑실리스터
     * 4D20
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 4턴 마나 14 쿨타임 9턴
     */
    public static int[] exilister(int[] rolls) {
        System.out.println("엑실리스터: 영창 4턴");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 어나이스필레인
     * 7D12
     * 남은 영창 시간이 2턴 이하일 때 모든 아군이 공격 불가
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 2턴 마나18 쿨타임 12턴
     */
    public static int[] annihilationPlain(int[] rolls) {
        System.out.println("어나이스필레인: 영창 2턴");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 엔시아스티켈리아
     * 9D10
     * 발동 시 다음 턴 모든 아군 받는 데미지 200%
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 5턴 마나16 쿨타임 13턴
     */
    public static int[] ensiasticalia(int[] rolls) {
        System.out.println("엔시아스티켈리아: 영창 5턴");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    public static double ensiasticaliaAllyDebuff() {
        return 2.0;
    }

    /**
     * 저주
     * 모든 아군에게 현재 체력의 절반만큼 피해를 줌
     * 다음 턴 데미지 200%
     * 마나7 쿨타임 6턴
     */
    public static double curse() {
        return 2.0;
    }

    /**
     * 갈취
     * (죽은 아군의 모든 스탯)/4 만큼 스탯 증가
     * 전투 당 1회
     *
     * @param deadAllyTotalStats 죽은 아군의 모든 스탯 합
     * @return 스탯 증가량
     */
    public static int extortion(int deadAllyTotalStats) {
        return deadAllyTotalStats / 4;
    }

    /**
     * 희생양
     * 다음 공격의 대상이 1명이라면 아군 1명까지 추가로 공격
     * 아군이 받는 데미지는 보정 영향X
     * 해당 공격의 데미지 300%
     * 마나6 쿨타임 8턴
     */
    public static double scapegoat() {
        return 3.0;
    }

    /**
     * 침식
     * 현재 ()불가 상태의 아군 1명 당 이번 턴 데미지 50% 증가
     * (턴 소모X)
     * 마나4 쿨타임 7턴
     *
     * @param disabledAlliesCount 불가 상태의 아군 수
     * @return 데미지 배율
     */
    public static double erosion(int disabledAlliesCount) {
        return 1.0 + (disabledAlliesCount * 0.5);
    }

    /**
     * 신앙심
     * 다음 턴까지 자신 제외 모두 행동불가
     * 데미지 200%
     * 마나7 쿨타임 13턴
     */
    public static double faith() {
        return 2.0;
    }
}

