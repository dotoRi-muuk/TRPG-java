package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

public class Gunslinger {
    /*
    건슬링거

    판정 사용 스탯 : 민첩

    기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.

    신중함 (패시브) : 전투 외의 상황에서는 총을 넣습니다. 1회 한정 총을 뽑아 공격하며, 해당 공격의 데미지가 3배로 증가합니다.
    응징 (패시브) : 반격을 한 번에 2회 진행할 수 있습니다.
    노림수 (패시브) : 적의 공격 회피 성공 시 다음 자신의 턴에 데미지가 2배로 증가합니다.
    심판자 (패시브) : 전투 시작 후 5번째 턴에, 그리고 매 5턴마다 발동합니다. 기본 공격 발동 시 (1D4)%의 추가 데미지를 입힙니다.

    퀵드로우 (기술) : 대상에게 D8의 피해를 입힙니다. '신중함' 발동 시 4D8의 피해를 입힙니다. (스태미나 1 소모)
    더블샷 (기술) : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
    헤드샷 (기술) : 대상에게 D20의 피해를 입힙니다. (스태미나 3 소모)
    일점사 (기술) : 대상에게 6D6의 피해를 입힙니다. (스태미나 4 소모)

    예고장 (스킬) : 대상에게 가하는 반격 데미지가 2배로 증가합니다. (중첩 불가) (마나 6 소모, 쿨타임 15턴)
    심판 대상 (스킬) : 지정한 적에게 가하는 데미지가 2배로 증가합니다. 그 외의 대상 공격 시 자신이 피해를 입습니다. (중첩 불가) 쿨타임 이후에 재사용하여 해제하거나 대상을 변경할 수 있습니다. (마나 8 소모, 쿨타임 10턴)
    빈틈 발견 (스킬) : 다음 턴까지 반격 시 기본 공격 대신 기술을 사용할 수 있습니다. (마나 4 소모, 쿨타임 4턴)
    재정비 (스킬) : 전장에서 이탈합니다. 다음 2턴동안 은신이 지속됩니다. 건슬링거를 타겟팅하는 스킬들이 해제됩니다. (마나 3 소모, 쿨타임 10턴)
    경고 (스킬) : 다음 턴까지 반격 데미지가 3배로 증가합니다. 다음 턴까지 반격하지 않을 시, 그 다음 턴에는 공격할 수 없습니다. 해당 스킬은 턴을 소모하지 않습니다. (마나 5 소모, 쿨타임 8턴)

    활약 기회 (전용 신속) : 신속 판정 성공 시 이번 턴 반격 데미지가 1.5배로 증가합니다.
    백스탭 (전용 수비) : 판정 성공 시 공격을 회피합니다. 1.5배의 데미지로 반격합니다. (스태미나 3 소모)
     */


    /**
     * 백스탭
     * @param stat 사용 스탯
     * @param damageTaken 받은 데미지
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result backStab(int stat, int damageTaken, PrintStream out) {
        out.println("건슬링거-백스탭 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, 3);

        out.print("백스탭 반격 데미지 적용: 데미지 배율 1.5배\n");
        int finalDamage = (int) (damageTaken * 1.5);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, 3);
    }

    /**
     * 활약 기회
     * @param stat 사용 스탯
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result opportunity(int stat, int baseDamage, PrintStream out) {
        out.println("건슬링거-활약 기회 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        out.print("활약 기회 신속 적용: 데미지 배율 1.5배\n");
        int finalDamage = (int) (baseDamage * 1.5);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, 0);
    }

    /**
     * 일점사
     * @param stat 사용 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result focusedFire(int stat, PrintStream out) {
        out.println("건슬링거-일점사 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 4);

        return new Result(0, Main.normalCalculation(stat, out, 6, 6), true, 0, 4);
    }

    /**
     * 헤드샷
     * @param stat 사용 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result HeadShot(int stat, PrintStream out) {
        out.println("건슬링거-헤드샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 3);

        return new Result(0, Main.normalCalculation(stat, out, 1, 20), true, 0, 3);
    }

    /**
     * 헤드샷
     * @param stat 사용 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result doubleShot(int stat, PrintStream out) {
        out.println("건슬링거-더블샷 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 2);

        return new Result(0, Main.normalCalculation(stat, out, 2, 6), true, 0, 2);
    }

    /**
     * 퀵드로우
     * @param stat 사용 스탯
     * @param prudence 신중함 패시브 적용 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result quickDraw(int stat, boolean prudence, PrintStream out) {
        out.println("건슬링거-퀵드로우 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 1);

        int dices = 1;
        if (prudence) {
            out.println("신중함 패시브 적용: D8 -> 4D8 적용");
            dices = 4;
        }
        return new Result(0, Main.normalCalculation(stat, out, dices, 8), true, 0, 1);
    }

    /**
     * 기본공격
     * @param stat 사용할 스탯
     * @param prudence 신중함 패시브 적용 여부
     * @param calculatedMove 노림수 패시브 적용 여부
     * @param judge 심판자 패시브 적용 여부
     * @param judgementTarget 심판 대상 스킬 적용 여부
     * @param warning 경고 스킬 적용 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean prudence, boolean calculatedMove, boolean judge, boolean judgementTarget, boolean warning, PrintStream out) {
        out.println("건슬링거-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

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
        int sideDamage = Main.sideDamage(finalDamage, stat, out);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, 0);
    }
}
