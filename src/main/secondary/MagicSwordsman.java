package main.secondary;

import main.Main;

import java.io.PrintStream;

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
     * @param out 출력 스트림
     * @return 회복할 마나
     */
    public static int manaAura(int level, PrintStream out) {
        out.printf("마나 오라: 기본 공격 → 마나 %d 회복%n", level);
        return level;
    }

    /**
     * 오라 블레이드 (패시브)
     * 모든 스킬이 마나를 2배 소모
     * 데미지 300%
     *
     * @param out 출력 스트림
     */
    public static void auraBlade(PrintStream out) {
        out.println("오라 블레이드: 마나 소모 200%, 데미지 300%");
    }

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
     * @param out 출력 스트림
     * @return 회복할 마나
     */
    public static int manaCirculation(int manaSpentSinceLastRest, PrintStream out) {
        int recovered = manaSpentSinceLastRest / 2;
        out.printf("마나 순환: 소모 마나 %d → %d 회복%n", manaSpentSinceLastRest, recovered);
        return recovered;
    }

    /**
     * 마나 축적 (패시브)
     * [이전 휴식 시점의 마나 - 현재의 마나] x 10% 데미지 증가
     *
     * @param manaAtLastRest 이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double manaAccumulation(int manaAtLastRest, int currentMana, PrintStream out) {
        int manaDiff = Math.max(0, manaAtLastRest - currentMana);
        double multiplier = 1.0 + (manaDiff * 0.10);
        out.printf("마나 축적: 마나 차이 %d → x%.2f 데미지%n", manaDiff, multiplier);
        return multiplier;
    }

    // ===== 스킬 =====

    /**
     * 마검사 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("마검사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 마나 슬래쉬
     * 3D6
     * 마나3 쿨타임2턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int manaSlash(int intelligence, PrintStream out) {
        out.println("마검사-마나 슬래쉬 사용 (3D6)");
        int totalDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 6, out);
            out.printf("%d번째 슬래쉬: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 3 소모, 쿨타임 2턴");
        return totalDamage + sideDamage;
    }

    /**
     * 마나 스트라이크
     * 2D10
     * 마나3 쿨타임3턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int manaStrike(int intelligence, PrintStream out) {
        out.println("마검사-마나 스트라이크 사용 (2D10)");
        int totalDamage = 0;

        for (int i = 1; i <= 2; i++) {
            int diceResult = Main.dice(1, 10, out);
            out.printf("%d번째 스트라이크: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 3 소모, 쿨타임 3턴");
        return totalDamage + sideDamage;
    }

    /**
     * 마나 스피어
     * D20
     * 마나3 쿨타임3턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int manaSpear(int intelligence, PrintStream out) {
        out.println("마검사-마나 스피어 사용 (D20)");
        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 마나 3 소모, 쿨타임 3턴");
        return defaultDamage + sideDamage;
    }

    /**
     * 스핀 크라이스트
     * 4D8
     * 마나4 쿨타임4턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int spinChryst(int intelligence, PrintStream out) {
        out.println("마검사-스핀 크라이스트 사용 (4D8)");
        int totalDamage = 0;

        for (int i = 1; i <= 4; i++) {
            int diceResult = Main.dice(1, 8, out);
            out.printf("%d번째 회전: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 4 소모, 쿨타임 4턴");
        return totalDamage + sideDamage;
    }

    /**
     * 트리플 슬레인
     * 3D12
     * 마나4 쿨타임4턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int tripleSlain(int intelligence, PrintStream out) {
        out.println("마검사-트리플 슬레인 사용 (3D12)");
        int totalDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 12, out);
            out.printf("%d번째 슬레인: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 4 소모, 쿨타임 4턴");
        return totalDamage + sideDamage;
    }

    /**
     * 에테리얼 임페리오
     * 3D20
     * 영창2턴 마나6 쿨타임7턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int etherealImperio(int intelligence, PrintStream out) {
        out.println("마검사-에테리얼 임페리오 사용 (3D20)");
        out.println("※ 영창 2턴");
        int totalDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 20, out);
            out.printf("%d번째 임페리오: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 마나 6 소모, 쿨타임 7턴");
        return totalDamage + sideDamage;
    }

    /**
     * 오버로드
     * 휴식할 때까지 지속
     * 매턴 행동 2회
     * 소모마나 200%
     * 데미지 150%
     * 쿨타임 7턴
     */
    public static void overload(PrintStream out) {
        out.println("마검사-오버로드 사용");
        out.println("※ 휴식까지 지속: 매턴 행동 2회");
        out.println("※ 소모마나 200%, 데미지 150%");
        out.println("※ 쿨타임 7턴");
    }

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
     *
     * @param out 출력 스트림
     * @return 회복 마나
     */
    public static int speedDrain(PrintStream out) {
        out.println("마검사-스피드레인 사용 (마나 2D8 회복)");
        int totalRecovered = 0;

        for (int i = 1; i <= 2; i++) {
            int diceResult = Main.dice(1, 8, out);
            out.printf("%d번째 흡수: %d%n", i, diceResult);
            totalRecovered += diceResult;
        }

        out.printf("총 마나 회복: %d%n", totalRecovered);
        out.println("※ 마나 2 소모, 쿨타임 4턴");
        return totalRecovered;
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
     * @param out 출력 스트림
     * @return 회복할 마나
     */
    public static int shiftRister(int staminaSpent, PrintStream out) {
        out.println("마검사-쉬프트리스터 사용");
        out.printf("스태미나 %d 소모 → 마나 %d 회복%n", staminaSpent, staminaSpent);
        out.println("※ 턴 소모X, 쿨타임 6턴");
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
     * @param out 출력 스트림
     * @return 소모할 마나 (전부)
     */
    public static int etheilSola(int currentMana, PrintStream out) {
        out.println("마검사-에테일 솔라 사용");
        out.println("※ 데미지 300%");
        out.println("※ 3턴 후까지: 마나 소모X, 마나 회복 불가");
        out.println("※ 받는 피해 50%");
        out.println("※ 스킬 영창 삭제");
        out.printf("※ 모든 마나 %d 소모%n", currentMana);
        out.println("※ 쿨타임 10턴");
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
     * @param damageTaken 받는 데미지
     * @param out 출력 스트림
     * @return 감소된 데미지
     */
    public static int flowAura(int manaSpentInPreviousAction, int damageTaken, PrintStream out) {
        out.println("마검사-플로우 오라 사용 (전용 수비)");
        double reduction = manaSpentInPreviousAction * 0.05;
        double multiplier = Math.max(0, 1.0 - reduction);
        int reducedDamage = (int)(damageTaken * multiplier);
        out.printf("이전 마나 소모 %d → 피해 %.0f%% 감소: %d → %d%n",
                   manaSpentInPreviousAction, reduction * 100, damageTaken, reducedDamage);
        return reducedDamage;
    }
}


