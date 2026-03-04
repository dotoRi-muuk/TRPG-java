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
     * 페이크 단검: 다음 턴의 공격 데미지가 1.5배로 증가합니다. 대상에게 1D4의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부 (이번 턴 같은 적에게 5회 이상 공격 시 데미지 2배)
     * @param regularCustomer 단골 손님 발동 여부 (지난 턴에 '일점사'가 발동된 적을 공격할 때 데미지 1.5배)
     * @param fakeDagger 페이크 단검 발동 여부 (공격 데미지 1.5배)
     * @param partyTime 파티 타임 발동 여부 (사용 후 최초로 공격한 적에게 가하는 피해 2배)
     * @param greatScar 거대한 상흔 발동 여부 (공격 데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result fakeDagger(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-페이크 단검 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();

        double modifier = getPassiveModifier(focusedFire, regularCustomer, fakeDagger, partyTime, greatScar, out);
        out.println("페이크 단검 데미지 최종 보정치: " + modifier + "배");

        int damage = Main.dice(1, 4, out);
        damage = calculateFinalDamage(damage, modifier, stat, "페이크 단검", precision, out);

        out.println("페이크 단검 효과 발동: 다음 턴의 공격 데미지가 1.5배로 증가합니다.");
        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 콩알탄: 대상에게 2D6의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부
     * @param regularCustomer 단골 손님 발동 여부
     * @param fakeDagger 페이크 단검 발동 여부
     * @param partyTime 파티 타임 발동 여부
     * @param greatScar 거대한 상흔 발동 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result beanBomb(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-콩알탄 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();

        double modifier = getPassiveModifier(focusedFire, regularCustomer, fakeDagger, partyTime, greatScar, out);
        out.println("콩알탄 데미지 최종 보정치: " + modifier + "배");

        int damage = Main.dice(2, 6, out);
        damage = calculateFinalDamage(damage, modifier, stat, "콩알탄", precision, out);

        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 기름통 투척: 대상에게 1D4의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부
     * @param regularCustomer 단골 손님 발동 여부
     * @param fakeDagger 페이크 단검 발동 여부
     * @param partyTime 파티 타임 발동 여부
     * @param greatScar 거대한 상흔 발동 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result oilBarrel(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-기름통 투척 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();

        double modifier = getPassiveModifier(focusedFire, regularCustomer, fakeDagger, partyTime, greatScar, out);
        out.println("기름통 투척 데미지 최종 보정치: " + modifier + "배");

        int damage = Main.dice(1, 4, out);
        damage = calculateFinalDamage(damage, modifier, stat, "기름통 투척", precision, out);

        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 라이터 투척: 대상에게 1D8의 피해를 입힙니다. 이번 턴에 기름통을 적중시켰다면 3D6의 추가 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param oilHit 기름통 적중 여부
     * @param focusedFire 일점사 발동 여부
     * @param regularCustomer 단골 손님 발동 여부
     * @param fakeDagger 페이크 단검 발동 여부
     * @param partyTime 파티 타임 발동 여부
     * @param greatScar 거대한 상흔 발동 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result lighterThrow(int stat, boolean oilHit, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-라이터 투척 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();

        double modifier = getPassiveModifier(focusedFire, regularCustomer, fakeDagger, partyTime, greatScar, out);
        out.println("라이터 투척 데미지 최종 보정치: " + modifier + "배");

        int damage = Main.dice(1, 8, out);
        if (oilHit) {
            out.println("기름통 적중 확인: 3D6 추가 데미지");
            damage += Main.dice(3, 6, out);
        }
        damage = calculateFinalDamage(damage, modifier, stat, "라이터 투척", precision, out);

        return new Result(0, damage, true, 0, 2);
    }

    /**
     * 특대형 단검: 대상에게 1D20의 피해를 입힙니다.
     * @param stat 판정에 사용할 스탯
     * @param focusedFire 일점사 발동 여부
     * @param regularCustomer 단골 손님 발동 여부
     * @param fakeDagger 페이크 단검 발동 여부
     * @param partyTime 파티 타임 발동 여부
     * @param greatScar 거대한 상흔 발동 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result xlDagger(int stat, boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, int precision, PrintStream out) {
        out.println("트릭스터-특대형 단검 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();

        double modifier = getPassiveModifier(focusedFire, regularCustomer, fakeDagger, partyTime, greatScar, out);
        out.println("특대형 단검 데미지 최종 보정치: " + modifier + "배");

        int damage = Main.dice(1, 20, out);
        damage = calculateFinalDamage(damage, modifier, stat, "특대형 단검", precision, out);

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
     * @param regularCustomer 지난 턴에 '일점사'가 발동된 적을 공격할 때 데미지가 1.5배로 증가
     * @param fakeDagger 공격 데미지가 1.5배로 증가
     * @param partyTime 사용 후 최초로 공격한 적에게 가하는 피해가 그 다음 턴까지 2배로 증가
     * @param eventPreparation 다음 턴 '돌발 이벤트'의 데미지 증가 효과가 기술 사용당 100%로 증가
     * @param greatScar 공격 데미지가 2배로 증가
     * @param mainEvent 난사 조건이 1 x (공격 시행 횟수)로 변경, 기본 공격 최대 10회 제한 제거
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int suddenEvent, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean eventPreparation, boolean greatScar, boolean mainEvent, int precision, PrintStream out) {
        out.println("트릭스터-기본공격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result();
        int count = 0;
        double modifier = 1.0;
        int rapidFireVerdict = mainEvent ? 1 : 3;
        modifier += suddenEvent * (eventPreparation ? 1.0 : 0.5);
        if (eventPreparation) {
            out.println("이벤트 준비 발동: 돌발 이벤트 데미지 증가 효과 기술 사용당 100% 증가");
            out.println("기본 공격 데미지 " + (suddenEvent * 100) + "% 증가");
        }
        else {
            out.println("기본 공격 데미지 " + (suddenEvent * 50) + "% 증가");
        }
        if (regularCustomer) {
            modifier *= 1.5;
            out.println("단골 손님 발동: 데미지 1.5배 증가");
        }
        if (fakeDagger) {
            modifier *= 1.5;
            out.println("페이크 단검 발동: 데미지 1.5배 증가");
        }
        if (partyTime) {
            modifier *= 2.0;
            out.println("파티 타임 발동: 데미지 2배 증가");
        }
        if (greatScar) {
            modifier *= 2.0;
            out.println("거대한 상흔 발동: 데미지 2배 증가");
        }
        out.println("기본 공격 데미지 최종 보정치: " + modifier + "배");
        int finalDamage = 0;
        while (true) {
            int damage = Main.dice(1, 6, out);
            finalDamage += damage;
            count++;
            out.println("기본 공격 " + count + "회차: " + damage + "데미지");
            int rapidFireCheck = stat - Main.dice(1, 20, out);
            int currentVerdict = rapidFireVerdict * count;
            if (rapidFireCheck >= currentVerdict) {
                out.println("난사 발동: 추가 공격 가능 (난사 판정치 " + currentVerdict + " 달성)");
                if (!mainEvent && count >= 10) {
                    out.println("기본 공격 최대 횟수 도달: 10회");
                    break;
                }
            } else {
                out.println("난사 미발동: 추가 공격 불가 (난사 판정치 " + currentVerdict + " 미달성)");
                break;
            }
        }
        finalDamage = calculateFinalDamage(finalDamage, modifier, stat, "기본 공격", precision, out);
        return new Result(0, finalDamage, true, 0, 0);
    }

    private static double getPassiveModifier(boolean focusedFire, boolean regularCustomer, boolean fakeDagger, boolean partyTime, boolean greatScar, PrintStream out) {
        double modifier = 1.0;
        if (focusedFire) {
            modifier *= 2.0;
            out.println("일점사 발동: 데미지 2배 증가");
        }
        if (regularCustomer) {
            modifier *= 1.5;
            out.println("단골 손님 발동: 데미지 1.5배 증가");
        }
        if (fakeDagger) {
            modifier *= 1.5;
            out.println("페이크 단검 발동: 데미지 1.5배 증가");
        }
        if (partyTime) {
            modifier *= 2.0;
            out.println("파티 타임 발동: 데미지 2배 증가");
        }
        if (greatScar) {
            modifier *= 2.0;
            out.println("거대한 상흔 발동: 데미지 2배 증가");
        }
        return modifier;
    }

    private static int calculateFinalDamage(int rawDamage, double modifier, int stat, String skillName, int precision, PrintStream out) {
        int damage = (int)(rawDamage * modifier);
        out.println("배율 적용 " + skillName + " 데미지: " + damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.println("데미지 보정치: " + sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.println("최종 데미지: " + damage);
        return damage;
    }
}
