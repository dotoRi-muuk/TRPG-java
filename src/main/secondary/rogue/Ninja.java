package main.secondary.rogue;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 닌자
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩 또는 신속
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 * <p>
 * 패시브:
 * <ul>
 *   <li>환영(illusion): 3턴마다 은신. 은신 중 공격 시 최종 데미지 x2, 공격 시 은신 해제.</li>
 *   <li>분신(doppelganger): 1턴에 2번 행동. 데미지 25% 감소 (최종 데미지 x0.75).</li>
 *   <li>순발력(quickReflexes): 회피 성공 후 추가 턴. 공격 없을 시 스태미나/마나 3 회복.</li>
 *   <li>내성(resistance): 고통 내성 - 받는 데미지 50%. 공포 내성 - 받는 데미지 1.5배, 가하는 최종 데미지 x2.</li>
 * </ul>
 */
public class Ninja {

    /**
     * 데미지 계산 공식 적용
     * [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param baseDamage             주사위로 굴린 기본 데미지
     * @param illusion               환영 패시브(은신 상태 시 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param applyClone             분신 패시브 적용 여부(데미지 x0.75)
     * @param resistanceType         내성 종류 ("none", "pain", "fear") - fear 시 최종 데미지 x2
     * @param stat                   주사위 보정에 사용할 스탯
     * @param staminaChange          스태미나 변화량
     * @param manaChange             마나 변화량
     * @param precision              정밀 스탯 (치명타 판정)
     * @param level                  레벨 ((100 + level²)% 최종 데미지)
     * @param externalDamageIncrease 외부 데미지 증가 % (합연산)
     * @param externalFinalDamagePct 외부 최종 데미지 배율 % (곱연산, 100 = 기본)
     * @param out                    출력 스트림
     * @param diceRoll               판정에서 사용한 주사위 값 (stat - verdict)
     * @return 결과 객체
     */
    private static Result applyNinjaDamageLogic(int baseDamage, int stat, boolean illusion,
                                                boolean ideologySeal, boolean applyClone,
                                                String resistanceType, int staminaChange,
                                                int manaChange, int precision,
                                                int level, int externalDamageIncrease, int externalFinalDamagePct,
                                                PrintStream out, int diceRoll) {
        // (100 + 데미지)% — 가산 보너스
        int flatBonus = externalDamageIncrease;
        if (ideologySeal) {
            out.println("이념 봉인 스킬 적용: 데미지 +300%");
            flatBonus += 300;
        }

        // (최종 데미지)% — 곱연산 배율
        double levelMultiplier = (100 + (long) level * level) / 100.0;
        double extFinalMultiplier = externalFinalDamagePct / 100.0;
        double finalDamageMultiplier = levelMultiplier * extFinalMultiplier;
        out.printf("레벨 최종 배율: (100 + %d^2)%% = %.0f%%%n", level, levelMultiplier * 100);
        out.printf("외부 데미지 증가: +%d%%, 외부 최종 데미지: %d%%%n", externalDamageIncrease, externalFinalDamagePct);

        if (applyClone) {
            out.println("분신 패시브 적용: 데미지 75%로 감소");
            finalDamageMultiplier *= 0.75;
        }
        if (illusion) {
            out.println("환영 패시브 적용: 은신 상태 공격 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }
        if ("fear".equals(resistanceType)) {
            out.println("내성(공포) 적용: 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalDamageMultiplier, out);

        // 주사위 보정
        int sideDamage = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);

        // 치명타 판정
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, manaChange, staminaChange);
    }

    /**
     * 닌자 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat                   사용할 스탯
     * @param illusion               환영 패시브(은신 상태 시 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param resistanceType         내성 종류 ("none", "pain", "fear")
     * @param precision              정밀 스탯
     * @param level                  레벨
     * @param externalDamageIncrease 외부 데미지 증가 %
     * @param externalFinalDamagePct 외부 최종 데미지 배율 %
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean illusion,
                               boolean ideologySeal, String resistanceType,
                               int precision, int level, int externalDamageIncrease, int externalFinalDamagePct,
                               PrintStream out) {
        out.println("닌자-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 6, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, ideologySeal, true,
                resistanceType, 0, 0, precision, level, externalDamageIncrease, externalFinalDamagePct, out, diceRoll);
    }

    /**
     * 일격 : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat                   사용할 스탯
     * @param illusion               환영 패시브(은신 상태 시 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param resistanceType         내성 종류 ("none", "pain", "fear")
     * @param precision              정밀 스탯
     * @param level                  레벨
     * @param externalDamageIncrease 외부 데미지 증가 %
     * @param externalFinalDamagePct 외부 최종 데미지 배율 %
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result strike(int stat, boolean illusion,
                                boolean ideologySeal, String resistanceType,
                                int precision, int level, int externalDamageIncrease, int externalFinalDamagePct,
                                PrintStream out) {
        out.println("닌자-일격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -2);
        }
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 6, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, ideologySeal, true,
                resistanceType, -2, 0, precision, level, externalDamageIncrease, externalFinalDamagePct, out, diceRoll);
    }

    /**
     * 난도 : 대상에게 3D8의 피해를 입힙니다. (스태미나 4 소모)
     *
     * @param stat                   사용할 스탯
     * @param illusion               환영 패시브(은신 상태 시 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param resistanceType         내성 종류 ("none", "pain", "fear")
     * @param precision              정밀 스탯
     * @param level                  레벨
     * @param externalDamageIncrease 외부 데미지 증가 %
     * @param externalFinalDamagePct 외부 최종 데미지 배율 %
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result slash(int stat, boolean illusion,
                               boolean ideologySeal, String resistanceType,
                               int precision, int level, int externalDamageIncrease, int externalFinalDamagePct,
                               PrintStream out) {
        out.println("닌자-난도 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -4);
        }
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 8, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, ideologySeal, true,
                resistanceType, -4, 0, precision, level, externalDamageIncrease, externalFinalDamagePct, out, diceRoll);
    }

    /**
     * 투척 표창 : 표창 1개를 소모하여 발동합니다. 대상에게 D8의 피해를 입힙니다. 이 기술은 턴을 소모하지 않습니다.
     *
     * @param stat                   사용할 스탯
     * @param illusion               환영 패시브(은신 상태 시 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param resistanceType         내성 종류 ("none", "pain", "fear")
     * @param precision              정밀 스탯
     * @param level                  레벨
     * @param externalDamageIncrease 외부 데미지 증가 %
     * @param externalFinalDamagePct 외부 최종 데미지 배율 %
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result throwShuriken(int stat, boolean illusion,
                                       boolean ideologySeal, String resistanceType,
                                       int precision, int level, int externalDamageIncrease, int externalFinalDamagePct,
                                       PrintStream out) {
        out.println("닌자-투척 표창 사용 (표창 1개 소모)");
        out.println("!턴 소모 없음!");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 8, out);
        return applyNinjaDamageLogic(baseDamage, stat, illusion, ideologySeal, true,
                resistanceType, 0, 0, precision, level, externalDamageIncrease, externalFinalDamagePct, out, diceRoll);
    }

    /**
     * 환영난무 : '환영' 패시브(은신) 활성화 중에만 발동 가능합니다. 대상에게 8D6의 피해를 입히며,
     * 다음 턴까지 분신 패시브의 데미지 감소 효과를 제거합니다. (스태미나 4 소모)
     *
     * @param stat                   사용할 스탯
     * @param illusion               환영 패시브(은신 상태여야 발동 가능, 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param resistanceType         내성 종류 ("none", "pain", "fear")
     * @param precision              정밀 스탯
     * @param level                  레벨
     * @param externalDamageIncrease 외부 데미지 증가 %
     * @param externalFinalDamagePct 외부 최종 데미지 배율 %
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result phantomDance(int stat, boolean illusion,
                                      boolean ideologySeal, String resistanceType,
                                      int precision, int level, int externalDamageIncrease, int externalFinalDamagePct,
                                      PrintStream out) {
        out.println("닌자-환영난무 사용");
        if (!illusion) {
            out.println("실패: 환영(은신) 상태가 아님");
            return new Result(0, 0, false, 0, 0);
        }
        out.println("다음 턴까지 분신 패시브 데미지 감소 효과 제거됨");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, -4);
        }
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(8, 6, out);
        // 환영난무는 분신 패시브 감소 효과를 제거하므로 applyClone = false
        return applyNinjaDamageLogic(baseDamage, stat, illusion, ideologySeal, false,
                resistanceType, -4, 0, precision, level, externalDamageIncrease, externalFinalDamagePct, out, diceRoll);
    }

    /**
     * 일점투척 : 보유한 모든 표창을 소모합니다. 각 표창마다 D10의 피해를 입힙니다.
     * 민첩과 신속 판정이 동시에 필요합니다. (마나 3 소모)
     *
     * @param dex                    민첩 스탯
     * @param speed                  신속 스탯
     * @param numShurikens           보유한 표창 개수
     * @param illusion               환영 패시브(은신 상태 시 최종 데미지 x2)
     * @param ideologySeal           이념 봉인 스킬(데미지 +300%)
     * @param resistanceType         내성 종류 ("none", "pain", "fear")
     * @param precision              정밀 스탯
     * @param level                  레벨
     * @param externalDamageIncrease 외부 데미지 증가 %
     * @param externalFinalDamagePct 외부 최종 데미지 배율 %
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result allOutThrow(int dex, int speed, int numShurikens,
                                     boolean illusion,
                                     boolean ideologySeal, String resistanceType,
                                     int precision, int level, int externalDamageIncrease, int externalFinalDamagePct,
                                     PrintStream out) {
        out.println("닌자-일점투척 사용");
        out.printf("표창 %d개 전량 소모\n", numShurikens);

        int verdict1 = Main.verdict(dex, out);
        int verdict2 = Main.verdict(speed, out);

        if (verdict1 <= 0 || verdict2 <= 0) {
            out.println("판정 실패 (민첩, 신속 모두 성공 필요)");
            return new Result(0, 0, false, -3, 0);
        }
        int diceRoll = dex - verdict1;
        int baseDamage = Main.dice(numShurikens, 10, out);
        return applyNinjaDamageLogic(baseDamage, dex, illusion, ideologySeal, true,
                resistanceType, 0, -3, precision, level, externalDamageIncrease, externalFinalDamagePct, out, diceRoll);
    }

    /**
     * 초신속: 턴을 1회 얻습니다. 단 이번 턴동안 받는 데미지가 100% 상승합니다.
     * 해당 스킬은 턴을 소모하지 않습니다. (마나 5)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 5 소모)
     */
    public static Result ultraSpeed(PrintStream out) {
        out.println("닌자-초신속 사용");
        out.println("턴 1회 획득, 이번 턴 받는 데미지 100% 상승");
        out.println("!턴 소모 없음!");
        out.println("마나 5 소모");
        return new Result(0, 0, true, -5, 0);
    }

    /**
     * 제어: 적에게 피해를 입거나 자신이 적을 공격하는 데에 실패할 때까지
     * (자신이 피해를 입지 않고 적에게 피해를 입힌 턴의 수) x 50%만큼 데미지가 증가합니다.
     * 해당 효과의 누적 턴 수는 스킬을 발동한 시점으로 고정됩니다. (마나 7, 쿨타임 11턴)
     *
     * @param uninterruptedTurns 스킬 발동 시점까지 연속으로 피해를 주고 받지 않은 턴 수
     * @param out                출력 스트림
     * @return 결과 객체 (마나 7 소모)
     */
    public static Result control(int uninterruptedTurns, PrintStream out) {
        out.println("닌자-제어 사용");
        int damageBonus = uninterruptedTurns * 50;
        out.printf("연속 무피격 공격 %d턴 → 데미지 +%d%% 적용%n", uninterruptedTurns, damageBonus);
        out.println("적 피해 또는 공격 실패 시 효과 종료");
        out.println("마나 7 소모");
        return new Result(0, 0, true, -7, 0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 버프 / 유틸리티 스킬 (데미지 없음, 상태 변화 반환)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 이념 봉인 : 3턴간 다음 효과를 부여합니다. (마나 15 소모)
     * <ul>
     *   <li>가하는 데미지 +300% (공식의 가산 보너스 적용)</li>
     *   <li>받는 데미지 50% 감소</li>
     *   <li>분신 패시브 데미지 감소 효과 제거</li>
     *   <li>적의 공격 목표를 자신으로 강제(어그로)</li>
     * </ul>
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 15 소모)
     */
    public static Result ideologySealBuff(PrintStream out) {
        out.println("닌자-이념 봉인 발동");
        out.println("3턴간 적용: 데미지 +300%, 받는 데미지 50%, 분신 감소 효과 제거, 어그로 강제");
        out.println("마나 15 소모");
        return new Result(0, 0, true, -15, 0);
    }

    /**
     * 마나 표창 : 마나를 소모하여 표창을 생성합니다. (마나 3 + 생성 개수 소모)
     *
     * @param count 생성할 표창 개수
     * @param out   출력 스트림
     * @return 결과 객체 (마나 3+count 소모)
     */
    public static Result manaShuriken(int count, PrintStream out) {
        int manaCost = 3 + count;
        out.printf("닌자-마나 표창 사용: 표창 %d개 생성\n", count);
        out.printf("마나 %d 소모\n", manaCost);
        return new Result(0, 0, true, -manaCost, 0);
    }

    /**
     * 분신 강화 : 이번 턴에 한해 분신 패시브의 데미지 감소 효과를 제거합니다. (마나 1 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 1 소모)
     */
    public static Result cloneEnhance(PrintStream out) {
        out.println("닌자-분신 강화 사용");
        out.println("이번 턴 분신 패시브 데미지 감소 효과 제거");
        out.println("마나 1 소모");
        return new Result(0, 0, true, -1, 0);
    }

    /**
     * 흐름 조절 : 순발력 패시브의 스태미나/마나 회복량을 3에서 10으로 변경합니다. (마나 1 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 1 소모)
     */
    public static Result flowControl(PrintStream out) {
        out.println("닌자-흐름 조절 사용");
        out.println("순발력 패시브 회복량 변경: 3 → 10 스태미나/마나");
        out.println("마나 1 소모");
        return new Result(0, 0, true, -1, 0);
    }

    /**
     * 흐름 잡기 : 순발력 패시브 발동 횟수에 비례하여 스탯을 버프합니다. (마나 6 소모)
     *
     * @param reflexCount 이번 전투에서 순발력 패시브가 발동된 횟수
     * @param out         출력 스트림
     * @return 결과 객체 (마나 6 소모)
     */
    public static Result flowCatch(int reflexCount, PrintStream out) {
        out.println("닌자-흐름 잡기 사용");
        out.printf("순발력 발동 횟수 %d회 기반 스탯 버프 적용\n", reflexCount);
        out.println("마나 6 소모");
        return new Result(0, 0, true, -6, 0);
    }

    /**
     * 분신난무 : 분신 패시브의 행동 횟수를 2→3으로, 순발력 패시브의 추가 행동을 1→2로 증가시킵니다. (마나 4 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 4 소모)
     */
    public static Result cloneFlurry(PrintStream out) {
        out.println("닌자-분신난무 사용");
        out.println("분신 패시브: 행동 횟수 2 → 3");
        out.println("순발력 패시브: 추가 행동 1 → 2");
        out.println("마나 4 소모");
        return new Result(0, 0, true, -4, 0);
    }

}
