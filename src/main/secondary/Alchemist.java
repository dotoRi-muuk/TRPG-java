package main.secondary;

import main.Main;

import java.io.PrintStream;

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
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double alchemyPreparation(int unknownPotions, PrintStream out) {
        int bonus = Math.min(unknownPotions * 20, 200);
        double multiplier = 1.0 + (bonus / 100.0);
        out.printf("연금 준비: 물약 %d개 → +%d%% (x%.2f)%n", unknownPotions, bonus, multiplier);
        return multiplier;
    }

    /**
     * 허약 (패시브)
     * 디버프를 가진 적에게 가하는 피해 200%
     * 적의 체력이 50% 이하일 때 250%로 증폭
     *
     * @param hasDebuff 적이 디버프를 가지고 있는지
     * @param enemyHpPercent 적의 체력 퍼센트
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double weakness(boolean hasDebuff, double enemyHpPercent, PrintStream out) {
        if (!hasDebuff) {
            out.println("허약: 적에게 디버프 없음");
            return 1.0;
        }
        if (enemyHpPercent <= 50.0) {
            out.printf("허약: 디버프 + 체력 %.0f%% (50%% 이하) → x2.5%n", enemyHpPercent);
            return 2.5;
        }
        out.println("허약: 디버프 있음 → x2.0");
        return 2.0;
    }

    // ===== 스킬 =====

    /**
     * 연금술사 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("연금술사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 독성물약
     * [미지의 물약] 1개 소모
     * 5D4 + 1턴동안 적 모든 스탯 -1
     * 마나3
     *
     * @param intelligence 지능 스탯
     * @param unknownPotions 미지의 물약 개수
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int toxicPotion(int intelligence, int unknownPotions, PrintStream out) {
        out.println("연금술사-독성물약 사용 (5D4)");
        out.println("※ 미지의 물약 1개 소모");

        double multiplier = alchemyPreparation(unknownPotions, out);

        int totalDamage = 0;
        for (int i = 1; i <= 5; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 독: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        int baseDamage = totalDamage + sideDamage;
        int finalDamage = (int)(baseDamage * multiplier);
        out.printf("총 데미지 : (%d + %d) x %.2f = %d%n", totalDamage, sideDamage, multiplier, finalDamage);
        out.println("※ 1턴동안 적 모든 스탯 -1");
        out.println("※ 마나 3 소모");
        return finalDamage;
    }

    /**
     * 폭발물약
     * [미지의 물약] 1개 소모
     * 5D6
     * 마나4
     *
     * @param intelligence 지능 스탯
     * @param unknownPotions 미지의 물약 개수
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int explosivePotion(int intelligence, int unknownPotions, PrintStream out) {
        out.println("연금술사-폭발물약 사용 (5D6)");
        out.println("※ 미지의 물약 1개 소모");

        double multiplier = alchemyPreparation(unknownPotions, out);

        int totalDamage = 0;
        for (int i = 1; i <= 5; i++) {
            int diceResult = Main.dice(1, 6, out);
            out.printf("%d번째 폭발: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(totalDamage, intelligence, out);
        int baseDamage = totalDamage + sideDamage;
        int finalDamage = (int)(baseDamage * multiplier);
        out.printf("총 데미지 : (%d + %d) x %.2f = %d%n", totalDamage, sideDamage, multiplier, finalDamage);
        out.println("※ 마나 4 소모");
        return finalDamage;
    }

    /**
     * 회복물약
     * [미지의 물약] 1개 소모
     * 아군 1명 체력 D10 회복
     * 마나3
     *
     * @param unknownPotions 미지의 물약 개수
     * @param out 출력 스트림
     * @return 회복량
     */
    public static int healingPotion(int unknownPotions, PrintStream out) {
        out.println("연금술사-회복물약 사용 (D10 회복)");
        out.println("※ 미지의 물약 1개 소모");

        double multiplier = alchemyPreparation(unknownPotions, out);

        int healRoll = Main.dice(1, 10, out);
        int finalHeal = (int)(healRoll * multiplier);
        out.printf("회복량: %d x %.2f = %d%n", healRoll, multiplier, finalHeal);
        out.println("※ 마나 3 소모");
        return finalHeal;
    }

    /**
     * 성급한 준비
     * [미지의 물약] D8개 생성
     * 마나5 쿨타임 5턴
     *
     * @param out 출력 스트림
     * @return 생성될 물약 개수
     */
    public static int hastyPreparation(PrintStream out) {
        out.println("연금술사-성급한 준비 사용");
        int potions = Main.dice(1, 8, out);
        out.printf("미지의 물약 %d개 생성%n", potions);
        out.println("※ 마나 5 소모, 쿨타임 5턴");
        return potions;
    }

    /**
     * 완벽한 준비
     * [미지의 물약] D20개 생성
     * 영창3턴 마나7 쿨타임8턴
     *
     * @param out 출력 스트림
     * @return 생성될 물약 개수
     */
    public static int perfectPreparation(PrintStream out) {
        out.println("연금술사-완벽한 준비 사용");
        out.println("※ 영창 3턴");
        int potions = Main.dice(1, 20, out);
        out.printf("미지의 물약 %d개 생성%n", potions);
        out.println("※ 마나 7 소모, 쿨타임 8턴");
        return potions;
    }
}


