package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 명사수
 * <p>
 * 판정 사용 스탯 : 민첩 또는 힘
 * <p>
 * 기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 * <p>
 * 포착 (패시브) : 기술과 스킬이 턴을 소모하지 않습니다. 1턴에 기술을 1회 사용할 수 있습니다. 전투 내에서 처음으로 공격한 적에게 가하는 데미지가 두 배로 증가하며, 그 외의 적에게 가하는 데미지는 절반으로 감소합니다. (대상이 사망하면 새로 지정)<br>
 * 무거운 시위 (패시브) : 스태미나 1을 소모하여 기본 공격의 데미지를 2D8로 변경할 수 있습니다.<br>
 * 오차 제거 (패시브) : 기본 공격 시 사용하는 스탯이 +3 증가합니다.<br>
 * 긴급사격 (패시브) : '무거운 시위'를 비활성화하며 발동할 수 있습니다. 기본 공격을 2회 사용할 수 있습니다.<br>
 * <p>
 * 파워샷 (기술) : 이번 턴의 기본 공격 데미지가 D6은 D8로, 2D8은 2D12로 변경됩니다. (스태미나 1 소모)<br>
 * 분열 화살 (기술) : 이번 턴의 기본 공격 데미지가 D6은 2D4로, 2D8은 4D6으로 변경됩니다. (스태미나 1 소모)<br>
 * 관통 화살 (기술) : 이번 턴의 기본 공격 데미지가 D6은 2D12로, 2D8은 D20으로 변경됩니다. (스태미나 0 소모)<br>
 * 폭탄 화살 (기술) : 이번 턴의 기본 공격 데미지가 2배로 증가합니다. (스태미나 2 소모)<br>
 * 더블 샷 (기술) : 기본 공격을 2회 사용합니다. 순환에 영향을 받지 않습니다. (스태미나 3 소모)<br>
 * <p>
 * 사냥감 (스킬) : 이번 턴 포착 대상에게 피해를 입혔다면 다음 턴 데미지가 2.5배로 증가합니다. (마나 3 소모, 쿨타임 4턴)<br>
 * 화살 강화 (스킬) : 이번 턴에 공격이 적중했다면 사용 가능합니다. 이번 턴 공격 데미지가 2배로 증가합니다. (마나 2 소모, 쿨타임 2턴)<br>
 * 차분함 (스킬) : 이전 턴에 공격이 적중했다면 사용 가능합니다. 이번 턴 공격 데미지가 2배로 증가합니다. (마나 2 소모, 쿨타임 2턴)<br>
 * 흐름 깨기 (스킬) : 다음 턴 공격이 불가능합니다. 이번 턴 공격 데미지가 3배로 증가합니다. (마나 2 소모, 쿨타임 4턴)<br>
 * 일정량 포기 (스킬) : 이번 턴의 공격 불가 효과를 기술 사용 불가로 변경합니다. (마나 1 소모, 쿨타임 8턴)<br>
 * 무대 (스킬) : 자신의 턴에 피해를 입히지 못할 때까지 발동합니다. 데미지가 2배로 증가합니다. 해제 시 다음 턴까지 행동 불가가 됩니다. (마나 5 소모, 쿨타임 10턴)<br>
 */
public class MasterArcher {

    /**
     * 명사수 기본공격
     * @param stat 사용할 스탯 (오차 제거 패시브: +3)
     * @param isHeavyString 무거운 시위 패시브 (1D6 -> 2D8, 스태미나 1 소모)
     * @param isFirstTarget 포착 패시브 (첫 대상 200%, 그외 50%)
     * @param isEmergency 긴급 사격 패시브 (기본 공격 연속 2회 사용)
     * @param ability 적용된 기술
     * @param preyEnabled 사냥감 스킬 활성화 여부 (데미지 250%)
     * @param isArrowReinforced 화살 강화 스킬 활성화 여부 (데미지 200%)
     * @param calm 차분함 스킬 활성화 여부 (데미지 200%)
     * @param cracking 흐름 깨기 스킬 활성화 여부 (데미지 300%)
     * @param stage 무대 스킬 활성화 여부 (데미지 200%)
     */
    public static Result plain(int stat, boolean isHeavyString, boolean isFirstTarget, boolean isEmergency, MasterArcherPassive ability, boolean preyEnabled, boolean isArrowReinforced, boolean calm, boolean cracking, boolean stage, PrintStream out) {

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
        float modifier = 1.0f;
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
                modifier *= 2.0f;
                out.println("폭탄 화살 기술 적용: 데미지 2배");
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
                    dices = 1;
                    sides = 20;
                    out.println("관통 화살 기술 적용: 2D8 → 1D20");
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
            modifier *= 2.0f;
            out.println("포착 패시브 적용: 첫 대상 데미지 2배");
        } else {
            modifier *= 0.5f;
            out.println("포착 패시브 적용: 첫 타격 이외 대상 데미지 50%");
        }
        if (preyEnabled) {
            modifier *= 2.5f;
            out.println("사냥감 스킬 적용: 데미지 2.5배");
        }
        if (isArrowReinforced) {
            modifier *= 2.0f;
            out.println("화살 강화 스킬 적용: 데미지 2배");
        }
        if (calm) {
            modifier *= 2.0f;
            out.println("차분함 스킬 적용: 데미지 2배");
        }
        if (cracking) {
            modifier *= 3.0f;
            out.println("흐름 깨기 스킬 적용: 데미지 3배");
        }
        if (stage) {
            modifier *= 2.0f;
            out.println("무대 스킬 적용: 데미지 2배");
        }
        out.printf("총 배율: %s%n", modifier);

        int damageDealt = 0;
        while (count-- > 0) {
            int defaultDamage = Main.dice(dices, sides, out);
            out.printf("기본 데미지: %d%n", defaultDamage);
            int damageAfterPassives = (int) (defaultDamage * modifier);
            out.printf("데미지 배율 적용 후 데미지: %d%n", damageAfterPassives);
            int sideDamage = Main.sideDamage(damageAfterPassives, effectiveStat, out);
            out.printf("추가 사이드 데미지: %d%n", sideDamage);
            int totalDamage = damageAfterPassives + sideDamage;
            out.printf("총 데미지 : %d%n", totalDamage);
            damageDealt += totalDamage;
        }
        return new Result(0, damageDealt, true, 0, 0);
    }

}


