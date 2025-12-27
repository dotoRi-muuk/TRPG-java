package main.secondary;

/**
 * 연금술사 (Alchemist)
 * 주 스탯: 지능 (지혜)
 * 특징: 디버프 발사대
 */
public class Alchemist {

    // ===== 패시브 스킬 =====

    /**
     * 연금 준비 (패시브)
     * [미지의 물약] 5개를 가지고 전투 시작
     * 공격기 없는 스킬도 판정 진행
     * 가진 [미지의 물약] 1개당 효과/데미지 20% 증가 (최대 200%)
     *
     * @param unknownPotions 미지의 물약 개수
     * @return 데미지 배율
     */
    public static double alchemyPreparation(int unknownPotions) {
        int bonus = Math.min(unknownPotions * 20, 200);
        return 1.0 + (bonus / 100.0);
    }

    /**
     * 허약 (패시브)
     * 디버프를 가진 적에게 가하는 피해 200%
     * 적의 체력이 50% 이하일 때 250%로 증폭
     *
     * @param hasDebuff 적이 디버프를 가지고 있는지
     * @param enemyHpPercent 적의 체력 퍼센트
     * @return 데미지 배율
     */
    public static double weakness(boolean hasDebuff, double enemyHpPercent) {
        if (!hasDebuff) return 1.0;
        if (enemyHpPercent <= 50.0) return 2.5;
        return 2.0;
    }

    // ===== 스킬 =====

    /**
     * 독성물약
     * [미지의 물약] 1개 소모
     * 5D4 + 1턴동안 적 모든 스탯 -1
     * 마나3
     */
    public static int[] toxicPotion(int[] rolls, int unknownPotions) {
        double multiplier = alchemyPreparation(unknownPotions);
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{(int)(total * multiplier)};
    }

    /**
     * 폭발물약
     * [미지의 물약] 1개 소모
     * 5D6
     * 마나4
     */
    public static int[] explosivePotion(int[] rolls, int unknownPotions) {
        double multiplier = alchemyPreparation(unknownPotions);
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{(int)(total * multiplier)};
    }

    /**
     * 회복물약
     * [미지의 물약] 1개 소모
     * 아군 1명 체력 D10 회복
     * 마나3
     */
    public static int[] healingPotion(int roll, int unknownPotions) {
        double multiplier = alchemyPreparation(unknownPotions);
        return new int[]{(int)(roll * multiplier)};
    }

    /**
     * 성급한 준비
     * [미지의 물약] D8개 생성
     * 마나5 쿨타임 5턴
     *
     * @param diceResult D8 주사위 결과
     * @return 생성될 물약 개수
     */
    public static int hastyPreparation(int diceResult) {
        return diceResult;
    }

    /**
     * 완벽한 준비
     * [미지의 물약] D20개 생성
     * 영창3턴 마나7 쿨타임8턴
     *
     * @param diceResult D20 주사위 결과
     * @return 생성될 물약 개수
     */
    public static int perfectPreparation(int diceResult) {
        System.out.println("완벽한 준비: 영창 3턴");
        return diceResult;
    }
}

