package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 석궁사수
 * <p>
 * 판정 사용 스탯 : 힘, 민첩
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 */
public class Crossbowman {
    private static final int DEFAULT_LEVEL = 1;
    private static final int DEFAULT_FINAL_DAMAGE_PERCENT = 100;

    private static int scarletArrowDamage(PrintStream out) {
        return Main.dice(1, 2, out) == 1 ? 1 : 15;
    }

    /**
     * 데미지 계산 공식 적용
     * [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param dices                주사위 수
     * @param sides                주사위 면 수
     * @param stat                 사용할 스탯
     * @param flatBonus            추가 고정 데미지 (%)
     * @param finalDamageMultiplier 최종 데미지 배율
     * @param precision            정밀 스탯
     * @param staminaUsed          소모 스태미나
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    private static Result calculate(int dices, int sides, int stat, int flatBonus, double finalDamageMultiplier,
                                    int precision, int staminaUsed, PrintStream out, int diceRoll) {
        return calculate(dices, sides, stat, flatBonus, finalDamageMultiplier, precision, staminaUsed, DEFAULT_LEVEL, 0, DEFAULT_FINAL_DAMAGE_PERCENT, false, out, diceRoll);
    }

    private static Result calculate(int dices, int sides, int stat, int flatBonus, double finalDamageMultiplier,
                                    int precision, int staminaUsed, int level, int externalDamageIncreasePercent,
                                    int externalFinalDamagePercent, boolean scarletRain, PrintStream out, int diceRoll) {
        int basic = 0;
        if (scarletRain) {
            out.println("붉은 강우 적용: 모든 화살 피해를 1 또는 15로 변경");
            for (int i = 0; i < dices; i++) {
                int arrowDamage = scarletArrowDamage(out);
                basic += arrowDamage;
            }
            out.printf("붉은 강우 기본 데미지 합계 : %d\n", basic);
        } else {
            basic = Main.dice(dices, sides, out);
        }
        out.printf("기본 데미지 : %d\n", basic);

        int totalDamageIncrease = flatBonus + externalDamageIncreasePercent;
        int levelMultiplierPercent = 100 + level * level;
        double totalFinalMultiplier = finalDamageMultiplier * (levelMultiplierPercent / 100.0) * (externalFinalDamagePercent / 100.0);
        int damage = Main.calculateDamage(basic, totalDamageIncrease, totalFinalMultiplier, out);

        int sideDmg = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDmg;
        out.printf("데미지 보정치 : %d\n", sideDmg);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        return new Result(0, damage, true, 0, staminaUsed);
    }

    /**
     * 장전/발사 패시브 - 장전: 화살을 장전합니다.
     *
     * @param out 출력 스트림
     * @return 장전된 화살 개수
     */
    public static int reload(PrintStream out) {
        return Main.dice(1, 6, out);
    }

