package main.secondary;

/**
 * 소환술사 (Summoner)
 * 주 스탯: 지능 (지혜)
 * 특징: 해.줘.
 */
public class Summoner {

    // ===== 패시브 스킬 =====

    /**
     * 유대감 (패시브)
     * 길들인 소환수의 수와 등급에 따라 데미지 증가
     * (보유한 소환수 1마리 등급 당 데미지 증가량:
     * 초급 5% / 중등 10% / 고위 15% / 특급 20% / 초월 25% / 전능 50%)
     *
     * @param basicCount 초급 소환수 개수
     * @param intermediateCount 중등 소환수 개수
     * @param advancedCount 고위 소환수 개수
     * @param specialCount 특급 소환수 개수
     * @param transcendentCount 초월 소환수 개수
     * @param omnipotentCount 전능 소환수 개수
     * @return 데미지 배율
     */
    public static double bond(int basicCount, int intermediateCount, int advancedCount,
                              int specialCount, int transcendentCount, int omnipotentCount) {
        double bonus = 0;
        bonus += basicCount * 5.0;
        bonus += intermediateCount * 10.0;
        bonus += advancedCount * 15.0;
        bonus += specialCount * 20.0;
        bonus += transcendentCount * 25.0;
        bonus += omnipotentCount * 50.0;
        return 1.0 + (bonus / 100.0);
    }

    // ===== 스킬 =====

    /**
     * 소환
     * 길들인 소환수를 소환시 [소환패] 1개 소모
     * 길들이지 않은 소환수도 소환 가능
     * 마나3 쿨타임 8턴
     * 전장의 소환수는 매턴 마나를 소모하게 한다
     * (하급 0, 중급 1, 돌파 2, 특급 3, 초월 4, 전능 6)
     *
     * @param summonGrade 소환수 등급 (0=하급, 1=중급, 2=돌파, 3=특급, 4=초월, 6=전능)
     * @return 매턴 소모 마나
     */
    public static int summonManaCostPerTurn(int summonGrade) {
        return switch (summonGrade) {
            case 0 -> 0; // 하급
            case 1 -> 1; // 중급
            case 2 -> 2; // 돌파
            case 3 -> 3; // 특급
            case 4 -> 4; // 초월
            case 6 -> 6; // 전능
            default -> 0;
        };
    }

    /**
     * 소환수를 이기는 주먹
     * D10
     * 스태3
     */
    public static int[] punchToBeatSummon(int roll) {
        return new int[]{roll};
    }

    /**
     * 말을 잘 듣게 하는 주먹
     * D10
     * 스태2
     */
    public static int[] punchToObey(int roll) {
        return new int[]{roll};
    }

    /**
     * 소환해제
     * 소환한 소환수를 소환 해제시킴
     * 이후 [지혜] 판정 성공 시 소환패를 1개 얻음
     * 판정: (지혜 스탯 - D20) > 0
     *
     * @param wisdomStat 지혜 스탯
     * @param diceRoll D20 주사위 결과
     * @return 판정 성공 여부
     */
    public static boolean dismissSummon(int wisdomStat, int diceRoll) {
        return (wisdomStat - diceRoll) > 0;
    }

    /**
     * 소환 증폭
     * 전투 내 영구적으로 해당 소환수가 가하는 피해 200/(현재 소환된 소환수의 수)% 증가
     * (중첩 불가)
     * 마나5 쿨타임5턴
     *
     * @param currentSummonCount 현재 소환된 소환수 개수
     * @return 데미지 배율
     */
    public static double summonAmplification(int currentSummonCount) {
        if (currentSummonCount == 0) return 1.0;
        return 1.0 + (200.0 / currentSummonCount / 100.0);
    }

    /**
     * 결속의 끈
     * 소환 후 5턴이 지난 소환수에게 사용 가능
     * 전투 내에서 영구적으로 소환수가 가하는 피해 200%
     * 마나7 쿨타임 7턴
     */
    public static double bondOfConnection() {
        return 2.0;
    }
}

