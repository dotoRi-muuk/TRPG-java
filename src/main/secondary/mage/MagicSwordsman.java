package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 마검사
 * <p>
 * 판정 사용 스탯 : 지능
 */
public class MagicSwordsman {

    /**
     * 마나 슬래쉬 : 대상에게 3D6의 피해를 입힙니다. (마나 3 소모, 쿨타임 2턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나를 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaSlash(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 슬래쉬 사용");
        return basicAttack(stat, 3, 6, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 마나 스트라이크 : 대상에게 2D10의 피해를 입힙니다. (마나 3 소모, 쿨타임 3턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaStrike(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 스트라이크 사용");
        return basicAttack(stat, 2, 10, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 마나 스피어 : 대상에게 D20의 피해를 입힙니다. (마나 3 소모, 쿨타임 3턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaSphere(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 스피어 사용");
        return basicAttack(stat, 1, 20, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 스핀 크라이스트 : 대상에게 4D8의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result spinChrist(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-스핀 크라이스트 사용");
        return basicAttack(stat, 4, 8, 4, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 트리플 슬레인 : 대상에게 3D12의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result tripleSlain(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-트리플 슬레인 사용");
        return basicAttack(stat, 3, 12, 4, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 에테리얼 임페리오 : 대상에게 3D20의 피해를 입힙니다. (영창 2턴, 마나 6 소모, 쿨타임 7턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result etherealImperio(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-에테리얼 임페리오 사용");
        return basicAttack(stat, 3, 20, 6, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 데미지만 다르고 로직이 같은 스킬의 데미지를 계산하는 공통 함수
     *
     * @param stat        사용할 스탯
     * @param dices       스킬의 주사위 개수
     * @param sides       스킬의 주사위 면수
     * @param manaUse     스킬의 마나 소모량
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    private static Result basicAttack(int stat, int dices, int sides, int manaUse, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, manaUse, 0);
        }

        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        double damageMultiplier = 1.0;

        // 마나 축적 패시브
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            double manaAccumulationBonus = manaDiff * 0.1;
            out.printf("마나 축적 패시브 적용: [이전 휴식 시점의 마나 - 현재의 마나] * 10%% 만큼 데미지 증가 (+%.1f)\n", manaAccumulationBonus);
            damageMultiplier += manaAccumulationBonus;
        }

        //오라 블레이드 패시브
        out.println("오라 블레이드 패시브 적용: 데미지 3배 증가. 마나 소모 2배 증가");
        damageMultiplier *= 3.0;
        manaUse *= 2;

        // 오버로드 패시브
        if (overload) {
            out.println("오버로드 패시브 적용: 데미지 1.5배 증가. 마나 소모 2배 증가");
            damageMultiplier *= 1.5;
            manaUse *= 2;
        }

        // 에테일 솔라 패시브
        if (ethailSolar) {
            out.println("에테일 솔라 패시브 적용: 이번 턴의 데미지가 3배로 증가. 모든 스킬이 마나를 소모하지 않음");
            damageMultiplier *= 3.0;
            manaUse = 0;
        }

        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        if (shiftLifter) {
            out.printf("쉬프트리스터 스킬 적용: 스킬이 마나 대신 스태미나를 소모하며, 소모한 스태미나만큼 마나를 회복합니다. (스태미나 소모: %d)\n", manaUse);
            return new Result(0, damage, true, -manaUse, manaUse);
        } else {
            return new Result(0, damage, true, manaUse, 0);
        }

    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat        사용할 스탯
     * @param level       사용자 레벨 (마나 오라 : 기본 공격 시 마나를 (자신의 레벨) 회복)
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 10% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 데미지가 1.5배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴의 데미지가 3배로 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int level, int lastMana, int currentMana, boolean overload, boolean ethailSolar, int precision, PrintStream out) {
        out.println("마검사-기본 공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        double damageMultiplier = 1.0;

        // 마나 축적 패시브
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            double manaAccumulationBonus = manaDiff * 0.1;
            out.printf("마나 축적 패시브 적용: [이전 휴식 시점의 마나 - 현재의 마나] * 10%% 만큼 데미지 증가 (+%.1f)\n", manaAccumulationBonus);
            damageMultiplier += manaAccumulationBonus;
        }

        // 오버로드 패시브
        if (overload) {
            out.println("오버로드 패시브 적용: 데미지 1.5배 증가");
            damageMultiplier *= 1.5;
        }

        // 에테일 솔라 패시브
        if (ethailSolar) {
            out.println("에테일 솔라 패시브 적용: 이번 턴의 데미지가 3배로 증가");
            damageMultiplier *= 3.0;
        }

        //오라 블레이드 패시브
        out.println("오라 블레이드 패시브 적용: 데미지 3배 증가");
        damageMultiplier *= 3.0;

        int damage = (int) Math.round(baseDamage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);

        // 마나 오라 패시브
        out.printf("마나 오라 패시브 적용: 마나 %d 회복%n", level);
        return new Result(0, damage, true, -level, 0);
    }

}