    /**
     * 기본 공격 : 대상에게 화살을 발사하여 피해를 입힙니다.
     *
     * @param stat                사용할 스탯
     * @param arrowCount          장전된 화살 개수
     * @param focusedBarrage      집중 난사 스킬 활성화 여부
     * @param executionArrow      처형 화살 스킬 활성화 여부
     * @param distanceCalculation 비거리 계산 패시브 활성화 여부
     * @param precision           정밀 스탯
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int arrowCount, boolean focusedBarrage, boolean executionArrow,
                               boolean distanceCalculation, int precision, PrintStream out) {
        return plain(stat, arrowCount, focusedBarrage, executionArrow, distanceCalculation, precision, DEFAULT_LEVEL, 0, DEFAULT_FINAL_DAMAGE_PERCENT, false, out);
    }

    public static Result plain(int stat, int arrowCount, boolean focusedBarrage, boolean executionArrow,
                               boolean distanceCalculation, int precision, int level,
                               int externalDamageIncreasePercent, int externalFinalDamagePercent, boolean scarletRain,
                               PrintStream out) {
        out.println("석궁사수-기본공격 사용");

        // 처형 화살 로직
        if (executionArrow) {
            int executionChance = (arrowCount / 25) * 5;
            out.printf("처형 확률 : %d%%\n", executionChance);
            int random = (int) (Math.random() * 100) + 1;
            if (executionChance >= random) {
                out.println("처형 발동!");
                return new Result(0, 0, true, 0, 0);
            } else {
                out.printf("처형 미발동 (주사위 %d > 확률 %d%%)\n", random, executionChance);
                return new Result(0, 0, false, 0, 0);
            }
        }

        // 판정 로직
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;

        int dices = arrowCount;
        int sides = 6;

        // 무차별 난사: 집중 난사 비활성화 && 화살 4개 이상
        if (!focusedBarrage && arrowCount >= 4) {
            out.println("무차별 난사 발동: 광역 공격");
        }

        // 집중 공격 배율 계산
        int multiplier = focusedBarrage ? 50 : 30;
        int flatBonus = arrowCount * multiplier;

        // 최종 데미지 배율
        double finalDamageMultiplier = 1.0;

        // 비거리 계산
        if (distanceCalculation) {
            int v1 = Main.verdict(stat, out);
            int v2 = Main.verdict(stat, out);
            if (v1 >= 0 && v2 >= 0) {
                finalDamageMultiplier *= 1.5;
                out.println("비거리 계산 성공: 최종 데미지 1.5배");
            }
        }

        return calculate(dices, sides, stat, flatBonus, finalDamageMultiplier, precision, 0, level,
                externalDamageIncreasePercent, externalFinalDamagePercent, scarletRain, out, diceRoll);
    }

    /**
     * 던지기 : 대상에게 1D6의 피해를 입힙니다. (스태미나 0 소모)
     *
     * @param stat      사용할 스탯
     * @param precision 정밀 스탯
     * @param out       출력 스트림
     * @return 결과 객체
     */
    public static Result throwWeapon(int stat, int precision, PrintStream out) {
        return throwWeapon(stat, precision, DEFAULT_LEVEL, 0, DEFAULT_FINAL_DAMAGE_PERCENT, false, out);
    }

    public static Result throwWeapon(int stat, int precision, int level, int externalDamageIncreasePercent,
                                     int externalFinalDamagePercent, boolean scarletRain, PrintStream out) {
        out.println("석궁사수-던지기 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        return calculate(1, 6, stat, 0, 1.0, precision, 0, level,
                externalDamageIncreasePercent, externalFinalDamagePercent, scarletRain, out, diceRoll);
    }

