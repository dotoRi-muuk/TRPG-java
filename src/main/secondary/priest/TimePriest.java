package main.secondary.priest;

import main.Result;

import java.io.PrintStream;

/**
 * 시간의 사제
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 */
public class TimePriest {

    /**
     * 시간의 사제 부식 : (자유 영창) 영창하는 동안 적 1명에게 매 턴 영창 1회 당 4D8의 피해를 입힙니다. (마나 15 소모, 쿨타임 10턴)
     *
     * @param stat       사용할 스탯
     * @param impatience 성급함 패시브 적용 여부
     * @param turns      행동 횟수(성급함 패시브)
     * @param piety      신앙심 패시브 적용 여부
     * @param chantTurns 영창 턴수
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result corrosion(int stat, boolean impatience, int turns, boolean piety, int chantTurns, int precision, PrintStream out) {
        out.println("시간의 사제-부식 사용");
        return attack(stat, chantTurns * 4, 8, 15, impatience, turns, piety, precision, out);
    }

    /**
     * 시간의 사제 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat       사용할 스탯
     * @param impatience 성급함 패시브 적용 여부
     * @param turns      행동 횟수(성급함 패시브)
     * @param piety      신앙심 패시브 적용 여부
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean impatience, int turns, boolean piety, int precision, PrintStream out) {
        out.println("시간의 사제-기본공격 사용");
        return attack(stat, 1, 6, 0, impatience, turns, piety, precision, out);
    }

    /**
     * 범용 공격 메소드
     *
     * @param stat       사용할 스탯
     * @param dices      공격의 주사위 개수
     * @param sides      공격의 주사위 면수
     * @param mana       소모 마나
     * @param impatience 성급함 패시브 적용 여부(행동 횟수 n+1일 때 자신이 가하는 피해가 100n% 증가)
     * @param turns      행동 횟수(성급함 패시브)
     * @param piety      신앙심 패시브 적용 여부(모든 아군 데미지 2배)
     * @param out        출력 스트림
     * @return 결과 객체
     */
    private static Result attack(int stat, int dices, int sides, int mana, boolean impatience, int turns, boolean piety, int precision, PrintStream out) {
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, mana, 0);
        }
        int baseDamage = main.Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);
        double damageMultiplier = 1.0;
        if (impatience) {
            double impatienceBonus = (turns - 1) * 1.0 + 1.0;
            out.printf("성급함 패시브 적용: 행동 횟수 %d+1일 때 자신이 가하는 피해 %.0f%% 증가%n", turns - 1, (impatienceBonus - 1) * 100);
            damageMultiplier *= impatienceBonus;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 모든 아군 데미지 2배");
            damageMultiplier *= 2.0;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d%n", damage);
        int sideDamage = main.Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        damage = main.Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);
        return new Result(0, damage, true, mana, 0);
    }
}
