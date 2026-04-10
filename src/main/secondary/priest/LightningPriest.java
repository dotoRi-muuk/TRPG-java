package main.secondary.priest;

import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.Map;

/**
 * 번개의 사제
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지 증가)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * <p>
 * [패시브]
 * - 축복: 자신이 공격 성공 시 아군이 1회 데미지 75% 증가
 * - 신성한 육체: 지능, 지혜 스탯 +3
 * - 신뢰: 아군 1명의 모든 스탯 +2
 * - 믿음: 아군 판정 실패 시 본인 행동불가 대신 지능 판정, 성공 시 아군 재판정 기회 지급
 */
public class LightningPriest {

    /**
     * 데미지 공식 적용 헬퍼
     * [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지 증가)% x (주사위 보정)
     * <p>
     * 레벨 계수 = (100 + level^2)%
     *
     * @param baseDamage       주사위 기본 데미지
     * @param verdict          판정 결과 (stat - D20)
     * @param totalDamageBonus 데미지 증가 합산 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param out              출력 스트림
     * @return 공식 적용 최종 데미지
     */
    private static int applyDamageFormula(int baseDamage, int verdict,
                                          int totalDamageBonus, int finalDamageBonus,
                                          int level, PrintStream out) {
        int safeLevel = Math.max(0, level);
        double levelCoeff = 100.0 + (double) safeLevel * safeLevel;
        double finalPct = levelCoeff + finalDamageBonus;
        double finalMultiplier = finalPct / 100.0;
        out.printf("레벨 %d 기반 최종 데미지 계수: (100 + %d^2) = %.1f%%%n", safeLevel, safeLevel, levelCoeff);

        double diceModifier = 1.0 + Math.max(0, verdict) * 0.1;

        int damage = (int) (baseDamage * ((100.0 + totalDamageBonus) / 100.0) * finalMultiplier * diceModifier);
        out.printf("데미지 계산: [(기본 %d) x (100 + %d)%%] x (최종 %.2f%%) x (주사위 보정 %.2f) = %d%n",
                baseDamage, totalDamageBonus, finalPct, diceModifier, damage);
        return damage;
    }

