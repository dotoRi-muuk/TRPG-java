package main.hidden;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 닌자 (숨겨진 직업)
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩 또는 신속
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 */
public class Ninja {

    /**
     * 데미지 계산 공식 적용
     * [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param dices             주사위 수
     * @param sides             주사위 면 수
     * @param stat              주사위 보정에 사용할 스탯
     * @param stealthActive     은신(환영 패시브) 활성화 여부 (최종 데미지 x2)
     * @param doppelgangerActive 분신 패시브 활성화 여부 (데미지 x0.75)
     * @param ideologySealActive 이념 봉인 스킬 활성화 여부 (데미지 +500%)
     * @param resistanceType    내성 종류 ("none", "pain", "fear") - fear 시 최종 데미지 x2
     * @param staminaUsed       소모 스태미나
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result calculate(int dices, int sides, int stat,
                                   boolean stealthActive, boolean doppelgangerActive,
                                   boolean ideologySealActive, String resistanceType,
                                   int staminaUsed, int level, PrintStream out) {
        int damage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d\n", damage);

        // (100 + 데미지)% 계산 - 이념 봉인 시 +500% 가산
        int flatBonus = 0;
        if (ideologySealActive) {
            out.println("이념 봉인 스킬 적용: 데미지 +500%");
            flatBonus += 500;
        }

        // (최종 데미지)% 계산 - 은신, 분신, 내성 적용
        double finalDamageMultiplier = 1.0;
        if (stealthActive) {
            out.println("환영 패시브 적용: 은신 상태 공격 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }
        if (doppelgangerActive) {
            out.println("분신 패시브 적용: 데미지 75%로 감소");
            finalDamageMultiplier *= 0.75;
        }
        if ("fear".equals(resistanceType)) {
            out.println("내성(공포) 적용: 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        damage = Main.calculateDamage(damage, flatBonus, finalDamageMultiplier, out);

        // 주사위 보정 = 1.0 + Max(0, 스탯 - D20) * 0.1
        int sideDmg = Main.sideDamage(damage, stat, out);
        damage += sideDmg;
        out.printf("데미지 보정치 : %d\n", sideDmg);
        out.printf("최종 데미지 : %d\n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, 0, staminaUsed);
    }

    /**
     * 일격: 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     * <p>
     * 힘(str) 판정 사용
     *
     * @param str               힘 스탯
     * @param stealthActive     은신 활성화 여부
     * @param doppelgangerActive 분신 패시브 활성화 여부
     * @param ideologySealActive 이념 봉인 활성화 여부
     * @param resistanceType    내성 종류
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result strike(int str, boolean stealthActive, boolean doppelgangerActive,
                                boolean ideologySealActive, String resistanceType, int level, PrintStream out) {
        out.println("닌자-일격 사용");
        out.println("스태미나 2 소모");

        int verdict = Main.verdict(str, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 2);
        }

        return calculate(2, 6, str, stealthActive, doppelgangerActive, ideologySealActive, resistanceType, 2, level, out);
    }

    /**
     * 난도: 대상에게 3D8의 피해를 입힙니다. (스태미나 4 소모)
     * <p>
     * 힘(str) 판정 사용
     *
     * @param str               힘 스탯
     * @param stealthActive     은신 활성화 여부
     * @param doppelgangerActive 분신 패시브 활성화 여부
     * @param ideologySealActive 이념 봉인 활성화 여부
     * @param resistanceType    내성 종류
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result mangle(int str, boolean stealthActive, boolean doppelgangerActive,
                                boolean ideologySealActive, String resistanceType, int level, PrintStream out) {
        out.println("닌자-난도 사용");
        out.println("스태미나 4 소모");

        int verdict = Main.verdict(str, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 4);
        }

        return calculate(3, 8, str, stealthActive, doppelgangerActive, ideologySealActive, resistanceType, 4, level, out);
    }

    /**
     * 투척 표창: 표창 1개를 소모하여 대상에게 D8의 피해를 입힙니다. 이 기술은 턴을 소모하지 않습니다.
     * <p>
     * 민첩(dex) 판정 사용
     *
     * @param dex               민첩 스탯
     * @param stealthActive     은신 활성화 여부
     * @param doppelgangerActive 분신 패시브 활성화 여부
     * @param ideologySealActive 이념 봉인 활성화 여부
     * @param resistanceType    내성 종류
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result throwShuriken(int dex, boolean stealthActive, boolean doppelgangerActive,
                                       boolean ideologySealActive, String resistanceType, int level, PrintStream out) {
        out.println("닌자-투척 표창 사용 (표창 1개 소모)");
        out.println("!턴 소모 없음!");

        int verdict = Main.verdict(dex, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }

        return calculate(1, 8, dex, stealthActive, doppelgangerActive, ideologySealActive, resistanceType, 0, level, out);
    }

    /**
     * 환영난무: '환영' 패시브에 의해 은신이 활성화된 턴에 발동 가능하며, 분신 패시브의 데미지 감소 효과를 제거합니다.
     * 대상에게 8D6의 피해를 입힙니다. (스태미나 4 소모)
     * <p>
     * 민첩(dex) 판정 사용
     *
     * @param dex               민첩 스탯
     * @param stealthActive     은신 활성화 여부 (필수)
     * @param ideologySealActive 이념 봉인 활성화 여부
     * @param resistanceType    내성 종류
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result phantomDance(int dex, boolean stealthActive,
                                      boolean ideologySealActive, String resistanceType, int level, PrintStream out) {
        out.println("닌자-환영난무 사용");
        out.println("스태미나 4 소모");

        if (!stealthActive) {
            out.println("실패: 은신(환영) 상태가 아님");
            return new Result(0, 0, false, 0, 0);
        }
        out.println("분신 패시브 데미지 감소 효과 제거됨");

        int verdict = Main.verdict(dex, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 4);
        }

        // 환영난무는 분신 패시브 감소 효과를 제거하므로 doppelgangerActive = false
        return calculate(8, 6, dex, stealthActive, false, ideologySealActive, resistanceType, 4, level, out);
    }

    /**
     * 일점투척: 보유한 모든 표창을 소모하여 대상에게 피해를 입힙니다. 각 표창은 D10의 피해를 입힙니다.
     * 민첩, 신속 판정이 동시에 필요합니다. (마나 3 소모)
     *
     * @param dex               민첩 스탯
     * @param speed             신속 스탯
     * @param numShurikens      보유한 표창 개수
     * @param stealthActive     은신 활성화 여부
     * @param doppelgangerActive 분신 패시브 활성화 여부
     * @param ideologySealActive 이념 봉인 활성화 여부
     * @param resistanceType    내성 종류
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result focusedThrow(int dex, int speed, int numShurikens,
                                      boolean stealthActive, boolean doppelgangerActive,
                                      boolean ideologySealActive, String resistanceType, int level, PrintStream out) {
        out.println("닌자-일점투척 사용");
        out.printf("표창 %d개 전량 소모\n", numShurikens);
        out.println("마나 3 소모");

        int verdict1 = Main.verdict(dex, out);
        int verdict2 = Main.verdict(speed, out);

        if (verdict1 <= 0 || verdict2 <= 0) {
            out.println("판정 실패 (민첩, 신속 모두 성공 필요)");
            return new Result(0, 0, false, -3, 0);
        }

        int damage = Main.dice(numShurikens, 10, out);
        out.printf("기본 데미지 : %d\n", damage);

        // (100 + 데미지)% 계산
        int flatBonus = 0;
        if (ideologySealActive) {
            out.println("이념 봉인 스킬 적용: 데미지 +500%");
            flatBonus += 500;
        }

        // (최종 데미지)% 계산
        double finalDamageMultiplier = 1.0;
        if (stealthActive) {
            out.println("환영 패시브 적용: 은신 상태 공격 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }
        if (doppelgangerActive) {
            out.println("분신 패시브 적용: 데미지 75%로 감소");
            finalDamageMultiplier *= 0.75;
        }
        if ("fear".equals(resistanceType)) {
            out.println("내성(공포) 적용: 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }

        damage = Main.calculateDamage(damage, flatBonus, finalDamageMultiplier, out);

        int sideDmg = Main.sideDamage(damage, dex, out);
        damage += sideDmg;
        out.printf("데미지 보정치 : %d\n", sideDmg);
        out.printf("최종 데미지 : %d\n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        return new Result(0, damage, true, -3, 0);
    }
}
