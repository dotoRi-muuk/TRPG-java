package main.secondary.rogue;

import main.Main;
import main.Result;
import main.Stat;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 겜블러
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩 또는 운
 */
public class Gambler {

    /**
     * 럭키 모먼트: 운 판정 성공 시 공격을 회피합니다. 이후 기술 반격(운 판정 없이 추가 공격 성공)을 합니다.
     * @param damageTaken 받은 데미지
     * @param luckStat 운 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result luckyMoment(int damageTaken, int luckStat, PrintStream out) {
        out.println("겜블러-럭키 모먼트 사용");

        int verdict = Main.verdict(luckStat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, 0);

        out.print("럭키 모먼트 반격 데미지 적용: 운 판정 없이 추가 공격 성공\n");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 흐름: 운 판정 성공 시 운 스탯 +3
     * @param luckStat 운 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result flow(int luckStat, PrintStream out) {
        out.println("겜블러-흐름 사용");
        int verdict = Main.verdict(luckStat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0, Map.of());
        }
        out.print("운 판정 성공: 운 스탯 +3\n");
        return new Result(0, 0, true, 0, 0, Map.of(
                Stat.LUCK, 3
        ));
    }

    /**
     * 공통 스킬 실행 로직
     *
     * @param skillName             스킬명
     * @param stat                  사용할 스탯
     * @param luckStat              운 스탯
     * @param decreasedLuck         감소한 운 스탯
     * @param out                   출력 스트림
     * @param baseDiceSide          기본 데미지 주사위 면수
     * @param bonusDiceNum          추가 데미지 주사위 개수
     * @param bonusDiceSide         추가 데미지 주사위 면수
     * @param requiredLuckSuccesses 추가 데미지 발동에 필요한 운 판정 성공 횟수
     * @param staminaCost           소모 스태미나
     * @return 결과 객체
     */
    private static Result executeSkill(String skillName, int stat, int luckStat, int decreasedLuck, int precision, PrintStream out,
                                       int baseDiceSide,
                                       int bonusDiceNum, int bonusDiceSide,
                                       int requiredLuckSuccesses, int staminaCost) {
        out.println("겜블러-" + skillName + " 사용");
        boolean misfortuneAversion = false;

        int verdict = Main.verdict(stat, out);
        int diceRoll = stat - verdict;
        if (verdict <= 0) {
            out.print("행운아 패시브 발동: 운 판정 시도\n");
            int luckVerdict = Main.verdict(luckStat, out);
            if (luckVerdict <= 0) {
                return new Result(0, 0, false, 0, staminaCost, Map.of(
                        Stat.LUCK, 2
                ));
            }
            misfortuneAversion = true;
        }
        int baseDamage = Main.dice(1, baseDiceSide, out);
        out.printf("기본 데미지: %d\n", baseDamage);

        List<Integer> luckyDiceRolls = new ArrayList<>();
        if (requiredLuckSuccesses > 0) {
            int successCount = 0;
            for (int i = 0; i < requiredLuckSuccesses; i++) {
                out.print("운 판정 시도\n");
                int v = Main.verdict(luckStat, out);
                if (v > 0) {
                    successCount++;
                    misfortuneAversion = true;
                    luckyDiceRolls.add(luckStat - v);
                }
            }

            if (successCount >= requiredLuckSuccesses) {
                int luckDamage = Main.dice(bonusDiceNum, bonusDiceSide, out);
                out.printf("운 판정 성공 추가 데미지: %d\n", luckDamage);
                baseDamage += luckDamage;
            }
        }

        double correction = 1.0 + (stat - diceRoll) * 0.1;
        out.printf("스탯 보정: 1 + (%d - %d) * 0.1 = %.2f%n", stat, diceRoll, correction);
        for (int luckDice : luckyDiceRolls) {
            double luckFactor = 1.0 + (luckStat - luckDice) * 0.1;
            out.printf("운 보정: 1 + (%d - %d) * 0.1 = %.2f%n", luckStat, luckDice, luckFactor);
            correction *= luckFactor;
        }
        int correctedDamage = (int) (baseDamage * correction);
        int sideDamage = correctedDamage - baseDamage;
        baseDamage = correctedDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        baseDamage = Main.criticalHit(precision, baseDamage, out);
        out.printf("최종 데미지 : %d%n", baseDamage);
        return new Result(0, baseDamage, true, 0, staminaCost, Map.of(
                Stat.LUCK, misfortuneAversion ? 0 : 2
        ));
    }

