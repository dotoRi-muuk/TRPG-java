package main.secondary.rogue;

import main.Main;
import main.Result;
import main.Stat;

import java.io.PrintStream;
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
    private static Result executeSkill(String skillName, int stat, int luckStat, int decreasedLuck, PrintStream out,
                                       int baseDiceSide,
                                       int bonusDiceNum, int bonusDiceSide,
                                       int requiredLuckSuccesses, int staminaCost) {
        out.println("겜블러-" + skillName + " 사용");
        boolean misfortuneAversion = false;

        int verdict = Main.verdict(stat, out);
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

        if (requiredLuckSuccesses > 0) {
            int successCount = 0;
            for (int i = 0; i < requiredLuckSuccesses; i++) {
                out.print("운 판정 시도\n");
                int v = Main.verdict(luckStat, out);
                if (v > 0) {
                    successCount++;
                    misfortuneAversion = true;
                }
            }

            if (successCount >= requiredLuckSuccesses) {
                int luckDamage = Main.dice(bonusDiceNum, bonusDiceSide, out);
                out.printf("운 판정 성공 추가 데미지: %d\n", luckDamage);
                baseDamage += luckDamage;
            }
        }

        int lucky = Main.verdict(luckStat, out);
        if (decreasedLuck > 0 && lucky > 0) {
            misfortuneAversion = true;
            double modifier = (decreasedLuck * 0.5);
            out.printf("일확천금 패시브 적용: 데미지 %.2f배\n", modifier);
            int windfallDamage = (int) (baseDamage * modifier);
            out.printf("일확천금 추가 데미지: %d\n", windfallDamage);
            baseDamage += windfallDamage;
        }
        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        baseDamage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
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
    public static Result coinToss(int stat, int luckStat, int decreasedLuck, PrintStream out) {
        return executeSkill("코인 토스", stat, luckStat, decreasedLuck, out, 4, 1, 12, 1, 1);
    }

    /**
     * 조커 카드: 1D6 + (운 판정 성공 시 2D12) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result jokerCard(int stat, int luckStat, int decreasedLuck, PrintStream out) {
        return executeSkill("조커 카드", stat, luckStat, decreasedLuck, out, 6, 2, 12, 1, 3);
    }

    /**
     * 블랙잭: 1D6 + (운 판정 성공 시 3D8) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result blackJack(int stat, int luckStat, int decreasedLuck, PrintStream out) {
        return executeSkill("블랙잭", stat, luckStat, decreasedLuck, out, 6, 3, 8, 1, 3);
    }

    /**
     * 야추 다이스: 1D8 + (운 판정 2회 성공 시 2D20) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result yachtDice(int stat, int luckStat, int decreasedLuck, PrintStream out) {
        return executeSkill("야추 다이스", stat, luckStat, decreasedLuck, out, 8, 2, 20, 2, 4);
    }

    /**
     * 로얄 플러쉬: 1D4 + (운 판정 3회 성공 시 4D20) 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result royalFlush(int stat, int luckStat, int decreasedLuck, PrintStream out) {
        return executeSkill("로얄 플러쉬", stat, luckStat, decreasedLuck, out, 4, 4, 20, 3, 7);
    }

    /**
     * 기본공격: 1D6 데미지
     * @param stat 사용할 스탯
     * @param luckStat 운 스탯
     * @param decreasedLuck 감소한 운 스탯 (일확천금 패시브)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int luckStat, int decreasedLuck, PrintStream out) {
        return executeSkill("기본공격", stat, luckStat, decreasedLuck, out, 6, 0, 0, 0, 0);
    }

}
