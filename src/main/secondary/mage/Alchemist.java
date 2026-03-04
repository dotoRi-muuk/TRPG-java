package main.secondary.mage;


import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 연금술사
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 */
public class Alchemist {

    /**
     * 완벽한 준비 : [미지의 물약]을 D20개 생성합니다. (영창 3턴, 마나 7 소모, 쿨타임 8턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result perfectPreparation(int stat, PrintStream out) {
        out.println("연금술사-완벽한 준비 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 8, 0);

        int potionCount = Main.dice(1, 20, out);
        out.printf("획득한 미지의 물약 개수 : %d%n", potionCount);

        return new Result(0, 0, true, 8, 0);
    }

    /**
     * 성급한 준비 : [미지의 물약]을 D8개 생성합니다. (마나 5 소모, 쿨타임 5턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체
     */
    public static Result hastyPreparation(int stat, PrintStream out) {
        out.println("연금술사-성급한 준비 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 5, 0);

        int potionCount = Main.dice(1, 8, out);
        out.printf("획득한 미지의 물약 개수 : %d%n", potionCount);

        return new Result(0, 0, true, 5, 0);
    }


    /**
     * 폭발 물약 : [미지의 물약] 1개를 소모합니다. 대상에게 5D6의 피해를 입힙니다. (마나 4 소모)
     *
     * @param stat               사용할 스탯
     * @param alchemyPreparation 미지의 물약 개수 (연금 준비 : 물약 1개당 효과/데미지 20% 증가)
     * @param frailty            허약 패시브 활성화 여부
     * @param frailtyPortion     상대의 체력 비율 (허약 : 디버프 적에게 가하는 피해 2배 증가, 체력 50% 이하일 때 2.5배 증가)
     * @param out                출력 스트림
     * @return 결과 객체
     */
    public static Result explosivePotion(int stat, int alchemyPreparation, boolean frailty, double frailtyPortion, int precision, PrintStream out) {
        out.println("연금술사-폭발 물약 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int damage = Main.dice(5, 6, out);
        out.printf("기본 데미지 : %d%n", damage);

        // 연금 준비 패시브
        if (alchemyPreparation > 0) {
            double alchemyPreparationBonus = alchemyPreparation * 0.2;
            out.printf("연금 준비 패시브 적용: 미지의 물약 %d개 보유로 데미지 %.1f%% 증가%n", alchemyPreparation, alchemyPreparationBonus * 100);
            damage = (int) (damage * (1 + alchemyPreparationBonus));
            out.printf("증가된 데미지 : %d%n", damage);
        }

        // 허약 패시브
        if (frailty) {
            if (frailtyPortion <= 0.5) {
                out.println("허약 패시브 적용: 적 체력 50% 이하로 데미지 2.5배 증가");
                damage = (int) (damage * 2.5);
            } else {
                out.println("허약 패시브 적용: 디버프 적에게 가하는 피해 2배 증가");
                damage *= 2;
            }
            out.printf("증가된 데미지 : %d%n", damage);
        }

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(0, damage, true, 0, 0);
    }

    /**
     * 독성 물약 : [미지의 물약] 1개를 소모합니다. 대상에게 5D4의 피해를 입히고, 1턴 동안 적의 모든 스탯을 1 감소시킵니다. (마나 3 소모)
     *
     * @param stat               사용할 스탯
     * @param alchemyPreparation 미지의 물약 개수 (연금 준비 : 물약 1개당 효과/데미지 20% 증가)
     * @param frailty            허약 패시브 활성화 여부
     * @param frailtyPortion     상대의 체력 비율 (허약 : 디버프 적에게 가하는 피해 2배 증가, 체력 50% 이하일 때 2.5배 증가)
     * @param out                출력 스트림
     * @return 결과 객체
     */
    public static Result toxicPotion(int stat, int alchemyPreparation, boolean frailty, double frailtyPortion, int precision, PrintStream out) {
        out.println("연금술사-독성 물약 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int damage = Main.dice(5, 4, out);
        out.printf("기본 데미지 : %d%n", damage);

        // 연금 준비 패시브
        if (alchemyPreparation > 0) {
            double alchemyPreparationBonus = alchemyPreparation * 0.2;
            out.printf("연금 준비 패시브 적용: 미지의 물약 %d개 보유로 데미지 %.1f%% 증가%n", alchemyPreparation, alchemyPreparationBonus * 100);
            damage = (int) (damage * (1 + alchemyPreparationBonus));
            out.printf("증가된 데미지 : %d%n", damage);
        }

        // 허약 패시브
        if (frailty) {
            if (frailtyPortion <= 0.5) {
                out.println("허약 패시브 적용: 적 체력 50% 이하로 데미지 2.5배 증가");
                damage = (int) (damage * 2.5);
            } else {
                out.println("허약 패시브 적용: 디버프 적에게 가하는 피해 2배 증가");
                damage *= 2;
            }
            out.printf("증가된 데미지 : %d%n", damage);
        }

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(0, damage, true, 0, 0);
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat               사용할 스탯
     * @param alchemyPreparation 미지의 물약 개수 (연금 준비 : 물약 1개당 효과/데미지 20% 증가)
     * @param frailty            허약 패시브 활성화 여부
     * @param frailtyPortion     상대의 체력 비율 (허약 : 디버프 적에게 가하는 피해 2배 증가, 체력 50% 이하일 때 2.5배 증가)
     * @param out                출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int alchemyPreparation, boolean frailty, double frailtyPortion, int precision, PrintStream out) {
        out.println("연금술사-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int damage = main.Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", damage);

        // 연금 준비 패시브
        if (alchemyPreparation > 0) {
            double alchemyPreparationBonus = alchemyPreparation * 0.2;
            out.printf("연금 준비 패시브 적용: 미지의 물약 %d개 보유로 데미지 %.1f%% 증가%n", alchemyPreparation, alchemyPreparationBonus * 100);
            damage = (int) (damage * (1 + alchemyPreparationBonus));
            out.printf("증가된 데미지 : %d%n", damage);
        }

        // 허약 패시브
        if (frailty) {
            if (frailtyPortion <= 0.5) {
                out.println("허약 패시브 적용: 적 체력 50% 이하로 데미지 2.5배 증가");
                damage = (int) (damage * 2.5);
            } else {
                out.println("허약 패시브 적용: 디버프 적에게 가하는 피해 2배 증가");
                damage *= 2;
            }
            out.printf("증가된 데미지 : %d%n", damage);
        }

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(0, damage, true, 0, 0);
    }
}