    /**
     * 빠른 장전 : 화살 1개를 즉시 장전합니다. (스태미나 1 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result quickReload(PrintStream out) {
        out.println("석궁사수-빠른 장전 사용");
        out.println("화살 1개 장전");
        return new Result(0, 0, true, 0, 1);
    }

    /**
     * 단일사격 : 화살을 1개 소모하여 대상에게 1D10의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat       사용할 스탯
     * @param arrowCount 장전된 화살 개수
     * @param precision  정밀 스탯
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result singleShot(int stat, int arrowCount, int precision, PrintStream out) {
        return singleShot(stat, arrowCount, precision, DEFAULT_LEVEL, 0, DEFAULT_FINAL_DAMAGE_PERCENT, false, out);
    }

    public static Result singleShot(int stat, int arrowCount, int precision, int level,
                                    int externalDamageIncreasePercent, int externalFinalDamagePercent, boolean scarletRain,
                                    PrintStream out) {
        out.println("석궁사수-단일사격 사용");

        if (arrowCount < 1) {
            out.println("화살이 부족합니다! (필요: 1)");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 2);
        int diceRoll = stat - verdict;
        return calculate(1, 10, stat, 0, 1.0, precision, 2, level,
                externalDamageIncreasePercent, externalFinalDamagePercent, scarletRain, out, diceRoll);
    }

    /**
     * 발광 화살 : 화살을 2개 소모하여 대상에게 2D8의 피해를 입힙니다.
     * 적중 시 다음 턴 적이 받는 최종 데미지 1.5배. (스태미나 4 소모)
     *
     * @param stat       사용할 스탯
     * @param arrowCount 장전된 화살 개수
     * @param precision  정밀 스탯
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result flareArrow(int stat, int arrowCount, int precision, PrintStream out) {
        return flareArrow(stat, arrowCount, precision, DEFAULT_LEVEL, 0, DEFAULT_FINAL_DAMAGE_PERCENT, false, out);
    }

    public static Result flareArrow(int stat, int arrowCount, int precision, int level,
                                    int externalDamageIncreasePercent, int externalFinalDamagePercent, boolean scarletRain,
                                    PrintStream out) {
        out.println("석궁사수-발광 화살 사용");

        if (arrowCount < 2) {
            out.println("화살이 부족합니다! (필요: 2)");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 4);
        int diceRoll = stat - verdict;
        out.println("적중 시 다음 턴 적이 받는 최종 데미지 1.5배");
        return calculate(2, 8, stat, 0, 1.0, precision, 4, level,
                externalDamageIncreasePercent, externalFinalDamagePercent, scarletRain, out, diceRoll);
    }

    /**
     * 마비 화살 : 화살을 1개 소모하여 대상에게 2D8 + 1D6의 피해를 입힙니다.
     * 적중 시 다음 턴 적의 모든 스탯 2 감소. (스태미나 6 소모)
     *
     * @param stat       사용할 스탯
     * @param arrowCount 장전된 화살 개수
     * @param precision  정밀 스탯
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result paralysisArrow(int stat, int arrowCount, int precision, PrintStream out) {
        return paralysisArrow(stat, arrowCount, precision, DEFAULT_LEVEL, 0, DEFAULT_FINAL_DAMAGE_PERCENT, false, out);
    }

    public static Result paralysisArrow(int stat, int arrowCount, int precision, int level,
                                        int externalDamageIncreasePercent, int externalFinalDamagePercent, boolean scarletRain,
                                        PrintStream out) {
        out.println("석궁사수-마비 화살 사용");

        if (arrowCount < 1) {
            out.println("화살이 부족합니다! (필요: 1)");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 6);
        int diceRoll = stat - verdict;

        out.println("적중 시 다음 턴 적의 모든 스탯 2 감소");
        int dice1;
        int dice2;
        if (scarletRain) {
            out.println("붉은 강우 적용: 모든 화살 피해를 1 또는 15로 변경");
            dice1 = scarletArrowDamage(out) + scarletArrowDamage(out);
            dice2 = scarletArrowDamage(out);
        } else {
            dice1 = Main.dice(2, 8, out);
            dice2 = Main.dice(1, 6, out);
        }
        int basic = dice1 + dice2;
        out.printf("기본 데미지 : %d + %d = %d\n", dice1, dice2, basic);

        int levelMultiplierPercent = 100 + level * level;
        double totalFinalMultiplier = (levelMultiplierPercent / 100.0) * (externalFinalDamagePercent / 100.0);
        int damage = Main.calculateDamage(basic, externalDamageIncreasePercent, totalFinalMultiplier, out);
        int sideDmg = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDmg;
        out.printf("데미지 보정치 : %d\n", sideDmg);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        return new Result(0, damage, true, 0, 6);
    }

    public static Result scarletRain(PrintStream out) {
        out.println("석궁사수-붉은 강우 사용");
        out.println("모든 화살의 피해가 1 또는 15로 변경됩니다.");
        return new Result(0, 0, true, 15, 0);
    }

    public static Result finalReloadAttack(int stat, int arrowsFirst, int arrowsSecond, int arrowsThird, int arrowsFourth,
                                           boolean focusedBarrage, boolean distanceCalculation, int precision, int level,
                                           int externalDamageIncreasePercent, int externalFinalDamagePercent, boolean scarletRain,
                                           PrintStream out) {
        out.println("석궁사수-종언의 장전 사용");
        out.println("다음 장전으로 모든 화살 소진 전까지 추가 장전 불가");
        out.println("이번 턴 4회 행동하며 기본 공격 화살 소모 개수를 직접 선택");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;

        int[] arrowsByAttack = {Math.max(0, arrowsFirst), Math.max(0, arrowsSecond), Math.max(0, arrowsThird), Math.max(0, arrowsFourth)};
        int arrowProductBonus = arrowsByAttack[1] * arrowsByAttack[2];
        out.printf("종언의 장전 보너스: 2회 화살(%d) x 3회 화살(%d) = +%d%%%n", arrowsByAttack[1], arrowsByAttack[2], arrowProductBonus);

        double finalDamageMultiplier = 1.0;
        if (distanceCalculation) {
            int v1 = Main.verdict(stat, out);
            int v2 = Main.verdict(stat, out);
            if (v1 >= 0 && v2 >= 0) {
                finalDamageMultiplier *= 1.5;
                out.println("비거리 계산 성공: 최종 데미지 1.5배");
            }
        }

        int concentratedMultiplier = focusedBarrage ? 50 : 30;
        int totalDamage = 0;
        for (int i = 0; i < arrowsByAttack.length; i++) {
            int arrows = arrowsByAttack[i];
            if (arrows <= 0) {
                out.printf("%d회차: 화살 미소모로 공격 생략%n", i + 1);
                continue;
            }
            int flatBonus = arrows * concentratedMultiplier + arrowProductBonus;
            Result attack = calculate(
                    arrows, 6, stat, flatBonus, finalDamageMultiplier, precision, 0, level,
                    externalDamageIncreasePercent, externalFinalDamagePercent, scarletRain, out, diceRoll
            );
            totalDamage += attack.damageDealt();
            out.printf("%d회차 누적 데미지: %d%n", i + 1, totalDamage);
        }
        return new Result(0, totalDamage, true, 0, 0);
    }

    /**
     * 복제화살 : 현재 장전된 화살을 2배로 복제합니다. (마나 10 소모)
     *
     * @param arrowCount 장전된 화살 개수
     * @param out        출력 스트림
     * @return 결과 객체
     */
    public static Result duplicateArrow(int arrowCount, PrintStream out) {
        out.println("석궁사수-복제화살 사용");
        out.println("장전된 화살 2배 복제");
        out.printf("화살 %d개 -> %d개\n", arrowCount, arrowCount * 2);
        return new Result(0, 0, true, 10, 0);
    }

