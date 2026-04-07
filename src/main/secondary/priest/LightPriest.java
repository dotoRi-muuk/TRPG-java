package main.secondary.priest;

import main.Result;

import java.io.PrintStream;

/**
 * 빛의 사제
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 * <p>
 * 회복량 계산 공식: [(기본 회복량) x (100 + 회복량 증가)%] x (최종 회복량 증가)% x (주사위 보정)
 * 레벨 기반 최종 회복량 기본 계수: (100 + (레벨)^2 / 2)%
 */
public class LightPriest {

    /**
     * 빛의 사제 헤븐즈 도어 : 1명의 죽음을 1회 극복시킵니다. 대상은 4D10의 체력으로 부활합니다.
     * (마나 15 소모, 영창 3턴, 쿨타임 20턴)
     *
     * @param stat            사용할 스탯
     * @param mercy           자비 패시브 적용 여부 (최종 회복량 1.2배)
     * @param favoritism      편애 패시브 적용 여부 (회복량 +50%)
     * @param transfer        양도 패시브 적용 여부 (회복량 +50%)
     * @param piety           신앙심 패시브 적용 여부 (회복량 +100%)
     * @param healBonus       직업 스킬 외 추가 회복량 증가 (%)
     * @param finalHealBonus  직업 스킬 외 추가 최종 회복량 증가 (%)
     * @param level           캐릭터 레벨 (최종 회복량 기본 계수 산출에 사용)
     * @param out             출력 스트림
     * @return 결과 객체
     */
    public static Result heavensDoor(int stat, boolean mercy, boolean favoritism, boolean transfer,
                                     boolean piety, int healBonus, int finalHealBonus, int level,
                                     PrintStream out) {
        out.println("빛의 사제-헤븐즈 도어 사용");
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 15, 0);
        }
        int baseHeal = main.Main.dice(4, 10, out);
        int healAmount = applyHealFormula(baseHeal, verdict, mercy, favoritism, transfer, piety,
                healBonus, finalHealBonus, level, out);
        out.printf("부활 후 체력 : %d\n", healAmount);
        return new Result(0, healAmount, true, 15, 0);
    }

    /**
     * 빛의 사제 기도 : 영창을 1턴 진행할 때마다 D6만큼 체력을 회복시킵니다.
     * 영창 턴 수에 제한은 없습니다. 피격 시 스킬이 취소됩니다.
     * (마나 4 소모, 쿨타임 8턴)
     *
     * @param stat           사용할 스탯
     * @param mercy          자비 패시브 적용 여부 (최종 회복량 1.2배)
     * @param favoritism     편애 패시브 적용 여부 (회복량 +50%)
     * @param transfer       양도 패시브 적용 여부 (회복량 +50%)
     * @param piety          신앙심 패시브 적용 여부 (회복량 +100%)
     * @param chantTurns     영창 턴수
     * @param healBonus      직업 스킬 외 추가 회복량 증가 (%)
     * @param finalHealBonus 직업 스킬 외 추가 최종 회복량 증가 (%)
     * @param level          캐릭터 레벨
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result prayer(int stat, boolean mercy, boolean favoritism, boolean transfer,
                                boolean piety, int chantTurns, int healBonus, int finalHealBonus,
                                int level, PrintStream out) {
        out.println("빛의 사제-기도 사용 (영창 " + chantTurns + "턴)");
        return normalHeal(stat, chantTurns, 6, 4, mercy, favoritism, transfer, piety,
                healBonus, finalHealBonus, level, out);
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
        return new Result(0, shieldAmount, true, 4, 0);
    }

    /**
     * 빛의 사제 빛의 성배 : 대상의 체력을 5D4만큼 회복시킵니다. (마나 3 소모, 쿨타임 4턴)
     *
     * @param stat           사용할 스탯
     * @param mercy          자비 패시브 적용 여부
     * @param favoritism     편애 패시브 적용 여부
     * @param transfer       양도 패시브 적용 여부
     * @param piety          신앙심 패시브 적용 여부
     * @param healBonus      직업 스킬 외 추가 회복량 증가 (%)
     * @param finalHealBonus 직업 스킬 외 추가 최종 회복량 증가 (%)
     * @param level          캐릭터 레벨
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result holyGrailOfLight(int stat, boolean mercy, boolean favoritism, boolean transfer,
                                          boolean piety, int healBonus, int finalHealBonus, int level,
                                          PrintStream out) {
        out.println("빛의 사제-빛의 성배 사용");
        return normalHeal(stat, 5, 4, 3, mercy, favoritism, transfer, piety,
                healBonus, finalHealBonus, level, out);
    }

    /**
     * 빛의 사제 힐 : 대상의 체력을 1D6만큼 회복시킵니다. (마나 1 소모)
     *
     * @param stat           사용할 스탯
     * @param mercy          자비 패시브 적용 여부 (최종 회복량 1.2배)
     * @param favoritism     편애 패시브 적용 여부 (회복량 +50%)
     * @param transfer       양도 패시브 적용 여부 (회복량 +50%)
     * @param piety          신앙심 패시브 적용 여부 (회복량 +100%)
     * @param healBonus      직업 스킬 외 추가 회복량 증가 (%)
     * @param finalHealBonus 직업 스킬 외 추가 최종 회복량 증가 (%)
     * @param level          캐릭터 레벨
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result heal(int stat, boolean mercy, boolean favoritism, boolean transfer,
                              boolean piety, int healBonus, int finalHealBonus, int level,
                              PrintStream out) {
        out.println("빛의 사제-힐 사용");
        return normalHeal(stat, 1, 6, 1, mercy, favoritism, transfer, piety,
                healBonus, finalHealBonus, level, out);
    }

    /**
     * 범용 힐링 함수
     * <p>
     * 회복량 계산 공식: [(기본 회복량) x (100 + 회복량 증가)%] x (최종 회복량 증가)% x (주사위 보정)
     *
     * @param stat           사용할 스탯
     * @param dices          주사위 개수
     * @param sides          주사위 면수
     * @param mana           소모 마나
     * @param mercy          자비 패시브 적용 여부 (전투 내 공격 없을 시 최종 회복량 1.2배)
     * @param favoritism     편애 패시브 적용 여부 (회복량 +50%)
     * @param transfer       양도 패시브 적용 여부 (본인 회복 불가, 회복량 +50%)
     * @param piety          신앙심 패시브 적용 여부 (회복량 +100%)
     * @param healBonus      직업 스킬 외 추가 회복량 증가 (%)
     * @param finalHealBonus 직업 스킬 외 추가 최종 회복량 증가 (%)
     * @param level          캐릭터 레벨 (최종 회복량 기본 계수: (100 + level^2/2)%)
     * @param out            출력 스트림
     * @return 결과 객체 (healAmount 은 damageDealt 필드에 반환)
     */
    private static Result normalHeal(int stat, int dices, int sides, int mana,
                                     boolean mercy, boolean favoritism, boolean transfer, boolean piety,
                                     int healBonus, int finalHealBonus, int level, PrintStream out) {
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, mana, 0);
        }

        int baseHeal = main.Main.dice(dices, sides, out);
        int healAmount = applyHealFormula(baseHeal, verdict, mercy, favoritism, transfer, piety,
                healBonus, finalHealBonus, level, out);
        out.printf("최종 회복량 : %d\n", healAmount);
        return new Result(0, healAmount, true, mana, 0);
    }

    /**
     * 회복량 공식 적용
     * [(기본 회복량) x (100 + 회복량 증가)%] x (최종 회복량 증가)% x (주사위 보정)
     *
     * @param baseHeal       기본 회복량 (주사위 결과)
     * @param verdict        판정 결과 (stat - D20, 양수일 때 보정 적용)
     * @param mercy          자비 패시브 (최종 회복량 x1.2)
     * @param favoritism     편애 패시브 (회복량 +50%)
     * @param transfer       양도 패시브 (회복량 +50%)
     * @param piety          신앙심 패시브 (회복량 +100%)
     * @param healBonus      추가 회복량 증가 (%)
     * @param finalHealBonus 추가 최종 회복량 증가 (%)
     * @param level          캐릭터 레벨
     * @param out            출력 스트림
     * @return 공식 적용 후 최종 회복량
     */
    private static int applyHealFormula(int baseHeal, int verdict,
                                        boolean mercy, boolean favoritism, boolean transfer, boolean piety,
                                        int healBonus, int finalHealBonus, int level, PrintStream out) {
        // 회복량 증가 (%) - 패시브 + 외부 입력
        int totalHealBonus = healBonus;
        if (favoritism) {
            out.println("편애 패시브 적용: 회복량 +50%");
            totalHealBonus += 50;
        }
        if (transfer) {
            out.println("양도 패시브 적용: 회복량 +50%");
            totalHealBonus += 50;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 회복량 +100%");
            totalHealBonus += 100;
        }

        // 최종 회복량 증가 (%) - 레벨 기반 계수 + 외부 입력
        // 레벨 계수: (100 + level^2/2)%, level=0 이면 기본 100%
        int safeLevel = Math.max(0, level);
        double levelCoeff = 100.0 + (safeLevel * safeLevel) / 2.0;
        double finalHealPct = levelCoeff + finalHealBonus;
        out.printf("레벨 %d 기반 최종 회복량 계수: (100 + %d^2/2) = %.1f%%%n", safeLevel, safeLevel, levelCoeff);

        double baseFinalMultiplier = finalHealPct / 100.0;
        double finalMultiplier = mercy ? baseFinalMultiplier * 1.2 : baseFinalMultiplier;
        if (mercy) {
            out.println("자비 패시브 적용: 최종 회복량 x1.2");
        }

        // 주사위 보정: 1 + max(0, verdict) * 0.1
        double diceModifier = 1.0 + Math.max(0, verdict) * 0.1;

        // 최종 공식: [(기본 회복량) x (100 + 회복량 증가)%] x (최종 회복량 증가)% x (주사위 보정)
        double healBonusMultiplier = (100.0 + totalHealBonus) / 100.0;
        int healAmount = (int) (baseHeal * healBonusMultiplier * finalMultiplier * diceModifier);
        out.printf("회복량 계산: [(기본 %d) x (100 + %d)%%] x (최종 %.2f%%) x (주사위 보정 %.2f) = %d%n",
                baseHeal, totalHealBonus, finalHealPct, diceModifier, healAmount);
        return healAmount;
    }

    /**
     * 빛의 사제 치유의 바람 : 대상에게 2D6의 데미지를 입힙니다. (마나 2 소모, 쿨타임 2턴)
     *
     * @param stat      사용할 스탯
     * @param precision 정밀 스탯
     * @param out       출력 스트림
     * @return 결과 객체
     */
    public static Result healingWind(int stat, int precision, PrintStream out) {
        out.println("빛의 사제-치유의 바람 사용");
        return normalAttack(stat, 2, 6, 2, precision, out);
    }

    /**
     * 빛의 사제 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat      사용할 스탯
     * @param precision 정밀 스탯
     * @param out       출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int precision, PrintStream out) {
        out.println("빛의 사제-기본공격 사용");
        return normalAttack(stat, 1, 6, 0, precision, out);
    }

    /**
     * 범용 공격 함수
     *
     * @param stat      사용할 스탯
     * @param dices     주사위 개수
     * @param sides     주사위 면수
     * @param mana      소모 마나
     * @param precision 정밀 스탯
     * @param out       출력 스트림
     * @return 결과 객체
     */
    private static Result normalAttack(int stat, int dices, int sides, int mana, int precision, PrintStream out) {
        int verdict = main.Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, mana, 0);
        }
        int diceRoll = stat - verdict;

        int damage = main.Main.dice(dices, sides, out);

        int sideDamage = main.Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = main.Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, mana, 0);
    }
}
