package main.secondary;

/**
 * 빛의 사제 (Light Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 완벽한 힐서폿
 */
public class LightPriest {

    // ===== 패시브 스킬 =====

    /**
     * 신성한 육체 (패시브)
     * 아군 회복량의 10% 만큼 보호막 획득
     *
     * @param healAmount 회복량
     * @return 보호막 양
     */
    public static int holyBody(int healAmount) {
        return healAmount / 10;
    }

    /**
     * 자비 (패시브)
     * 본인이 전투 내에서 공격한 적이 없다면 회복량 150%
     *
     * @param hasAttacked 전투 내에서 공격 여부
     * @return 회복량 배율
     */
    public static double mercy(boolean hasAttacked) {
        return hasAttacked ? 1.0 : 1.5;
    }

    // ===== 스킬 =====

    /**
     * 힐
     * D6 회복
     * 마나1
     */
    public static int[] heal(int roll, boolean hasAttacked) {
        double multiplier = mercy(hasAttacked);
        return new int[]{(int)(roll * multiplier)};
    }

    /**
     * 치유의 바람
     * 2D6
     * 마나2 쿨타임 2턴
     */
    public static int[] healingWind(int[] rolls, boolean hasAttacked) {
        double multiplier = mercy(hasAttacked);
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{(int)(total * multiplier)};
    }

    /**
     * 빛의 성배
     * 5D4 회복
     * 마나3 쿨타임4턴
     */
    public static int[] chaliceOfLight(int[] rolls, boolean hasAttacked) {
        double multiplier = mercy(hasAttacked);
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{(int)(total * multiplier)};
    }

    /**
     * 기원
     * 다음턴까지 5D8 보호막 획득
     * 마나4 쿨타임6턴
     */
    public static int[] prayer(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 헤븐즈 도어
     * 1명의 죽음을 1회 극복
     * 4D10 체력으로 부활
     * 마나15 쿨타임 20턴
     */
    public static int[] heavensDoor(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 편애
     * [축복] 대상지정 효과 -> 아군 1명
     * 회복량 250%
     * 전투내 영구 지속, 취소 불가
     * 마나6
     */
    public static double favoritism() {
        return 2.5;
    }

    /**
     * 양도
     * [신성한 육체]효과 제거
     * 본인 회복 불가
     * 회복량 150%
     * 전투 내 영구 지속, 취소 불가
     * 마나4
     */
    public static double transfer() {
        return 1.5;
    }

    /**
     * 증오
     * [자비] 회복량 150% -> 아군 데미지 150%
     * 전투 내 영구 지속, 취소 불가
     * 마나13
     */
    public static double hatred() {
        return 1.5;
    }

    /**
     * 이기심
     * [이타심]효과 제거
     * 아군 치유시 (이번턴 치유량) x 10% 해당 아군 데미지 증가
     * 전투 내 영구 지속, 취소 불가
     * 마나12
     *
     * @param healAmount 치유량
     * @return 데미지 배율
     */
    public static double selfishness(int healAmount) {
        return 1.0 + (healAmount * 0.10);
    }

    /**
     * 신앙심
     * 다음턴 모든 적 행동 불가
     * 회복량 200%
     * 모든 아군 데미지 150%
     * 마나4 쿨타임7턴
     */
    public static double faithHealMultiplier() {
        return 2.0;
    }

    public static double faithDamageMultiplier() {
        return 1.5;
    }
}

