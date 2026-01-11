package main.secondary.archer;

import main.Main;
import main.Result;
import main.Stat;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 석궁사수
 * <p>
 * 판정 사용 스탯 : 힘, 민첩
 * <p>
 * -   기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 * <p>
 * *   장전/발사 (패시브) : 매 턴마다 D6의 화살 1개를 장전합니다. 기본 공격 적중 시 장전된 화살을 모두 소모하여 공격합니다. 각 화살은 D6의 데미지를 입힙니다.<br>
 * *   무차별 난사 (패시브) : 화살이 4개 이상 장전되어있을 시 발동합니다. 기본 공격이 광역 공격으로 변경됩니다.<br>
 * *   집중 공격 (패시브) : 장전된 화살 1개당 데미지가 30% 증가합니다.<br>
 * *   비거리 계산 (패시브) : 공격 판정을 2회 진행하여 모두 성공해야 하며, 성공 시 데미지가 2배로 증가합니다.<br>
 * <p>
 * -   던지기 (기술) : 대상에게 D6의 피해를 입힙니다. (스태미나 0 소모)<br>
 *     빠른 장전 (기술) : 화살 1개를 장전합니다. (스태미나 1 소모)<br>
 * -   단일사격 (기술) : 화살을 1개 소모하여 대상에게 D10의 피해를 입힙니다. (스태미나 2 소모)<br>
 * -   발광 화살 (기술) : 화살을 2개 소모하여 대상에게 2D8의 피해를 입힙니다. 다음 턴까지 적이 받는 데미지를 1.5배로 증가시킵니다. (스태미나 4 소모)<br>
 * -   마비 화살 (기술) : 화살을 1개 소모하여 대상에게 2D8 + D6의 피해를 입힙니다. 다음 턴까지 적의 모든 스탯을 2 감소시킵니다. (스태미나 6 소모)<br>
 * <p>
 *     복제화살 (스킬) : 장전된 화살을 2배로 복제합니다. (마나 10 소모, 쿨타임 15턴)<br>
 *     재빠른 손놀림 (스킬) : 5턴 동안 매 턴 화살 1개를 추가로 장전합니다. 민첩 판정이 필요합니다. (마나 5 소모, 쿨타임 10턴)<br>
 * *   처형 화살 (스킬) : 장전한 모든 화살을 처형 화살로 변경합니다. 처형 화살은 데미지가 없으며, 기본 공격 시 처형 화살 25개당 5% 확률로 처형을 발동합니다. (마나 8 소모, 쿨타임 14턴)<br>
 * *   오차 제거 (스킬) : '무차별 난사'를 비활성화합니다. '집중 공격'의 데미지 증가 효과가 30%에서 50%로 강화됩니다. (마나 6 소모, 쿨타임 8턴)<br>
 * <p>
 * -   화살 꺾기 (전용 수비) : (6 * 소모 화살) 만큼 받는 데미지가 감소합니다. (스태미나 3 소모)<br>
 * -   이럴 때 일수록! (전용 수비) : 받은 데미지 4마다 화살 1개를 장전합니다. (스태미나 9 소모)<br>
 */
public class Crossbowman {

