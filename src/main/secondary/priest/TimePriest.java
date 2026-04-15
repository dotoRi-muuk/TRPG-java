package main.secondary.priest;

import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.Map;

/**
 * 시간의 사제
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * 직업 외 최종 데미지 기본값: 100% (1배)
 * <p>
 * [패시브]
 * - 축복: 매 턴 행동을 2회 할 수 있습니다. (영창 중 / 휴식 중에는 사용할 수 없습니다.) 공격이 없는 스킬도 판정을 진행합니다.
 * - 신성한 육체: 자신의 민첩, 신속 스탯이 +3 증가합니다.
 * - 냉정: 행동 불가 상태의 적에게 받는 데미지 100% 증가를 부여합니다.
 *         아군이 가하는 피해가 50 × (해당 아군의 행동 횟수-1)% 만큼 증가합니다. (자신 제외)
 * - 침착: 아군이 행동 불가 상태일 때 받는 피해가 절반으로 감소합니다.
 */
public class TimePriest {

    // ──────────────────────────────────────────────
    // 데미지 계산 헬퍼
    // ──────────────────────────────────────────────

    /**
     * 데미지 공식 적용 헬퍼
     * [(기본 데미지) x (100 + 데미지 증가)%] x [(레벨 계수)% x (직업 외 최종 데미지)%] x (주사위 보정)
     * <p>
     * 레벨 계수 = (100 + level^2)%
     * 직업 외 최종 데미지는 곱연산으로 적용 (기본값 100 = 1배).
     *
     * @param baseDamage       주사위 기본 데미지
     * @param verdict          판정 결과 (stat - D20)
     * @param totalDamageBonus 데미지 증가 합산 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param level            캐릭터 레벨
     * @param out              출력 스트림
     * @return 공식 적용 최종 데미지
     */
    private static int applyDamageFormula(int baseDamage, int verdict,
                                          int totalDamageBonus, int finalDamageBonus,
                                          int level, PrintStream out) {
        int safeLevel = Math.max(0, level);
        double levelCoeff = 100.0 + (long) safeLevel * safeLevel;
        double finalMultiplier = (levelCoeff / 100.0) * (finalDamageBonus / 100.0);
        out.printf("레벨 %d 기반 최종 데미지 계수: (100 + %d^2) = %.1f%%%n", safeLevel, safeLevel, levelCoeff);
        out.printf("직업 외 최종 데미지: %d%% (×%.4f)%n", finalDamageBonus, finalDamageBonus / 100.0);

        double diceModifier = 1.0 + Math.max(0, verdict) * 0.1;

        int damage = (int) (baseDamage * ((100.0 + totalDamageBonus) / 100.0) * finalMultiplier * diceModifier);
        out.printf("데미지 계산: [(기본 %d) x (100 + %d)%%] x (최종 %.4f배) x (주사위 보정 %.2f) = %d%n",
                baseDamage, totalDamageBonus, finalMultiplier, diceModifier, damage);
        return damage;
    }

    // ──────────────────────────────────────────────
    // 데미지 스킬
    // ──────────────────────────────────────────────

