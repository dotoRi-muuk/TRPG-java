package main.secondary.thief;

import main.Main;
import main.Result;
import main.Stat;

import java.io.PrintStream;
import java.util.Map;

public class Gambler {
    /*
    겜블러

    판정 사용 스탯 : 힘, 민첩(운)

-   기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.

*   행운아 (패시브) : 판정 실패 시 [운] 판정을 성공시켜 1회 재판정이 가능합니다. (운 판정에는 사용할 수 없습니다.)
*   일확천금 (패시브) : 자신의 [운] 스탯을 감소시킬 수 있습니다. 휴식 시에 다시 변경 가능합니다. 공격 발동에 [운] 판정이 성공할 시 해당 기술/스킬의 데미지가 (감소한 운 스탯) * 50% 만큼 증가합니다. (중첩 가능)
*   액땜 (패시브) : 운 판정 실패 시 다음 성공까지 운 스탯이 +2 증가합니다. 운 스탯 증가는 중첩될 수 있습니다.
    감수 (패시브) : 휴식 시 운 스탯 2개와 다른 스탯 1개를 교환할 수 있습니다. (한 가지 스탯으로만 바꿀 수 있으며, 체력, 스태미나, 마나는 불가능합니다.)

-   코인 토스 (기술) : 대상에게 1D4의 피해를 입힙니다. 이후 운 판정 성공 시 D12의 피해를 입힙니다. (스태미나 1 소모)
-   조커 카드 (기술) : 대상에게 1D6의 피해를 입힙니다. 이후 운 판정 성공 시 2D12의 피해를 입힙니다. (스태미나 3 소모)
-   블랙잭 (기술) : 대상에게 1D6의 피해를 입힙니다. 이후 운 판정 성공 시 3D8의 피해를 입힙니다. (스태미나 3 소모)
-   야추 다이스 (기술) : 대상에게 1D8의 피해를 입힙니다. 이후 운 판정 2회 성공 시 2D20의 피해를 입힙니다. (스태미나 4 소모)
-   로얄 플러쉬 (기술) : 대상에게 1D4의 피해를 입힙니다. 이후 운 판정 3회 성공 시 4D20의 피해를 입힙니다. (스태미나 7 소모)

-   흐름 (스킬) : 운 판정 성공 후 사용할 수 있습니다. 다음 턴까지 운 판정 스탯이 +3 증가합니다. 턴을 소모하지 않습니다. (마나 3 소모, 쿨타임 5턴)
    잭팟 (스킬) : 모든 플레이어가 모든 스탯을 1 잃습니다. 모든 플레이어가 운 판정을 진행하여, 판정값이 가장 높은 플레이어가 해당 스탯을 전부 얻습니다. 스탯 변경은 이 전투에 한하여 지속됩니다. (마나 5 소모, 쿨타임 10턴)
    룰렛 (스킬) : 모든 플레이어가 마나와 스태미나를 5씩 잃습니다. 모든 플레이어가 운 판정을 진행하여, 판정값이 가장 높은 플레이어가 해당 스탯을 전부 얻습니다. 스탯 변경은 이 전투에 한하여 지속됩니다. (전투 내 지속) (마나 5 소모, 쿨타임 8턴)
    핀볼 (스킬) : 모든 플레이어가 힘과 민첩을 2 잃습니다. 모든 플레이어가 운 판정을 진행하여, 판정값이 가장 높은 플레이어가 해당 스탯을 전부 얻습니다. 스탯 변경은 이 전투에 한하여 지속됩니다. (전투 내 지속) (마나 4 소모, 쿨타임 8턴)
    낙수패 (스킬) : 다음 턴까지 1개의 스탯이 0으로 감소합니다. 감소한 스탯만큼 운 스탯이 상승합니다. (마나 6 소모, 쿨타임 10턴)

-   럭키 모먼트 (전용 수비) : 운 판정 성공 시 공격을 회피합니다. 이후 기술 반격(운 판정 없이 추가 공격 성공)을 합니다.
     */

    /**
     * 갬블러 럭키 모먼트: 운 판정 성공 시 공격을 회피합니다. 이후 기술 반격(운 판정 없이 추가 공격 성공)을 합니다.
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
     * 갬블러 흐름: 운 판정 성공 시 운 스탯 +3
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
     * 겜블러 코인 토스: 1D4 + (운 판정 성공 시 1D12) 데미지
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
     * 겜블러 조커 카드: 1D6 + (운 판정 성공 시 2D12) 데미지
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
     * 겜블러 블랙잭: 1D6 + (운 판정 성공 시 3D8) 데미지
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
     * 겜블러 야추 다이스: 1D8 + (운 판정 2회 성공 시 2D20) 데미지
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
     * 겜블러 로얄 플러쉬: 1D4 + (운 판정 3회 성공 시 4D20) 데미지
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
     * 겜블러 기본공격: 1D6 데미지
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
