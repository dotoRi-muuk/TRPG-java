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
    public static Result lumenConversion(int stat, int condensation, boolean annihilator, boolean global, int precision, PrintStream out) {
        out.println("마도사-루멘 컨버전 사용");

        if (global) {
            out.println("광역 사용: 4D20");
            return basicAttack(stat, condensation, 4, 20, annihilator, 15, 18, precision, out);
        } else {
            out.println("1인 사용: 10D12");
            return basicAttack(stat, condensation, 10, 12, annihilator, 15, 18, precision, out);
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
    public static Result etherCatastrophe(int stat, int condensation, boolean annihilator, int precision, PrintStream out) {
        out.println("마도사-에테르 카타스트로피 사용");

        return basicAttack(stat, condensation, 5, 20, annihilator, 7, 10, precision, out);
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
    public static Result manaBullet(int stat, int condensation, boolean annihilator, int precision, PrintStream out) {
        out.println("마도사-마력탄 사용");

        return basicAttack(stat, condensation, 5, 4, annihilator, 2, 0, precision, out);
    }

    private static Result basicAttack(int stat, int condensation, int dices, int sides, boolean annihilator, int mana, int cast, int precision, PrintStream out) {
        int verdict = Main.verdict(stat, out);

        if (annihilator) mana *= 2;

        if (verdict <= 0) return new Result(0, 0, false, mana, 0);
        int diceRoll = stat - verdict;

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

        int sideDamage = Main.sideDamage(baseDamage, stat, out, diceRoll);
        baseDamage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        baseDamage = Main.criticalHit(precision, baseDamage, out);
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
    public static Result plain(int stat, boolean annihilator, int precision, PrintStream out) {
        out.println("마도사-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // 어나일레이터 패시브
        if (annihilator) {
            out.println("어나일레이터 패시브 적용: 데미지 3배 증가");
            baseDamage *= 3;
            out.printf("어나일레이터 적용 데미지 : %d%n", baseDamage);
        }

        int sideDamage = Main.sideDamage(baseDamage, stat, out, diceRoll);
        baseDamage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        baseDamage = Main.criticalHit(precision, baseDamage, out);
        out.printf("최종 데미지 : %d%n", baseDamage);

        return new Result(baseDamage, 0, true, 0, 0);
    }

    /**
     * 엘레아 엑시디움 노바 :
     * 4D20 + 6D4 + D50의 피해를 입히고,
     * 공격 주사위 기본값만큼 마나를 회복하며, 회복 마나의 1/5만큼 스킬 3개의 쿨타임을 감소시킵니다.
     * (영창 16턴, 마나 40-레벨, 쿨타임 20턴)
     */
    public static Result eleaExcidiumNova(int stat, int level, int precision, PrintStream out) {
        out.println("마도사-엘레아 엑시디움 노바 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, Math.max(0, 40 - level), 0);
        int diceRoll = stat - verdict;

        int rawDiceDamage = Main.dice(4, 20, out) + Main.dice(6, 4, out) + Main.dice(1, 50, out);
        out.printf("기본 데미지 (4D20 + 6D4 + D50) : %d%n", rawDiceDamage);

        int sideDamage = Main.sideDamage(rawDiceDamage, stat, out, diceRoll);
        int finalDamage = rawDiceDamage + sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);

        int manaRecover = rawDiceDamage;
        int cooldownReduction = manaRecover / 5;
        int manaCost = Math.max(0, 40 - level);
        int netMana = manaCost - manaRecover;
        out.printf("마나 회복: 공격 주사위 기본값만큼 %d 회복%n", manaRecover);
        out.printf("쿨타임 감소: 회복 마나의 1/5 = %d (스킬 3개)%n", cooldownReduction);
        out.printf("마나 소모: (40 - 레벨) = %d, 순마나 변화: %d%n", manaCost, -netMana);

        return new Result(0, finalDamage, true, netMana, 0);
    }

    /**
     * 마나 회로 수복 :
     * 영창을 진행하며 지난 (영창 진행량)턴 동안 사용한 마나의 1/3을 회복합니다. (마나 3, 쿨타임 6턴)
     */
    public static Result manaCircuitRestoration(int chantProgress, int manaSpentDuringChant, PrintStream out) {
        out.println("마도사-마나 회로 수복 사용");
        int recover = Math.max(0, manaSpentDuringChant) / 3;
        int netManaUsed = 3 - recover;
        out.printf("영창 진행량: %d턴%n", Math.max(0, chantProgress));
        out.printf("지난 %d턴 사용 마나의 1/3 회복: %d%n", Math.max(0, chantProgress), recover);
        out.printf("순마나 변화: %d (양수=소모, 음수=회복)%n", netManaUsed);
        return new Result(0, 0, true, netManaUsed, 0);
    }

}