    /**
     * 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat             사용할 스탯
     * @param impatience       성급함 스킬 적용 여부 (행동 횟수 n+1일 때 자신 피해 +100n%)
     * @param turns            행동 횟수 (성급함 스킬용)
     * @param piety            신앙심 스킬 발동 여부 (아군 데미지 +75%)
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean impatience, int turns, boolean piety,
                               int damageBonus, int finalDamageBonus, int level,
                               int precision, PrintStream out) {
        out.println("시간의 사제 - 기본공격 사용");
        return damageAttack(stat, 1, 6, 0, impatience, turns, piety,
                damageBonus, finalDamageBonus, level, precision, out);
    }

    /**
     * 부식 (자유 영창) : 영창하는 동안 적 1명에게 매 턴 영창 1회 당 (영창 시간)D12의 피해를 입힙니다.
     * (마나 8 소모, 쿨타임 10턴)
     *
     * @param stat             사용할 스탯
     * @param chantTurns       현재 영창 턴 수 (주사위 개수)
     * @param impatience       성급함 스킬 적용 여부
     * @param turns            행동 횟수 (성급함 스킬용)
     * @param piety            신앙심 스킬 발동 여부
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result corrosion(int stat, int chantTurns, boolean impatience, int turns, boolean piety,
                                   int damageBonus, int finalDamageBonus, int level,
                                   int precision, PrintStream out) {
        int safeTurns = Math.max(1, chantTurns);
        out.printf("시간의 사제 - 부식 사용 (영창 %d턴: %dD12)%n", safeTurns, safeTurns);
        return damageAttack(stat, safeTurns, 12, 8, impatience, turns, piety,
                damageBonus, finalDamageBonus, level, precision, out);
    }

    // ──────────────────────────────────────────────
    // 비(非)데미지 스킬
    // ──────────────────────────────────────────────

    /**
     * 가속 : 아군 1명의 다음 행동에 1회 추가 행동을 부여합니다. (마나 8 소모, 쿨타임 3턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result acceleration(int stat, PrintStream out) {
        out.println("시간의 사제 - 가속 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 8, 0, Map.of());
        }
        out.println("아군 1명의 다음 행동에 1회 추가 행동 부여");
        return new Result(0, 0, true, 8, 0, Map.of());
    }

    /**
     * 정지 : 1명의 대상에게 다음 턴 동안 행동 불가를 부여합니다. (마나 7 소모, 쿨타임 5턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result stop(int stat, PrintStream out) {
        out.println("시간의 사제 - 정지 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 7, 0, Map.of());
        }
        out.println("대상에게 다음 턴 동안 행동 불가 부여");
        return new Result(0, 0, true, 7, 0, Map.of());
    }

    /**
     * 유예 : 1명의 대상에게 다음 턴 동안 공격 불가를 부여하고,
     * 적이 다음에 피격 당하는 스킬에 대해 받는 데미지를 75% 증가시킵니다. (마나 9 소모, 쿨타임 4턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result suspension(int stat, PrintStream out) {
        out.println("시간의 사제 - 유예 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 9, 0, Map.of());
        }
        out.println("대상에게 다음 턴 동안 공격 불가 부여");
        out.println("다음 피격 스킬에 대해 받는 데미지 +75%");
        return new Result(0, 0, true, 9, 0, Map.of());
    }

    /**
     * 감속 : 1명의 대상에게 다음 턴 동안 수비 불가를 부여하고,
     * 적이 다음에 피격 당하는 스킬에 대해 받는 최종 데미지를 2배로 증가시킵니다. (마나 13 소모, 쿨타임 5턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result deceleration(int stat, PrintStream out) {
        out.println("시간의 사제 - 감속 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 13, 0, Map.of());
        }
        out.println("대상에게 다음 턴 동안 수비 불가 부여");
        out.println("다음 피격 스킬에 대해 받는 최종 데미지 ×2");
        return new Result(0, 0, true, 13, 0, Map.of());
    }

    /**
     * 시간의 틈새 (자유 영창, 지혜 판정 없음) :
     * 영창하는 동안 자신과 적 1명에게 행동 불가를 부여합니다. (매 턴 마나 7 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result timeGap(PrintStream out) {
        out.println("시간의 사제 - 시간의 틈새 사용 (지혜 판정 없음)");
        out.println("자신과 적 1명에게 행동 불가 부여 (영창 지속 중)");
        return new Result(0, 0, true, 7, 0, Map.of());
    }

    /**
     * 강탈 : 매 턴 50% 확률로 자신 제외 모든 아군에게 행동 불가 부여.
     * 영창 2개를 동시에 진행 가능, [축복] 영창 중 사용 가능.
     * 전투 내 영구 지속, 취소 불가. (마나 10 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result seizure(PrintStream out) {
        out.println("시간의 사제 - 강탈 사용");
        out.println("매 턴 50% 확률로 자신 제외 모든 아군 행동 불가 부여");
        out.println("영창 2개 동시 진행 가능 / 전투 내 영구 지속, 취소 불가");
        return new Result(0, 0, true, 10, 0, Map.of());
    }

    /**
     * 자기중심 : 자신 외 모든 아군의 스탯 -3.
     * 자신이 아군에게 줄 수 있는 버프 스킬의 대상을 자신으로 고정.
     * 자신의 지능, 지혜 스탯 +5.
     * 전투 내 영구 지속, 취소 불가. (마나 13 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result selfCentered(PrintStream out) {
        out.println("시간의 사제 - 자기중심 사용");
        out.println("자신 외 모든 아군 스탯 -3");
        out.println("버프 스킬 대상 자신으로 고정 (자신에게 사용할 수 없던 스킬 포함)");
        out.println("자신의 지능, 지혜 스탯 +5");
        out.println("전투 내 영구 지속, 취소 불가");
        return new Result(0, 0, true, 13, 0, Map.of());
    }

    /**
     * 성급함 : [냉정] 효과를 변경하여, 행동 횟수가 n+1일 때 자신이 가하는 피해가 100n% 증가.
     * 자신을 제외한 아군이 매턴 번갈아가며 한 명씩만 행동 가능.
     * 전투 내 영구 지속, 취소 불가. (마나 10 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result impatienceSkill(PrintStream out) {
        out.println("시간의 사제 - 성급함 사용");
        out.println("[냉정] 효과 변경: 행동 횟수 n+1일 때 자신 피해 +100n%");
        out.println("자신 제외 아군이 매턴 번갈아가며 한 명씩만 행동");
        out.println("전투 내 영구 지속, 취소 불가");
        return new Result(0, 0, true, 10, 0, Map.of());
    }

    /**
     * 기복 : [침착] 효과를 제거합니다. 자신은 행동 불가 효과를 무시합니다.
     * 행동 불가 상태인 아군이 받는 최종 데미지가 2배로 증가합니다.
     * 자신이 행동 불가를 부여하는 공격을 받을 때 받는 데미지가 50%로 감소합니다.
     * 전투 내 영구 지속, 취소 불가. (마나 15 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result fluctuation(PrintStream out) {
        out.println("시간의 사제 - 기복 사용");
        out.println("[침착] 효과 제거");
        out.println("자신은 행동 불가 효과 무시");
        out.println("행동 불가 아군이 받는 최종 데미지 ×2");
        out.println("행동 불가 부여 공격 수신 시 받는 데미지 50%로 감소");
        out.println("전투 내 영구 지속, 취소 불가");
        return new Result(0, 0, true, 15, 0, Map.of());
    }

    /**
     * 신앙심 : 다음 턴 모든 아군의 행동 횟수를 1회 추가합니다.
     * 모든 적에게 행동 불가를 부여하고, 모든 아군의 데미지가 75% 증가합니다.
     * (마나 10 소모, 쿨타임 9턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result pietySkill(int stat, PrintStream out) {
        out.println("시간의 사제 - 신앙심 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 10, 0, Map.of());
        }
        out.println("다음 턴 모든 아군 행동 횟수 +1");
        out.println("모든 적에게 행동 불가 부여");
        out.println("모든 아군 데미지 +75%");
        return new Result(0, 0, true, 10, 0, Map.of());
    }

    // ──────────────────────────────────────────────
    // 내부 헬퍼
    // ──────────────────────────────────────────────

    /**
     * 범용 데미지 공격 메소드
     * <p>
     * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param stat             사용할 스탯
     * @param dices            주사위 개수
     * @param sides            주사위 면수
     * @param mana             소모 마나
     * @param impatience       성급함 스킬 적용 여부 (행동 횟수 n+1일 때 자신 피해 +100n%)
     * @param turns            행동 횟수 (성급함 스킬용)
     * @param piety            신앙심 스킬 발동 여부 (아군 데미지 +75%)
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    private static Result damageAttack(int stat, int dices, int sides, int mana,
                                       boolean impatience, int turns, boolean piety,
                                       int damageBonus, int finalDamageBonus, int level,
                                       int precision, PrintStream out) {
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, mana, 0, Map.of());
        }

        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // 총 데미지 증가 (합산, additive)
        int totalDamageBonus = damageBonus;

        if (impatience) {
            int impatienceBonus = (turns - 1) * 100;
            out.printf("성급함 적용: 행동 횟수 %d+1 → 데미지 +%d%%%n", turns - 1, impatienceBonus);
            totalDamageBonus += impatienceBonus;
        }
        if (piety) {
            out.println("신앙심 발동: 아군 데미지 +75%");
            totalDamageBonus += 75;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus, finalDamageBonus, level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);
        return new Result(0, damage, true, mana, 0, Map.of());
    }
}
