package main.secondary;

/**
 * 마검사 (Magic Swordsman)
 * 주 스탯: 지능 (지혜, 마나)
 * 특징: 지팡이는 거둘 뿐
 */
public class MagicSwordsman {

    // ===== 패시브 스킬 =====

    /**
     * 마나 오라 (패시브)
     * 기본 공격 시 마나 (자신의 레벨)만큼 회복
     *
     * @param level 캐릭터 레벨
     * @return 회복할 마나
     */
    public static int manaAura(int level) {
        return level;
    }

    /**
     * 오라 블레이드 (패시브)
     * 모든 스킬이 마나를 2배 소모
     * 데미지 300%
     */
    public static double auraBladeManaCostMultiplier() {
        return 2.0;
    }

    public static double auraBladeDamageMultiplier() {
        return 3.0;
    }

    /**
     * 마나 순환 (패시브)
     * 휴식 시 이전 휴식 이후로 소모한 마나의 50% 회복
     *
     * @param manaSpentSinceLastRest 이전 휴식 이후 소모한 마나
     * @return 회복할 마나
     */
    public static int manaCirculation(int manaSpentSinceLastRest) {
        return manaSpentSinceLastRest / 2;
    }

    /**
     * 마나 축적 (패시브)
     * [이전 휴식 시점의 마나 - 현재의 마나] x 10% 데미지 증가
     *
     * @param manaAtLastRest 이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @return 데미지 배율
     */
    public static double manaAccumulation(int manaAtLastRest, int currentMana) {
        int manaDiff = Math.max(0, manaAtLastRest - currentMana);
        return 1.0 + (manaDiff * 0.10);
    }

    // ===== 스킬 =====

    /**
     * 마나 슬래쉬
     * 3D6
     * 마나3 쿨타임2턴
     */
    public static int[] manaSlash(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 마나 스트라이크
     * 2D10
     * 마나3 쿨타임3턴
     */
    public static int[] manaStrike(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 마나 스피어
     * D20
     * 마나3 쿨타임3턴
     */
    public static int[] manaSpear(int roll) {
        return new int[]{roll};
    }

    /**
     * 스핀 크라이스트
     * 4D8
     * 마나4 쿨타임4턴
     */
    public static int[] spinChryst(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 트리플 슬레인
     * 3D12
     * 마나4 쿨타임4턴
     */
    public static int[] tripleSlain(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 에테리얼 임페리오
     * 3D20
     * 영창2턴 마나6 쿨타임7턴
     */
    public static int[] etherealImperio(int[] rolls) {
        System.out.println("에테리얼 임페리오: 영창 2턴");
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 오버로드
     * 휴식할 때까지 지속
     * 매턴 행동 2회
     * 소모마나 200%
     * 데미지 150%
     * 쿨타임 7턴
     */
    public static double overloadManaCostMultiplier() {
        return 2.0;
    }

    public static double overloadDamageMultiplier() {
        return 1.5;
    }

    /**
     * 스피드레인
     * 마나2D8 회복
     * 마나2 쿨타임4턴
     */
    public static int[] speedDrain(int[] rolls) {
        int total = 0;
        for (int roll : rolls) {
            total += roll;
        }
        return new int[]{total};
    }

    /**
     * 쉬프트리스터
     * 이번턴 스킬로 공격 성공 시 발동 가능
     * 마나 대신 스태미나 소모
     * 해당 값만큼 마나 회복
     * (턴 소모X)
     * 쿨타임 6턴
     *
     * @param staminaSpent 소모한 스태미나
     * @return 회복할 마나
     */
    public static int shiftRister(int staminaSpent) {
        return staminaSpent;
    }

    /**
     * 에테일 솔라
     * 데미지 300%
     * 3턴 후까지 모든 스킬이 마나 소모X
     * 3턴 후까지 마나 회복 불가
     * 받는 피해 50%
     * 스킬 영창 삭제
     * 모든 마나 소모
     * 쿨타임 10턴
     *
     * @param currentMana 현재 마나
     * @return 소모할 마나 (전부)
     */
    public static int etheilSola(int currentMana) {
        return currentMana;
    }

    public static double etheilSolaDamageMultiplier() {
        return 3.0;
    }

    public static double etheilSolaDamageReduction() {
        return 0.5;
    }

    /**
     * 플로우 오라 (전용 수비)
     * [이전 행동에 소모한 마나] x 5% 받는 피해 감소
     *
     * @param manaSpentInPreviousAction 이전 행동에 소모한 마나
     * @return 받는 피해 배율 (1.0 = 100%)
     */
    public static double flowAura(int manaSpentInPreviousAction) {
        double reduction = manaSpentInPreviousAction * 0.05;
        return Math.max(0, 1.0 - reduction);
    }
}

