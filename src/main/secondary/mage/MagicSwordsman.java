package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 마검사
 * <p>
 * 판정 사용 스탯 : 지능
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가 수치)%] x (최종 데미지)% x (주사위 보정)
 * <ul>
 *   <li>데미지 증가 수치: 오라 블레이드(+300%), 마나 축적([이전 휴식 마나 - 현재 마나] * 20%)</li>
 *   <li>최종 데미지%: 오버로드(×2), 에테일 솔라(×3) — 곱셈 적용</li>
 *   <li>주사위 보정: 1 + max(0, 판정값) × 0.1</li>
 * </ul>
 */
public class MagicSwordsman {

    // ============================
    // 공격 스킬 (Attack Skills)
    // ============================

    /**
     * 마나 슬래쉬 : 대상에게 3D6의 피해를 입힙니다. (마나 3 소모, 쿨타임 2턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaSlash(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 슬래쉬 사용");
        return skillAttack(stat, 3, 6, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 마나 스트라이크 : 대상에게 2D10의 피해를 입힙니다. (마나 3 소모, 쿨타임 3턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaStrike(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 스트라이크 사용");
        return skillAttack(stat, 2, 10, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 마나 스피어 : 대상에게 1D20의 피해를 입힙니다. (마나 3 소모, 쿨타임 3턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaSpear(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 스피어 사용");
        return skillAttack(stat, 1, 20, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 스핀 크라이스트 : 대상에게 4D8의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result spinChrist(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-스핀 크라이스트 사용");
        return skillAttack(stat, 4, 8, 4, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 트리플 슬레인 : 대상에게 3D12의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result tripleSlain(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-트리플 슬레인 사용");
        return skillAttack(stat, 3, 12, 4, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 에테리얼 임페리오 : 대상에게 3D20의 피해를 입힙니다. (영창 2턴, 마나 6 소모, 쿨타임 7턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음, 영창 삭제)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result etherealImperio(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-에테리얼 임페리오 사용");
        return skillAttack(stat, 3, 20, 6, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    // ============================
    // 버프 / 유틸 스킬
    // ============================

    /**
     * 스피드레인 : 마나를 2D8만큼 회복합니다. (마나 2 소모, 쿨타임 4턴)
     * <p>
     * 판정 성공 시 2D8 마나를 회복합니다. 오라 블레이드 패시브에 의해 마나 소모가 2배가 됩니다.
     * 에테일 솔라 적용 시 마나를 소모하지 않습니다.
     *
     * @param stat        사용할 스탯
     * @param ethailSolar 에테일 솔라 적용 여부 (마나 소모 없음)
     * @param out         출력 스트림
     * @return 결과 객체 (damageDealt = 회복한 마나량, manaUsed = 소모 마나 - 회복 마나)
     */
    public static Result speedDrain(int stat, boolean ethailSolar, PrintStream out) {
        out.println("마검사-스피드레인 사용");

        // 오라 블레이드 패시브: 마나 소모 2배 증가
        int manaUse = ethailSolar ? 0 : 4; // 2 * 2(오라 블레이드)
        if (!ethailSolar) {
            out.println("오라 블레이드 패시브 적용: 마나 소모 2배 증가 (2 → 4)");
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, manaUse, 0);
        }

        int manaRecovered = Main.dice(2, 8, out);
        out.printf("마나 %d 회복%n", manaRecovered);

