package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.Random;

/**
 * 마도사
 * <p>
 * 판정 사용 스탯 : 지능
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 * <p>
 * 패시브:
 * - 마도학: 피해를 주는 스킬 발동 시 (해당 스킬의 원래 영창 시간) * 10% 확률로 적의 수비를 무시합니다.
 * - 넘치는 지혜: 영창을 포함한 스킬 발동 시 마나를 추가로 소모할 수 있습니다. 추가로 소모한 마나 2당 영창 시간이 1턴씩 감소합니다.
 * - 마력의 범람: (해당 스킬 영창 시작으로부터 소모한 총 마나) * 40% 만큼 최종 데미지가 증가합니다.
 * - 마나 합성: 마나가 (마나 스탯)*3 증가합니다. 휴식 시 마나를 (마나 스탯) 추가로 회복합니다. 스태미나를 회복하지 않는 대신 마나를 자신의 레벨만큼 추가로 회복할 수 있습니다.
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
     * @param stat            사용할 스탯
     * @param magicLandTurns  마력의 땅 영창 진행 턴 수 (마력의 땅 : 영창 진행 턴 수 * 70% 만큼 데미지 증가)
     * @param annihilator     어나일레이터 스킬 활성화 여부 (최종 데미지 5배, 마나 소모 2배)
     * @param global          광역 사용 여부 (true: 4D20, false: 10D12)
     * @param precision       정밀 스탯
     * @param out             출력 스트림
     * @return 결과 객체
     */
    public static Result lumenConversion(int stat, int magicLandTurns, boolean annihilator, boolean global, int precision, PrintStream out) {
        out.println("마도사-루멘 컨버전 사용");

        if (global) {
            out.println("광역 사용: 4D20");
            return basicAttack(stat, magicLandTurns, 4, 20, annihilator, 15, 18, precision, out);
        } else {
            out.println("1인 사용: 10D12");
            return basicAttack(stat, magicLandTurns, 10, 12, annihilator, 15, 18, precision, out);
        }
    }

    /**
     * 에테르 카타스트로피 : 대상에게 5D20의 피해를 입힙니다. (영창 10턴, 마나 7 소모, 쿨타임 10턴)
     *
     * @param stat           사용할 스탯
     * @param magicLandTurns 마력의 땅 영창 진행 턴 수 (마력의 땅 : 영창 진행 턴 수 * 70% 만큼 데미지 증가)
     * @param annihilator    어나일레이터 스킬 활성화 여부 (최종 데미지 5배, 마나 소모 2배)
     * @param precision      정밀 스탯
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result etherCatastrophe(int stat, int magicLandTurns, boolean annihilator, int precision, PrintStream out) {
        out.println("마도사-에테르 카타스트로피 사용");

        return basicAttack(stat, magicLandTurns, 5, 20, annihilator, 7, 10, precision, out);
    }

    /**
     * 마력탄 : 대상에게 5D4의 피해를 입힙니다. (마나 2 소모, 쿨타임 3턴)
     *
     * @param stat           사용할 스탯
     * @param magicLandTurns 마력의 땅 영창 진행 턴 수 (마력의 땅 : 영창 진행 턴 수 * 70% 만큼 데미지 증가)
     * @param annihilator    어나일레이터 스킬 활성화 여부 (최종 데미지 5배, 마나 소모 2배)
     * @param precision      정밀 스탯
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result manaBullet(int stat, int magicLandTurns, boolean annihilator, int precision, PrintStream out) {
        out.println("마도사-마력탄 사용");

        return basicAttack(stat, magicLandTurns, 5, 4, annihilator, 2, 0, precision, out);
    }

    /**
     * 스킬 공격 공통 함수
     * <p>
     * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     * <p>
     * - (100 + 데미지)%: 마력의 땅(+magicLandTurns*70%), 마력의 범람(+mana*40%)
     * - (최종 데미지)%: 어나일레이터(x5)
     * - (주사위 보정): sideDamage (판정 주사위 재사용) + 정밀 판정(치명타)
     *
     * @param stat           사용할 스탯
     * @param magicLandTurns 마력의 땅 영창 진행 턴 수
     * @param dices          주사위 개수
     * @param sides          주사위 면수
     * @param annihilator    어나일레이터 스킬 활성화 여부 (최종 데미지 5배, 마나 소모 2배)
     * @param mana           기본 마나 소모량
     * @param cast           해당 스킬의 원래 영창 시간 (마도학 패시브 판정에 사용)
     * @param precision      정밀 스탯
     * @param out            출력 스트림
     * @return 결과 객체
     */
    private static Result basicAttack(int stat, int magicLandTurns, int dices, int sides, boolean annihilator, int mana, int cast, int precision, PrintStream out) {
        int verdict = Main.verdict(stat, out);

        if (annihilator) mana *= 2;

        if (verdict <= 0) return new Result(0, 0, false, mana, 0);
        int diceRoll = stat - verdict;

        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // (100 + 데미지)% 구성 — 덧셈 퍼센트 보정
        int flatBonus = 0;

        // 마력의 땅 스킬 효과
        if (magicLandTurns > 0) {
            int magicLandBonus = magicLandTurns * 70;
            out.printf("마력의 땅 스킬 적용: 영창 진행 턴 수 %d * 70%% 만큼 데미지 증가 (+%d%%)%n", magicLandTurns, magicLandBonus);
            flatBonus += magicLandBonus;
        }

        // 마력의 범람 패시브: (해당 스킬 영창 시작으로부터 소모한 총 마나) * 40% 만큼 최종 데미지 증가
        if (mana > 0) {
            int manaOverflowBonus = mana * 40;
            out.printf("마력의 범람 패시브 적용: (해당 스킬 영창 시작으로부터 소모한 총 마나) * 40%% 만큼 데미지 증가 (+%d%%)%n", manaOverflowBonus);
            flatBonus += manaOverflowBonus;
        }

        // (최종 데미지)% 구성 — 곱셈 배율 보정
        double finalMultiplier = 1.0;

        // 어나일레이터 스킬: 최종 데미지 5배, 마나 소모 2배
        if (annihilator) {
            out.println("어나일레이터 스킬 적용: 최종 데미지 5배, 마나 소모 2배 증가");
            finalMultiplier *= 5.0;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out);

        // 주사위 보정 (판정 주사위 재사용)
        int sideDamage = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        //마도학 패시브
        if (cast > 0) {
            Random random = new Random();
            int i = random.nextInt(10) + 1;
            if (i <= cast) {
                out.printf("마도학 패시브 적용: 영창 시간 %d * 10%% 확률로 적의 수비를 무시함 (주사위 결과: %d, 성공)%n", cast, i);
                return new Result(damage, 0, true, mana, 0);
            } else {
                out.printf("마도학 패시브 적용: 영창 시간 %d * 10%% 확률로 적의 수비를 무시함 (주사위 결과: %d, 실패)%n", cast, i);
            }
        }

        return new Result(damage, 0, true, mana, 0);
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     * <p>
     * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param stat        사용할 스탯
     * @param annihilator 어나일레이터 스킬 활성화 여부 (최종 데미지 5배, 마나 소모 2배)
     * @param precision   정밀 스탯
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

        // (100 + 데미지)% 구성 — 덧셈 퍼센트 보정
        int flatBonus = 0;

        // (최종 데미지)% 구성 — 곱셈 배율 보정
        double finalMultiplier = 1.0;

        // 어나일레이터 스킬: 최종 데미지 5배
        if (annihilator) {
            out.println("어나일레이터 스킬 적용: 최종 데미지 5배");
            finalMultiplier *= 5.0;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out);

        // 주사위 보정 (판정 주사위 재사용)
        int sideDamage = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(damage, 0, true, 0, 0);
    }

    /**
     * 엘레아 엑시디움 노바 : 4D20, 6D4, D50의 피해를 입힙니다.
     * 공격 주사위의 판정된 기본 값만큼 마나를 회복합니다.
     * 회복한 마나의 5분의 1만큼 스킬 3개의 쿨타임을 감소시킵니다.
     * (영창 16턴, 마나 40-레벨, 쿨타임 20턴)
     * <p>
     * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param stat           사용할 스탯
     * @param level          캐릭터 레벨 (마나 소모 = 40 - 레벨)
     * @param magicLandTurns 마력의 땅 영창 진행 턴 수 (마력의 땅 : 영창 진행 턴 수 * 70% 만큼 데미지 증가)
     * @param annihilator    어나일레이터 스킬 활성화 여부 (최종 데미지 5배, 마나 소모 2배)
     * @param precision      정밀 스탯
     * @param out            출력 스트림
     * @return 결과 객체 (manaUsed는 순소모, 음수이면 순회복)
     */
    public static Result eleaExidiumNova(int stat, int level, int magicLandTurns, boolean annihilator, int precision, PrintStream out) {
        out.println("마도사-엘레아 엑시디움 노바 사용");
        int baseMana = Math.max(0, 40 - level);
        if (annihilator) baseMana *= 2;

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, baseMana, 0);
        int diceRoll = stat - verdict;

        // 4D20 + 6D4 + 1D50
        int d20Damage = Main.dice(4, 20, out);
        out.printf("4D20 결과: %d%n", d20Damage);
        int d4Damage = Main.dice(6, 4, out);
        out.printf("6D4 결과: %d%n", d4Damage);
        int d50Damage = Main.dice(1, 50, out);
        out.printf("D50 결과: %d%n", d50Damage);

        int rawTotal = d20Damage + d4Damage + d50Damage;
        out.printf("기본 데미지 합계 : %d%n", rawTotal);

        // (100 + 데미지)% 구성 — 덧셈 퍼센트 보정
        int flatBonus = 0;

        // 마력의 땅 스킬 효과
        if (magicLandTurns > 0) {
            int magicLandBonus = magicLandTurns * 70;
            out.printf("마력의 땅 스킬 적용: 영창 진행 턴 수 %d * 70%% 만큼 데미지 증가 (+%d%%)%n", magicLandTurns, magicLandBonus);
            flatBonus += magicLandBonus;
        }

        // 마력의 범람 패시브: (해당 스킬 영창 시작으로부터 소모한 총 마나) * 40% 만큼 최종 데미지 증가
        if (baseMana > 0) {
            int manaOverflowBonus = baseMana * 40;
            out.printf("마력의 범람 패시브 적용: (해당 스킬 영창 시작으로부터 소모한 총 마나) * 40%% 만큼 데미지 증가 (+%d%%)%n", manaOverflowBonus);
            flatBonus += manaOverflowBonus;
        }

        // (최종 데미지)% 구성 — 곱셈 배율 보정
        double finalMultiplier = 1.0;

        // 어나일레이터 스킬: 최종 데미지 5배
        if (annihilator) {
            out.println("어나일레이터 스킬 적용: 최종 데미지 5배");
            finalMultiplier *= 5.0;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        int damage = Main.calculateDamage(rawTotal, flatBonus, finalMultiplier, out);

        // 주사위 보정 (판정 주사위 재사용)
        int sideDamage = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        // 마나 회복: 공격 주사위 기본값(d20+d4+d50 합) 만큼 회복
        int manaRecovered = d20Damage + d4Damage + d50Damage;
        out.printf("마나 회복: 공격 주사위 기본 값 합계 %d 회복%n", manaRecovered);

        // 쿨타임 감소: 회복한 마나 / 5 만큼 스킬 3개 쿨타임 감소
        int cooldownReduction = manaRecovered / 5;
        out.printf("쿨타임 감소: 회복 마나 %d / 5 = %d 턴만큼 스킬 3개 쿨타임 감소%n", manaRecovered, cooldownReduction);

        int netMana = baseMana - manaRecovered;
        out.printf("마나 소모 %d - 회복 %d = 순 마나 변화: %d (음수이면 순 회복)%n", baseMana, manaRecovered, netMana);
        return new Result(0, damage, true, netMana, 0);
    }

    /**
     * 마나 회로 수복 : 영창을 진행합니다.
     * 지난 (영창 진행량)턴 동안 사용한 마나의 3분의 1을 회복합니다.
     * (마나 3, 쿨타임 6턴)
     *
     * @param chantProgress    영창 진행 턴 수
     * @param manaUsedInPeriod 지난 (영창 진행량)턴 동안 사용한 마나
     * @param out              출력 스트림
     * @return 결과 객체 (manaUsed = 3 - 회복량, 음수이면 순수 회복)
     */
    public static Result manaCircuitRestoration(int chantProgress, int manaUsedInPeriod, PrintStream out) {
        out.println("마도사-마나 회로 수복 사용");
        out.printf("영창 진행량: %d턴. 지난 %d턴 동안 사용한 마나: %d%n", chantProgress, chantProgress, manaUsedInPeriod);
        int manaRecovered = manaUsedInPeriod / 3;
        out.printf("회복할 마나: %d / 3 = %d%n", manaUsedInPeriod, manaRecovered);
        int netMana = 3 - manaRecovered;
        out.printf("마나 소모 3 - 회복 %d = 순 마나 변화: %d (음수이면 순수 회복)%n", manaRecovered, netMana);
        return new Result(0, 0, true, netMana, 0);
    }

}

