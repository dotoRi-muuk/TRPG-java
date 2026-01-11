package main.secondary.thief;

import main.Main;
import main.Result;

import java.io.PrintStream;

public class Assassin {
    /*
    암살자

    판정 사용 스탯 : 힘 또는 민첩 또는 신속

-   기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.

*   암살 대상 (패시브) : 전투 시작 시 '전장 이탈' 상태로 시작합니다. 이때 대상 1명을 지정하며, 전장 복귀 턴에 한하여 지정한 대상에게 가하는 피해가 4배로 증가합니다.
    일도양단 (패시브) : 전장 복귀 턴에 발동합니다. 자신의 턴을 2회 사용할 수 있으며, 기술이 순환의 영향을 받지 않으며, 스태미나 소모 없이 기술을 사용할 수 있습니다.
    묵음 (패시브) : 전장 복귀 턴에 피해를 입힌 적에게 다음 턴까지 행동 불가를 부여합니다.
*   생사여탈 (패시브) : 전투 시작 턴에 한하여 '암살'의 데미지가 2배로 증가합니다. 전장 복귀 턴에 한하여 암살자가 피해를 입힌 적의 체력이 10% 이하라면 즉시 처형합니다.

-   암살 (기술) : 대상에게 4D20의 피해를 입힙니다. (스태미나 44 소모)
-   급소 찌르기 (기술) : 대상에게 2D10의 피해를 입힙니다. (스태미나 5 소모)
-   목 긋기 (기술) : 대상에게 D20의 피해를 입힙니다. (스태미나 4 소모)
-   손목 긋기 (기술) : 대상에게 4D4의 피해를 입힙니다. (스태미나 3 소모)
-   후방 공격 (기술) : 대상에게 3D6의 피해를 입힙니다. (스태미나 3 소모)

    연막 (스킬) : 자신을 제외한 다른 대상의 첫 턴에 발동 가능합니다. 신속 판정에 성공할 경우, 이번 턴 동안 적에게 행동 불가를 부여하며 전장 이탈이 가능합니다. (마나 4 소모, 쿨타임 10턴)
    암살준비 (스킬) : '전장 이탈' 상태일 때 1회 발동할 수 있습니다. 자신의 스태미나와 마나를 각각 20 회복하며, 이후 전장 이탈 동안 휴식을 할 수 없습니다.
    타겟 포착 (스킬) : 다음 턴에 무조건 전장에 복귀합니다.
    전략적 후퇴 (스킬) : 전장 이탈 상태가 되며, 다음 3턴 이내에 강제로 전장에 복귀합니다. (마나 3 소모, 쿨타임 8턴)
*   확인 사살 (스킬) : '암살'을 적중시킨 적의 체력이 30% 이하일 때 발동합니다. 다음 '암살'의 데미지가 2배로 증가합니다. (마나 2 소모, 쿨타임 5턴)
-   위장 (전용 수비) : 민첩 판정 성공 시 적의 공격을 회피할 수 있습니다. 신속 판정에 성공할 경우 전장 이탈이 가능하며, 2턴 후 강제로 전장에 복귀합니다. (마나 7 소모)
     */


    /**
     * 암살자 위장: 민첩 판정 성공 시 적의 공격을 회피할 수 있습니다. 신속 판정에 성공할 경우 전장 이탈이 가능하며, 2턴 후 강제로 전장에 복귀합니다.
     * @param agiDex 판정에 사용할 민첩 스탯
     * @param speed 판정에 사용할 신속 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result camouflage(int agiDex, int speed, PrintStream out) {
        out.println("암살자-위장 사용");

        int verdict = Main.verdict(agiDex, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        out.print("위장 성공: 적의 공격 회피\n");

        int swiftVerdict = Main.verdict(speed, out);

        if (swiftVerdict > 0) {
            out.print("신속 판정 성공: 전장 이탈 가능, 2턴 후 강제 전장 복귀\n");
        } else {
            out.print("신속 판정 실패: 전장 이탈 불가\n");
        }

        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 암살자 암살: 대상에게 4D20의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (데미지 4배)
     * @param powerOverLifeAndDeath '생사여탈' 패시브 적용 여부 (전투 시작 턴에 한하여 데미지 2배)
     * @param confirmKill '확인 사살' 스킬 적용 여부 (대상 체력 30% 이하일 때 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result assassinate(int stat, boolean assassinationTarget, boolean powerOverLifeAndDeath, boolean confirmKill, PrintStream out) {
        return commonAttack("암살", stat, 4, 20, out, damage -> {
            damage = applyAssassinationTarget(damage, assassinationTarget, out);
            if (powerOverLifeAndDeath) {
                out.println("'생사여탈' 패시브 적용. 데미지 2배 증가");
                damage *= 2;
                out.printf("적용 후 데미지: %d%n", damage);
            }
            if (confirmKill) {
                out.println("'확인 사살' 스킬 적용. 데미지 2배 증가");
                damage *= 2;
                out.printf("적용 후 데미지: %d%n", damage);
            }
            return damage;
        });
    }

    /**
     * 암살자 기본 공격: 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean assassinationTarget, PrintStream out) {
        return commonAttack("기본 공격", stat, 1, 6, out, damage ->
                applyAssassinationTarget(damage, assassinationTarget, out));
    }

    /**
     * 암살자 급소 찌르기: 대상에게 2D10의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result vitalPointStab(int stat, boolean assassinationTarget, PrintStream out) {
        return commonAttack("급소 찌르기", stat, 2, 10, out, damage ->
                applyAssassinationTarget(damage, assassinationTarget, out));
    }

    /**
     * 암살자 목 긋기: 대상에게 1D20의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result throatSlit(int stat, boolean assassinationTarget, PrintStream out) {
        return commonAttack("목 긋기", stat, 1, 20, out, damage ->
                applyAssassinationTarget(damage, assassinationTarget, out));
    }

    /**
     * 암살자 손목 긋기: 대상에게 4D4의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result wristSlit(int stat, boolean assassinationTarget, PrintStream out) {
        return commonAttack("손목 긋기", stat, 4, 4, out, damage ->
                applyAssassinationTarget(damage, assassinationTarget, out));
    }

    /**
     * 암살자 후방 공격: 대상에게 3D6의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result backStab(int stat, boolean assassinationTarget, PrintStream out) {
        return commonAttack("후방 공격", stat, 3, 6, out, damage ->
                applyAssassinationTarget(damage, assassinationTarget, out));
    }

    private static Result commonAttack(String skillName, int stat, int diceCount, int diceFaces, PrintStream out, DamageCalculator calculator) {
        out.println("암살자-" + skillName + " 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict < 0) {
            return new Result();
        }
        int baseDamage = Main.dice(diceCount, diceFaces, out);
        out.printf("기본 데미지: %d\n", baseDamage);

        if (calculator != null) {
            baseDamage = calculator.calculate(baseDamage);
        }

        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        baseDamage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", baseDamage);
        return new Result(0, baseDamage, true, 0, 0);
    }

    private static int applyAssassinationTarget(int damage, boolean assassinationTarget, PrintStream out) {
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용. 데미지 4배 증가");
            damage *= 4;
            out.printf("적용 후 데미지: %d%n", damage);
        }
        return damage;
    }

    @FunctionalInterface
    private interface DamageCalculator {
        int calculate(int damage);
    }
}
