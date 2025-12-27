package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Gambler {

    /**
     * 겜블러 기본공격
     * @param stat 사용할 스탯
     * @param reducedLuck 일확천금 패시브 (감소한 운 스탯)
     * @param jackpotActive 일확천금 패시브 적용 여부
     */
    public static int plain(int stat, int reducedLuck, boolean jackpotActive, PrintStream out) {
        out.println("겜블러-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 일확천금 패시브
        if (jackpotActive && reducedLuck > 0) {
            double multiplier = 1.0 + (reducedLuck * 0.5);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("일확천금 패시브 적용: 감소 운 %d → x%.1f = %d%n", reducedLuck, multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 코인 토스 기술
     * D4, 운 판정 성공 시 D12
     * 스태미나 1 소모
     */
    public static int coinToss(int stat, int luck, int reducedLuck, boolean jackpotActive, PrintStream out) {
        out.println("겜블러-코인 토스 사용");
        int defaultDamage = Main.dice(1, 4, out);

        // 운 판정
        int luckCheck = luck - Main.dice(1, 20, out);
        out.printf("운 판정: %d - D20 = %d%n", luck, luckCheck);

        if (luckCheck > 0) {
            out.println("운 판정 성공! D12로 변경");
            defaultDamage = Main.dice(1, 12, out);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (jackpotActive && reducedLuck > 0) {
            double multiplier = 1.0 + (reducedLuck * 0.5);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("일확천금 패시브 적용: x%.1f → %d%n", multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * 조커 카드 기술
     * D6, 운 판정 성공 시 2D12
     * 스태미나 3 소모
     */
    public static int jokerCard(int stat, int luck, int reducedLuck, boolean jackpotActive, PrintStream out) {
        out.println("겜블러-조커 카드 사용");
        int defaultDamage = Main.dice(1, 6, out);

        int luckCheck = luck - Main.dice(1, 20, out);
        out.printf("운 판정: %d - D20 = %d%n", luck, luckCheck);

        if (luckCheck > 0) {
            out.println("운 판정 성공! 2D12로 변경");
            defaultDamage = Main.dice(2, 12, out);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (jackpotActive && reducedLuck > 0) {
            double multiplier = 1.0 + (reducedLuck * 0.5);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("일확천금 패시브 적용: x%.1f → %d%n", multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 블랙잭 기술
     * D6, 운 판정 성공 시 3D8
     * 스태미나 3 소모
     */
    public static int blackjack(int stat, int luck, int reducedLuck, boolean jackpotActive, PrintStream out) {
        out.println("겜블러-블랙잭 사용");
        int defaultDamage = Main.dice(1, 6, out);

        int luckCheck = luck - Main.dice(1, 20, out);
        out.printf("운 판정: %d - D20 = %d%n", luck, luckCheck);

        if (luckCheck > 0) {
            out.println("운 판정 성공! 3D8로 변경");
            defaultDamage = Main.dice(3, 8, out);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (jackpotActive && reducedLuck > 0) {
            double multiplier = 1.0 + (reducedLuck * 0.5);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("일확천금 패시브 적용: x%.1f → %d%n", multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 야추 다이스 기술
     * D8, 운 판정 2회 성공 시 2D20
     * 스태미나 4 소모
     */
    public static int yatzyDice(int stat, int luck, int reducedLuck, boolean jackpotActive, PrintStream out) {
        out.println("겜블러-야추 다이스 사용");
        int defaultDamage = Main.dice(1, 8, out);

        // 운 판정 1회
        int luckCheck1 = luck - Main.dice(1, 20, out);
        out.printf("운 판정 1회: %d - D20 = %d%n", luck, luckCheck1);

        // 운 판정 2회
        int luckCheck2 = luck - Main.dice(1, 20, out);
        out.printf("운 판정 2회: %d - D20 = %d%n", luck, luckCheck2);

        if (luckCheck1 > 0 && luckCheck2 > 0) {
            out.println("운 판정 2회 성공! 2D20으로 변경");
            defaultDamage = Main.dice(2, 20, out);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (jackpotActive && reducedLuck > 0) {
            double multiplier = 1.0 + (reducedLuck * 0.5);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("일확천금 패시브 적용: x%.1f → %d%n", multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

    /**
     * 로얄 플러쉬 기술
     * D4, 운 판정 3회 성공 시 4D20
     * 스태미나 7 소모
     */
    public static int royalFlush(int stat, int luck, int reducedLuck, boolean jackpotActive, PrintStream out) {
        out.println("겜블러-로얄 플러쉬 사용");
        int defaultDamage = Main.dice(1, 4, out);

        int successCount = 0;
        for (int i = 1; i <= 3; i++) {
            int luckCheck = luck - Main.dice(1, 20, out);
            out.printf("운 판정 %d회: %d - D20 = %d%n", i, luck, luckCheck);
            if (luckCheck > 0) {
                successCount++;
            }
        }

        if (successCount == 3) {
            out.println("운 판정 3회 성공! 4D20으로 변경");
            defaultDamage = Main.dice(4, 20, out);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (jackpotActive && reducedLuck > 0) {
            double multiplier = 1.0 + (reducedLuck * 0.5);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("일확천금 패시브 적용: x%.1f → %d%n", multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 7 소모");
        return totalDamage;
    }

}

