package main.secondary.thief;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 닌자
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩 또는 신속
 * <p>
 * -    기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 * <p>
 * *   환영 (패시브) : 3번째 턴, 이후 매 3턴마다 은신 상태가 됩니다. 은신 상태일 때에는 공격의 대상으로 지정될 수 없으며, 공격 시 해제됩니다. 해당 턴에 공격 시 데미지가 1.5배로 증가합니다.<br>
 * *   분신 (패시브) : 매 턴마다 2회씩 행동할 수 있습니다. 단, 가하는 데미지는 75%로 감소합니다.<br>
 * *   순발력 (패시브) : 회피 성공 후 신속 판정에 성공할 경우, 즉시 턴을 1회 사용할 수 있습니다. 해당 턴의 공격 데미지는 75%로 감소합니다. 공격하지 않을 경우 스태미나와 마나를 3 회복합니다.<br>
 *     내성 (패시브) : '고통 내성'과 '독 내성' 중 하나를 선택하여 사용합니다. 휴식 시 변경 가능합니다.<br>
 *     - 고통 내성 : 받는 피해가 50% 감소합니다.<br>
 *     - 독 내성 : 매 턴 상태이상을 2회 무시합니다.<br>
 * <p>
 * -   일격 (기술) : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)<br>
 * -   난도 (기술) : 대상에게 3D8의 피해를 입힙니다. (스태미나 4 소모)<br>
 * -   투척 표창 (기술) : 표창 1개를 소모하여 발동합니다. 대상에게 D8의 피해를 입힙니다. 이 기술은 턴을 소모하지 않습니다<br>
 * -   환영난무 (기술) : '환영' 패시브에 의해 은신이 활성화된 턴에 발동 가능하며, 이후 3턴동안 '분신' 패시브의 효과가 제거됩니다. 대상에게 8D6의 피해를 입힙니다. (스태미나 4 소모)<br>
 * <p>
 *     마나 표창 (스킬) : 표창을 생성합니다. 다음 발동 시 가지고 있던 표창은 소모됩니다. (마나 3+생성한 표창 개수만큼 소모, 쿨타임 2턴)<br>
 *     분신 강화 (스킬) : 이번 턴 동안 '분신' 패시브의 데미지 감소 효과를 제거합니다. 이 스킬은 턴을 소모하지 않습니다. (마나 1 소모, 쿨타임 2턴)<br>
 *     민첩함 (스킬) : 이번 턴 동안 '순발력' 패시브의 데미지 감소 효과를 제거합니다. 이 스킬은 턴을 소모하지 않습니다. (마나 1 소모, 쿨타임 2턴)<br>
 *     흐름 잡기 (스킬) : 다음 2턴 동안 '순발력' 패시브가 발동한 횟수만큼 힘, 민첩, 신속 스탯이 상승합니다. 이 효과는 전투 내내 지속됩니다. (마나 6 소모, 쿨타임 10턴)<br>
 *     분신난무 (스킬) : '분신' 패시브의 행동 횟수가 2회에서 3회로 증가하고, '순발력' 패시브의 행동 횟수가 1회에서 2회로 증가합니다. (마나 4 소모, 쿨타임 8턴)<br>
 * -   일점투척 (스킬) : 보유한 모든 표창을 소모하여 대상에게 피해를 입힙니다. 각 표창은 D10의 피해를 입힙니다. 민첩, 신속 판정이 동시에 필요합니다. '마나 표창' 발동 이후 턴 소모 없이 발동 가능합니다. (마나 3 소모, 쿨타임 3턴)<br>
 * *   이념 봉인 (스킬) : 다음 5턴 동안 어떠한 방법으로도 체력, 마나, 스태미나를 회복할 수 없습니다. 다음 3턴 동안 제약 없이 공격 효과를 발동할 수 있으며, '환영 난무' 사용 시 분신 제거 효과를 무시합니다. 받는 데미지가 50% 감소하고, 가하는 데미지가 3배로 증가합니다. 마나를 소모하지 않지만, 대신 '마나 표창'으로 소모할 수 있는 마나가 최대 15로 제한됩니다. 버프 효과가 끝난 턴에 모든 스태미나와 마나를 잃습니다.<br>
 */
