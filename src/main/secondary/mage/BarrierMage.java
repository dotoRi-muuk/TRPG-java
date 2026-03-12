package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 결계술사 (BarrierMage - Secondary Mage Class)
 * <p>
 * 판정 사용 스탯 : 지능
 * <p>
 * 패시브:
 * - 결계 확장: '결계' 스킬을 자유롭게 영창. 영창 시간만큼 결계 효과를 대상에게 추가 부여 (적 100%, 아군 50%).
 * - 견고한 결계: 공격하지 않는 스킬도 판정 진행. 결계 효과는 다음 결계 시전 시도까지 지속.
 * - 전개: 스킬 영창 중 기본 공격 사용 가능. 적중 시 다음 전개 마나 소모 없음.
 * - 영역: (전개된 결계의 영창 시간 합계) * 50% 만큼 최종 데미지 증가.
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 */
public class BarrierMage {

    /**
     * 기본 공격: 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat                 지능 스탯
     * @param castSum              전개된 결계의 영창 시간 합계 (영역 패시브: castSum * 50% 최종 데미지 증가)
     * @param reinforceBarrier     강화 결계 활성화 여부 (데미지 + reinforceBarrierCast * 40%)
     * @param reinforceBarrierCast 강화 결계 영창 시간
     * @param sealBarrier          봉인 결계 활성화 여부 (최종 데미지 +100%)
     * @param cloneBarrier         분신 결계 활성화 여부 (데미지 -50%)
     * @param precision            정밀 스탯
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int castSum,
                               boolean reinforceBarrier, int reinforceBarrierCast,
                               boolean sealBarrier, boolean cloneBarrier,
                               int precision, PrintStream out) {
        out.println("결계술사-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        // (100 + 데미지)% — 강화 결계 적용
        int flatBonus = 0;
        if (reinforceBarrier) {
            int reinforceBonus = reinforceBarrierCast * 40;
            out.printf("강화 결계 적용: 데미지 +%d%%%n", reinforceBonus);
            flatBonus += reinforceBonus;
        }

        // (최종 데미지)% — 영역, 봉인 결계, 분신 결계 적용
        double finalMultiplier = 1.0;
        if (castSum > 0) {
            double domainBonus = castSum * 0.5;
            out.printf("영역 패시브 적용: 최종 데미지 +%.0f%%%n", domainBonus * 100);
            finalMultiplier += domainBonus;
        }
        if (sealBarrier) {
            out.println("봉인 결계 적용: 최종 데미지 +100%");
            finalMultiplier += 1.0;
        }
        if (cloneBarrier) {
            out.println("분신 결계 적용: 데미지 -50%");
            finalMultiplier *= 0.5;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        int damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out);

        // 주사위 보정
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);

        // 치명타 판정
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(0, damage, true, 0, 0);
    }

    /**
     * 역장 결계: 결계 내 대상에게 매 턴 1D6의 보호막을 생성합니다. (매 턴 마나 3 소모)
     *
     * @param stat    지능 스탯
     * @param castSum 결계 총 영창 시간 (결계 확장: castSum * 50% 결계 효과 추가 부여)
     * @param out     출력 스트림
     * @return 결과 객체
     */
    public static Result forceField(int stat, int castSum, PrintStream out) {
        out.println("결계술사-역장 결계 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int shield = Main.dice(1, 6, out);
        out.printf("기본 보호막 : %d%n", shield);

        // 결계 확장 패시브: castSum * 50% 추가 효과
        if (castSum > 0) {
            double expansionFactor = 1.0 + castSum * 0.5;
            shield = (int) (shield * expansionFactor);
            out.printf("결계 확장 적용: 보호막 %.0f%%만큼 추가. 총 보호막: %d%n", expansionFactor * 100, shield);
        }

        out.printf("최종 보호막 : %d%n", shield);
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 결계 잔영: 이전에 사용한 결계를 선택하여 다음 턴까지 자신과 1명의 대상에게 발동합니다.
     * (현재 전개된 결계를 대상으로 할 수 없음) (마나: 선택한 결계의 수 * 3 소모, 쿨타임 6턴)
     *
     * @param stat          지능 스탯
     * @param selectedCount 선택한 결계의 수
     * @param out           출력 스트림
     * @return 결과 객체
     */
    public static Result barrierAfterimage(int stat, int selectedCount, PrintStream out) {
        out.println("결계술사-결계 잔영 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int manaUsed = selectedCount * 3;
        out.printf("결계 잔영: 결계 %d개 선택, 마나 %d 소모%n", selectedCount, manaUsed);
        out.println("이전 결계를 자신과 1명의 대상에게 적용 (다음 턴까지 지속)");

        return new Result(0, 0, true, manaUsed, 0);
    }

    /**
     * 초과 전개: 결계가 존재하는 상태에서 또 다른 결계를 전개할 수 있습니다.
     * ([전개], [영역]의 효과를 받지 않음) (매 턴 마나 2 추가 소모)
     *
     * @param stat 지능 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result overDeployment(int stat, PrintStream out) {
        out.println("결계술사-초과 전개 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        out.println("초과 전개: 추가 결계 전개 ([전개], [영역] 효과 미적용)");
        return new Result(0, 0, true, 2, 0);
    }

    /**
     * 기운 회수: 모든 결계를 해제하고, 해당 결계에 소모된 마나의 50%를 회수합니다.
     * (마나 2 소모, 쿨타임 7턴)
     *
     * @param stat           지능 스탯
     * @param totalManaSpent 결계에 소모된 총 마나
     * @param out            출력 스트림
     * @return 결과 객체 (manaUsed = 2 - recoveredMana)
     */
    public static Result manaRecovery(int stat, int totalManaSpent, PrintStream out) {
        out.println("결계술사-기운 회수 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 2, 0);

        int recoveredMana = totalManaSpent / 2;
        out.printf("기운 회수: 모든 결계 해제, 마나 %d 회수 (소모된 마나 %d의 50%%)%n", recoveredMana, totalManaSpent);
        out.println("마나 2 소모");

        return new Result(0, 0, true, 2 - recoveredMana, 0);
    }
}
