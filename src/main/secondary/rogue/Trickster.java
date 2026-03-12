package main.secondary.rogue;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 트릭스터
 * <p>
 * 판정 사용 스탯 : 힘 또는 민첩
 */
public class Trickster {

    /**
     * 페이크 단검: 이번 턴의 공격 데미지가 1.5배로 증가합니다. 대상에게 1D4의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부 (이번 턴 같은 적에게 5회 이상 공격 시 최종 데미지 2배)
     * @param regularCustomer 단골 손님 발동 여부 (지난 턴에 '일점사'가 발동된 적을 공격할 때 최종 데미지 3배)
     * @param fakeDagger 페이크 단검 발동 여부 (공격 데미지 증가 +50%)
     * @param partyTime 파티 타임 발동 여부 (사용 후 최초로 공격한 적에게 가하는 최종 데미지 2배)
     * @param greatScar 거대한 상흔 발동 여부 (최종 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result fakeDagger(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-페이크 단검 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int diceRoll = stat - verdict;

        int damageIncreasePercent = getDamageIncreasePercent(fakeDagger, out);
        double finalDamageMultiplier = getFinalDamageMultiplier(focusedFire, regularCustomer, partyTime, greatScar, out);

        int damage = Main.dice(1, 4, out);
        damage = calculateFinalDamage(damage, damageIncreasePercent, finalDamageMultiplier, stat, "페이크 단검", precision, out, diceRoll);

        out.println("페이크 단검 효과 발동: 이번 턴의 공격 데미지가 1.5배로 증가합니다.");
        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 콩알탄: 대상에게 2D6의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부 (최종 데미지 2배)
     * @param regularCustomer 단골 손님 발동 여부 (최종 데미지 3배)
     * @param fakeDagger 페이크 단검 발동 여부 (데미지 증가 +50%)
     * @param partyTime 파티 타임 발동 여부 (최종 데미지 2배)
     * @param greatScar 거대한 상흔 발동 여부 (최종 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result beanBomb(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-콩알탄 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int diceRoll = stat - verdict;

        int damageIncreasePercent = getDamageIncreasePercent(fakeDagger, out);
        double finalDamageMultiplier = getFinalDamageMultiplier(focusedFire, regularCustomer, partyTime, greatScar, out);

        int damage = Main.dice(2, 6, out);
        damage = calculateFinalDamage(damage, damageIncreasePercent, finalDamageMultiplier, stat, "콩알탄", precision, out, diceRoll);

        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 기름통 투척: 대상에게 1D4의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부 (최종 데미지 2배)
     * @param regularCustomer 단골 손님 발동 여부 (최종 데미지 3배)
     * @param fakeDagger 페이크 단검 발동 여부 (데미지 증가 +50%)
     * @param partyTime 파티 타임 발동 여부 (최종 데미지 2배)
     * @param greatScar 거대한 상흔 발동 여부 (최종 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result oilBarrel(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-기름통 투척 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int diceRoll = stat - verdict;

        int damageIncreasePercent = getDamageIncreasePercent(fakeDagger, out);
        double finalDamageMultiplier = getFinalDamageMultiplier(focusedFire, regularCustomer, partyTime, greatScar, out);

        int damage = Main.dice(1, 4, out);
        damage = calculateFinalDamage(damage, damageIncreasePercent, finalDamageMultiplier, stat, "기름통 투척", precision, out, diceRoll);

        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 라이터 투척: 대상에게 1D8의 피해를 입힙니다. 이번 턴에 기름통을 적중시켰다면 3D6의 추가 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param oilHit 기름통 적중 여부
     * @param focusedFire 일점사 발동 여부 (최종 데미지 2배)
     * @param regularCustomer 단골 손님 발동 여부 (최종 데미지 3배)
     * @param fakeDagger 페이크 단검 발동 여부 (데미지 증가 +50%)
     * @param partyTime 파티 타임 발동 여부 (최종 데미지 2배)
     * @param greatScar 거대한 상흔 발동 여부 (최종 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result lighterThrow(int stat, boolean oilHit, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-라이터 투척 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int diceRoll = stat - verdict;

        int damageIncreasePercent = getDamageIncreasePercent(fakeDagger, out);
        double finalDamageMultiplier = getFinalDamageMultiplier(focusedFire, regularCustomer, partyTime, greatScar, out);

        int damage = Main.dice(1, 8, out);
        if (oilHit) {
            out.println("기름통 적중 확인: 3D6 추가 데미지");
            damage += Main.dice(3, 6, out);
        }
        damage = calculateFinalDamage(damage, damageIncreasePercent, finalDamageMultiplier, stat, "라이터 투척", precision, out, diceRoll);

        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 특대형 단검: 대상에게 1D20의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부 (최종 데미지 2배)
     * @param regularCustomer 단골 손님 발동 여부 (최종 데미지 3배)
     * @param fakeDagger 페이크 단검 발동 여부 (데미지 증가 +50%)
     * @param partyTime 파티 타임 발동 여부 (최종 데미지 2배)
     * @param greatScar 거대한 상흔 발동 여부 (최종 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result xlDagger(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-특대형 단검 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int diceRoll = stat - verdict;

        int damageIncreasePercent = getDamageIncreasePercent(fakeDagger, out);
        double finalDamageMultiplier = getFinalDamageMultiplier(focusedFire, regularCustomer, partyTime, greatScar, out);

        int damage = Main.dice(1, 20, out);
        damage = calculateFinalDamage(damage, damageIncreasePercent, finalDamageMultiplier, stat, "특대형 단검", precision, out, diceRoll);

        return new Result(0, damage, true, 0, 3);
    }
    /**
     * 대기 시간 (전용 수비): 자신만을 공격 대상으로 하는 '기술'을 적이 사용 시 사용할 수 있습니다.
     * 기본 공격 발동 성공 이후 1D6 ≥ 3이라면 적의 기술을 취소합니다.
     * @param stat 판정에 사용할 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result waitingTime(int stat, PrintStream out) {
        out.println("트릭스터-대기 시간 사용 (전용 수비)");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            out.println("대기 시간 발동 실패 (판정 실패)");
            return new Result(0, 0, false, 0, 0);
        }

        int check = Main.dice(1, 6, out);
        if (check >= 3) {
            out.println("대기 시간 성공: 적의 기술 취소 (주사위 " + check + " >= 3)");
            return new Result(0, 0, true, 0, 0);
        } else {
            out.println("대기 시간 실패: (주사위 " + check + " < 3)");
            return new Result(0, 0, false, 0, 0);
        }
    }

    /**
     * 기본공격: 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param suddenEvent 이번 턴에 사용한 기술 횟수 (돌발 이벤트 : suddenEvent * 50% 만큼 기본 공격 데미지가 증가)
     * @param focusedFire 일점사 발동 여부 (이번 턴 같은 적에게 5회 이상 공격 시 최종 데미지 2배)
     * @param regularCustomer 지난 턴에 '일점사'가 발동된 적을 공격할 때 최종 데미지가 3배로 증가
     * @param fakeDagger 공격 데미지 증가 +50%
     * @param partyTime 사용 후 최초로 공격한 적에게 가하는 최종 데미지가 그 다음 턴까지 2배로 증가
     * @param eventPreparation 다음 턴 '돌발 이벤트'의 데미지 증가 효과가 기술 사용당 100%로 증가
     * @param greatScar 최종 데미지 2배로 증가
     * @param mainEvent 난사 조건이 1 x (공격 시행 횟수)로 변경, 기본 공격 최대 10회 제한 제거
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int suddenEvent, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean eventPreparation, boolean greatScar, boolean mainEvent, int precision, PrintStream out) {
        out.println("트릭스터-기본공격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int diceRoll = stat - verdict;
        int count = 0;
        int rapidFireVerdict = mainEvent ? 1 : 3;

        // 데미지 증가 (additive %): 돌발 이벤트 + 페이크 단검 효과
        int damageIncreasePercent = suddenEvent * (eventPreparation ? 100 : 50);
        if (eventPreparation) {
            out.println("이벤트 준비 발동: 돌발 이벤트 데미지 증가 효과 기술 사용당 100% 증가");
            out.println("기본 공격 데미지 증가 " + (suddenEvent * 100) + "%");
        } else {
            out.println("기본 공격 데미지 증가 " + (suddenEvent * 50) + "%");
        }
        if (fakeDagger) {
            damageIncreasePercent += 50;
            out.println("페이크 단검 발동: 데미지 증가 +50%");
        }

        // 최종 데미지 배율 (multiplicative)
        double finalDamageMultiplier = getFinalDamageMultiplier(focusedFire, regularCustomer, partyTime, greatScar, out);

        out.println("기본 공격 데미지 증가 합계: " + damageIncreasePercent + "%, 최종 데미지 배율: " + finalDamageMultiplier + "배");
        int totalFinalDamage = 0;
        while (true) {
            int damage = Main.dice(1, 6, out);
            count++;
            out.println("기본 공격 " + count + "회차: " + damage + "데미지");
            totalFinalDamage += calculateFinalDamage(damage, damageIncreasePercent, finalDamageMultiplier, stat, "기본 공격", precision, out, diceRoll);
            int rapidFireCheck = stat - Main.dice(1, 20, out);
            int currentVerdict = rapidFireVerdict * count;
            if (rapidFireCheck >= currentVerdict) {
                out.println("난사 발동: 추가 공격 가능 (난사 판정치 " + currentVerdict + " 달성)");
                diceRoll = rapidFireCheck;
                if (!mainEvent && count >= 10) {
                    out.println("기본 공격 최대 횟수 도달: 10회");
                    break;
                }
            } else {
                out.println("난사 미발동: 추가 공격 불가 (난사 판정치 " + currentVerdict + " 미달성)");
                break;
            }
        }
        return new Result(0, totalFinalDamage, true, 0, 0);
    }

    /**
     * 데미지 증가 (additive %) 계산 - 페이크 단검 버프 활성 여부에 따른 추가 %
     * 최종 공식: [(기본 데미지) * (100 + 데미지 증가)%] 에서 사용
     */
    private static int getDamageIncreasePercent(boolean fakeDagger, PrintStream out) {
        int percent = 0;
        if (fakeDagger) {
            percent += 50;
            out.println("페이크 단검 발동: 데미지 증가 +50%");
        }
        return percent;
    }