    /**
     * 번개의 사제 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat             사용할 스탯
     * @param monopoly         독점 패시브 적용 여부 (아군 데미지 n% 증가를 본인 최종 데미지 n+100% 증가로 변경)
     * @param monopolyAmount   독점 적용 시 n 값 (%)
     * @param piety            신앙심 스킬 적용 여부 (아군 데미지 50% 증가)
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean monopoly, int monopolyAmount, boolean piety,
                               int damageBonus, int finalDamageBonus, int level, int precision,
                               PrintStream out) {
        out.println("번개의 사제-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0, Map.of());
        }

        int baseDamage = Main.dice(1, 6, out);

        int totalDamageBonus = damageBonus;
        if (monopoly) {
            out.printf("독점 패시브 적용: 최종 데미지 %d%%로 증가%n", monopolyAmount + 100);
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 아군 데미지 50% 증가");
            totalDamageBonus += 50;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus,
                finalDamageBonus + (monopoly ? monopolyAmount : 0), level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 0, 0, Map.of());
    }

    /**
     * 스파크 (스킬) : 대상에게 4D6의 피해를 입힙니다. (마나 3 소모)
     *
     * @param stat             사용할 스탯
     * @param monopoly         독점 패시브 적용 여부
     * @param monopolyAmount   독점 적용 시 n 값 (%)
     * @param piety            신앙심 스킬 적용 여부
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result spark(int stat, boolean monopoly, int monopolyAmount, boolean piety,
                               int damageBonus, int finalDamageBonus, int level, int precision,
                               PrintStream out) {
        out.println("스파크 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 3, 0, Map.of());
        }

        int baseDamage = Main.dice(4, 6, out);

        int totalDamageBonus = damageBonus;
        if (monopoly) {
            out.printf("독점 패시브 적용: 최종 데미지 %d%%로 증가%n", monopolyAmount + 100);
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 아군 데미지 50% 증가");
            totalDamageBonus += 50;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus,
                finalDamageBonus + (monopoly ? monopolyAmount : 0), level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 3, 0, Map.of());
    }

    /**
     * 체인 라이트닝 (스킬) : 최대 3명 대상. 적에게 4D8 피해, 아군에게 2D4 보호막. (마나 5 소모, 쿨타임 3턴)
     *
     * @param stat             사용할 스탯
     * @param monopoly         독점 패시브 적용 여부
     * @param monopolyAmount   독점 적용 시 n 값 (%)
     * @param piety            신앙심 스킬 적용 여부
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체 (적 피해 반환)
     */
    public static Result chainLightning(int stat, boolean monopoly, int monopolyAmount, boolean piety,
                                        int damageBonus, int finalDamageBonus, int level, int precision,
                                        PrintStream out) {
        out.println("체인 라이트닝 사용 (최대 3명 대상)");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 5, 0, Map.of());
        }

        // 적 피해: 4D8
        int baseDamage = Main.dice(4, 8, out);

        int totalDamageBonus = damageBonus;
        if (monopoly) {
            out.printf("독점 패시브 적용: 최종 데미지 %d%%로 증가%n", monopolyAmount + 100);
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 아군 데미지 50% 증가");
            totalDamageBonus += 50;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus,
                finalDamageBonus + (monopoly ? monopolyAmount : 0), level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("적 최종 데미지 : %d\n", damage);

        out.println();
        out.println("아군 보호막 부여: 2D4");
        int shieldAmount = Main.dice(2, 4, out);
        out.printf("부여된 보호막 양 : %d\n", shieldAmount);

        return new Result(0, damage, true, 5, 0, Map.of());
    }

    /**
     * 체인 라이트닝 보호막 계산 (2D4)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체 (보호막 량 반환)
     */
    public static Result chainLightningShield(int stat, PrintStream out) {
        out.println("체인 라이트닝 - 아군 보호막 부여");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 5, 0, Map.of());
        }
        int shieldAmount = Main.dice(2, 4, out);
        out.printf("부여된 보호막 양 : %d\n", shieldAmount);
        return new Result(0, shieldAmount, true, 5, 0, Map.of());
    }

    /**
     * 일렉트릭 필드 (스킬) : 영창 진행. 매턴 (영창 턴 수)×D12 피해.
     * 영창 종료 시 (영창 턴 수)×30% 데미지 증가 (최대 300%).
     * 지혜 판정 불가. (마나 8 소모, 쿨타임 7턴)
     *
     * @param stat             사용할 스탯
     * @param chantTurns       현재 영창 턴 수
     * @param monopoly         독점 패시브 적용 여부
     * @param monopolyAmount   독점 적용 시 n 값 (%)
     * @param piety            신앙심 스킬 적용 여부
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result electricField(int stat, int chantTurns, boolean monopoly, int monopolyAmount,
                                       boolean piety, int damageBonus, int finalDamageBonus, int level,
                                       int precision, PrintStream out) {
        int safeTurns = Math.max(1, chantTurns);
        out.println("일렉트릭 필드 사용 (영창 " + safeTurns + "턴)");
        out.printf("이번 턴 피해: %d×D12%n", safeTurns);
        int endBonus = Math.min(safeTurns * 30, 300);
        out.printf("영창 종료 시 데미지 증가 (최대 300%%): %d%%%n", endBonus);

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 8, 0, Map.of());
        }

        // 이번 턴 피해: n×D12
        int baseDamage = Main.dice(safeTurns, 12, out);

        int totalDamageBonus = damageBonus + endBonus;
        if (monopoly) {
            out.printf("독점 패시브 적용: 최종 데미지 %d%%로 증가%n", monopolyAmount + 100);
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 아군 데미지 50% 증가");
            totalDamageBonus += 50;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus,
                finalDamageBonus + (monopoly ? monopolyAmount : 0), level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        return new Result(0, damage, true, 8, 0, Map.of());
    }

    /**
     * 스트라이크 (스킬) : 대상에게 3D20의 피해를 입힙니다. (마나 5 소모, 쿨타임 5턴)
     *
     * @param stat             사용할 스탯
     * @param monopoly         독점 패시브 적용 여부
     * @param monopolyAmount   독점 적용 시 n 값 (%)
     * @param piety            신앙심 스킬 적용 여부
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result strike(int stat, boolean monopoly, int monopolyAmount, boolean piety,
                                int damageBonus, int finalDamageBonus, int level, int precision,
                                PrintStream out) {
        out.println("스트라이크 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 5, 0, Map.of());
        }

        int baseDamage = Main.dice(3, 20, out);

        int totalDamageBonus = damageBonus;
        if (monopoly) {
            out.printf("독점 패시브 적용: 최종 데미지 %d%%로 증가%n", monopolyAmount + 100);
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 아군 데미지 50% 증가");
            totalDamageBonus += 50;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus,
                finalDamageBonus + (monopoly ? monopolyAmount : 0), level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 5, 0, Map.of());
    }

    /**
     * 일레이스터 (스킬) : 1회 아군 1명 공격 데미지 100% 증가. (마나 5 소모, 쿨타임 5턴)
     * 버프 스킬로 직접 데미지 없음.
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result elraister(int stat, PrintStream out) {
        out.println("일레이스터 사용");
        out.println("아군 1명의 다음 공격 데미지 100% 증가 적용.");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 5, 0, Map.of());
        }
        out.println("버프 적용 성공!");
        return new Result(0, 0, true, 5, 0, Map.of());
    }

    /**
     * 신뇌격 (스킬) : 대상에게 4D20의 피해를 입힙니다. (영창 2턴, 마나 4 소모)
     *
     * @param stat             사용할 스탯
     * @param monopoly         독점 패시브 적용 여부
     * @param monopolyAmount   독점 적용 시 n 값 (%)
     * @param piety            신앙심 스킬 적용 여부
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%)
     * @param level            캐릭터 레벨
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result divineThunderStrike(int stat, boolean monopoly, int monopolyAmount, boolean piety,
                                             int damageBonus, int finalDamageBonus, int level, int precision,
                                             PrintStream out) {
        out.println("신뇌격 사용 (영창 2턴)");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 4, 0, Map.of());
        }

        int baseDamage = Main.dice(4, 20, out);

        int totalDamageBonus = damageBonus;
        if (monopoly) {
            out.printf("독점 패시브 적용: 최종 데미지 %d%%로 증가%n", monopolyAmount + 100);
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 아군 데미지 50% 증가");
            totalDamageBonus += 50;
        }

        int damage = applyDamageFormula(baseDamage, verdict, totalDamageBonus,
                finalDamageBonus + (monopoly ? monopolyAmount : 0), level, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 4, 0, Map.of());
    }
}
