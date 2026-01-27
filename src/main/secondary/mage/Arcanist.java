package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.Random;

/**
 * 마도사
 * <p>
 * 판정 사용 스탯 : 지능
 */
public class Arcanist {

    /**
     * 폭주오라 : 영창 중 사용 가능합니다. ((기존 영창 시간) - (남은 영창 시간)) * 10% 만큼 받는 피해가 감소합니다. 이번 턴 상태이상을 1회 무시합니다. 순환에 영향을 받지 않습니다. (마나 3 소모)
     *
     * @param stat          사용할 스탯
     * @param damageTaken   받는 피해량
     * @param totalCast     기존 영창 시간
     * @param remainingCast 남은 영창 시간
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result rampageAura(int stat, int damageTaken, int totalCast, int remainingCast, PrintStream out) {
        out.println("마도사-폭주오라 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 3, 0);

        double damageReduction = ((totalCast - remainingCast) * 0.1);
        out.printf("폭주오라 패시브 적용: ((기존 영창 시간) - (남은 영창 시간)) * 10%% 만큼 받는 피해가 감소. (%.1f%%)%n", damageReduction * 100);

        int reducedDamage = (int) (damageTaken * damageReduction);
        out.printf("감소된 피해량 : %d%n", reducedDamage);

        return new Result(damageTaken - reducedDamage, 0, true, 3, 0);
    }


    /**
     * 루멘 컨버전 : (광역) 4D20 또는 (1인) 10D12 중 선택하여 사용합니다. 피격 대상은 다음 턴까지 마나 사용이 봉인됩니다. (영창 18턴, 마나 15 소모, 쿨타임 20턴)
     *
     * @param stat         사용할 스탯
     * @param condensation 영창 진행 턴 수 (응집 : 영창 진행 턴 수 * 50% 만큼 데미지 증가)
     * @param annihilator  어나일레이터 패시브 활성화 여부 (데미지 3배 증가, 마나 소모 2배)
     * @param global       광역 사용 여부 (true: 4D20, false: 10D12)
     * @param out          출력 스트림
     * @return 결과 객체
     */
    public static Result lumenConversion(int stat, int condensation, boolean annihilator, boolean global, PrintStream out) {
        out.println("마도사-루멘 컨버전 사용");

        if (global) {
            out.println("광역 사용: 4D20");
            return basicAttack(stat, condensation, 4, 20, annihilator, 15, 18, out);
        } else {
            out.println("1인 사용: 10D12");
            return basicAttack(stat, condensation, 10, 12, annihilator, 15, 18, out);
        }
    }

    /**
     * 에테르 카타스트로피 : 대상에게 5D20의 피해를 입힙니다. (영창 10턴, 마나 7 소모, 쿨타임 10턴)
     *
     * @param stat         사용할 스탯
     * @param condensation 영창 진행 턴 수 (응집 : 영창 진행 턴 수 * 50% 만큼 데미지 증가)
     * @param annihilator  어나일레이터 패시브 활성화 여부 (데미지 3배 증가, 마나 소모 2배)
     * @param out          출력 스트림
     * @return 결과 객체
     */
    public static Result etherCatastrophe(int stat, int condensation, boolean annihilator, PrintStream out) {
        out.println("마도사-에테르 카타스트로피 사용");

        return basicAttack(stat, condensation, 5, 20, annihilator, 7, 10, out);
    }

    /**
     * 마력탄 : 대상에게 5D4의 피해를 입힙니다. (마나 2 소모, 쿨타임 3턴)
     *
     * @param stat         사용할 스탯
     * @param condensation 영창 진행 턴 수 (응집 : 영창 진행 턴 수 * 50% 만큼 데미지 증가)
     * @param annihilator  어나일레이터 패시브 활성화 여부 (데미지 3배 증가, 마나 소모 2배)
     * @param out          출력 스트림
     * @return 결과 객체
     */
    public static Result manaBullet(int stat, int condensation, boolean annihilator, PrintStream out) {
        out.println("마도사-마력탄 사용");

        return basicAttack(stat, condensation, 5, 4, annihilator, 2, 0, out);
    }

    private static Result basicAttack(int stat, int condensation, int dices, int sides, boolean annihilator, int mana, int cast, PrintStream out) {
        int verdict = Main.verdict(stat, out);

        if (annihilator) mana *= 2;

        if (verdict <= 0) return new Result(0, 0, false, mana, 0);

        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // 응집 패시브
        if (condensation > 0) {
            double condensationBonus = condensation * 0.5 + 1.0;
            out.printf("응집 스킬 적용: 영창 진행 턴 수 %d * 50%% 만큼 데미지 증가%n", condensation);
            baseDamage = (int) (baseDamage * condensationBonus);
            out.printf("응집 스킬 적용 데미지 : %d%n", baseDamage);
        }

        // 어나일레이터 패시브
        if (annihilator) {
            out.println("어나일레이터 스킬 적용: 데미지 3배 증가, 마나 소모 2배 증가");
            baseDamage *= 3;
            out.printf("어나일레이터 스킬 적용 데미지 : %d%n", baseDamage);
        }

        // 마력의 범람 패시브
        if (mana > 0) {
            double manaOverflowBonus = mana * 0.3;
            out.printf("마력의 범람 패시브 적용: (해당 스킬 발동에 소모한 총 마나) * 30%% 만큼 데미지 증가 (+%.1f)%n", manaOverflowBonus);
            baseDamage += (int) manaOverflowBonus;
            out.printf("마력의 범람 적용 데미지 : %d%n", baseDamage);
        }

        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        baseDamage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", baseDamage);

        //마도학 패시브
        if (cast > 0) {
            Random random = new Random();
            int i = random.nextInt(10) + 1;
            if (i <= cast) {
                out.printf("마도학 패시브 적용: 영창 시간 %d * 10%% 확률로 적의 수비를 무시함 (주사위 결과: %d, 성공)%n", cast, i);
                return new Result(baseDamage, 0, true, mana, 0);
            } else {
                out.printf("마도학 패시브 적용: 영창 시간 %d * 10%% 확률로 적의 수비를 무시함 (주사위 결과: %d, 실패)%n", cast, i);
            }
        }

        return new Result(baseDamage, 0, true, mana, 0);
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat        사용할 스탯
     * @param annihilator 어나일레이터 패시브 활성화 여부 (데미지 3배 증가, 마나 소모 2배)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean annihilator, PrintStream out) {
        out.println("마도사-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // 어나일레이터 패시브
        if (annihilator) {
            out.println("어나일레이터 패시브 적용: 데미지 3배 증가");
            baseDamage *= 3;
            out.printf("어나일레이터 적용 데미지 : %d%n", baseDamage);
        }

        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        baseDamage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", baseDamage);

        return new Result(baseDamage, 0, true, 0, 0);
    }

}
