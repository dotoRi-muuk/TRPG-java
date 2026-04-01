package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 명사수
 * <p>
 * 판정 사용 스탯 : 민첩 또는 힘
 */
public class MasterArcher {

    /**
     * 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     * <p>
     * 데미지 계산식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     * - "데미지" 보너스는 합산(additive), "최종 데미지" 보너스는 곱연산(multiplicative).
     * @param stat 사용할 스탯 (오차 제거 패시브: +3)
     * @param isHeavyString 무거운 시위 패시브 (1D6 -> 2D8, 스태미나 1 소모)
     * @param isFirstTarget 포착 패시브 (첫 대상 +100%, 그외 -50%)
     * @param isEmergency 긴급 사격 패시브 (기본 공격 연속 2회 사용)
     * @param ability 적용된 기술
     * @param preyEnabled 사냥감 스킬 활성화 여부 (데미지 +300%)
     * @param arrowOverheatCount 화살 과열 스킬 스택 수 (전 턴 기술 수, 스택당 +60%)
     * @param calm 차분함 스킬 활성화 여부 (데미지 +100%)
     * @param cracking 흐름 깨기 스킬 활성화 여부 (데미지 +300%)
     * @param stageTurn 무대 스킬 지속 턴 (최종 데미지 턴당 x70% 증가, 최대 +500%.
     *                  데미지를 입히지 못하면 해제되며 다음 턴까지 행동불능. 마나 5 소모, 쿨타임 10턴)
     */
    public static Result plain(int stat, boolean isHeavyString, boolean isFirstTarget, boolean isEmergency, MasterArcherPassive ability, boolean preyEnabled, int arrowOverheatCount, boolean calm, boolean cracking, int stageTurn, int precision, int level, PrintStream out) {

        if (isEmergency && isHeavyString) {
            out.println("긴급 사격과 무거운 시위는 동시에 사용할 수 없습니다!");
            return new Result(0, 0, false, 0, 0);
        }

        out.println("명사수-기본공격 사용");

        // 오차 제거 패시브: 스탯 +3
        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = effectiveStat - verdict;


        /*

파워샷 (기술) : 이번 턴의 기본 공격 데미지가 D6은 D8로, 2D8은 2D12로 변경됩니다. (스태미나 1 소모)
분열 화살 (기술) : 이번 턴의 기본 공격 데미지가 D6은 2D4로, 2D8은 4D6으로 변경됩니다. (스태미나 1 소모)
관통 화살 (기술) : 이번 턴의 기본 공격 데미지가 D6은 2D12로, 2D8은 D20으로 변경됩니다. (스태미나 0 소모)
폭탄 화살 (기술) : 이번 턴의 기본 공격 데미지가 2배로 증가합니다. (스태미나 2 소모)
더블 샷 (기술) : 기본 공격을 2회 사용합니다. 순환에 영향을 받지 않습니다. (스태미나 3 소모)
         */

        if (isHeavyString) {
            out.println("무거운 시위 패시브 적용: 1D6 → 2D8");
        }

        int count = 1;
        if (isEmergency) {
            count += 1;
            out.println("긴급 사격 패시브 적용: 기본 공격 2회 사용");
        }
        int dices, sides;
        float damageAdditive = 0.0f;
        float finalDamageMultiplier = 1.0f;
        switch (ability) {
            case POWER_SHOT -> {
                if (isHeavyString) {
                    dices = 2;
                    sides = 12;
                    out.println("파워샷 기술 적용: 2D8 → 2D12");
                } else {
                    dices = 1;
                    sides = 8;
                    out.println("파워샷 기술 적용: 1D6 → 1D8");
                }
            }
            case DOUBLE_SHOT ->{
                count += 1;
                out.print("더블 샷 기술 적용: 기본 공격 2회 사용\n");
                if (isHeavyString) {
                    dices = 2;
                    sides = 8;
                } else {
                    dices = 1;
                    sides = 6;
                }
            }
            case SPLIT_ARROW -> {
                if (isHeavyString) {
                    dices = 4;
                    sides = 6;
                    out.println("분열 화살 기술 적용: 2D8 → 4D6");
                } else {
                    dices = 2;
                    sides = 4;
                    out.println("분열 화살 기술 적용: 1D6 → 2D4");
                }
            }
            case EXPLOSIVE_ARROW -> {
                damageAdditive += 1.0f;
                out.println("폭탄 화살 기술 적용: 데미지 +100%");
                if (isHeavyString) {
                    dices = 2;
                    sides = 8;
                } else {
                    dices = 1;
                    sides = 6;
                }
            }
            case PENETRATING_ARROW -> {
                if (isHeavyString) {
                    dices = 2;
                    sides = 20;
                    out.println("관통 화살 기술 적용: 2D8 → 2D20");
                } else {
                    dices = 2;
                    sides = 12;
                    out.println("관통 화살 기술 적용: 1D6 → 2D12");
                }
            }
            default -> {
                if (isHeavyString) {
                    dices = 2;
                    sides = 8;
                } else {
                    dices = 1;
                    sides = 6;
                }
            }
        }

        if (isFirstTarget) {
            damageAdditive += 1.0f;
            out.println("포착 패시브 적용: 첫 대상 데미지 +100%");
        } else {
            damageAdditive -= 0.5f;
            out.println("포착 패시브 적용: 첫 타격 이외 대상 데미지 -50%");
        }
        if (preyEnabled) {
            damageAdditive += 3.0f;
            out.println("사냥감 스킬 적용: 데미지 +300%");
        }
        if (arrowOverheatCount > 0) {
            damageAdditive += arrowOverheatCount * 0.6f;
            out.printf("화살 과열 스킬 적용: 데미지 +%.0f%% (스택 %d)%n", arrowOverheatCount * 60.0f, arrowOverheatCount);
        }
        if (calm) {
            damageAdditive += 1.0f;
            out.println("차분함 스킬 적용: 데미지 +100%");
        }
        if (cracking) {
            damageAdditive += 3.0f;
            out.println("흐름 깨기 스킬 적용: 데미지 +300%");
        }
        if (stageTurn > 0) {
            float stageBonus = Math.min(stageTurn * 0.7f, 5.0f);
            finalDamageMultiplier *= (1.0f + stageBonus);
            out.printf("무대 스킬 적용: 최종 데미지 %.1f배 (%d턴 지속)%n", 1.0f + stageBonus, stageTurn);
        }
        out.printf("데미지 배율: %+.0f%%, 최종 데미지 배율: %.2f%n", damageAdditive * 100, finalDamageMultiplier);

        int damageDealt = 0;
        while (count-- > 0) {
            int defaultDamage = Main.dice(dices, sides, out);
            out.printf("기본 데미지: %d%n", defaultDamage);
            int damageAfterPassives = (int) (defaultDamage * (1.0f + damageAdditive) * finalDamageMultiplier);
            out.printf("데미지 배율 적용 후 데미지: %d%n", damageAfterPassives);
            int sideDamage = Main.sideDamage(damageAfterPassives, effectiveStat, out, diceRoll);
            out.printf("추가 사이드 데미지: %d%n", sideDamage);
            int totalDamage = damageAfterPassives + sideDamage;
            totalDamage = Main.criticalHit(precision, totalDamage, out);
            out.printf("총 데미지 : %d%n", totalDamage);
            damageDealt += totalDamage;
        }
        int levelAdjustedDamage = (int)(damageDealt * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, Main.levelMultiplier(level) * 100.0, levelAdjustedDamage);
        return new Result(0, levelAdjustedDamage, true, 0, 0);
    }

}


