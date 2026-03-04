package main.secondary.priest;

import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.Map;

/**
 * 번개의 사제
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 */
public class LightningPriest {
    /**
     * 번개의 사제 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat           사용할 스탯
     * @param monopoly       독점 패시브 적용 여부 (아군 데미지 n% 증가 효과를 본인 데미지 n+100% 증가 효과로 변경)
     * @param monopolyAmount 독점 패시브 적용 시 증가하는 데미지 비율(아군 데미지 n% 증가 효과의 n 값)
     * @param piety          신앙심 패시브 적용 여부 (아군 데미지 1.5배)
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean monopoly, double monopolyAmount, boolean piety, int precision, PrintStream out) {
        out.println("번개의 사제-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0, Map.of());
        }

        int baseDamage = Main.dice(1, 6, out);
        double damageMultiplier = 1.0;
        if (monopoly) {
            out.printf("독점 패시브 적용: 데미지 %.1f%%로 증가%n", monopolyAmount + 100);
            damageMultiplier *= (monopolyAmount + 100) / 100.0;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 아군 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 0, 0, Map.of());
    }

    /**
     * 스파크 (스킬) : 대상에게 2D4의 피해를 입힙니다. (마나 1 소모)
     *
     * @param stat           사용할 스탯
     * @param monopoly       독점 패시브 적용 여부 (아군 데미지 n% 증가 효과를 본인 데미지 n+100% 증가 효과로 변경)
     * @param monopolyAmount 독점 패시브 적용 시 증가하는 데미지 비율(아군 데미지 n% 증가 효과의 n 값)
     * @param piety          신앙심 패시브 적용 여부 (아군 데미지 1.5배)
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result spark(int stat, boolean monopoly, double monopolyAmount, boolean piety, int precision, PrintStream out) {
        out.println("스파크 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 1, 0, Map.of());
        }

        int baseDamage = Main.dice(2, 4, out);
        double damageMultiplier = 1.0;
        if (monopoly) {
            out.printf("독점 패시브 적용: 데미지 %.1f%%로 증가%n", monopolyAmount + 100);
            damageMultiplier *= (monopolyAmount + 100) / 100.0;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 아군 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 1, 0, Map.of());
    }

    /**
     *
     * 체인 라이트닝 (스킬) : 적에게는 2D6의 피해를 입히고, 아군에게는 2D4의 보호막을 부여합니다. (마나 3 소모, 쿨타임 3턴)
     *
     * @param stat           사용할 스탯
     * @param monopoly       독점 패시브 적용 여부 (아군 데미지 n% 증가 효과를 본인 데미지 n+100% 증가 효과로 변경)
     * @param monopolyAmount 독점 패시브 적용 시 증가하는 데미지 비율(아군 데미지 n% 증가 효과의 n 값)
     * @param piety          신앙심 패시브 적용 여부 (아군 데미지 1.5배)
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result chainLightning(int stat, boolean monopoly, double monopolyAmount, boolean piety, int precision, PrintStream out) {
        out.println("체인 라이트닝 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 3, 0, Map.of());
        }

        // Enemy damage
        int baseDamage = Main.dice(2, 6, out);
        double damageMultiplier = 1.0;
        if (monopoly) {
            out.printf("독점 패시브 적용: 데미지 %.1f%%로 증가%n", monopolyAmount + 100);
            damageMultiplier *= (monopolyAmount + 100) / 100.0;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 아군 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        out.println();
        out.println("아군 보호막 부여: 2D4");
        int shieldAmount = Main.dice(2, 4, out);
        out.printf("부여된 보호막 양 : %d\n", shieldAmount);

        return new Result(0, damage, true, 3, 0, Map.of());
    }

    /**
     * 일렉트릭 필드 (스킬) : (최대 6턴 영창) (광역) 아군의 데미지를 (영창 턴 수) * 50%만큼 증가시키고, 적에게 (4*(max((영창 턴 수) - 2, 0)))D4의 피해를 입힙니다. (마나 8 소모, 쿨타임 7턴)
     *
     * @param stat           사용할 스탯
     * @param chantTurns     영창 턴 수
     * @param monopoly       독점 패시브 적용 여부 (아군 데미지 n% 증가 효과를 본인 데미지 n+100% 증가 효과로 변경)
     * @param monopolyAmount 독점 패시브 적용 시 증가하는 데미지 비율(아군 데미지 n% 증가 효과의 n 값)
     * @param piety          신앙심 패시브 적용 여부 (아군 데미지 1.5배)
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result electricField(int stat, int chantTurns, boolean monopoly, double monopolyAmount, boolean piety, int precision, PrintStream out) {
        out.println("일렉트릭 필드 사용 (영창 " + chantTurns + "턴)");

        // Buff output
        out.printf("아군 데미지 증가: %d%%\n", chantTurns * 50);

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 8, 0, Map.of());
        }

        // Damage calculation: (4 * max(chantTurns - 2, 0))D4
        int diceCount = 4 * Math.max(chantTurns - 2, 0);
        int baseDamage = 0;
        if (diceCount > 0) {
            baseDamage = Main.dice(diceCount, 4, out);
        } else {
            out.println("피해 주사위 없음 (영창 턴 부족)");
        }

        double damageMultiplier = 1.0;
        if (monopoly) {
            out.printf("독점 패시브 적용: 데미지 %.1f%%로 증가%n", monopolyAmount + 100);
            damageMultiplier *= (monopolyAmount + 100) / 100.0;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 아군 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        return new Result(0, damage, true, 8, 0, Map.of());
    }

    /**
     * 스트라이크 (스킬) : 대상에게 2D8의 피해를 입힙니다. (마나 2 소모, 쿨타임 5턴)
     *
     * @param stat           사용할 스탯
     * @param monopoly       독점 패시브 적용 여부 (아군 데미지 n% 증가 효과를 본인 데미지 n+100% 증가 효과로 변경)
     * @param monopolyAmount 독점 패시브 적용 시 증가하는 데미지 비율(아군 데미지 n% 증가 효과의 n 값)
     * @param piety          신앙심 패시브 적용 여부 (아군 데미지 1.5배)
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result strike(int stat, boolean monopoly, double monopolyAmount, boolean piety, int precision, PrintStream out) {
        out.println("스트라이크 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 2, 0, Map.of());
        }

        int baseDamage = Main.dice(2, 8, out);
        double damageMultiplier = 1.0;
        if (monopoly) {
            out.printf("독점 패시브 적용: 데미지 %.1f%%로 증가%n", monopolyAmount + 100);
            damageMultiplier *= (monopolyAmount + 100) / 100.0;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 아군 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 2, 0, Map.of());
    }

    /**
     * 신뇌격 (스킬) : 대상에게 3D20의 피해를 입힙니다. (영창 2턴, 마나 7 소모)
     *
     * @param stat           사용할 스탯
     * @param monopoly       독점 패시브 적용 여부 (아군 데미지 n% 증가 효과를 본인 데미지 n+100% 증가 효과로 변경)
     * @param monopolyAmount 독점 패시브 적용 시 증가하는 데미지 비율(아군 데미지 n% 증가 효과의 n 값)
     * @param piety          신앙심 패시브 적용 여부 (아군 데미지 1.5배)
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result divineThunderStrike(int stat, boolean monopoly, double monopolyAmount, boolean piety, int precision, PrintStream out) {
        out.println("신뇌격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 7, 0, Map.of());
        }

        int baseDamage = Main.dice(3, 20, out);
        double damageMultiplier = 1.0;
        if (monopoly) {
            out.printf("독점 패시브 적용: 데미지 %.1f%%로 증가%n", monopolyAmount + 100);
            damageMultiplier *= (monopolyAmount + 100) / 100.0;
        }
        if (piety) {
            out.println("신앙심 패시브 적용: 아군 데미지 1.5배");
            damageMultiplier *= 1.5;
        }
        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        return new Result(0, damage, true, 7, 0, Map.of());
    }
}
