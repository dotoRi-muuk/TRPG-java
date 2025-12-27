package main.secondary;

import main.Main;

import java.io.PrintStream;

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
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double bond(int basicCount, int intermediateCount, int advancedCount,
                              int specialCount, int transcendentCount, int omnipotentCount, PrintStream out) {
        double bonus = 0;
        bonus += basicCount * 5.0;
        bonus += intermediateCount * 10.0;
        bonus += advancedCount * 15.0;
        bonus += specialCount * 20.0;
        bonus += transcendentCount * 25.0;
        bonus += omnipotentCount * 50.0;
        double multiplier = 1.0 + (bonus / 100.0);
        out.printf("유대감: 총 보너스 %.0f%% → x%.2f 데미지%n", bonus, multiplier);
        return multiplier;
    }

    // ===== 스킬 =====

    /**
     * 소환술사 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("소환술사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 소환
     * 길들인 소환수를 소환시 [소환패] 1개 소모
     * 길들이지 않은 소환수도 소환 가능
     * 마나3 쿨타임 8턴
     * 전장의 소환수는 매턴 마나를 소모하게 한다
     * (하급 0, 중급 1, 돌파 2, 특급 3, 초월 4, 전능 6)
     *
     * @param summonGrade 소환수 등급 (0=하급, 1=중급, 2=돌파, 3=특급, 4=초월, 6=전능)
     * @param out 출력 스트림
     * @return 매턴 소모 마나
     */
    public static int summon(int summonGrade, PrintStream out) {
        out.println("소환술사-소환 사용");
        int manaCost = switch (summonGrade) {
            case 0 -> 0; // 하급
            case 1 -> 1; // 중급
            case 2 -> 2; // 돌파
            case 3 -> 3; // 특급
            case 4 -> 4; // 초월
            case 6 -> 6; // 전능
            default -> 0;
        };
        String gradeName = switch (summonGrade) {
            case 0 -> "하급";
            case 1 -> "중급";
            case 2 -> "돌파";
            case 3 -> "특급";
            case 4 -> "초월";
            case 6 -> "전능";
            default -> "알 수 없음";
        };
        out.printf("소환수 등급: %s, 매턴 마나 %d 소모%n", gradeName, manaCost);
        out.println("※ 소환패 1개 소모");
        out.println("※ 마나 3 소모, 쿨타임 8턴");
        return manaCost;
    }

    /**
     * 소환수를 이기는 주먹
     * D10
     * 스태3
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int punchToBeatSummon(int intelligence, PrintStream out) {
        out.println("소환술사-소환수를 이기는 주먹 사용 (D10)");
        int defaultDamage = Main.dice(1, 10, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 3 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 말을 잘 듣게 하는 주먹
     * D10
     * 스태2
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int punchToObey(int intelligence, PrintStream out) {
        out.println("소환술사-말을 잘 듣게 하는 주먹 사용 (D10)");
        int defaultDamage = Main.dice(1, 10, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 2 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 소환해제
     * 소환한 소환수를 소환 해제시킴
     * 이후 [지혜] 판정 성공 시 소환패를 1개 얻음
     * 판정: (지혜 스탯 - D20) > 0
     *
     * @param wisdomStat 지혜 스탯
     * @param out 출력 스트림
     * @return 판정 성공 여부
     */
    public static boolean dismissSummon(int wisdomStat, PrintStream out) {
        out.println("소환술사-소환해제 사용");
        int diceRoll = Main.dice(1, 20, out);
        boolean success = (wisdomStat - diceRoll) > 0;
        out.printf("지혜 판정: %d - %d = %d (%s)%n",
                   wisdomStat, diceRoll, wisdomStat - diceRoll,
                   success ? "성공 → 소환패 획득" : "실패");
        return success;
    }

    /**
     * 소환 증폭
     * 전투 내 영구적으로 해당 소환수가 가하는 피해 200/(현재 소환된 소환수의 수)% 증가
     * (중첩 불가)
     * 마나5 쿨타임5턴
     *
     * @param currentSummonCount 현재 소환된 소환수 개수
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double summonAmplification(int currentSummonCount, PrintStream out) {
        out.println("소환술사-소환 증폭 사용");
        if (currentSummonCount == 0) {
            out.println("소환수가 없습니다.");
            return 1.0;
        }
        double bonus = 200.0 / currentSummonCount;
        double multiplier = 1.0 + (bonus / 100.0);
        out.printf("소환수 %d마리 → 피해 +%.0f%% (x%.2f)%n", currentSummonCount, bonus, multiplier);
        out.println("※ 마나 5 소모, 쿨타임 5턴");
        return multiplier;
    }

    /**
     * 결속의 끈
     * 소환 후 5턴이 지난 소환수에게 사용 가능
     * 전투 내에서 영구적으로 소환수가 가하는 피해 200%
     * 마나7 쿨타임 7턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double bondOfConnection(PrintStream out) {
        out.println("소환술사-결속의 끈 사용");
        out.println("※ 소환 후 5턴이 지난 소환수에게 사용 가능");
        out.println("※ 소환수 피해 200%");
        out.println("※ 마나 7 소모, 쿨타임 7턴");
        return 2.0;
    }
}

