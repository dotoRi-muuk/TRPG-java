package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 건슬링거
 * <p>
 * 판정 사용 스탯 : 민첩
 */
public class Gunslinger {

    private static int calculateAttackDamage(int baseDamage, int verdict, int damageIncreasePercent,
                                             double finalMultiplier, int precision, PrintStream out) {
        out.printf("기본 데미지 : %d%n", baseDamage);
        double diceModifier = 1.0 + Math.max(0, verdict) * 0.1;
        out.printf("주사위 보정: %.2f%n", diceModifier);
        int finalDamage = Main.calculateDamage(baseDamage, damageIncreasePercent, finalMultiplier, diceModifier, out);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);
        return finalDamage;
    }

    private static int getWeightedJudgmentBonus(int weightedJudgmentCounterCount, PrintStream out) {
        if (weightedJudgmentCounterCount <= 0) {
            return 0;
        }
        int bonus = weightedJudgmentCounterCount * 30;
        out.printf("가중 심판 적용: 반격 %d회 → 데미지 +%d%%%n", weightedJudgmentCounterCount, bonus);
        return bonus;
    }

    /**
     * 백스탭 : 판정 성공 시 공격을 회피합니다. 1.5배의 데미지로 반격합니다. (스태미나 3 소모)
     *
     * @param stat        사용 스탯
     * @param damageTaken 받은 데미지
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result backStab(int stat, int damageTaken, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-백스탭 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, 3);

        out.print("백스탭 반격 데미지 적용: 데미지 배율 1.5배\n");
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int finalDamage = calculateAttackDamage(damageTaken, verdict, damageIncreasePercent, 1.5, precision, out);
        return new Result(0, finalDamage, true, 0, 3);
    }

    /**
     * 활약 기회 : 신속 판정 성공 시 이번 턴 반격 데미지가 1.5배로 증가합니다.
     *
     * @param stat       사용 스탯
     * @param baseDamage 기본 데미지
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result opportunity(int stat, int baseDamage, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-활약 기회 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        out.print("활약 기회 신속 적용: 데미지 배율 1.5배\n");
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int finalDamage = calculateAttackDamage(baseDamage, verdict, damageIncreasePercent, 1.5, precision, out);
        return new Result(0, finalDamage, true, 0, 0);
    }

    /**
     * 일점사 : 대상에게 6D6의 피해를 입힙니다. (스태미나 4 소모)
     *
     * @param stat 사용 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result focusedFire(int stat, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-일점사 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 4);
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int baseDamage = Main.dice(6, 6, out);
        return new Result(0, calculateAttackDamage(baseDamage, verdict, damageIncreasePercent, 1.0, precision, out), true, 0, 4);
    }

    /**
     * 헤드샷 : 대상에게 D20의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat 사용 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result HeadShot(int stat, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-헤드샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 3);
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int baseDamage = Main.dice(1, 20, out);
        return new Result(0, calculateAttackDamage(baseDamage, verdict, damageIncreasePercent, 1.0, precision, out), true, 0, 3);
    }

    /**
     * 헤드샷 : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat 사용 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result doubleShot(int stat, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-더블샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 2);
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int baseDamage = Main.dice(2, 6, out);
        return new Result(0, calculateAttackDamage(baseDamage, verdict, damageIncreasePercent, 1.0, precision, out), true, 0, 2);
    }

    /**
     * 퀵드로우 : 대상에게 D8의 피해를 입힙니다. '신중함' 발동 시 4D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat     사용 스탯
     * @param prudence 신중함 패시브 적용 여부
     * @param out      출력 스트림
     * @return 결과 객체
     */
    public static Result quickDraw(int stat, boolean prudence, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-퀵드로우 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 1);
        int dices = 1;
        if (prudence) {
            out.println("신중함 패시브 적용: D8 -> 4D8 적용");
            dices = 4;
        }
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int baseDamage = Main.dice(dices, 8, out);
        return new Result(0, calculateAttackDamage(baseDamage, verdict, damageIncreasePercent, 1.0, precision, out), true, 0, 1);
    }

    /**
     * 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat            사용할 스탯
     * @param prudence        신중함 패시브 적용 여부
     * @param calculatedMove  노림수 패시브 적용 여부
     * @param judge           심판자 패시브 적용 여부
     * @param judgementTarget 심판 대상 스킬 적용 여부
     * @param warning         경고 스킬 적용 여부
     * @param out             출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean prudence, boolean calculatedMove, boolean judge, boolean judgementTarget,
                               boolean warning, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        double finalMultiplier = 1.0;
        if (prudence) {
            out.println("신중함 패시브 적용: 데미지 3배 증가");
            finalMultiplier *= 3.0;
        }
        if (calculatedMove) {
            out.println("노림수 패시브 적용: 데미지 2배 증가");
            finalMultiplier *= 2.0;
        }
        if (judgementTarget) {
            out.println("심판 대상 스킬 적용: 데미지 2배 증가");
            finalMultiplier *= 2.0;
        }
        if (warning) {
            out.println("경고 스킬 적용: 데미지 3배 증가");
            finalMultiplier *= 3.0;
        }
        out.printf("최종 데미지 배율 : %.2f%n", finalMultiplier);
        int defaultDamage = Main.dice(1, 6, out);
        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        if (judge) {
            int judgeBonusPercent = Main.dice(1, 4, out);
            out.printf("심판자 패시브 적용: 데미지 +%d%%%n", judgeBonusPercent);
            damageIncreasePercent += judgeBonusPercent;
        }
        int finalDamage = calculateAttackDamage(defaultDamage, verdict, damageIncreasePercent, finalMultiplier, precision, out);
        return new Result(0, finalDamage, true, 0, 0);
    }

    /**
     * 퍼펙트 콤보샷 : D4, 2D6, 3D8, 4D10, D40의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat                       사용 스탯
     * @param weightedJudgmentCounterCount 가중 심판 적용 시 반격 횟수
     * @param precision                  정밀 스탯
     * @param out                        출력 스트림
     * @return 결과 객체
     */
    public static Result perfectComboShot(int stat, int weightedJudgmentCounterCount, int precision, PrintStream out) {
        out.println("건슬링거-퍼펙트 콤보샷 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 8);

        int damageIncreasePercent = getWeightedJudgmentBonus(weightedJudgmentCounterCount, out);
        int baseDamage = Main.dice(1, 4, out)
                + Main.dice(2, 6, out)
                + Main.dice(3, 8, out)
                + Main.dice(4, 10, out)
                + Main.dice(1, 40, out);
        int finalDamage = calculateAttackDamage(baseDamage, verdict, damageIncreasePercent, 1.0, precision, out);
        return new Result(0, finalDamage, true, 0, 8);
    }

    /**
     * 가중 심판 : 다음 4회의 공격 동안 지금까지 시행한 반격 수 x 30%만큼 데미지가 증가합니다. (마나 6 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result weightedJudgment(PrintStream out) {
        out.println("건슬링거-가중 심판 사용");
        out.println("다음 4회의 공격 동안 반격 횟수 x 30% 만큼 데미지가 증가합니다.");
        out.println("턴을 소모하지 않습니다. (쿨타임 7턴)");
        return new Result(0, 0, true, 6, 0);
    }
}