public class Ninja {

    /**
     * 닌자 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes  순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, PrintStream out) {
        out.println("닌자-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int baseDamage = Main.dice(1, 6, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, 0, 0, out);
    }

    private static Result applyNinjaDamageLogic(int baseDamage, int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, boolean applyClone, int staminaChange, int manaChange, PrintStream out) {
        double damageMultiplier = 1.0;
        if (applyClone) {
            out.println("분신 패시브 적용: 데미지 75%로 감소");
            damageMultiplier *= 0.75;
        }
        if (illusion) {
            out.println("환영 패시브 적용: 은신 상태 공격 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        if (quickReflexes) {
            out.println("순발력 패시브 적용: 데미지 75%로 감소");
            damageMultiplier *= 0.75;
        }
        if (ideologySeal) {
            out.println("이념 봉인 스킬 적용: 가하는 데미지 3배");
            damageMultiplier *= 3.0;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, manaChange, staminaChange);
    }

    /**
     * 일격 (기술) : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result strike(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, PrintStream out) {
        out.println("닌자-일격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -2);
        }
        int baseDamage = Main.dice(2, 6, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, -2, 0, out);
    }

    /**
     * 난도 (기술) : 대상에게 3D8의 피해를 입힙니다. (스태미나 4 소모)
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result slash(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, PrintStream out) {
        out.println("닌자-난도 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -4);
        }
        int baseDamage = Main.dice(3, 8, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, -4, 0, out);
    }

    /**
     * 투척 표창 (기술) : 표창 1개를 소모하여 발동합니다. 대상에게 D8의 피해를 입힙니다. 이 기술은 턴을 소모하지 않습니다
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result throwShuriken(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, PrintStream out) {
        out.println("닌자-투척 표창 사용 (표창 1개 소모)");
        out.println("!턴 소모 없음!");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int baseDamage = Main.dice(1, 8, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, 0, 0, out);
    }

    /**
     * 환영난무 (기술) : '환영' 패시브에 의해 은신이 활성화된 턴에 발동 가능하며, 이후 3턴동안 '분신' 패시브의 효과가 제거됩니다. 대상에게 8D6의 피해를 입힙니다. (스태미나 4 소모)
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result phantomDance(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, PrintStream out) {
        out.println("닌자-환영난무 사용");
        if (!illusion) {
            out.println("실패: 환영(은신) 상태가 아님");
            return new Result(0, 0, false, 0, 0);
        }
        out.println("3턴간 분신 패시브 제거됨");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -4);
        }
        int baseDamage = Main.dice(8, 6, out);
        // 분신 패시브 제거되므로 75% 감소 적용 안함 (applyClone = false)
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, false, -4, 0, out);
    }

    /**
     * 일점투척 (스킬) : 보유한 모든 표창을 소모하여 대상에게 피해를 입힙니다. 각 표창은 D10의 피해를 입힙니다. 민첩, 신속 판정이 동시에 필요합니다. (마나 3 소모)
     * @param dex 민첩 스탯
     * @param speed 신속 스탯
     * @param numShurikens 보유한 표창 개수
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result allOutThrow(int dex, int speed, int numShurikens, boolean illusion, boolean quickReflexes, boolean ideologySeal, PrintStream out) {
        out.println("닌자-일점투척 사용");
        out.printf("표창 %d개 전량 소모\n", numShurikens);

        int verdict1 = Main.verdict(dex, out);
        int verdict2 = Main.verdict(speed, out);

        if (verdict1 <= 0 || verdict2 <= 0) {
            out.println("판정 실패 (민첩, 신속 모두 성공 필요)");
            return new Result(0, 0, false, -3, 0);
        }
        int baseDamage = Main.dice(numShurikens, 10, out);
        return applyNinjaDamageLogic(baseDamage, dex, illusion, quickReflexes, ideologySeal, true, 0, -3, out);
    }

}
