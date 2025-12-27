package main.secondary;

/**
 * 시간의 사제 (Time Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 서폿, 디버프, 전장 조절
 */
public class TimePriest {

    // ===== 패시브 스킬 =====

    /**
     * 냉정 (패시브)
     * 행동불가 상태의 적에게 받는 피해 200% 부여
     * 행동횟수가 1+n인 아군에게 가하는 피해 50n% 증가 (자신 제외)
     *
     * @param allyExtraActions 아군의 추가 행동 횟수 (n)
     * @return 데미지 배율
     */
    public static double calm(int allyExtraActions) {
        return 1.0 + (allyExtraActions * 0.50);
    }

    public static double calmEnemyDebuff() {
        return 2.0;
    }

    /**
     * 침착 (패시브)
     * 아군이 행동불가 상태일 때 받는 피해 50%
     */
    public static double composure() {
        return 0.5;
    }

    // ===== 스킬 =====

    /**
     * 유예
     * 1명의 대상에게 다음 턴 동안 공격불가
     * 받는 데미지 150% 부여
     * 마나9 쿨타임 4턴
     */
    public static double suspension() {
        return 1.5;
    }

    /**
     * 감속
     * 1명의 대상에게 다음 턴 동안 수비불가
     * 받는 데미지 200% 부여
     * 마나8 쿨타임 5턴
     */
    public static double deceleration() {
        return 2.0;
    }

    /**
     * 부식
     * 자유 영창, 영창하는 동안 적 1명에게 매턴 영창 1회 당 4D8
     * 마나15 쿨타임 10턴
     */
    public static int[] corrosion(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 시간의 틈새
     * 자유 영창, 영창하는 동안 자신과 적1명 행동불가
     * 매턴 마나 7소모
     */
    public static int timeGapManaCostPerTurn() {
        return 7;
    }

    /**
     * 강탈
     * 전투 시작 시 자신 외 아군 1명 이상 존재 시 사용 가능
     * 매턴 50% 확률로 자신 제외 모든 아군 행동불가
     * 영창 2개 동시에 진행 가능, [축복]영창 중 사용 가능
     * 전투 내 영구 지속, 취소 불가
     * 마나10
     *
     * @param diceRoll D20 주사위 결과 (1~20)
     * @return 행동불가 발동 여부
     */
    public static boolean plunder(int diceRoll) {
        boolean success = diceRoll <= 10; // 50% 확률
        System.out.println("강탈: 50% 확률 판정 (D20 <= 10), 결과: " +
                          (success ? "행동불가 발동" : "발동 안 함"));
        return success;
    }

    /**
     * 성급함
     * 전투 시작 시 자신 외 아군 1명 이상 존재 시 사용 가능
     * [냉정] -> 행동횟수 n+1일 때 자신이 가하는 피해 100n% 증가
     * 아군 받는 피해 200%
     * 전투 내 영구 지속, 취소 불가
     * 마나10
     *
     * @param extraActions 추가 행동 횟수 (n)
     * @return 데미지 배율
     */
    public static double impatience(int extraActions) {
        return 1.0 + (extraActions * 1.0);
    }

    public static double impatienceAllyDebuff() {
        return 2.0;
    }

    /**
     * 기복
     * 전투 시작 시 자신 외 아군 1명 이상 존재 시 사용 가능
     * [침착]효과 제거
     * 자신은 행동불가 효과 무시
     * 행동불가 아군 받는 피해 200%
     * 자신이 행동불가를 부여하는 공격을 받을 때 받는 피해 25%
     * 전투 내 영구 지속, 취소 불가
     * 마나15
     */
    public static double upsAndDownsAllyDebuff() {
        return 2.0;
    }

    public static double upsAndDownsSelfDefense() {
        return 0.25;
    }

    /**
     * 신앙심
     * 다음 턴 모든 아군 행동 횟수 1회 추가
     * 모든 적 행동불가, 모든 아군 데미지 200%
     * 마나10 쿨타임 9턴
     */
    public static double faith() {
        return 2.0;
    }
}

