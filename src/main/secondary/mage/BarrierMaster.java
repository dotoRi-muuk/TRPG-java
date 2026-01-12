package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 결계술사
 * <p>
 * 판정 사용 스탯 : 지능
 */
public class BarrierMaster {

    /**
     * 역장 결계 : 결계 내 대상에게 매 턴 1D6의 보호막을 생성합니다. (매 턴 마나 3 소모)
     * @param stat 사용할 스탯
     * @param castSum 결계 총 영창 시간 (결계 확장 : castSum * 50% 만큼 결계 효과 추가 부여)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result forceFieldBarrier(int stat, int castSum, boolean enableBarrierExpansion, PrintStream out) {
        out.println("결계술사-역장 결계 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseShield = Main.dice(1, 6, out);
        out.printf("기본 보호막 : %d%n", baseShield);

        double barrierExpansion = castSum * 0.5 + 1.0;

        // 결계 확장 패시브
        if (castSum > 0 && enableBarrierExpansion) {
            out.println("경계 확장 패시브 적용: 결계 효과 추가 부여");
            baseShield = (int)(baseShield * barrierExpansion);
            out.printf("%.1f%%만큼 결계 효과 추가 부여. 총 보호막: %d%n", barrierExpansion * 100, baseShield);
        }

        out.printf("최종 보호막 : %d%n", baseShield);
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 사용할 스탯
     * @param castSum 결계 총 영창 시간 (결계 확장 : castSum * 50% 만큼 결계 효과 추가 부여, 영역 : castSum * 50% 만큼 데미지 증가)
     * @param enableBarrierExpansion 경계 확장 패시브 본인 활성화 여부
     * @param enhancementBarrier 강화 결계 패시브 (결계 내 대상이 (결계 영창 시간) * 40% 만큼 데미지 증가)
     * @param sealingBarrier 봉인 결계 패시브 (결계 내 대상이 주는 데미지가 2배로 증가)
     * @param cloneBarrier 분신 결계 패시브 (결계 내 대상이 주는 데미지가 절반 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int castSum, boolean enableBarrierExpansion, boolean enhancementBarrier, int enhancementBarrierCast, boolean sealingBarrier, boolean cloneBarrier, PrintStream out) {
        out.println("결계술사-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        double damageModifier = 1.0;

        double barrierExpansion = castSum * 0.5 + 1.0;

        // 영역 패시브
        if (castSum > 0) {
            double areaBonus = castSum * 0.5;
            out.printf("영역 패시브 적용: 데미지 %.1f 증가%n", areaBonus);
            damageModifier += areaBonus;
        }

        // 강화 결계 패시브
        if (enhancementBarrier) {
            double enhancementBonus = enhancementBarrierCast * 0.4 + 1.0;
            out.printf("강화 결계 패시브 적용: 데미지 %.1f 증가%n", enhancementBonus);
            if (castSum > 0 && enableBarrierExpansion) {
                out.println("경계 확장 패시브 적용: 결계 효과 추가 부여");
                enhancementBonus *= barrierExpansion;
                out.printf("%.1f%%만큼 결계 효과 추가 부여. 총 배율: %.1f%n", barrierExpansion * 100, enhancementBonus);
            }
            damageModifier *= enhancementBonus;
        }

        // 봉인 결계 패시브
        if (sealingBarrier) {
            out.println("봉인 결계 패시브 적용: 주는 데미지 2배 증가");
            double enhancementBonus = 2.0;
            if (castSum > 0 && enableBarrierExpansion) {
                out.println("경계 확장 패시브 적용: 결계 효과 추가 부여");
                enhancementBonus *= barrierExpansion;
                out.printf("%.1f%%만큼 결계 효과 추가 부여. 총 배율: %.1f%n", barrierExpansion * 100, enhancementBonus);
            }
            damageModifier *= enhancementBonus;
        }

        // 분신 결계 패시브
        if (cloneBarrier) {
            out.println("분신 결계 패시브 적용: 주는 데미지 50% 감소");
            double reductionBonus = 2;
            if (castSum > 0 && enableBarrierExpansion) {
                out.println("경계 확장 패시브 적용: 결계 효과 추가 부여");
                reductionBonus *= barrierExpansion;
                out.printf("%.1f%%만큼 결계 효과 추가 부여. 총 배율: %.1f%n", barrierExpansion * 100, 1 / reductionBonus);
            }
            damageModifier /= reductionBonus;
        }

        int damageAfterModifier = (int)(baseDamage * damageModifier);
        out.printf("배율 적용 후 데미지 : %d%n", damageAfterModifier);
        int sideDamage = Main.sideDamage(damageAfterModifier, stat, out);
        damageAfterModifier += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", damageAfterModifier);
        return new Result(0, damageAfterModifier, true, 0, 0);
    }

}
