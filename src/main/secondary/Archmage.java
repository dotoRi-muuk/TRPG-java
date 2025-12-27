package main.secondary;

/**
 * 마도사 (Archmage)
 * 주 스탯: 지능 (지혜)
 * 특징: 장시간 영창 / 최고출력 스킬
 */
public class Archmage {

    // ===== 패시브 스킬 =====

    /**
     * 마도학 (패시브)
     * 피해를 주는 스킬 발동 시 (해당 스킬의 원래 영창 시간) x 10% 확률로 적의 수비 무시
     *
     * @param originalChantTime 원래 영창 시간
     * @param diceRoll D20 주사위 결과 (1~20)
     * @return 수비 무시 성공 여부
     */
    public static boolean magicScholar(int originalChantTime, int diceRoll) {
        int probability = originalChantTime * 10;
        int threshold = (probability * 20) / 100; // D20 기준으로 변환
        boolean success = diceRoll <= threshold;

        System.out.println("마도학: 수비 무시 확률 " + probability + "% (D20 <= " + threshold + "), 결과: " +
                          (success ? "성공" : "실패"));
        return success;
    }

    /**
     * 마력의 범람 (패시브)
     * (하나의 스킬 발동에 소모한 총 마나) x 30% 데미지 증가
     *
     * @param totalManaSpent 총 소모 마나
     * @return 데미지 배율
     */
    public static double manaFlood(int totalManaSpent) {
        return 1.0 + (totalManaSpent * 0.30);
    }

    // ===== 스킬 =====

    /**
     * 마력 순환
     * 다음 영창 스킬의 영창 시간 2턴 감소
     * 마나2 쿨타임 4턴
     */
    public static void manaCirculation() {
        System.out.println("마력 순환: 다음 영창 스킬의 영창 시간 -2턴");
    }

    /**
     * 응집
     * 영창 진행(지혜x)
     * 다음 스킬 피해 (영창 진행 턴) x 50% 데미지 증가
     * 마나3 쿨타임 5턴
     *
     * @param chantTurns 영창 진행 턴 수
     * @return 데미지 배율
     */
    public static double condensation(int chantTurns) {
        System.out.println("응집: 영창 " + chantTurns + "턴 진행");
        return 1.0 + (chantTurns * 0.50);
    }

    /**
     * 마력탄
     * 5D4
     * 마나2 쿨타임 3턴
     *
     * @param rolls 5D4 결과
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     */
    public static int[] magicBolt(int[] rolls, boolean usedManaCirculation, boolean usedMagicConcentration) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 마력 집중
     * 다음 스킬의 영창 시간 50%
     * 영창 시간 감소 효과를 2배로 받음
     * 마나4 쿨타임 6턴
     */
    public static void magicConcentration() {
        System.out.println("마력 집중: 다음 스킬 영창 시간 50%, 감소 효과 2배");
    }

    /**
     * 정신 소모
     * 스태미나 -> 마나 1:2 비율로 교환
     * (턴 소모X) 마나2 쿨타임 6턴
     *
     * @param staminaToConvert 교환할 스태미나
     * @return 얻을 마나
     */
    public static int mentalExhaustion(int staminaToConvert) {
        return staminaToConvert * 2;
    }

    /**
     * 에테르 카타스트로피
     * 5D20
     * 영창10턴 마나7 쿨타임10턴
     *
     * @param rolls 5D20 결과
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     */
    public static int[] etherCatastrophe(int[] rolls, boolean usedManaCirculation, boolean usedMagicConcentration) {
        int baseChant = 10;
        if (usedManaCirculation) baseChant -= 2;
        if (usedMagicConcentration) {
            baseChant = (int)(baseChant * 0.5);
            if (usedManaCirculation) baseChant -= 2; // 감소 효과 2배
        }

        System.out.println("에테르 카타스트로피: 최종 영창 시간 " + baseChant + "턴");

        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 루멘 컨버전 (광역)
     * 4D20
     * 피격 대상은 다음턴까지 [마나] 사용 봉인
     * 영창18턴 마나15 쿨타임20턴
     *
     * @param rolls 4D20 결과
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     */
    public static int[] lumenConversionAOE(int[] rolls, boolean usedManaCirculation, boolean usedMagicConcentration) {
        int baseChant = 18;
        if (usedManaCirculation) baseChant -= 2;
        if (usedMagicConcentration) {
            baseChant = (int)(baseChant * 0.5);
            if (usedManaCirculation) baseChant -= 2; // 감소 효과 2배
        }

        System.out.println("루멘 컨버전(광역): 최종 영창 시간 " + baseChant + "턴");

        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 루멘 컨버전 (단일)
     * 10D12
     * 피격 대상은 다음턴까지 [마나] 사용 봉인
     * 영창18턴 마나15 쿨타임20턴
     *
     * @param rolls 10D12 결과
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     */
    public static int[] lumenConversionSingle(int[] rolls, boolean usedManaCirculation, boolean usedMagicConcentration) {
        int baseChant = 18;
        if (usedManaCirculation) baseChant -= 2;
        if (usedMagicConcentration) {
            baseChant = (int)(baseChant * 0.5);
            if (usedManaCirculation) baseChant -= 2; // 감소 효과 2배
        }

        System.out.println("루멘 컨버전(단일): 최종 영창 시간 " + baseChant + "턴");

        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 어나일레이터
     * 공격 스킬을 2회 사용할 때까지 지속
     * 데미지 300% 마나 소모 200%
     * 마나3 쿨타임10턴
     */
    public static double annihilatorDamageMultiplier() {
        return 3.0;
    }

    public static double annihilatorManaCostMultiplier() {
        return 2.0;
    }

    /**
     * 오버로드 코어
     * 모든 스킬 쿨타임 초기화
     * 마나(사용횟수) x 5 쿨타임5턴
     *
     * @param useCount 사용 횟수
     * @return 필요 마나
     */
    public static int overloadCoreManaCost(int useCount) {
        return useCount * 5;
    }

    /**
     * 폭주오라 (전용수비)
     * 영창중 사용 가능
     * {(영창 기본 시간) - (남은 영창시간)} x 10% 받는 피해 감소
     * 이번턴 상태이상 1회 무시
     * 순환영향X 마나3
     *
     * @param baseChantTime 영창 기본 시간
     * @param remainingChantTime 남은 영창 시간
     * @return 받는 피해 배율 (1.0 = 100%)
     */
    public static double rampageAura(int baseChantTime, int remainingChantTime) {
        int elapsed = baseChantTime - remainingChantTime;
        double reduction = elapsed * 0.10;
        return Math.max(0, 1.0 - reduction);
    }
}