        // manaUsed = 소모량 - 회복량 (음수이면 순 회복)
        return new Result(0, manaRecovered, true, manaUse - manaRecovered, 0);
    }

    // ============================
    // 전용 수비
    // ============================

    /**
     * 플로우 오라 (전용 수비) : (이전 휴식 시점의 마나 - 현재의 마나) × 2% 만큼 받는 최종 데미지가 감소합니다.
     *
     * @param damageTaken 받은 데미지
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param out         출력 스트림
     * @return 결과 객체 (damageTaken = 최종 받는 피해량)
     */
    public static Result flowAura(int damageTaken, int lastMana, int currentMana, PrintStream out) {
        out.println("마검사-플로우 오라 사용");

        int manaDiff = Math.max(0, lastMana - currentMana);
        double damageReductionRate = manaDiff * 0.02;
        int damageReduced = (int) (damageTaken * damageReductionRate);
        int finalDamageTaken = damageTaken - damageReduced;

        out.printf("플로우 오라 적용: [%d - %d] × 2%% = %.1f%% 받는 최종 데미지 감소%n",
                lastMana, currentMana, damageReductionRate * 100);
        out.printf("감소된 피해량: %d (최종 받는 피해: %d)%n", damageReduced, finalDamageTaken);

        return new Result(finalDamageTaken, 0, true, 0, 0);
    }

    // ============================
    // 기본 공격
    // ============================

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     * <ul>
     *   <li>마나 오라 (패시브): 이전 턴에 마나를 소모했다면 기본 공격 시 마나를 (자신의 레벨)만큼 회복합니다.</li>
     *   <li>오라 블레이드 (패시브): 데미지가 +300% 증가합니다. (기본 공격에는 마나 소모 2배 미적용)</li>
     *   <li>마나 축적 (패시브): [이전 휴식 시점의 마나 - 현재의 마나] × 20% 만큼 데미지가 증가합니다.</li>
     * </ul>
     *
     * @param stat        사용할 스탯
     * @param level       사용자 레벨 (마나 오라: 기본 공격 시 마나를 레벨만큼 회복)
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배)
     * @param precision   정밀 스탯 (치명타 판정)
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

        // flatBonus: (100 + X)% 에 더해지는 퍼센트 보너스
        int flatBonus = 0;
        // finalMultiplier: 최종 데미지 배율 (곱셈)
        double finalMultiplier = 1.0;

        // 마나 축적 패시브: [이전 휴식 시점의 마나 - 현재의 마나] × 20% 만큼 데미지 증가
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            int manaAccumulationBonus = manaDiff * 20;
            out.printf("마나 축적 패시브 적용: [%d - %d] × 20%% = +%d%% 데미지 증가%n",
                    lastMana, currentMana, manaAccumulationBonus);
            flatBonus += manaAccumulationBonus;
        }

        // 오라 블레이드 패시브: 데미지 +300% 증가 (기본 공격에는 마나 소모 2배 미적용)
        out.println("오라 블레이드 패시브 적용: 데미지 +300% 증가");
        flatBonus += 300;

        // 오버로드: 최종 데미지 2배 증가
        if (overload) {
            out.println("오버로드 적용: 최종 데미지 2배 증가");
            finalMultiplier *= 2.0;
        }

        // 에테일 솔라: 최종 데미지 3배 증가
        if (ethailSolar) {
            out.println("에테일 솔라 적용: 최종 데미지 3배 증가");
            finalMultiplier *= 3.0;
        }

        // 주사위 보정: 1 + max(0, 판정값) × 0.1
        double diceModifier = 1.0 + Math.max(0, verdict) * 0.1;
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, diceModifier, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        // 마나 오라 패시브: 이전 턴에 마나를 소모했다면(lastMana > currentMana) 기본 공격 시 마나 회복
        if (lastMana > currentMana) {
            out.printf("마나 오라 패시브 적용: 마나 %d 회복%n", level);
            return new Result(0, damage, true, -level, 0);
        }

        return new Result(0, damage, true, 0, 0);
    }

    // ============================
    // 패시브 — 휴식
    // ============================

    /**
     * 마나 순환 (패시브) : 휴식 시 이전 휴식 이후로 소모한 마나의 50%를 회복합니다.
     *
     * @param manaSpentSinceLastRest 이전 휴식 이후 소모한 총 마나
     * @param out                    출력 스트림
     * @return 회복하는 마나량
     */
    public static int manaCirculation(int manaSpentSinceLastRest, PrintStream out) {
        int manaRecovered = manaSpentSinceLastRest / 2;
        out.printf("마나 순환 패시브 적용: 소모한 마나 %d의 50%% = %d 마나 회복%n",
                manaSpentSinceLastRest, manaRecovered);
        return manaRecovered;
    }

    // ============================
    // 내부 공통 함수
    // ============================

    /**
     * 공격 스킬 공통 데미지 계산 함수.
     * <p>
     * 데미지 공식: [(기본 데미지) × (100 + 데미지 증가 수치)%] × (최종 데미지)% × (주사위 보정)
     *
     * @param stat        사용할 스탯
     * @param dices       주사위 개수
     * @param sides       주사위 면수
     * @param manaUse     기본 마나 소모량 (오라 블레이드/오버로드에 의해 증가됨)
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @return 결과 객체
     */
    private static Result skillAttack(int stat, int dices, int sides, int manaUse,
                                      int lastMana, int currentMana,
                                      boolean overload, boolean ethailSolar, boolean shiftLifter,
                                      int precision, PrintStream out) {
        // 오라 블레이드 패시브: 마나 소모 2배
        manaUse *= 2;

        // 오버로드: 마나 소모 2배 (에테일 솔라 적용 중이 아닐 때)
        if (overload && !ethailSolar) {
            manaUse *= 2;
        }

        // 에테일 솔라: 마나 소모 없음
        if (ethailSolar) {
            manaUse = 0;
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, manaUse, 0);
        }

        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // flatBonus: (100 + X)% 에 더해지는 퍼센트 보너스
        int flatBonus = 0;
        // finalMultiplier: 최종 데미지 배율 (곱셈)
        double finalMultiplier = 1.0;

        // 마나 축적 패시브: [이전 휴식 시점의 마나 - 현재의 마나] × 20% 만큼 데미지 증가
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            int manaAccumulationBonus = manaDiff * 20;
            out.printf("마나 축적 패시브 적용: [%d - %d] × 20%% = +%d%% 데미지 증가%n",
                    lastMana, currentMana, manaAccumulationBonus);
            flatBonus += manaAccumulationBonus;
        }

        // 오라 블레이드 패시브: 데미지 +300% 증가
        out.println("오라 블레이드 패시브 적용: 데미지 +300% 증가. 마나 소모 2배 증가");
        flatBonus += 300;

        // 오버로드: 최종 데미지 2배 증가
        if (overload) {
            out.println("오버로드 적용: 최종 데미지 2배 증가. 마나 소모 2배 증가");
            finalMultiplier *= 2.0;
        }

        // 에테일 솔라: 최종 데미지 3배 증가
        if (ethailSolar) {
            out.println("에테일 솔라 적용: 최종 데미지 3배 증가. 마나 소모 없음");
            finalMultiplier *= 3.0;
        }

        // 주사위 보정: 1 + max(0, 판정값) × 0.1
        double diceModifier = 1.0 + Math.max(0, verdict) * 0.1;
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, diceModifier, out);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        if (shiftLifter) {
            out.printf("쉬프트리스터 스킬 적용: 스킬이 마나 대신 스태미나를 소모하며, 소모한 스태미나만큼 마나를 회복합니다. (스태미나 소모: %d)%n", manaUse);
            return new Result(0, damage, true, -manaUse, manaUse);
        } else {
            return new Result(0, damage, true, manaUse, 0);
        }
    }
}
