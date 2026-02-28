package main.secondary.priest;

import main.Result;

import java.io.PrintStream;

/**
 * 빛의 사제
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 */
public class LightPriest {

    /**
     * 빛의 사제 헤븐즈 도어 : 1명의 죽음을 1회 극복시킵니다. 대상은 4D10의 체력으로 부활합니다. (마나 15 소모, 쿨타임 20턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result heavensDoor(int stat, PrintStream out) {
        out.println("빛의 사제-헤븐즈 도어 사용");
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 15, 0);
        }
        int healAmount = main.Main.dice(4, 10, out);
        out.printf("부활 후 체력 : %d\n", healAmount);
        return new Result(healAmount, 0, true, 15, 0);
    }

    /**
     * 빛의 사제 기도 : (영창 턴수)D6만큼 체력을 회복시킵니다. 영창 턴 수에 제한은 없습니다. (마나 4 소모, 쿨타임 8턴)
     *
     * @param stat       사용할 스탯
     * @param mercy      자비 패시브 적용 여부
     * @param favoritism 편애 패시브 적용 여부
     * @param transfer   양도 패시브 적용 여부
     * @param piety      신앙심 패시브 적용 여부
     * @param chantTurns 영창 턴수
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result prayer(int stat, boolean mercy, boolean favoritism, boolean transfer, boolean piety, int chantTurns, PrintStream out) {
        out.println("빛의 사제-기도 사용");
        return normalHeal(stat, chantTurns, 6, 4, mercy, favoritism, transfer, piety, out);
    }

    /**
     * 빛의 사제 기원 : 다음 턴까지 5D8의 보호막을 획득합니다. (마나 4 소모, 쿨타임 6턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result invocation(int stat, PrintStream out) {
        out.println("빛의 사제-기원 사용");
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 4, 0);
        }
        int shieldAmount = main.Main.dice(5, 8, out);
        out.printf("획득한 보호막 : %d\n", shieldAmount);
        return new Result(0, 0, true, 4, 0);
    }

    public static Result holyGrailOfLight(int stat, boolean mercy, boolean favoritism, boolean transfer, boolean piety, PrintStream out) {
        out.println("빛의 사제-빛의 성배 사용");
        return normalHeal(stat, 5, 4, 3, mercy, favoritism, transfer, piety, out);
    }

    /**
     * 빛의 사제 힐 : 대상의 체력을 1D6만큼 회복시킵니다. (마나 1 소모)
     *
     * @param stat       사용할 스탯
     * @param mercy      자비 패시브 적용 여부
     * @param favoritism 편애 패시브 적용 여부
     * @param transfer   양도 패시브 적용 여부
     * @param piety      신앙심 패시브 적용 여부
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result heal(int stat, boolean mercy, boolean favoritism, boolean transfer, boolean piety, PrintStream out) {
        out.println("빛의 사제-힐 사용");
        return normalHeal(stat, 1, 6, 1, mercy, favoritism, transfer, piety, out);
    }

    /**
     * 범용 힐링 함수
     *
     * @param stat       사용할 스탯
     * @param dices      주사위 개수
     * @param sides      주사위 면수
     * @param mana       소모 마나
     * @param mercy      자비 패시브 적용 여뷰(회복량 1.5배)
     * @param favoritism 편애 패시브 적용 여부(회복량 2.5배)
     * @param transfer   양도 패시브 적용 여부(본인 회복 불가, 회복량 1.5배)
     * @param piety      신앙심 패시브 적용 여부(회복량 2배, 아군 데미지 1.5배)
     * @param out        출력 스트림
     * @return 결과 객체
     */
    private static Result normalHeal(int stat, int dices, int sides, int mana, boolean mercy, boolean favoritism, boolean transfer, boolean piety, PrintStream out) {
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, mana, 0);
        }

        int healAmount = main.Main.dice(dices, sides, out);
        double healMultiplier = 1.0;
        if (mercy) {
            out.println("자비 패시브 적용: 회복량 1.5배");
            healMultiplier *= 1.5;
        }
        if (favoritism) {
            out.println("편애 패시브 적용: 회복량 2.5배");
            healMultiplier *= 2.5;
        }
        if (transfer) {
            out.println("양도 패시브 적용: 본인 회복 불가, 회복량 1.5배");
            healMultiplier *= 1.5;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 회복량 2배");
            healMultiplier *= 2.0;
        }
        healAmount = (int) Math.round(healAmount * healMultiplier);
        out.printf("최종 회복량 : %d\n", healAmount);
        return new Result(0, 0, true, mana, 0);

    }


    /**
     * 빛의 사제 치유의 바람 : 대상에게 2D6의 데미지를 입힙니다. (마나 2 소모, 쿨타임 2턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result healingWind(int stat, int precision, PrintStream out) {
        out.println("빛의 사제-치유의 바람 사용");
        return normalAttack(stat, 2, 6, 2, precision, out);
    }

    /**
     * 빛의 사제 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int precision, PrintStream out) {
        out.println("빛의 사제-기본공격 사용");
        return normalAttack(stat, 1, 6, 0, precision, out);
    }

    /**
     * @param stat  사용할 스탯
     * @param out   출력 스트림
     * @param dices 주사위 개수
     * @param sides 주사위 면수
     * @param mana 소모 마나
     * @return 결과 객체
     */
    private static Result normalAttack(int stat, int dices, int sides, int mana, int precision, PrintStream out) {
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, mana, 0);
        }

        int damage = main.Main.dice(dices, sides, out);

        int sideDamage = main.Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = main.Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, mana, 0);
    }
}
