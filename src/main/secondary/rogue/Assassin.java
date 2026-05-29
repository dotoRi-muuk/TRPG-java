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
    private static final String[] MOMENTARY_TURN_EFFECTS = {
            "시간의 사이를 꿰뚫어: 내 턴에 총 4회 공격합니다.",
            "뚫린 찰나를 뛰어: 적의 공격으로 입은 피해만큼 체력을 회복합니다.",
            "처음부터 아무것도 있지 않았던 것 처럼: 모두가 행동 후 5회 행동합니다.",
            "죽음으로 매꾸리라: 모든 적이 행동 후 행동하며, 찰나 속의 필살 동안 적이 입은 피해의 합만큼 모든 적에게 피해를 입힙니다."
    };

    private static final String[] MOMENTARY_ALLOWED_TECHNIQUES = {
            "우누스", "두오", "트레스", "콰투오르", "쿠에", "식스", "세프템", "옥토", "노엠", "데케임"
    };

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
                                     boolean confirmKill, boolean targetLocked, int precision, PrintStream out) {
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
        return commonAttack("암살", stat, 4, 20, precision, 0, finalMultiplier, 44, out);
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
    public static Result plain(int stat, boolean assassinationTarget, int precision, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        return commonAttack("기본 공격", stat, 1, 6, precision, 0, finalMultiplier, 0, out);
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
    public static Result vitalPointStab(int stat, boolean assassinationTarget, int precision, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        return commonAttack("급소 찌르기", stat, 4, 12, precision, 0, finalMultiplier, 13, out);
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
    public static Result throatSlit(int stat, boolean assassinationTarget, int precision, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        return commonAttack("목 긋기", stat, 1, 20, precision, 0, finalMultiplier, 3, out);
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
    public static Result wristSlit(int stat, boolean assassinationTarget, int precision, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        return commonAttack("손목 긋기", stat, 4, 8, precision, 0, finalMultiplier, 10, out);
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
    public static Result backStab(int stat, boolean assassinationTarget, int precision, PrintStream out) {
        double finalMultiplier = 1.0;
        if (assassinationTarget) {
            out.println("'암살 대상' 패시브 적용: 최종 데미지 x4 (400%)");
            finalMultiplier = 4.0;
        }
        return commonAttack("후방 공격", stat, 3, 12, precision, 0, finalMultiplier, 12, out);
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
    public static Result smokeScreen(int speed, PrintStream out) {
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
    public static Result preparation(boolean inStealth, PrintStream out) {
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
    public static Result targetLock(PrintStream out) {
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
    public static Result tacticalRetreat(PrintStream out) {
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
    public static Result confirmKill(PrintStream out) {
        out.println("암살자-확인 사살 사용");
        out.println("다음 '암살' 데미지 x2.");
        return new Result(0, 0, true, 2, 0);
    }

    /**
     * 필살: 마지막으로 [암살]을 적중한 적에게 [치명적 외상]을 부여합니다.
     * 해당 적은 다음 [암살]로 받는 최종 데미지가 200%로 증가하고 이후 행동불가를 1회 얻습니다.
     * (마나 6, 쿨타임 7턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 6 소모)
     */
    public static Result deathblow(PrintStream out) {
        out.println("암살자-필살 사용");
        out.println("마지막으로 [암살]을 적중한 적에게 [치명적 외상] 부여");
        out.println("[치명적 외상]: 다음 [암살] 최종 데미지 200% 증가, 이후 행동불가 1회");
        out.println("마나 6 소모, 쿨타임 7턴");
        return new Result(0, 0, true, 6, 0);
    }

    /**
     * 예리: 영창을 진행합니다.
     * 영창을 진행한 1턴당 다음 공격까지의 정밀 스탯이 3 증가합니다.
     * 해당 스킬은 지혜 판정이 불가능합니다.
     * (마나 4, 쿨타임 3턴)
     *
     * @param channeledTurns 영창 진행 턴 수
     * @param out            출력 스트림
     * @return 결과 객체 (마나 4 소모)
     */
    public static Result keen(int channeledTurns, PrintStream out) {
        out.println("암살자-예리 사용");
        out.printf("영창 진행 턴 수: %d%n", channeledTurns);
        int precisionIncrease = channeledTurns * 3;
        out.printf("정밀 스탯 +%d (다음 공격까지 유지)%n", precisionIncrease);
        out.println("지혜 판정 불가");
        out.println("마나 4 소모, 쿨타임 3턴");
        return new Result(0, 0, true, 4, 0);
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
    public static Result camouflage(int agiDex, int speed, PrintStream out) {
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

    private static Result momentaryTechniqueAttack(String skillName, int stat, int mainDiceCount, int mainDiceFaces,
                                                   int precision, boolean momentaryFatalityActive, PrintStream out) {
        out.println("암살자-" + skillName + " 사용");
        if (!momentaryFatalityActive) {
            out.println("실패: [찰나 속의 필살] 지속 중에만 사용할 수 있습니다.");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict < 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int diceRoll = stat - verdict;

        int baseDamage = Main.dice(1, 12, out);
        if (mainDiceCount > 0 && mainDiceFaces > 0) {
            int additional = Main.dice(mainDiceCount, mainDiceFaces, out);
            baseDamage += additional;
            out.printf("%s 추가 데미지(%dD%d): %d%n", skillName, mainDiceCount, mainDiceFaces, additional);
        }
        out.printf("%s 기본 데미지(D12 포함): %d%n", skillName, baseDamage);

        double diceModifier = computeDiceModifier(stat, diceRoll);
        out.printf("주사위 보정: %.2f%n", diceModifier);
        int damage = Main.calculateDamage(baseDamage, 0, 1.0, diceModifier, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("%s 최종 데미지 : %d%n", skillName, damage);
        return new Result(0, damage, true, 0, 0);
    }

    public static Result momentaryFatality(int turn, boolean alreadyUsedInBattle,
                                           int damageTakenFromEnemyAttack, int accumulatedDamageDuringEffect,
                                           PrintStream out) {
        out.println("암살자-찰나 속의 필살 사용");
        if (alreadyUsedInBattle) {
            out.println("실패: 전투 중 1회만 사용 가능합니다.");
            return new Result(0, 0, false, 0, 0);
        }
        if (turn < 1 || turn > 4) {
            out.println("실패: 찰나 속의 필살은 1~4턴 효과만 존재합니다.");
            return new Result(0, 0, false, 0, 0);
        }

        out.println("지속 턴: 4턴");
        out.println("지속 중 행동불가/공격불가 면역");
        out.println("지속 중 공격 가능 기술: 우누스~데케임");
        out.printf("%d턴 효과 발동: %s%n", turn, MOMENTARY_TURN_EFFECTS[turn - 1]);
        if (turn == 2) {
            out.printf("회복량: 적 공격으로 입은 피해 %d만큼 회복%n", Math.max(0, damageTakenFromEnemyAttack));
            return new Result(-Math.max(0, damageTakenFromEnemyAttack), 0, true, 0, 0);
        }
        if (turn == 4) {
            out.printf("폭발 피해: 누적 피해 %d를 모든 적에게 부여%n", Math.max(0, accumulatedDamageDuringEffect));
            return new Result(0, Math.max(0, accumulatedDamageDuringEffect), true, 0, 0);
        }
        return new Result(0, 0, true, 0, 0);
    }

    public static Result momentaryDeathblow(int turn, boolean alreadyUsedInBattle,
                                            int damageTakenFromEnemyAttack, int accumulatedDamageDuringEffect,
                                            PrintStream out) {
        return momentaryFatality(turn, alreadyUsedInBattle, damageTakenFromEnemyAttack, accumulatedDamageDuringEffect, out);
    }

    public static Result canUseMomentaryTechnique(String techniqueName, PrintStream out) {
        for (String allowed : MOMENTARY_ALLOWED_TECHNIQUES) {
            if (allowed.equals(techniqueName)) {
                out.printf("사용 가능 기술 확인: [%s] 사용 가능%n", techniqueName);
                return new Result(0, 0, true, 0, 0);
            }
        }
        out.printf("사용 불가 기술: [%s] - 찰나 속의 필살 중에는 우누스~데케임만 사용 가능%n", techniqueName);
        return new Result(0, 0, false, 0, 0);
    }

    public static Result unus(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("우누스", stat, 0, 0, precision, momentaryFatalityActive, out);
    }

    public static Result duo(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("두오", stat, 2, 20, precision, momentaryFatalityActive, out);
    }

    public static Result tres(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("트레스", stat, 3, 8, precision, momentaryFatalityActive, out);
    }

    public static Result quatuor(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("콰투오르", stat, 4, 10, precision, momentaryFatalityActive, out);
    }

    public static Result quattuor(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return quatuor(stat, precision, momentaryFatalityActive, out);
    }

    public static Result que(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("쿠에", stat, 5, 6, precision, momentaryFatalityActive, out);
    }

    public static Result six(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("식스", stat, 6, 6, precision, momentaryFatalityActive, out);
    }

    public static Result sex(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return six(stat, precision, momentaryFatalityActive, out);
    }

    public static Result septem(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("세프템", stat, 7, 8, precision, momentaryFatalityActive, out);
    }

    public static Result octo(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("옥토", stat, 8, 7, precision, momentaryFatalityActive, out);
    }

    public static Result noem(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("노엠", stat, 9, 10, precision, momentaryFatalityActive, out);
    }

    public static Result novem(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return noem(stat, precision, momentaryFatalityActive, out);
    }

    public static Result decem(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return momentaryTechniqueAttack("데케임", stat, 10, 12, precision, momentaryFatalityActive, out);
    }

    public static Result dekeim(int stat, int precision, boolean momentaryFatalityActive, PrintStream out) {
        return decem(stat, precision, momentaryFatalityActive, out);
    }

    public static Result throwDagger(int stat, boolean defenseTriggered, int throwMarkBonus, PrintStream out) {
        return throwDagger(stat, 0, defenseTriggered, throwMarkBonus, out);
    }

    public static Result throwDagger(int stat, int precision, boolean defenseTriggered, int throwMarkBonus, PrintStream out) {
        out.println("암살자-투척 사용");
        if (!defenseTriggered) {
            out.println("실패: 수비 발동 시에만 사용할 수 있습니다.");
            return new Result(0, 0, false, 0, 0);
        }
        int verdict = Main.verdict(stat, out);
        if (verdict < 0) {
            out.println("투척 실패: [단검 표식] 생성 실패");
            return new Result(0, 0, false, 0, 0);
        }

        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 12, out);
        double diceModifier = computeDiceModifier(stat, diceRoll);
        int damage = Main.calculateDamage(baseDamage, 0, 1.0, diceModifier, out);
        damage = Main.criticalHit(precision, damage, out);

        int marksApplied = 1 + Math.max(0, throwMarkBonus);
        out.printf("적중: [단검 표식] %d개 생성%n", marksApplied);
        out.println("해당 공격은 적의 수비 효과를 발동시키지 않습니다.");
        return new Result(0, damage, true, 0, 0);
    }

    public static Result foreseenAssassination(int stat, boolean hitByDaggerThrow, int precision, PrintStream out) {
        out.println("암살자-예견된 암살 사용");
        if (!hitByDaggerThrow) {
            out.println("실패: [단검 투척]에 적중한 대상에게만 사용 가능합니다.");
            return new Result(0, 0, false, 0, 0);
        }
        return commonAttack("예견된 암살", stat, 1, 80, precision, 0, 1.0, 0, out);
    }

    public static Result predictedAssassination(int stat, boolean hitByDaggerThrow, int precision, PrintStream out) {
        return foreseenAssassination(stat, hitByDaggerThrow, precision, out);
    }

    public static Result namelessSlash(int targetDaggerMarks, PrintStream out) {
        out.println("암살자-무명참 사용");
        if (targetDaggerMarks < 5) {
            out.printf("실패: 대상 [단검 표식]이 5 이상이어야 합니다. (현재 %d)%n", targetDaggerMarks);
            return new Result(0, 0, false, 0, 0);
        }
        out.println("성공: 대상에게 매턴 [단검 표식]을 부여합니다.");
        out.println("성공: 해당 대상에게 [투척]으로 부여하는 [단검 표식]이 1개 증가합니다.");
        return new Result(0, 0, true, 0, 0);
    }

    public static Result conclusion(int stat, int targetDaggerMarks, int allEnemiesDaggerMarks, int precision, PrintStream out) {
        out.println("암살자-종결 사용");
        if (targetDaggerMarks < 10) {
            out.printf("실패: 대상 [단검 표식]이 10 이상이어야 합니다. (현재 %d)%n", targetDaggerMarks);
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict < 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int diceRoll = stat - verdict;

        int d250 = Main.dice(1, 250, out);
        int markDamage = 0;
        for (int i = 0; i < Math.max(0, allEnemiesDaggerMarks); i++) {
            markDamage += Main.dice(1, 10, out);
        }
        int baseDamage = d250 + markDamage;
        out.printf("종결 기본 데미지: D250(%d) + [모든 적 단검 표식 %d] x D10 합(%d) = %d%n",
                d250, Math.max(0, allEnemiesDaggerMarks), markDamage, baseDamage);

        double diceModifier = computeDiceModifier(stat, diceRoll);
        int damage = Main.calculateDamage(baseDamage, 0, 1.0, diceModifier, out);
        damage = Main.criticalHit(precision, damage, out);
        out.println("효과: 모든 적의 [단검 표식] 제거");
        return new Result(0, damage, true, 0, 0);
    }

    public static Result finalization(int stat, int targetDaggerMarks, int allEnemiesDaggerMarks, int precision, PrintStream out) {
        return conclusion(stat, targetDaggerMarks, allEnemiesDaggerMarks, precision, out);
    }
}
