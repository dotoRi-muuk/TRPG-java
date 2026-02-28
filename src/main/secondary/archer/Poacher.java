package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 밀렵꾼
 * <p>
 * 판정 사용 스탯 : 힘, 민첩
 */
public class Poacher {

    /**
     * 덫 깔기 : 적이 공격 시도 시 판정을 시행합니다. 판정 성공 시 1D10의 데미지를 입히며, 해당 턴에 한하여 적의 공격 데미지가 75%로 감소합니다.
     *
     * @param stat                 사용할 스탯
     * @param damageTaken          상대에게 받은 데미지
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 150%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +2)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 200%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result setTrap(int stat, int damageTaken, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, int precision, PrintStream out) {
        int staminaChange = 3;

        out.println("밀렵꾼-덫 깔기 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +2");
            effectiveStat += 2;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, staminaChange);

        int baseDamage = Main.dice(1, 10, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 1.5배");
            modifier *= 1.5f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 2배");
            modifier *= 2.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result((int) (damageTaken * 0.75), finalDamage, true, 0, staminaChange);
    }

    /**
     * 머리찍기 : 대상에게 1D8의 피해를 입힙니다.
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 150%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +2)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 200%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result headSmash(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, int precision, PrintStream out) {
        int staminaChange = 1;

        out.println("밀렵꾼-머리찍기 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +2");
            effectiveStat += 2;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(2, 12, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 1.5배");
            modifier *= 1.5f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 2배");
            modifier *= 2.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 올가미 탄 : 대상에게 1D8의 피해를 입힙니다. 다음 턴까지 적에게 행동 불가를 부여합니다.
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 150%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +2)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 200%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result snareShot(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, int precision, PrintStream out) {
        int staminaChange = 8;

        out.println("밀렵꾼-올가미 탄 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +2");
            effectiveStat += 2;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, staminaChange);

        int baseDamage = Main.dice(1, 8, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 1.5배");
            modifier *= 1.5f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 2배");
            modifier *= 2.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("배율 적용 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        out.println("적에게 행동 불가 부여 (다음 턴까지)");
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 헤드샷 : 대상에게 2D12의 피해를 입힙니다.
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 150%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +2)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 200%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result headShot(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, int precision, PrintStream out) {
        int staminaChange = 4;

        out.println("밀렵꾼-헤드샷 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +2");
            effectiveStat += 2;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, staminaChange);

        int baseDamage = Main.dice(2, 12, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 1.5배");
            modifier *= 1.5f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 2배");
            modifier *= 2.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 기본공격 : 대상에게 2D4의 데미지를 입힙니다. (산탄 패시브 적용됨)
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 (디버프 대상 150%)
     * @param survivalOfTheFittest 약육강식 패시브 (자신 체력% > 적 체력% 시 스탯 +2)
     * @param contemptForTheWeak   약자멸시 스킬 ((자신 체력% - 적 체력%) >= 10% 시 데미지 200%)
     * @param reload               장전 기술 (다음 턴 데미지 2D4->2D6, 2D8->2D12)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean reload, int precision, PrintStream out) {
        int staminaChange = 0;

        out.println("밀렵꾼-기본공격 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +2");
            effectiveStat += 2;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int dices = 2, sides = 4; // 기본값 2D4

        int buckshot = Main.dice(1, 20, out);
        boolean buckshotApplied = false;

        if (buckshot - stat >= 10) {
            out.printf("산탄 패시브 적용: 데미지 주사위 2D8로 변경 (스탯 %d - 주사위 %d >= 10)\n", stat, buckshot);
            sides = 8;
            buckshotApplied = true;
        }
        if (reload) {
            staminaChange += 2;
            if (buckshotApplied) {
                out.println("장전 기술 적용: 데미지 주사위 2D12로 변경");
                sides = 12;
            } else {
                out.println("장전 기술 적용: 데미지 주사위 2D6로 변경");
                sides = 6;
            }
        }

        int baseDamage = Main.dice(dices, sides, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 1.5배");
            modifier *= 1.5f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 2배");
            modifier *= 2.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, staminaChange);

    }
}
