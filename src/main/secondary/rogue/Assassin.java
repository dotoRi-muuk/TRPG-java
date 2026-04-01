package main.secondary.rogue;

import main.Main;
import main.Result;

import java.io.PrintStream;


/**
 * 암살자
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩 또는 신속
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * <p>
 * 패시브:
 * <ul>
 *   <li>암살 대상: 전투 시작 시 '전장 이탈' 상태로 시작. 전장 복귀 턴에 지정 대상 피해 400% (최종 데미지 x4).</li>
 *   <li>일도양단: 전장 복귀 턴에 발동. 자신의 턴을 2회 사용, 순환 미적용, 스태미나 소모 없이 기술 사용.</li>
 *   <li>묵음: 전장 복귀 턴에 피해를 입힌 적에게 다음 턴까지 행동 불가 부여.</li>
 *   <li>생사여탈: 전투 시작 턴에 '암살' 최종 데미지 2배. 전장 복귀 턴에 적 체력 10% 이하 시 즉시 처형.</li>
 * </ul>
 */
public class Assassin {

    /**
     * 주사위 보정 배율 계산 (stat 판정 결과로부터)
     *
     * @param stat     사용할 스탯
     * @param diceRoll 판정에 사용된 주사위 값
     * @return 주사위 보정 배율 (1.0 + max(0, stat - diceRoll) * 0.1)
     */
    private static double computeDiceModifier(int stat, int diceRoll) {
        int statBonus = Math.max(0, stat - diceRoll);
        return 1.0 + statBonus * 0.1;
    }

    /**
     * 공통 공격 처리 (새 데미지 공식 적용)
     * 데미지 = [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param skillName      스킬 이름
     * @param stat           판정에 사용할 스탯
     * @param diceCount      주사위 수
     * @param diceFaces      주사위 면 수
     * @param precision      정밀 스탯 (치명타 판정용)
     * @param damageIncrease 데미지 증가 % (덧셈 보정)
     * @param finalMultiplier 최종 데미지 배율 (곱셈 보정)
     * @param staminaCost    스태미나 소모량
     * @param out            출력 스트림
     * @return 결과 객체
     */
    private static Result commonAttack(String skillName, int stat, int diceCount, int diceFaces, int precision,
                                       int damageIncrease, double finalMultiplier, int staminaCost, PrintStream out) {
        out.println("암살자-" + skillName + " 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict < 0) {
            return new Result(0, 0, false, 0, staminaCost);
        }
        int diceRoll = stat - verdict;

        int baseDamage = Main.dice(diceCount, diceFaces, out);
        out.printf("기본 데미지: %d\n", baseDamage);

        double diceModifier = computeDiceModifier(stat, diceRoll);
        out.printf("주사위 보정: %.2f\n", diceModifier);

        int damage = Main.calculateDamage(baseDamage, damageIncrease, finalMultiplier, diceModifier, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);
        return new Result(0, damage, true, 0, staminaCost);
    }

