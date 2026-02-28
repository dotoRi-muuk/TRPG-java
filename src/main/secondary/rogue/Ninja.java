package main.secondary.rogue;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 닌자
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩 또는 신속
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
    public static Result plain(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, int precision, PrintStream out) {
        out.println("닌자-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int baseDamage = Main.dice(1, 6, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, 0, 0, precision, out);
    }

    private static Result applyNinjaDamageLogic(int baseDamage, int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, boolean applyClone, int staminaChange, int manaChange, int precision, PrintStream out) {
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
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, manaChange, staminaChange);
    }

    /**
     * 일격 : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result strike(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, int precision, PrintStream out) {
        out.println("닌자-일격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -2);
        }
        int baseDamage = Main.dice(2, 6, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, -2, 0, precision, out);
    }

    /**
     * 난도 : 대상에게 3D8의 피해를 입힙니다. (스태미나 4 소모)
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result slash(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, int precision, PrintStream out) {
        out.println("닌자-난도 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -4);
        }
        int baseDamage = Main.dice(3, 8, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, -4, 0, precision, out);
    }

    /**
     * 투척 표창 : 표창 1개를 소모하여 발동합니다. 대상에게 D8의 피해를 입힙니다. 이 기술은 턴을 소모하지 않습니다
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result throwShuriken(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, int precision, PrintStream out) {
        out.println("닌자-투척 표창 사용 (표창 1개 소모)");
        out.println("!턴 소모 없음!");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int baseDamage = Main.dice(1, 8, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, true, 0, 0, precision, out);
    }

    /**
     * 환영난무 : '환영' 패시브에 의해 은신이 활성화된 턴에 발동 가능하며, 이후 3턴동안 '분신' 패시브의 효과가 제거됩니다. 대상에게 8D6의 피해를 입힙니다. (스태미나 4 소모)
     * @param stat 사용할 스탯
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result phantomDance(int stat, boolean illusion, boolean quickReflexes, boolean ideologySeal, int precision, PrintStream out) {
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
        return applyNinjaDamageLogic(baseDamage, stat, illusion, quickReflexes, ideologySeal, false, -4, 0, precision, out);
    }

    /**
     * 일점투척 : 보유한 모든 표창을 소모하여 대상에게 피해를 입힙니다. 각 표창은 D10의 피해를 입힙니다. 민첩, 신속 판정이 동시에 필요합니다. (마나 3 소모)
     * @param dex 민첩 스탯
     * @param speed 신속 스탯
     * @param numShurikens 보유한 표창 개수
     * @param illusion 환영 패시브(은신 상태 시 데미지 1.5배)
     * @param quickReflexes 순발력 패시브(회피 성공 후 턴 1회 사용, 데미지 75%로 감소)
     * @param ideologySeal 이념 봉인 스킬(가하는 데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result allOutThrow(int dex, int speed, int numShurikens, boolean illusion, boolean quickReflexes, boolean ideologySeal, int precision, PrintStream out) {
        out.println("닌자-일점투척 사용");
        out.printf("표창 %d개 전량 소모\n", numShurikens);

        int verdict1 = Main.verdict(dex, out);
        int verdict2 = Main.verdict(speed, out);

        if (verdict1 <= 0 || verdict2 <= 0) {
            out.println("판정 실패 (민첩, 신속 모두 성공 필요)");
            return new Result(0, 0, false, -3, 0);
        }
        int baseDamage = Main.dice(numShurikens, 10, out);
        return applyNinjaDamageLogic(baseDamage, dex, illusion, quickReflexes, ideologySeal, true, 0, -3, precision, out);
    }

}