    /**
     * 최종 데미지 배율 (multiplicative) 계산
     * 최종 공식: [...] * (최종 데미지)% 에서 사용
     * 일점사 x2, 단골 손님 x3, 파티 타임 x2, 거대한 상흔 x2
     */
    private static double getFinalDamageMultiplier(boolean focusedFire, boolean regularCustomer, boolean partyTime, boolean greatScar, PrintStream out) {
        double multiplier = 1.0;
        if (focusedFire) {
            multiplier *= 2.0;
            out.println("일점사 발동: 최종 데미지 2배 증가");
        }
        if (regularCustomer) {
            multiplier *= 3.0;
            out.println("단골 손님 발동: 최종 데미지 3배 증가");
        }
        if (partyTime) {
            multiplier *= 2.0;
            out.println("파티 타임 발동: 최종 데미지 2배 증가");
        }
        if (greatScar) {
            multiplier *= 2.0;
            out.println("거대한 상흔 발동: 최종 데미지 2배 증가");
        }
        return multiplier;
    }

    /**
     * 최종 데미지 계산 공식:
     * [(기본 데미지) * (100 + 데미지 증가)%] * (최종 데미지 배율)% * (주사위 보정)
     *
     * @param rawDamage            기본 주사위 데미지
     * @param damageIncreasePercent 데미지 증가 합계 (additive %)
     * @param finalDamageMultiplier 최종 데미지 배율 (multiplicative, e.g. 2.0 = 200%)
     * @param stat                 판정 스탯 (주사위 보정에 사용)
     * @param skillName            스킬 이름 (로그 출력용)
     * @param precision            정밀 스탯 (치명타 판정에 사용)
     * @param out                  출력 스트림
     * @param diceRoll             판정 주사위 값 (주사위 보정에 사용)
     * @return 최종 데미지
     */
    private static int calculateFinalDamage(int rawDamage, int damageIncreasePercent, double finalDamageMultiplier, int stat, String skillName, int precision, PrintStream out, int diceRoll) {
        // Step 1: [(기본 데미지) * (100 + 데미지 증가)%]
        int afterIncrease = (int)(rawDamage * (100 + damageIncreasePercent) / 100.0);
        out.printf("[%s] 기본 데미지 %d * (100 + %d) / 100 = %d%n", skillName, rawDamage, damageIncreasePercent, afterIncrease);

        // Step 2: * (최종 데미지 배율)%
        int afterFinalMultiplier = (int)(afterIncrease * finalDamageMultiplier);
        out.printf("[%s] 데미지 증가 후 %d * %.2f배 = %d%n", skillName, afterIncrease, finalDamageMultiplier, afterFinalMultiplier);

        // Step 3: * (주사위 보정) — sideDamage
        int sideDamage = Main.sideDamage(afterFinalMultiplier, stat, out, diceRoll);
        int afterSide = afterFinalMultiplier + sideDamage;
        out.printf("[%s] 주사위 보정 %d + %d = %d%n", skillName, afterFinalMultiplier, sideDamage, afterSide);

        // Step 4: 치명타 판정
        int finalDmg = Main.criticalHit(precision, afterSide, out);
        out.printf("[%s] 최종 데미지: %d%n", skillName, finalDmg);
        return finalDmg;
    }
}
