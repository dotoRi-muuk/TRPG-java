package main.secondary;

import main.Main;

import java.io.PrintStream;

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
     * @param out 출력 스트림
     * @return 수비 무시 성공 여부
     */
    public static boolean magicScholar(int originalChantTime, PrintStream out) {
        int probability = originalChantTime * 10;
        int threshold = (probability * 20) / 100; // D20 기준으로 변환
        int diceRoll = Main.dice(1, 20, out);
        boolean success = diceRoll <= threshold;

        out.printf("마도학: 수비 무시 확률 %d%% (D20 <= %d), 결과: %s%n",
                   probability, threshold, success ? "성공" : "실패");
        return success;
    }

    /**
     * 마력의 범람 (패시브)
     * (하나의 스킬 발동에 소모한 총 마나) x 30% 데미지 증가
     *
     * @param totalManaSpent 총 소모 마나
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double manaFlood(int totalManaSpent, PrintStream out) {
        double multiplier = 1.0 + (totalManaSpent * 0.30);
        out.printf("마력의 범람: 마나 %d 소모 → x%.2f 데미지%n", totalManaSpent, multiplier);
        return multiplier;
    }

    // ===== 스킬 =====

    /**
     * 마력 순환
     * 다음 영창 스킬의 영창 시간 2턴 감소
     * 마나2 쿨타임 4턴
     */
    public static void manaCirculation(PrintStream out) {
        out.println("마도사-마력 순환 사용");
        out.println("※ 다음 영창 스킬의 영창 시간 -2턴");
        out.println("※ 마나 2 소모, 쿨타임 4턴");
    }

    /**
     * 응집
     * 영창 진행(지혜x)
     * 다음 스킬 피해 (영창 진행 턴) x 50% 데미지 증가
     * 마나3 쿨타임 5턴
     *
     * @param chantTurns 영창 진행 턴 수
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double condensation(int chantTurns, PrintStream out) {
        out.println("마도사-응집 사용");
        out.printf("영창 %d턴 진행%n", chantTurns);
        double multiplier = 1.0 + (chantTurns * 0.50);
        out.printf("다음 스킬 데미지: x%.2f%n", multiplier);
        out.println("※ 마나 3 소모, 쿨타임 5턴");
        return multiplier;
    }

    /**
     * 마력탄
     * 5D4
     * 마나2 쿨타임 3턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int magicBolt(int intelligence, PrintStream out) {
        out.println("마도사-마력탄 사용 (5D4)");
        int baseDamage = 0;

        for (int i = 1; i <= 5; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 마력탄: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 마나 2 소모, 쿨타임 3턴");
        return totalDamage;
    }

    /**
     * 마력 집중
     * 다음 스킬의 영창 시간 50%
     * 영창 시간 감소 효과를 2배로 받음
     * 마나4 쿨타임 6턴
     */
    public static void magicConcentration(PrintStream out) {
        out.println("마도사-마력 집중 사용");
        out.println("※ 다음 스킬 영창 시간 50%, 감소 효과 2배");
        out.println("※ 마나 4 소모, 쿨타임 6턴");
    }

    /**
     * 정신 소모
     * 스태미나 -> 마나 1:2 비율로 교환
     * (턴 소모X) 마나2 쿨타임 6턴
     *
     * @param staminaToConvert 교환할 스태미나
     * @param out 출력 스트림
     * @return 얻을 마나
     */
    public static int mentalExhaustion(int staminaToConvert, PrintStream out) {
        out.println("마도사-정신 소모 사용");
        int manaGained = staminaToConvert * 2;
        out.printf("스태미나 %d → 마나 %d 획득%n", staminaToConvert, manaGained);
        out.println("※ 턴 소모X, 마나 2 소모, 쿨타임 6턴");
        return manaGained;
    }

    /**
     * 에테르 카타스트로피
     * 5D20
     * 영창10턴 마나7 쿨타임10턴
     *
     * @param intelligence 지능 스탯
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int etherCatastrophe(int intelligence, boolean usedManaCirculation, boolean usedMagicConcentration, PrintStream out) {
        out.println("마도사-에테르 카타스트로피 사용 (5D20)");

        int baseChant = 10;
        if (usedManaCirculation) baseChant -= 2;
        if (usedMagicConcentration) {
            baseChant = (int)(baseChant * 0.5);
            if (usedManaCirculation) baseChant -= 2; // 감소 효과 2배
        }
        out.printf("최종 영창 시간: %d턴%n", baseChant);

        int baseDamage = 0;
        for (int i = 1; i <= 5; i++) {
            int diceResult = Main.dice(1, 20, out);
            out.printf("%d번째 에테르: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 영창 10턴, 마나 7 소모, 쿨타임 10턴");
        return totalDamage;
    }

    /**
     * 루멘 컨버전 (광역)
     * 4D20
     * 피격 대상은 다음턴까지 [마나] 사용 봉인
     * 영창18턴 마나15 쿨타임20턴
     *
     * @param intelligence 지능 스탯
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int lumenConversionAOE(int intelligence, boolean usedManaCirculation, boolean usedMagicConcentration, PrintStream out) {
        out.println("마도사-루멘 컨버전(광역) 사용 (4D20)");

        int baseChant = 18;
        if (usedManaCirculation) baseChant -= 2;
        if (usedMagicConcentration) {
            baseChant = (int)(baseChant * 0.5);
            if (usedManaCirculation) baseChant -= 2; // 감소 효과 2배
        }
        out.printf("최종 영창 시간: %d턴%n", baseChant);

        int baseDamage = 0;
        for (int i = 1; i <= 4; i++) {
            int diceResult = Main.dice(1, 20, out);
            out.printf("%d번째 루멘: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 피격 대상 다음턴까지 마나 사용 봉인");
        out.println("※ 영창 18턴, 마나 15 소모, 쿨타임 20턴");
        return totalDamage;
    }

    /**
     * 루멘 컨버전 (단일)
     * 10D12
     * 피격 대상은 다음턴까지 [마나] 사용 봉인
     * 영창18턴 마나15 쿨타임20턴
     *
     * @param intelligence 지능 스탯
     * @param usedManaCirculation 마력 순환 사용 여부
     * @param usedMagicConcentration 마력 집중 사용 여부
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int lumenConversionSingle(int intelligence, boolean usedManaCirculation, boolean usedMagicConcentration, PrintStream out) {
        out.println("마도사-루멘 컨버전(단일) 사용 (10D12)");

        int baseChant = 18;
        if (usedManaCirculation) baseChant -= 2;
        if (usedMagicConcentration) {
            baseChant = (int)(baseChant * 0.5);
            if (usedManaCirculation) baseChant -= 2; // 감소 효과 2배
        }
        out.printf("최종 영창 시간: %d턴%n", baseChant);

        int baseDamage = 0;
        for (int i = 1; i <= 10; i++) {
            int diceResult = Main.dice(1, 12, out);
            out.printf("%d번째 루멘: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 피격 대상 다음턴까지 마나 사용 봉인");
        out.println("※ 영창 18턴, 마나 15 소모, 쿨타임 20턴");
        return totalDamage;
    }

    /**
     * 어나일레이터
     * 공격 스킬을 2회 사용할 때까지 지속
     * 데미지 300% 마나 소모 200%
     * 마나3 쿨타임10턴
     */
    public static void annihilator(PrintStream out) {
        out.println("마도사-어나일레이터 사용");
        out.println("※ 공격 스킬 2회 사용까지: 데미지 300%, 마나 소모 200%");
        out.println("※ 마나 3 소모, 쿨타임 10턴");
    }

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
     * @param out 출력 스트림
     * @return 필요 마나
     */
    public static int overloadCore(int useCount, PrintStream out) {
        out.println("마도사-오버로드 코어 사용");
        int manaCost = useCount * 5;
        out.printf("모든 스킬 쿨타임 초기화, 마나 %d 소모%n", manaCost);
        out.println("※ 쿨타임 5턴");
        return manaCost;
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
     * @param damageTaken 받는 데미지
     * @param out 출력 스트림
     * @return 감소된 데미지
     */
    public static int rampageAura(int baseChantTime, int remainingChantTime, int damageTaken, PrintStream out) {
        out.println("마도사-폭주오라 사용 (전용 수비)");
        int elapsed = baseChantTime - remainingChantTime;
        double reduction = elapsed * 0.10;
        double multiplier = Math.max(0, 1.0 - reduction);
        int reducedDamage = (int)(damageTaken * multiplier);
        out.printf("영창 진행 %d턴 → 피해 %.0f%% 감소: %d → %d%n",
                   elapsed, reduction * 100, damageTaken, reducedDamage);
        out.println("※ 이번턴 상태이상 1회 무시");
        out.println("※ 마나 3 소모");
        return reducedDamage;
    }
}