    /**
     * 석궁사수 던지기
     * @param stat 사용할 스탯
     * @param loadedArrows 장전된 화살 개수
     * @param calculateRange 비거리 계산 패시브 활성화 여부
     * @param eliminateError 오차 제거 스킬 활성화 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result toss(int stat, int loadedArrows, boolean calculateRange, boolean eliminateError, PrintStream out) {
        out.println("석궁사수-던지기 사용");

        // 1. 판정 로직
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        // 비거리 계산 시 추가 판정
        if (calculateRange) {
            out.println("비거리 계산 적용: 추가 판정을 진행합니다.");
            verdict = Main.verdict(stat, out);
            if (verdict <= 0) return new Result(0, 0, false, 0, 0, new HashMap<>());
        }

        // 2. 데미지 계산
        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d\n", baseDamage);

        // 3. 집중 공격 및 오차 제거 배율
        double focusModifier = 0.3;
        if (eliminateError) {
            focusModifier = 0.5;
            out.println("오차 제거 적용: 집중 공격 데미지 증가 효과가 50%로 강화됩니다.");
        }

        double damageModifier = 1 + (focusModifier * loadedArrows);
        out.printf("집중 공격 데미지 증가 배율 : %.1f * %d = %.1f\n", focusModifier, loadedArrows, damageModifier);

        // 4. 비거리 계산 배율
        if (calculateRange) {
            out.println("비거리 계산 적용: 데미지 2배 증가");
            damageModifier *= 2;
        }

        // 5. 최종 데미지 산출
        int damageAfterModifier = (int)(baseDamage * damageModifier);
        out.printf("배율 적용 후 데미지 : %d\n", damageAfterModifier);

        int sideDamage = Main.sideDamage(damageAfterModifier, stat, out);
        damageAfterModifier += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", damageAfterModifier);

        return new Result(0, damageAfterModifier, true, 0, 0);
    }

    /**
     * 석궁사수 기본공격
     * @param stat 사용할 스탯
     * @param loadedArrows 장전된 화살 개수
     * @param executionArrows 처형 화살 개수
     * @param indiscriminateFire 무차별 난사 패시브 활성화 여부
     * @param calculateRange 비거리 계산 패시브 활성화 여부
     * @param eliminateError 오차 제거 스킬 활성화 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int loadedArrows, int executionArrows, boolean indiscriminateFire, boolean calculateRange, boolean eliminateError, PrintStream out) {
        out.println("석궁사수-기본공격 사용");

        // 1. 판정 로직
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        // 비거리 계산 시 추가 판정
        if (calculateRange) {
            out.println("비거리 계산 적용: 추가 판정을 진행합니다.");
            verdict = Main.verdict(stat, out);
            if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        }

        // 2. 처형 화살 로직 (처형 화살이 있을 때만 수행)
        if (executionArrows > 0) {
            int executionChance = (executionArrows / 25) * 5;
            out.printf("처형 확률 : %d%%\n", executionChance);

            // 확률이 존재할 때만 주사위 굴림
            if (executionChance > 0) {
                int random = (int)(Math.random() * 100) + 1;
                if (executionChance >= random) {
                    out.printf("처형 발동! (주사위 %d <= 확률 %d%%)\n", random, executionChance);
                    return new Result(0, Integer.MAX_VALUE, true, 0, 0);
                } else {
                    out.printf("처형 미발동 (주사위 %d > 확률 %d%%)\n", random, executionChance);
                }
            }
        }

        // 3. 무차별 난사 로그
        if (indiscriminateFire) {
            out.println("무차별 난사 적용: 기본 공격이 광역으로 적용됩니다.");
        }

        // 4. 데미지 계산
        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d\n", baseDamage);

        // 일반 화살 데미지 (처형 화살 유무와 무관하게 적용)
        int arrowDamage = Main.dice(loadedArrows, 6, out);
        out.printf("장전된 화살 (%d개) 데미지 : %d\n", loadedArrows, arrowDamage);

        // 5. 집중 공격 및 오차 제거 배율
        double focusModifier = 0.3;
        if (eliminateError) {
            focusModifier = 0.5;
            out.println("오차 제거 적용: 집중 공격 데미지 증가 효과가 50%로 강화됩니다.");
        }

        double damageModifier = 1 + (focusModifier * loadedArrows);
        out.printf("집중 공격 데미지 증가 배율 : %.1f * %d = %.1f\n", focusModifier, loadedArrows, damageModifier);

        // 6. 비거리 계산 배율
        if (calculateRange) {
            out.println("비거리 계산 적용: 데미지 2배 증가");
            damageModifier *= 2;
        }

        // 7. 최종 데미지 산출
        int damageAfterModifier = (int)((baseDamage + arrowDamage) * damageModifier);
        out.printf("배율 적용 후 데미지 : %d\n", damageAfterModifier);

        int sideDamage = Main.sideDamage(damageAfterModifier, stat, out);
        damageAfterModifier += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", damageAfterModifier);

        return new Result(0, damageAfterModifier, true, 0, 0);
    }

    /**
     * 단일사격
     * @param stat 사용할 스탯
     * @param loadedArrows 장전된 화살 개수
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result singleShot(int stat, int loadedArrows, PrintStream out) {
        out.println("석궁사수-단일사격 사용");

        if (loadedArrows < 1) {
            out.println("화살이 부족합니다! (필요: 1)");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(1, 10, out);
        out.printf("기본 데미지 : %d\n", baseDamage);

        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int finalDamage = baseDamage + sideDamage;
        out.printf("최종 데미지 : %d\n", finalDamage);

        return new Result(0, finalDamage, true, 0, 2);
    }

    /**
     * 발광 화살
     * @param stat 사용할 스탯
     * @param loadedArrows 장전된 화살 개수
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result luminousArrow(int stat, int loadedArrows, PrintStream out) {
        out.println("석궁사수-발광 화살 사용");

        if (loadedArrows < 2) {
            out.println("화살이 부족합니다! (필요: 2)");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(2, 8, out);
        out.printf("기본 데미지 : %d\n", baseDamage);

        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int finalDamage = baseDamage + sideDamage;
        out.printf("최종 데미지 : %d\n", finalDamage);
        out.println("효과: 다음 턴까지 적이 받는 데미지를 1.5배로 증가시킵니다.");

        return new Result(0, finalDamage, true, 0, 4);
    }

    /**
     * 마비 화살
     * @param stat 사용할 스탯
     * @param loadedArrows 장전된 화살 개수
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result paralysisArrow(int stat, int loadedArrows, PrintStream out) {
        out.println("석궁사수-마비 화살 사용");

        if (loadedArrows < 1) {
            out.println("화살이 부족합니다! (필요: 1)");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int dice1 = Main.dice(2, 8, out);
        int dice2 = Main.dice(1, 6, out);
        int baseDamage = dice1 + dice2;
        out.printf("기본 데미지 : %d + %d = %d\n", dice1, dice2, baseDamage);

        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int finalDamage = baseDamage + sideDamage;
        out.printf("최종 데미지 : %d\n", finalDamage);
        out.println("효과: 다음 턴까지 적의 모든 스탯을 2 감소시킵니다.");

        return new Result(0, finalDamage, true, 0, 6);
    }

    /**
     * 화살 꺾기
     * @param stat 사용할 스탯
     * @param damageTaken 받은 데미지
     * @param consumedArrows 소모할 화살 개수
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result arrowBreak(int stat, int damageTaken, int consumedArrows, PrintStream out) {
        out.println("석궁사수-화살 꺾기 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, 3);

        int reduction = 6 * consumedArrows;
        out.printf("데미지 감소량 : 6 * %d = %d\n", consumedArrows, reduction);

        int finalDamage = Math.max(0, damageTaken - reduction);
        out.printf("최종 받는 데미지 : %d - %d = %d\n", damageTaken, reduction, finalDamage);

        return new Result(finalDamage, 0, true, 0, 3);
    }

    /**
     * 이럴 때 일수록!
     * @param stat 사용할 스탯
     * @param damageTaken 받은 데미지
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result crisisReload(int stat, int damageTaken, PrintStream out) {
        out.println("석궁사수-이럴 때 일수록! 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(damageTaken, 0, false, 0, 9);

        int reloadAmount = damageTaken / 4;
        out.printf("받은 데미지 : %d\n", damageTaken);
        out.printf("장전할 화살 개수 : %d / 4 = %d\n", damageTaken, reloadAmount);

        return new Result(damageTaken, 0, true, 0, 9);
    }
}