    /**
     * 코인 토스: 1D4 + (운 판정 성공 시 1D12) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result coinToss(int stat, int luckStat, int decreasedLuck, int precision, PrintStream out) {
        return executeSkill("코인 토스", stat, luckStat, decreasedLuck, precision, out, 4, 1, 12, 1, 1);
    }

    /**
     * 조커 카드: 1D6 + (운 판정 성공 시 2D12) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result jokerCard(int stat, int luckStat, int decreasedLuck, int precision, PrintStream out) {
        return executeSkill("조커 카드", stat, luckStat, decreasedLuck, precision, out, 6, 2, 12, 1, 3);
    }

    /**
     * 블랙잭: 1D6 + (운 판정 성공 시 3D8) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result blackJack(int stat, int luckStat, int decreasedLuck, int precision, PrintStream out) {
        return executeSkill("블랙잭", stat, luckStat, decreasedLuck, precision, out, 6, 3, 8, 1, 3);
    }

    /**
     * 야추 다이스: 1D8 + (운 판정 2회 성공 시 2D20) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result yachtDice(int stat, int luckStat, int decreasedLuck, int precision, PrintStream out) {
        return executeSkill("야추 다이스", stat, luckStat, decreasedLuck, precision, out, 8, 2, 20, 2, 4);
    }

    /**
     * 로얄 플러쉬: 1D4 + (운 판정 3회 성공 시 4D20) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result royalFlush(int stat, int luckStat, int decreasedLuck, int precision, PrintStream out) {
        return executeSkill("로얄 플러쉬", stat, luckStat, decreasedLuck, precision, out, 4, 4, 20, 3, 7);
    }

    /**
     * 기본공격: 1D6 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int luckStat, int decreasedLuck, int precision, PrintStream out) {
        return executeSkill("기본공격", stat, luckStat, decreasedLuck, precision, out, 6, 0, 0, 0, 0);
    }

    /**
     * 최강의 불운 : n턴 동안 [운] 스탯이 0으로 감소합니다.
     * 재사용하여 (원래 운 스탯) x (지속 시간의 절반)만큼 운 스탯이 상승합니다.
     * (마나 6)
     *
     * @param originalLuck  원래 운 스탯
     * @param durationTurns 지속 시간 (턴 수)
     * @param reactivate    재사용 여부 (true: 운 스탯 상승 효과 발동)
     * @param out           출력 스트림
     * @return 결과 객체 (마나 6 소모)
     */
    public static Result supremeBadLuck(int originalLuck, int durationTurns, boolean reactivate, PrintStream out) {
        out.println("겜블러-최강의 불운 사용");
        if (reactivate) {
            int luckGain = originalLuck * (durationTurns / 2);
            out.printf("재사용: 원래 운 스탯 %d × 지속 시간의 절반 %d = 운 스탯 +%d 상승%n",
                    originalLuck, durationTurns / 2, luckGain);
        } else {
            out.printf("%d턴 동안 [운] 스탯이 0으로 감소합니다.%n", durationTurns);
        }
        return new Result(0, 0, true, -6, 0);
    }

    /**
     * 럭키 : 적에게 공격 당하기 직전에 사용할 수 있습니다.
     * 해당 공격을 확정적으로 회피합니다. D20의 [운]을 잃습니다.
     * (마나 3, 쿨타임 6턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 3 소모, D20 운 소실)
     */
    public static Result lucky(PrintStream out) {
        out.println("겜블러-럭키 사용");
        out.println("적의 공격을 확정적으로 회피합니다.");
        int luckLost = Main.dice(1, 20, out);
        out.printf("D20 운 소실: %d%n", luckLost);
        return new Result(0, 0, true, -3, 0);
    }

}
