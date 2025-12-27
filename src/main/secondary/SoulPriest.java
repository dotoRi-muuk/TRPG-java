package main.secondary;

/**
 * 영혼의 사제 (Soul Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 디버프, 딜, 왕귀
 */
public class SoulPriest {

    // ===== 패시브 스킬 =====

    /**
     * 축복 (패시브)
     * [영혼] 누적에 따라 아래 효과 얻음
     * 3개 적의 모든 스탯 -2
     * 5개 적이 가하는 피해 75%
     * 10개 받는 피해 75%
     * 20개 데미지 영혼 x 20% 증가 (최대 600%)
     *
     * @param souls 영혼 개수
     * @return 데미지 배율
     */
    public static double blessingDamageBonus(int souls) {
        if (souls < 20) return 1.0;
        double bonus = Math.min(souls * 20.0, 600.0);
        return 1.0 + (bonus / 100.0);
    }

    public static double blessingEnemyDamageReduction(int souls) {
        return (souls >= 5) ? 0.75 : 1.0;
    }

    public static double blessingDefense(int souls) {
        return (souls >= 10) ? 0.75 : 1.0;
    }

    /**
     * 신비한 육체 (패시브)
     * 마나 0으로 고정
     * (마나)/5 만큼 전투 시작 시 영혼 획득
     * (소수점 내림)
     *
     * @param mana 마나 (전투 시작 시)
     * @return 영혼 개수
     */
    public static int mysteriousBody(int mana) {
        return mana / 5;
    }

    // ===== 스킬 =====

    /**
     * 흡수
     * 2D4 회복
     * [영혼] 1개 소모
     */
    public static int[] absorb(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 저주
     * 3D4
     * 다음 턴까지 가하는 피해 50% 부여
     * [영혼]2개 소모
     * 쿨타임 4턴
     */
    public static int[] curse(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    public static double curseDebuff() {
        return 0.5;
    }

    /**
     * 흉통
     * 8D4
     * 다음 턴까지 받는 피해 150% 부여
     * [영혼] 3개 소모
     * 쿨타임 6턴
     */
    public static int[] chestPain(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    public static double chestPainDebuff() {
        return 1.5;
    }

    /**
     * 원한
     * ([영혼] 소모 개수)D12
     * 쿨타임 8턴
     *
     * @param soulsSpent 소모한 영혼 개수
     * @param rolls 주사위 결과들
     * @return 총 데미지
     */
    public static int[] grudge(int soulsSpent, int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 수거
     * 영혼 D4개 획득
     * 쿨타임 6턴
     *
     * @param roll D4 결과
     * @return 획득할 영혼 개수
     */
    public static int collect(int roll) {
        return roll;
    }

    /**
     * 구속
     * 1명의 적에게 ([영혼] 소모 개수)/2턴 후까지 행동불가 부여(소수점 내림)
     * 쿨타임 10턴
     *
     * @param soulsSpent 소모한 영혼 개수
     * @return 행동불가 턴 수
     */
    public static int restraint(int soulsSpent) {
        return soulsSpent / 2;
    }

    /**
     * 폐허
     * [축복]효과 변경
     * 10개 자신 제외 모두의 스탯 -5
     * 15개 자신 제외 모두의 가하는 피해 50%
     * 20개 자신 받는 피해 50% / 자신 제외 모두가 받는 피해 150%
     * 30개 데미지 영혼 x 30% 증가 (최대 1200%)
     * 전투 내 영구 지속, 취소 불가
     * [영혼]5개 소모
     *
     * @param souls 영혼 개수
     * @return 데미지 배율
     */
    public static double ruinsDamageBonus(int souls) {
        if (souls < 30) return 1.0;
        double bonus = Math.min(souls * 30.0, 1200.0);
        return 1.0 + (bonus / 100.0);
    }

    public static double ruinsOtherDamage(int souls) {
        return (souls >= 15) ? 0.5 : 1.0;
    }

    public static double ruinsSelfDefense(int souls) {
        return (souls >= 20) ? 0.5 : 1.0;
    }

    public static double ruinsOtherDefense(int souls) {
        return (souls >= 20) ? 1.5 : 1.0;
    }

    /**
     * 수명 단축
     * 모든 아군 최대 체력이 50%로 감소
     * 자신의 모든 스킬 쿨타임 50%
     * 전투 내 영구 지속, 취소 불가
     * [영혼] 4개 소모
     */
    public static double lifeShortening() {
        return 0.5;
    }

    /**
     * 처형
     * 아군 1명 즉시 처형
     * [영혼] 3개 획득
     * (해당 아군 전투 내 부활 불가)
     * (영구적으로 데미지 150%)
     */
    public static int executionSoulsGained() {
        return 3;
    }

    public static double executionDamageBonus() {
        return 1.5;
    }

    /**
     * 신앙심
     * 사망 시 발동, 다음 턴까지 자신 제외 모두 행동불가
     * [영혼]이 20개 이상이라면 절반을 소모
     * (소모량) x 5% 체력으로 부활
     * 전투 당 1회
     *
     * @param souls 영혼 개수
     * @return 부활 체력 퍼센트
     */
    public static double faithReviveHp(int souls) {
        if (souls < 20) return 0;
        int consumed = souls / 2;
        return consumed * 5.0;
    }
}

