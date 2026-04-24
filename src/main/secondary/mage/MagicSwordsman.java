package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 마검사
 * <p>
 * 판정 사용 스탯 : 지능
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가율)%] x (최종 데미지)% x (주사위 보정)
 * <p>
 * 패시브:
 * - 마나 오라: 이전 턴에 마나를 소모했다면 기본 공격 시 마나를 (자신의 레벨) 회복합니다.
 * - 오라 블레이드: 모든 스킬이 마나를 2배 소모합니다. 데미지가 300% 증가합니다.
 * - 마나 순환: 휴식 시 이전 휴식 이후로 소모한 마나의 50%를 회복합니다.
 * - 마나 축적: [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지가 증가합니다.
 */
public class MagicSwordsman {

    /**
     * 마나 슬래쉬 : 대상에게 3D6의 피해를 입힙니다. (마나 3 소모, 쿨타임 2턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 최종 데미지 2배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴 최종 데미지 3배 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나를 회복)
     * @param precision   정밀 스탯
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
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 최종 데미지 2배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴 최종 데미지 3배 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯
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
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 최종 데미지 2배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴 최종 데미지 3배 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result manaSphere(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-마나 스피어 사용");
        return skillAttack(stat, 1, 20, 3, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 스핀 크라이스트 : 대상에게 4D8의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     *
     * @param stat        사용할 스탯
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 최종 데미지 2배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴 최종 데미지 3배 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯
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
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 최종 데미지 2배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴 최종 데미지 3배 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯
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
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param currentMana 현재 마나 (마나 축적 : [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가)
     * @param overload    오버로드 적용 여부 (모든 스킬이 마나를 2배 소모, 최종 데미지 2배 증가)
     * @param ethailSolar 에테일 솔라 적용 여부 (이번 턴 최종 데미지 3배 증가, 모든 스킬이 마나를 소모하지 않음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (스킬이 마나 대신 스태미나를 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result etherealImperio(int stat, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        out.println("마검사-에테리얼 임페리오 사용");
        return skillAttack(stat, 3, 20, 6, lastMana, currentMana, overload, ethailSolar, shiftLifter, precision, out);
    }

    /**
     * 스피드레인 : 마나를 2D8만큼 회복합니다. (마나 2 소모, 쿨타임 4턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체 (manaUsed = 2 - 회복량, 음수이면 순수 회복)
     */
    public static Result speedDrain(int stat, PrintStream out) {
        out.println("마검사-스피드레인 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 2, 0);
        }

        int manaRecovered = Main.dice(2, 8, out);
        out.printf("마나 %d 회복 (소모 2, 순수 회복 %d)%n", manaRecovered, manaRecovered - 2);
        return new Result(0, 0, true, 2 - manaRecovered, 0);
    }

    /**
     * 플로우 오라 (전용 수비) :
     * (이전 휴식 시점의 마나 - 현재의 마나) * 2% 만큼 받는 최종 데미지가 감소합니다.
     *
     * @param manaSpentSinceRest 이전 휴식 이후로 소모한 마나 (이전 휴식 시점의 마나 - 현재의 마나)
     * @param damageTaken        받은 데미지
     * @param out                출력 스트림
     * @return 결과 객체 (damageTaken = 감소된 데미지)
     */
    public static Result flowAura(int manaSpentSinceRest, int damageTaken, PrintStream out) {
        out.println("마검사-플로우 오라 (전용 수비) 사용");

        double reductionRate = manaSpentSinceRest * 0.02;
        int reducedDamage = (int) (damageTaken * reductionRate);
        int finalDamage = damageTaken - reducedDamage;
        out.printf("플로우 오라 적용: (이전 휴식 이후 소모 마나 %d) * 2%% = %.1f%% 피해 감소%n",
                manaSpentSinceRest, reductionRate * 100);
        out.printf("받는 피해: %d → %d (%d 감소)%n", damageTaken, finalDamage, reducedDamage);
        return new Result(finalDamage, 0, true, 0, 0);
    }

    /**
     * 스킬 공격 공통 함수
     * <p>
     * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가율)%] x (최종 데미지)% x (주사위 보정)
     * <p>
     * - (100 + 데미지 증가율)%: 마나 축적(+manaDiff*20%), 오라 블레이드(+300%)
     * - (최종 데미지)%: 오버로드(x2), 에테일 솔라(x3)
     * - (주사위 보정): sideDamage (판정 주사위 재사용)
     *
     * @param stat        사용할 스탯
     * @param dices       스킬의 주사위 개수
     * @param sides       스킬의 주사위 면수
     * @param manaUse     스킬의 기본 마나 소모량
     * @param lastMana    이전 휴식 시점의 마나
     * @param currentMana 현재 마나
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    private static Result skillAttack(int stat, int dices, int sides, int manaUse, int lastMana, int currentMana, boolean overload, boolean ethailSolar, boolean shiftLifter, int precision, PrintStream out) {
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, manaUse, 0);
        }
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // (100 + 데미지 증가율)% 구성 — 덧셈 퍼센트 보정
        int flatBonus = 0;

        // 마나 축적 패시브: [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            int manaAccumulationBonus = manaDiff * 20;
            out.printf("마나 축적 패시브 적용: [이전 휴식 시점의 마나 - 현재의 마나] * 20%% 만큼 데미지 증가 (+%d%%)%n",
                    manaAccumulationBonus);
            flatBonus += manaAccumulationBonus;
        }

        // 오라 블레이드 패시브: 데미지 +300%, 마나 소모 2배
        out.println("오라 블레이드 패시브 적용: 데미지 +300%. 마나 소모 2배 증가");
        flatBonus += 300;
        manaUse *= 2;

        // (최종 데미지)% 구성 — 곱셈 배율 보정
        double finalMultiplier = 1.0;

        // 오버로드: 최종 데미지 2배, 마나 소모 2배
        if (overload) {
            out.println("오버로드 적용: 최종 데미지 2배. 마나 소모 2배 증가");
            finalMultiplier *= 2.0;
            manaUse *= 2;
        }

        // 에테일 솔라: 최종 데미지 3배, 마나 소모 없음
        if (ethailSolar) {
            out.println("에테일 솔라 적용: 최종 데미지 3배. 마나 소모 없음");
            finalMultiplier *= 3.0;
            manaUse = 0;
        }

        // [(기본 데미지) x (100 + 데미지 증가율)%] x (최종 데미지)%
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out);

        // 주사위 보정 (판정 주사위 재사용)
        int sideDamage = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        if (shiftLifter) {
            out.printf("쉬프트리스터 스킬 적용: 스킬이 마나 대신 스태미나를 소모하며, 소모한 스태미나만큼 마나를 회복합니다. (스태미나 소모: %d)%n", manaUse);
            return new Result(0, damage, true, -manaUse, manaUse);
        } else {
            return new Result(0, damage, true, manaUse, 0);
        }
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     * <p>
     * 마나 오라 패시브: 이전 턴에 마나를 소모했다면 기본 공격 시 마나를 (자신의 레벨) 회복합니다.
     * <p>
     * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가율)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param stat             사용할 스탯
     * @param level            사용자 레벨 (마나 오라 : 기본 공격 시 마나를 (자신의 레벨) 회복)
     * @param lastMana         이전 휴식 시점의 마나 (마나 축적 패시브용)
     * @param currentMana      현재 마나 (마나 축적 패시브용)
     * @param manaUsedLastTurn 이전 턴에 마나를 소모했는지 여부 (마나 오라 패시브 조건)
     * @param overload         오버로드 적용 여부 (최종 데미지 2배)
     * @param ethailSolar      에테일 솔라 적용 여부 (최종 데미지 3배)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int level, int lastMana, int currentMana, boolean manaUsedLastTurn, boolean overload, boolean ethailSolar, int precision, PrintStream out) {
        out.println("마검사-기본 공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 0, 0);
        }
        int diceRoll = stat - verdict;

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // (100 + 데미지 증가율)% 구성 — 덧셈 퍼센트 보정
        int flatBonus = 0;

        // 마나 축적 패시브: [이전 휴식 시점의 마나 - 현재의 마나] * 20% 만큼 데미지 증가
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            int manaAccumulationBonus = manaDiff * 20;
            out.printf("마나 축적 패시브 적용: [이전 휴식 시점의 마나 - 현재의 마나] * 20%% 만큼 데미지 증가 (+%d%%)%n",
                    manaAccumulationBonus);
            flatBonus += manaAccumulationBonus;
        }

        // 오라 블레이드 패시브: 데미지 +300% (기본 공격은 마나 소모 없음)
        out.println("오라 블레이드 패시브 적용: 데미지 +300%");
        flatBonus += 300;

        // (최종 데미지)% 구성 — 곱셈 배율 보정
        double finalMultiplier = 1.0;

        // 오버로드: 최종 데미지 2배
        if (overload) {
            out.println("오버로드 적용: 최종 데미지 2배");
            finalMultiplier *= 2.0;
        }

        // 에테일 솔라: 최종 데미지 3배
        if (ethailSolar) {
            out.println("에테일 솔라 적용: 최종 데미지 3배");
            finalMultiplier *= 3.0;
        }

        // [(기본 데미지) x (100 + 데미지 증가율)%] x (최종 데미지)%
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out);

        // 주사위 보정 (판정 주사위 재사용)
        int sideDamage = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        // 마나 오라 패시브: 이전 턴에 마나를 소모했다면 마나 회복
        if (manaUsedLastTurn) {
            out.printf("마나 오라 패시브 적용: 이전 턴 마나 소모 → 마나 %d 회복%n", level);
            return new Result(0, damage, true, -level, 0);
        } else {
            return new Result(0, damage, true, 0, 0);
        }
    }

    /**
     * 오라 블레이드 [에클레트] : [오라 블레이드]의 효과를 대신합니다.
     * 공격 시 (진행 턴 수)D12의 추가 데미지를 가합니다. 휴식 시 취소됩니다.
     * (매턴 마나 3 소모)
     *
     * @param activeTurns 현재 에클레트 진행 턴 수 (0이면 첫 활성화)
     * @param out         출력 스트림
     * @return 결과 객체 (마나 3 소모)
     */
    public static Result auraBladEclat(int activeTurns, PrintStream out) {
        out.println("마검사-오라 블레이드 [에클레트] 사용");
        out.println("오라 블레이드의 효과를 대신합니다. 공격 시 (진행 턴 수)D12 추가 데미지 부여.");
        out.printf("현재 진행 턴 수: %d → 다음 공격에 %dD12 추가 데미지 적용.%n", activeTurns, activeTurns);
        out.println("매턴 마나 3 소모. 휴식 시 효과 취소됩니다.");
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 에클레어 도미니아 : 오라 블레이드 [에클레트] 발동 중에 사용 가능합니다.
     * 6D8의 피해를 입힙니다. 오라 블레이드 [에클레트]의 효과를 4회 발동합니다.
     *
     * @param stat        사용할 스탯
     * @param activeTurns 에클레트 진행 턴 수 (각 회당 추가 (activeTurns)D12 데미지)
     * @param lastMana    이전 휴식 시점의 마나 (마나 축적 패시브용)
     * @param currentMana 현재 마나 (마나 축적 패시브용)
     * @param overload    오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배)
     * @param ethailSolar 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음)
     * @param shiftLifter 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result eclaireDominia(int stat, int activeTurns,
                                         int lastMana, int currentMana,
                                         boolean overload, boolean ethailSolar, boolean shiftLifter,
                                         int precision, PrintStream out) {
        out.println("마검사-에클레어 도미니아 사용");
        if (activeTurns <= 0) {
            out.println("[오라 블레이드 에클레트]가 활성화되어 있지 않아 사용할 수 없습니다.");
            return new Result(0, 0, false, 0, 0);
        }

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            return new Result(0, 0, false, 3 * (overload ? 2 : 1), 0);
        }
        int diceRoll = stat - verdict;

        // 6D8 기본 피해
        int baseDamage = Main.dice(6, 8, out);
        out.printf("6D8 기본 데미지: %d%n", baseDamage);

        // 오라 블레이드 [에클레트] 효과 4회 발동: 각 회당 (activeTurns)D12 추가 데미지
        int eclatBonus = 0;
        for (int i = 1; i <= 4; i++) {
            int bonus = Main.dice(activeTurns, 12, out);
            out.printf("에클레트 효과 %d회차: %dD12 = %d%n", i, activeTurns, bonus);
            eclatBonus += bonus;
        }
        out.printf("에클레트 4회 발동 추가 데미지 합계: %d%n", eclatBonus);
        baseDamage += eclatBonus;
        out.printf("기본 데미지 합계 (6D8 + 에클레트 4회): %d%n", baseDamage);

        // (100 + 데미지 증가율)% 구성
        int flatBonus = 0;
        if (lastMana > currentMana) {
            int manaDiff = lastMana - currentMana;
            int manaAccumulationBonus = manaDiff * 20;
            out.printf("마나 축적 패시브 적용: [이전 마나 - 현재 마나] * 20%% = +%d%%%n", manaAccumulationBonus);
            flatBonus += manaAccumulationBonus;
        }
        out.println("오라 블레이드 패시브 적용: 데미지 +300%. 마나 소모 2배 증가");
        flatBonus += 300;
        int manaUse = 3 * 2; // 오라 블레이드 마나 소모 2배

        // (최종 데미지)% 구성
        double finalMultiplier = 1.0;
        if (overload) {
            out.println("오버로드 적용: 최종 데미지 2배. 마나 소모 2배 증가");
            finalMultiplier *= 2.0;
            manaUse *= 2;
        }
        if (ethailSolar) {
            out.println("에테일 솔라 적용: 최종 데미지 3배. 마나 소모 없음");
            finalMultiplier *= 3.0;
            manaUse = 0;
        }

        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out);
        int sideDmg = Main.sideDamage(damage, stat, out, diceRoll);
        damage += sideDmg;
        out.printf("데미지 보정치 : %d%n", sideDmg);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        if (shiftLifter) {
            out.printf("쉬프트리스터 적용: 마나 대신 스태미나 %d 소모, 마나 %d 회복.%n", manaUse, manaUse);
            return new Result(0, damage, true, -manaUse, manaUse);
        }
        return new Result(0, damage, true, manaUse, 0);
    }

}