    /**
     * 암살: 대상에게 4D20의 피해를 입힙니다. (스태미나 44 소모)
     * <p>
     * 패시브 '암살 대상' 적용 시 최종 데미지 x4 (400%)
     * 패시브 '생사여탈' 적용 시 최종 데미지 추가 x2
     * 스킬 '확인 사살' 적용 시 최종 데미지 추가 x2
     * 버프 '포착' 적용 시 최종 데미지 x2.5
     *
     * @param stat                   판정에 사용할 스탯
     * @param assassinationTarget    '암살 대상' 패시브 적용 여부 (전장 복귀 턴, 최종 데미지 x4)
     * @param powerOverLifeAndDeath  '생사여탈' 패시브 적용 여부 (전투 시작 턴, 최종 데미지 x2)
     * @param confirmKill            '확인 사살' 스킬 적용 여부 (다음 암살 데미지 x2)
     * @param targetLocked           '포착' 버프 적용 여부 (최종 데미지 x2.5)
     * @param precision              정밀 스탯 (치명타 판정용)
     * @param out                    출력 스트림
     * @return 결과 객체
     */
    public static Result assassinate(int stat, boolean assassinationTarget, boolean powerOverLifeAndDeath,
                                     boolean confirmKill, boolean targetLocked, int precision, int level, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier *= 4.0;
        }
        if (powerOverLifeAndDeath) {
            out.println("'생사여탈' 패시브 적용: 최종 데미지 x2");
            finalMultiplier *= 2.0;
        }
        if (confirmKill) {
            out.println("'확인 사살' 스킬 적용: 데미지 x2");
            finalMultiplier *= 2.0;
        }
        if (targetLocked) {
            out.println("'포착' 버프 적용: 최종 데미지 x2.5 (250%)");
            finalMultiplier *= 2.5;
        }
        Result result = commonAttack("암살", stat, 4, 20, precision, 0, finalMultiplier, 44, out);
        if (!result.succeeded()) return result;
        int damage = (int)(result.damageDealt() * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);
        return new Result(0, damage, true, 0, result.staminaUsed());
    }

    /**
     * 기본 공격: 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat                판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (최종 데미지 x4)
     * @param precision           정밀 스탯 (치명타 판정용)
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean assassinationTarget, int precision, int level, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        Result result = commonAttack("기본 공격", stat, 1, 6, precision, 0, finalMultiplier, 0, out);
        if (!result.succeeded()) return result;
        int damage = (int)(result.damageDealt() * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);
        return new Result(0, damage, true, 0, result.staminaUsed());
    }

    /**
     * 급소 찌르기: 대상에게 4D12의 피해를 입힙니다. (스태미나 13 소모)
     *
     * @param stat                판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (최종 데미지 x4)
     * @param precision           정밀 스탯 (치명타 판정용)
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result vitalPointStab(int stat, boolean assassinationTarget, int precision, int level, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        Result result = commonAttack("급소 찌르기", stat, 4, 12, precision, 0, finalMultiplier, 13, out);
        if (!result.succeeded()) return result;
        int damage = (int)(result.damageDealt() * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);
        return new Result(0, damage, true, 0, result.staminaUsed());
    }

    /**
     * 목 긋기: 대상에게 1D20의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat                판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (최종 데미지 x4)
     * @param precision           정밀 스탯 (치명타 판정용)
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result throatSlit(int stat, boolean assassinationTarget, int precision, int level, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        Result result = commonAttack("목 긋기", stat, 1, 20, precision, 0, finalMultiplier, 3, out);
        if (!result.succeeded()) return result;
        int damage = (int)(result.damageDealt() * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);
        return new Result(0, damage, true, 0, result.staminaUsed());
    }

    /**
     * 손목 긋기: 대상에게 4D8의 피해를 입힙니다. (스태미나 10 소모)
     *
     * @param stat                판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (최종 데미지 x4)
     * @param precision           정밀 스탯 (치명타 판정용)
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result wristSlit(int stat, boolean assassinationTarget, int precision, int level, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        Result result = commonAttack("손목 긋기", stat, 4, 8, precision, 0, finalMultiplier, 10, out);
        if (!result.succeeded()) return result;
        int damage = (int)(result.damageDealt() * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);
        return new Result(0, damage, true, 0, result.staminaUsed());
    }

    /**
     * 후방 공격: 대상에게 3D12의 피해를 입힙니다. (스태미나 12 소모)
     *
     * @param stat                판정에 사용할 스탯
     * @param assassinationTarget '암살 대상' 패시브 적용 여부 (최종 데미지 x4)
     * @param precision           정밀 스탯 (치명타 판정용)
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result backStab(int stat, boolean assassinationTarget, int precision, int level, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        Result result = commonAttack("후방 공격", stat, 3, 12, precision, 0, finalMultiplier, 12, out);
        if (!result.succeeded()) return result;
        int damage = (int)(result.damageDealt() * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);
        return new Result(0, damage, true, 0, result.staminaUsed());
    }

    /**
     * 연막: 자신을 제외한 다른 대상의 턴에 발동 가능합니다.
     * 신속 판정 성공 시 이번 턴 동안 적에게 행동 불가를 부여하며 전장 이탈이 가능합니다.
     * (마나 4 소모, 쿨타임 10턴)
     *
     * @param speed 판정에 사용할 신속 스탯
     * @param out   출력 스트림
     * @return 결과 객체 (성공 시 적 행동 불가 + 전장 이탈, 마나 4 소모)
     */
    public static Result smokeScreen(int speed, int level, PrintStream out) {
        out.println("암살자-연막 사용");
        int verdict = Main.verdict(speed, out);
        if (verdict < 0) {
            out.println("신속 판정 실패: 연막 효과 없음");
            return new Result(0, 0, false, 0, 0);
        }
        out.println("신속 판정 성공: 이번 턴 적에게 행동 불가 부여, 전장 이탈");
        return new Result(0, 0, true, 4, 0);
    }

    /**
     * 암살준비: '전장 이탈' 상태일 때 1회 발동 가능합니다.
     * 자신의 스태미나와 마나를 각각 20 회복합니다.
     * 이후 전장 이탈 동안 휴식을 할 수 없습니다.
     *
     * @param inStealth 현재 '전장 이탈' 상태 여부
     * @param out       출력 스트림
     * @return 결과 객체 (성공 시 스태미나 20 회복, 마나 20 회복)
     */
    public static Result preparation(boolean inStealth, int level, PrintStream out) {
        out.println("암살자-암살준비 사용");
        if (!inStealth) {
            out.println("전장 이탈 상태가 아님: 암살준비 사용 불가");
            return new Result(0, 0, false, 0, 0);
        }
        out.println("암살준비 성공: 스태미나 20, 마나 20 회복. 이후 전장 이탈 동안 휴식 불가.");
        return new Result(0, 0, true, -20, -20);
    }

    /**
     * 타겟 포착: 다음 턴에 무조건 전장에 복귀합니다.
     * 한 명의 적에게 [포착]을 부여합니다.
     * [포착]: 다음 턴 [암살] 기술에 받는 최종 데미지가 250%로 증가합니다.
     * (마나 4 소모, 쿨타임 5턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 4 소모, 전장 복귀 강제, [포착] 부여)
     */
    public static Result targetLock(int level, PrintStream out) {
        out.println("암살자-타겟 포착 사용");
        out.println("다음 턴 전장 복귀 강제. 대상에게 [포착] 부여: 다음 턴 [암살] 최종 데미지 x2.5 (250%)");
        return new Result(0, 0, true, 4, 0);
    }

    /**
     * 전략적 후퇴: 전장 이탈 상태가 되며, 다음 3턴 이내에 강제로 전장에 복귀합니다.
     * (마나 3 소모, 쿨타임 8턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 3 소모, 전장 이탈, 3턴 후 강제 복귀)
     */
    public static Result tacticalRetreat(int level, PrintStream out) {
        out.println("암살자-전략적 후퇴 사용");
        out.println("전장 이탈. 3턴 이내 강제 전장 복귀.");
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 확인 사살: '암살'을 적중시킨 적의 체력이 30% 이하일 때 발동합니다.
     * 다음 '암살'의 데미지가 2배로 증가합니다.
     * (마나 2 소모, 쿨타임 5턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 2 소모, 다음 암살 데미지 x2)
     */
    public static Result confirmKill(int level, PrintStream out) {
        out.println("암살자-확인 사살 사용");
        out.println("다음 '암살' 데미지 x2.");
        return new Result(0, 0, true, 2, 0);
    }

    /**
     * 위장: 민첩 판정 성공 시 적의 공격을 회피할 수 있습니다.
     * 신속 판정에 성공할 경우 전장 이탈이 가능하며, 2턴 후 강제로 전장에 복귀합니다.
     * (마나 7 소모)
     *
     * @param agiDex 판정에 사용할 민첩 스탯
     * @param speed  판정에 사용할 신속 스탯
     * @param out    출력 스트림
     * @return 결과 객체 (마나 7 소모)
     */
    public static Result camouflage(int agiDex, int speed, int level, PrintStream out) {
        out.println("암살자-위장 사용");

        int verdict = Main.verdict(agiDex, out);

        if (verdict < 0) {
            out.println("민첩 판정 실패: 위장 실패");
            return new Result(0, 0, false, 0, 0);
        }

        out.println("민첩 판정 성공: 적의 공격 회피");

        int swiftVerdict = Main.verdict(speed, out);
        if (swiftVerdict >= 0) {
            out.println("신속 판정 성공: 전장 이탈, 2턴 후 강제 전장 복귀");
        } else {
            out.println("신속 판정 실패: 전장 이탈 불가");
        }

        return new Result(0, 0, true, 7, 0);
    }
}

