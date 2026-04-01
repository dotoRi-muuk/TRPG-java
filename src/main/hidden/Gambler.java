package main.hidden;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 겜블러 (숨겨진 직업)
 * <p>
 * 판정 사용 스탯 : 운
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 */
public class Gambler {

    /**
     * 기본공격: 대상에게 1D6의 피해를 입힙니다.
     *
     * @param stat          사용할 스탯
     * @param reducedLuck   감소한 운 스탯 (일확천금 패시브용)
     * @param jackpotActive 일확천금 패시브 활성화 여부
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int reducedLuck, boolean jackpotActive, int level, PrintStream out) {
        out.println("겜블러-기본공격 사용");

        int damage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", damage);

        int statVerdict = Main.verdict(stat, out);
        int statDice = stat - statVerdict;
        double statFactor = 1.0 + (stat - statDice) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, statDice, statFactor);
        int corrected = (int) (damage * statFactor);
        int sideDmg = corrected - damage;
        damage = corrected;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        out.printf("총 데미지 : %d%n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, 0);
    }

    /**
     * 코인 토스: 1D4 + (운 판정 성공 시 1D12) 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat          사용할 스탯
     * @param luck          운 스탯
     * @param reducedLuck   감소한 운 스탯 (일확천금 패시브용)
     * @param jackpotActive 일확천금 패시브 활성화 여부
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result coinToss(int stat, int luck, int reducedLuck, boolean jackpotActive, int level, PrintStream out) {
        out.println("겜블러-코인 토스 사용");
        out.println("스태미나 1 소모");

        int damage = Main.dice(1, 4, out);
        out.printf("기본 데미지 : %d%n", damage);

        int statVerdict = Main.verdict(stat, out);
        int statDice = stat - statVerdict;

        out.println("운 판정 시도");
        int luckVerdict = Main.verdict(luck, out);
        int luckDice = luck - luckVerdict;

        if (luckVerdict >= 0) {
            int bonusDamage = Main.dice(1, 12, out);
            out.printf("운 판정 성공: 추가 데미지 %d%n", bonusDamage);
            damage += bonusDamage;

            if (jackpotActive) {
                double multiplier = 1 + reducedLuck * 0.5;
                int newDamage = (int) (damage * multiplier);
                out.printf("일확천금 패시브 적용: 데미지 %.2f배 → %d%n", multiplier, newDamage);
                damage = newDamage;
            }
        }

        double statFactor = 1.0 + (stat - statDice) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, statDice, statFactor);
        double luckFactor = 1.0 + (luck - luckDice) * 0.1;
        out.printf("운 보정: 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice, luckFactor);
        int corrected = (int) (damage * statFactor * luckFactor);
        int sideDmg = corrected - damage;
        damage = corrected;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        out.printf("총 데미지 : %d%n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, 1);
    }

    /**
     * 조커 카드: 1D6 + (운 판정 성공 시 2D12) 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat          사용할 스탯
     * @param luck          운 스탯
     * @param reducedLuck   감소한 운 스탯 (일확천금 패시브용)
     * @param jackpotActive 일확천금 패시브 활성화 여부
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result jokerCard(int stat, int luck, int reducedLuck, boolean jackpotActive, int level, PrintStream out) {
        out.println("겜블러-조커 카드 사용");
        out.println("스태미나 3 소모");

        int damage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", damage);

        int statVerdict = Main.verdict(stat, out);
        int statDice = stat - statVerdict;

        out.println("운 판정 시도");
        int luckVerdict = Main.verdict(luck, out);
        int luckDice = luck - luckVerdict;

        if (luckVerdict >= 0) {
            int bonusDamage = Main.dice(2, 12, out);
            out.printf("운 판정 성공: 추가 데미지 %d%n", bonusDamage);
            damage += bonusDamage;

            if (jackpotActive) {
                double multiplier = 1 + reducedLuck * 0.5;
                int newDamage = (int) (damage * multiplier);
                out.printf("일확천금 패시브 적용: 데미지 %.2f배 → %d%n", multiplier, newDamage);
                damage = newDamage;
            }
        }

        double statFactor = 1.0 + (stat - statDice) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, statDice, statFactor);
        double luckFactor = 1.0 + (luck - luckDice) * 0.1;
        out.printf("운 보정: 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice, luckFactor);
        int corrected = (int) (damage * statFactor * luckFactor);
        int sideDmg = corrected - damage;
        damage = corrected;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        out.printf("총 데미지 : %d%n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, 3);
    }

    /**
     * 블랙잭: 1D6 + (운 판정 성공 시 3D8) 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat          사용할 스탯
     * @param luck          운 스탯
     * @param reducedLuck   감소한 운 스탯 (일확천금 패시브용)
     * @param jackpotActive 일확천금 패시브 활성화 여부
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result blackjack(int stat, int luck, int reducedLuck, boolean jackpotActive, int level, PrintStream out) {
        out.println("겜블러-블랙잭 사용");
        out.println("스태미나 3 소모");

        int damage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", damage);

        int statVerdict = Main.verdict(stat, out);
        int statDice = stat - statVerdict;

        out.println("운 판정 시도");
        int luckVerdict = Main.verdict(luck, out);
        int luckDice = luck - luckVerdict;

        if (luckVerdict >= 0) {
            int bonusDamage = Main.dice(3, 8, out);
            out.printf("운 판정 성공: 추가 데미지 %d%n", bonusDamage);
            damage += bonusDamage;

            if (jackpotActive) {
                double multiplier = 1 + reducedLuck * 0.5;
                int newDamage = (int) (damage * multiplier);
                out.printf("일확천금 패시브 적용: 데미지 %.2f배 → %d%n", multiplier, newDamage);
                damage = newDamage;
            }
        }

        double statFactor = 1.0 + (stat - statDice) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, statDice, statFactor);
        double luckFactor = 1.0 + (luck - luckDice) * 0.1;
        out.printf("운 보정: 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice, luckFactor);
        int corrected = (int) (damage * statFactor * luckFactor);
        int sideDmg = corrected - damage;
        damage = corrected;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        out.printf("총 데미지 : %d%n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, 3);
    }

    /**
     * 야추 다이스: 1D8 + (운 판정 2회 성공 시 2D20) 피해를 입힙니다. (스태미나 4 소모)
     *
     * @param stat          사용할 스탯
     * @param luck          운 스탯
     * @param reducedLuck   감소한 운 스탯 (일확천금 패시브용)
     * @param jackpotActive 일확천금 패시브 활성화 여부
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result yatzyDice(int stat, int luck, int reducedLuck, boolean jackpotActive, int level, PrintStream out) {
        out.println("겜블러-야추 다이스 사용");
        out.println("스태미나 4 소모");

        int damage = Main.dice(1, 8, out);
        out.printf("기본 데미지 : %d%n", damage);

        int statVerdict = Main.verdict(stat, out);
        int statDice = stat - statVerdict;

        out.println("운 판정 시도 (1/2)");
        int luckVerdict1 = Main.verdict(luck, out);
        int luckDice1 = luck - luckVerdict1;

        out.println("운 판정 시도 (2/2)");
        int luckVerdict2 = Main.verdict(luck, out);
        int luckDice2 = luck - luckVerdict2;

        boolean allSuccess = luckVerdict1 >= 0 && luckVerdict2 >= 0;

        if (allSuccess) {
            int bonusDamage = Main.dice(2, 20, out);
            out.printf("운 판정 2회 성공: 추가 데미지 %d%n", bonusDamage);
            damage += bonusDamage;

            if (jackpotActive) {
                double multiplier = 1 + reducedLuck * 0.5;
                int newDamage = (int) (damage * multiplier);
                out.printf("일확천금 패시브 적용: 데미지 %.2f배 → %d%n", multiplier, newDamage);
                damage = newDamage;
            }
        }

        double statFactor = 1.0 + (stat - statDice) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, statDice, statFactor);
        double luckFactor1 = 1.0 + (luck - luckDice1) * 0.1;
        out.printf("운 보정 (1/2): 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice1, luckFactor1);
        double luckFactor2 = 1.0 + (luck - luckDice2) * 0.1;
        out.printf("운 보정 (2/2): 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice2, luckFactor2);
        int corrected = (int) (damage * statFactor * luckFactor1 * luckFactor2);
        int sideDmg = corrected - damage;
        damage = corrected;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        out.printf("총 데미지 : %d%n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, 4);
    }

    /**
     * 로얄 플러쉬: 1D4 + (운 판정 3회 성공 시 4D20) 피해를 입힙니다. (스태미나 7 소모)
     *
     * @param stat          사용할 스탯
     * @param luck          운 스탯
     * @param reducedLuck   감소한 운 스탯 (일확천금 패시브용)
     * @param jackpotActive 일확천금 패시브 활성화 여부
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result royalFlush(int stat, int luck, int reducedLuck, boolean jackpotActive, int level, PrintStream out) {
        out.println("겜블러-로얄 플러쉬 사용");
        out.println("스태미나 7 소모");

        int damage = Main.dice(1, 4, out);
        out.printf("기본 데미지 : %d%n", damage);

        int statVerdict = Main.verdict(stat, out);
        int statDice = stat - statVerdict;

        out.println("운 판정 시도 (1/3)");
        int luckVerdict1 = Main.verdict(luck, out);
        int luckDice1 = luck - luckVerdict1;

        out.println("운 판정 시도 (2/3)");
        int luckVerdict2 = Main.verdict(luck, out);
        int luckDice2 = luck - luckVerdict2;

        out.println("운 판정 시도 (3/3)");
        int luckVerdict3 = Main.verdict(luck, out);
        int luckDice3 = luck - luckVerdict3;

        boolean allSuccess = luckVerdict1 >= 0 && luckVerdict2 >= 0 && luckVerdict3 >= 0;

        if (allSuccess) {
            int bonusDamage = Main.dice(4, 20, out);
            out.printf("운 판정 3회 성공: 추가 데미지 %d%n", bonusDamage);
            damage += bonusDamage;

            if (jackpotActive) {
                double multiplier = 1 + reducedLuck * 0.5;
                int newDamage = (int) (damage * multiplier);
                out.printf("일확천금 패시브 적용: 데미지 %.2f배 → %d%n", multiplier, newDamage);
                damage = newDamage;
            }
        }

        double statFactor = 1.0 + (stat - statDice) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, statDice, statFactor);
        double luckFactor1 = 1.0 + (luck - luckDice1) * 0.1;
        out.printf("운 보정 (1/3): 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice1, luckFactor1);
        double luckFactor2 = 1.0 + (luck - luckDice2) * 0.1;
        out.printf("운 보정 (2/3): 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice2, luckFactor2);
        double luckFactor3 = 1.0 + (luck - luckDice3) * 0.1;
        out.printf("운 보정 (3/3): 1 + (%d - %d) * 0.1 = %.2f%n", luck, luckDice3, luckFactor3);
        int corrected = (int) (damage * statFactor * luckFactor1 * luckFactor2 * luckFactor3);
        int sideDmg = corrected - damage;
        damage = corrected;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        out.printf("총 데미지 : %d%n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, 7);
    }
}
