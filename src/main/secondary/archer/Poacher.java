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
     * 덫 깔기 : 적이 공격 시도 시 판정을 시행합니다. 판정 성공 시 3D10의 데미지를 입힙니다.
     *
     * @param stat                 사용할 스탯
     * @param damageTaken          상대에게 받은 데미지
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 200%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +3)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 300%)
     * @param overwhelm            압도 스킬 적용 여부 (데미지 500%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result setTrap(int stat, int damageTaken, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean overwhelm, int precision, PrintStream out) {
        int staminaChange = 3;

        out.println("밀렵꾼-덫 깔기 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +3");
            effectiveStat += 3;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, staminaChange);
        int diceRoll = effectiveStat - verdict;

        int baseDamage = Main.dice(3, 10, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 2배");
            modifier *= 2.0f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 3배");
            modifier *= 3.0f;
        }
        if (overwhelm) {
            out.println("압도 스킬 적용: 데미지 5배");
            modifier *= 5.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        out.println("Defense Disabled");
        return new Result(damageTaken, finalDamage, true, 0, staminaChange);
    }

    /**
     * 머리찍기 : 대상에게 3D8의 피해를 입힙니다.
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 200%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +3)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 300%)
     * @param overwhelm            압도 스킬 적용 여부 (데미지 500%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result headSmash(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean overwhelm, int precision, PrintStream out) {
        int staminaChange = 1;

        out.println("밀렵꾼-머리찍기 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +3");
            effectiveStat += 3;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = effectiveStat - verdict;

        int baseDamage = Main.dice(3, 8, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 2배");
            modifier *= 2.0f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 3배");
            modifier *= 3.0f;
        }
        if (overwhelm) {
            out.println("압도 스킬 적용: 데미지 5배");
            modifier *= 5.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 올가미 탄 : 대상에게 3D8의 피해를 입힙니다. 다음 턴까지 적에게 행동 불가를 부여합니다.
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 200%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +3)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 300%)
     * @param overwhelm            압도 스킬 적용 여부 (데미지 500%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result snareShot(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean overwhelm, int precision, PrintStream out) {
        int staminaChange = 8;

        out.println("밀렵꾼-올가미 탄 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +3");
            effectiveStat += 3;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, staminaChange);
        int diceRoll = effectiveStat - verdict;

        int baseDamage = Main.dice(3, 8, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 2배");
            modifier *= 2.0f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 3배");
            modifier *= 3.0f;
        }
        if (overwhelm) {
            out.println("압도 스킬 적용: 데미지 5배");
            modifier *= 5.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("배율 적용 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        out.println("적에게 행동 불가 부여 (다음 턴까지)");
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 헤드샷 : 대상에게 5D12의 피해를 입힙니다.
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 200%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +3)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 300%)
     * @param overwhelm            압도 스킬 적용 여부 (데미지 500%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result headShot(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean overwhelm, int precision, PrintStream out) {
        int staminaChange = 4;

        out.println("밀렵꾼-헤드샷 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +3");
            effectiveStat += 3;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, staminaChange);
        int diceRoll = effectiveStat - verdict;

        int baseDamage = Main.dice(5, 12, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 2배");
            modifier *= 2.0f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 3배");
            modifier *= 3.0f;
        }
        if (overwhelm) {
            out.println("압도 스킬 적용: 데미지 5배");
            modifier *= 5.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 기본공격 : 대상에게 4D4의 데미지를 입힙니다. (산탄 패시브 적용됨)
     *
     * @param stat                 사용할 스탯
     * @param hunting              사냥 패시브 (디버프 대상 200%)
     * @param survivalOfTheFittest 약육강식 패시브 (자신 체력% > 적 체력% 시 스탯 +3)
     * @param contemptForTheWeak   약자멸시 스킬 ((자신 체력% - 적 체력%) >= 10% 시 데미지 300%)
     * @param overwhelm            압도 스킬 (데미지 500%)
     * @param reload               장전 기술 (다음 턴 데미지 4D4->4D6, 4D8->4D12)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean overwhelm, boolean reload, int precision, PrintStream out) {
        int staminaChange = 0;

        out.println("밀렵꾼-기본공격 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +3");
            effectiveStat += 3;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = effectiveStat - verdict;

        int dices = 4, sides = 4; // 기본값 4D4

        int buckshot = Main.dice(1, 20, out);
        boolean buckshotApplied = false;

        if (stat - buckshot >= 10) {
            out.printf("산탄 패시브 적용: 데미지 주사위 4D8로 변경 (스탯 %d - 주사위 %d >= 10)\n", stat, buckshot);
            sides = 8;
            buckshotApplied = true;
        }
        if (reload) {
            staminaChange += 3;
            if (buckshotApplied) {
                out.println("장전 기술 적용: 데미지 주사위 4D12로 변경");
                sides = 12;
            } else {
                out.println("장전 기술 적용: 데미지 주사위 4D6로 변경");
                sides = 6;
            }
        }

        int baseDamage = Main.dice(dices, sides, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 2배");
            modifier *= 2.0f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 3배");
            modifier *= 3.0f;
        }
        if (overwhelm) {
            out.println("압도 스킬 적용: 데미지 5배");
            modifier *= 5.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(0, finalDamage, true, 0, staminaChange);
    }

    /**
     * 사냥터 : 2턴 동안 덫이 확정적으로 적중합니다.
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result huntingGround(PrintStream out) {
        out.println("밀렵꾼-사냥터 사용");
        out.println("Traps hit reliably for 2 turns");
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 지뢰 : 적의 공격에 반응하여 5D12의 데미지를 입힙니다.
     *
     * @param stat                 사용할 스탯
     * @param damageTaken          상대에게 받은 데미지
     * @param hunting              사냥 패시브 적용 여부 (디버프 대상 데미지 200%)
     * @param survivalOfTheFittest 약육강식 패시브 적용 여부 (자신 체력% > 적 체력% 시 스탯 +3)
     * @param contemptForTheWeak   약자멸시 스킬 적용 여부 ((자신 체력% - 적 체력%) >= 10% 시 데미지 300%)
     * @param overwhelm            압도 스킬 적용 여부 (데미지 500%)
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result landmine(int stat, int damageTaken, boolean hunting, boolean survivalOfTheFittest, boolean contemptForTheWeak, boolean overwhelm, int precision, PrintStream out) {
        out.println("밀렵꾼-지뢰 사용");

        int effectiveStat = stat;

        if (survivalOfTheFittest) {
            out.println("약육강식 패시브 적용: 모든 스탯 +3");
            effectiveStat += 3;
        }

        int verdict = Main.verdict(effectiveStat, out);

        if (verdict <= 0) return new Result(damageTaken, 0, false, 6, 0);
        int diceRoll = effectiveStat - verdict;

        int baseDamage = Main.dice(5, 12, out);
        float modifier = 1.0f;
        out.printf("기본 데미지 : %d\n", baseDamage);

        if (hunting) {
            out.println("사냥 패시브 적용: 디버프 대상 데미지 2배");
            modifier *= 2.0f;
        }
        if (contemptForTheWeak) {
            out.println("약자멸시 스킬 적용: 데미지 3배");
            modifier *= 3.0f;
        }
        if (overwhelm) {
            out.println("압도 스킬 적용: 데미지 5배");
            modifier *= 5.0f;
        }
        int finalDamage = (int) (baseDamage * modifier);
        out.printf("최종 데미지 : %d\n", finalDamage);

        int sideDamage = Main.sideDamage(finalDamage, effectiveStat, out, diceRoll);
        finalDamage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d\n", finalDamage);
        return new Result(damageTaken, finalDamage, true, 6, 0);
    }

    /**
     * 대구경파쇄산탄 탄환 : [대구경파쇄산탄 탄환]을 장전합니다.
     * 다음 기본 공격의 피해가 4D4는 4D10으로, 4D8은 3D40으로 변경됩니다.
     * 다음 기본 공격이 16의 스태미나를 소모합니다.
     * (마나 15 소모, 쿨타임 10턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 15 소모)
     */
    public static Result largeCaliberFragBullet(PrintStream out) {
        out.println("밀렵꾼-대구경파쇄산탄 탄환 사용");
        out.println("[대구경파쇄산탄 탄환] 장전.");
        out.println("다음 기본 공격: 4D4 → 4D10, 4D8 → 3D40으로 변경.");
        out.println("다음 기본 공격 시 스태미나 16 추가 소모.");
        return new Result(0, 0, true, 15, 0);
    }

    /**
     * 대구경충격벅샷 탄환 : [대구경충격벅샷 탄환]을 장전합니다.
     * 이후 수비 대신에 기본 공격을 진행할 수 있습니다.
     * 4D4는 3D10으로, 4D8은 2D50으로 변경됩니다.
     * 다음 기본 공격이 20의 스태미나를 소모하고 적의 공격을 취소시킵니다.
     * (마나 16, 쿨타임 12턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 16 소모)
     */
    public static Result largeCaliberShockBuckshot(PrintStream out) {
        out.println("밀렵꾼-대구경충격벅샷 탄환 사용");
        out.println("[대구경충격벅샷 탄환] 장전.");
        out.println("이후 수비 대신 기본 공격 진행 가능.");
        out.println("다음 기본 공격: 4D4 → 3D10, 4D8 → 2D50으로 변경.");
        out.println("다음 기본 공격 시 스태미나 20 추가 소모 및 적의 공격 취소.");
        return new Result(0, 0, true, 16, 0);
    }

    /**
     * 압도 : 압도 버프를 활성화합니다. 데미지 5배 효과를 부여합니다.
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result overwhelm(PrintStream out) {
        out.println("밀렵꾼-압도 사용");
        out.println("압도 버프 활성화: 데미지 5배 (1턴)");
        return new Result(0, 0, true, 7, 0);
    }

    /**
     * 전리품 : 전리품 효과를 적용합니다.
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result loot(PrintStream out) {
        out.println("밀렵꾼-전리품 사용");
        out.println("전리품 획득");
        return new Result(0, 0, true, 5, 0);
    }
}
