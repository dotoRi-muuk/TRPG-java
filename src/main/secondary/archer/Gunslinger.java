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


    /**
     * 백스탭 : 판정 성공 시 공격을 회피합니다. 1.5배의 데미지로 반격합니다. (스태미나 3 소모)
     *
     * @param stat        사용 스탯
     * @param damageTaken 받은 데미지
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result backStab(int stat, int damageTaken, int precision, PrintStream out) {
        out.println("건슬링거-백스탭 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, 3);

        out.print("백스탭 반격 데미지 적용: 데미지 배율 1.5배\n");
        int finalDamage = (int) (damageTaken * 1.5);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
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
    public static Result opportunity(int stat, int baseDamage, int precision, PrintStream out) {
        out.println("건슬링거-활약 기회 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        out.print("활약 기회 신속 적용: 데미지 배율 1.5배\n");
        int finalDamage = (int) (baseDamage * 1.5);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, 0);
    }

    /**
     * 일점사 : 대상에게 6D6의 피해를 입힙니다. (스태미나 4 소모)
     *
     * @param stat 사용 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result focusedFire(int stat, int precision, PrintStream out) {
        out.println("건슬링거-일점사 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 4);
        int diceRoll = stat - verdict;
        return new Result(0, Main.criticalHit(precision, Main.normalCalculation(stat, out, 6, 6, diceRoll), out), true, 0, 4);
    }

    /**
     * 헤드샷 : 대상에게 D20의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat 사용 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result HeadShot(int stat, int precision, PrintStream out) {
        out.println("건슬링거-헤드샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 3);
        int diceRoll = stat - verdict;
        return new Result(0, Main.criticalHit(precision, Main.normalCalculation(stat, out, 1, 20, diceRoll), out), true, 0, 3);
    }

    /**
     * 헤드샷 : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat 사용 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result doubleShot(int stat, int precision, PrintStream out) {
        out.println("건슬링거-더블샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 2);
        int diceRoll = stat - verdict;
        return new Result(0, Main.criticalHit(precision, Main.normalCalculation(stat, out, 2, 6, diceRoll), out), true, 0, 2);
    }

    /**
     * 퀵드로우 : 대상에게 D8의 피해를 입힙니다. '신중함' 발동 시 4D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat     사용 스탯
     * @param prudence 신중함 패시브 적용 여부
     * @param out      출력 스트림
     * @return 결과 객체
     */
    public static Result quickDraw(int stat, boolean prudence, int precision, PrintStream out) {
        out.println("건슬링거-퀵드로우 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 1);
        int diceRoll = stat - verdict;
        int dices = 1;
        if (prudence) {
            out.println("신중함 패시브 적용: D8 -> 4D8 적용");
            dices = 4;
        }
        return new Result(0, Main.criticalHit(precision, Main.normalCalculation(stat, out, dices, 8, diceRoll), out), true, 0, 1);
    }

    /**
     * 퍼펙트 콤보샷 : D4 2D6 3D8 4D10 D40의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat      사용 스탯
     * @param precision 정밀 스탯
     * @param out       출력 스트림
     * @return 결과 객체
     */
    public static Result perfectComboShot(int stat, int precision, PrintStream out) {
        out.println("건슬링거-퍼펙트 콤보샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 8);
        int diceRoll = stat - verdict;

        int d4   = Main.dice(1,  4,  out);
        int d2d6 = Main.dice(2,  6,  out);
        int d3d8 = Main.dice(3,  8,  out);
        int d4d10= Main.dice(4, 10,  out);
        int d40  = Main.dice(1, 40,  out);
        int baseDamage = d4 + d2d6 + d3d8 + d4d10 + d40;
        out.printf("콤보샷 기본 데미지 (D4+2D6+3D8+4D10+D40): %d+%d+%d+%d+%d = %d%n",
                d4, d2d6, d3d8, d4d10, d40, baseDamage);

        int sideDamage = Main.sideDamage(baseDamage, stat, out, diceRoll);
        int finalDamage = baseDamage + sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);
        return new Result(0, finalDamage, true, 0, 8);
    }

    /**
     * 가중 심판 : 다음 4회의 공격동안 지금까지 시행한 반격의 수 x 30%만큼 데미지가 증가합니다.
     * 해당 스킬은 턴을 소모하지 않습니다.
     * (마나 6, 쿨타임 7턴)
     *
     * @param counterCount 지금까지 시행한 반격의 수
     * @param out          출력 스트림
     * @return 결과 객체 (마나 6 소모)
     */
    public static Result weightedJudgment(int counterCount, PrintStream out) {
        out.println("건슬링거-가중 심판 사용");
        out.println("!턴 소모 없음!");
        int damageBonus = counterCount * 30;
        out.printf("반격 횟수 %d × 30%% = 데미지 +%d%% (다음 4회 공격 적용)%n", counterCount, damageBonus);
        return new Result(0, 0, true, -6, 0);
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
    public static Result plain(int stat, boolean prudence, boolean calculatedMove, boolean judge, boolean judgementTarget, boolean warning, int precision, PrintStream out) {
        out.println("건슬링거-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;

        int modifier = 1;
        if (prudence) {
            out.println("신중함 패시브 적용: 데미지 3배 증가");
            modifier *= 3;
        }
        if (calculatedMove) {
            out.println("노림수 패시브 적용: 데미지 2배 증가");
            modifier *= 2;
        }
        if (judgementTarget) {
            out.println("심판 대상 스킬 적용: 데미지 2배 증가");
            modifier *= 2;
        }
        if (warning) {
            out.println("경고 스킬 적용: 데미지 3배 증가");
            modifier *= 3;
        }
        out.printf("총 배율 : %d\n", modifier);
        int defaultDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d\n", defaultDamage);
        int finalDamage = defaultDamage * modifier;
        out.printf("데미지 배율 적용 후 데미지 : %d\n", finalDamage);
        if (judge) {
            int judgeBonusPercent = Main.dice(1, 4, out);
            int judgeBonusDamage = finalDamage * judgeBonusPercent / 100;
            out.printf("심판자 패시브 적용: 추가 데미지 %d%% -> %d\n", judgeBonusPercent, judgeBonusDamage);
            finalDamage += judgeBonusDamage;
        }
        int sideDamage = Main.sideDamage(finalDamage, stat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, 0);
    }
}