    /**
     * 재빠른 손놀림 : 매 턴 화살 1개를 추가로 장전합니다. (마나 5 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result quickHands(PrintStream out) {
        out.println("석궁사수-재빠른 손놀림 사용");
        out.println("재빠른 손놀림: 매 턴 화살 1개 추가 장전 (민첩 판정 지속)");
        return new Result(0, 0, true, 5, 0);
    }

    /**
     * 처형 화살 : 처형 화살을 장전합니다. 데미지 0, 25발당 5% 처형 확률. (마나 8 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result executionArrowSkill(PrintStream out) {
        out.println("석궁사수-처형 화살 사용");
        out.println("처형 화살 장전: 데미지 0, 25발당 5% 처형 확률");
        return new Result(0, 0, true, 8, 0);
    }

    /**
     * 집중 난사 : 무차별 난사를 비활성화하고 집중 공격 효과를 50%로 강화합니다. (마나 6 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result focusedBarrageSkill(PrintStream out) {
        out.println("석궁사수-집중 난사 사용");
        out.println("집중 난사: 무차별 난사 비활성화, 집중 공격 효과 50%로 강화");
        return new Result(0, 0, true, 6, 0);
    }

    /**
     * 화살 꺾기 : (6 * 소모 화살) 만큼 받는 데미지가 감소합니다. (스태미나 3 소모)
     *
     * @param consumedArrows 소모할 화살 개수
     * @param out            출력 스트림
     * @return 결과 객체
     */
    public static Result breakArrow(int consumedArrows, PrintStream out) {
        out.println("석궁사수-화살 꺾기 사용");
        int reduction = 6 * consumedArrows;
        out.println("화살 꺾기: 받는 데미지 " + reduction + " 감소");
        return new Result(0, 0, true, 0, 3);
    }

    /**
     * 이럴 때 일수록! : 받은 데미지 4마다 화살 1개를 장전합니다. (스태미나 9 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result inTheseTimes(PrintStream out) {
        out.println("석궁사수-이럴 때 일수록! 사용");
        out.println("받은 데미지 4마다 화살 1개 장전");
        return new Result(0, 0, true, 0, 9);
    }
}
