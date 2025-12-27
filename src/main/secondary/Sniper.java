package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Sniper {

    /**
     * 저격수 기본공격
     * @param stat 사용할 스탯
     * @param numBuffs 정조준 패시브 (버프 개수당 25% 증가)
     * @param notAttackedFor5Turns 급소조준 패시브 (5턴 이상 미공격 시 200%)
     */
    public static int plain(int stat, int numBuffs, boolean notAttackedFor5Turns, PrintStream out) {
        out.println("저격수-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 정조준 패시브: 버프 1개당 25% 증가
        if (numBuffs > 0) {
            double multiplier = 1.0 + (numBuffs * 0.25);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("정조준 패시브 적용: 버프 %d개 → x%.2f = %d%n", numBuffs, multiplier, totalDamage);
        }

        // 급소조준 패시브: 5턴 이상 미공격 시 200%
        if (notAttackedFor5Turns) {
            totalDamage *= 2;
            out.printf("급소조준 패시브 적용: x2.0 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 확보 기술
     * 공격 전까지 광역 공격을 받지 않음, 스태미나 2 소모
     * (데미지 없음)
     */
    public static void secure(PrintStream out) {
        out.println("저격수-확보 사용");
        out.println("※ 공격 전까지 광역 공격을 받지 않음");
        out.println("※ 스태미나 2 소모");
    }

    /**
     * 조립 기술
     * 다음 공격 데미지 150%
     * (데미지 없음)
     */
    public static void assemble(PrintStream out) {
        out.println("저격수-조립 사용");
        out.println("※ 다음 공격 데미지 150%%");
    }

    /**
     * 장전 기술
     * 탄환 얻음, 스태미나 3 소모
     * (데미지 없음)
     */
    public static void load(PrintStream out) {
        out.println("저격수-장전 사용");
        out.println("※ 탄환 획득");
        out.println("※ 스태미나 3 소모");
    }

    /**
     * 조준 기술
     * 다음 공격이 적의 수비를 무시
     * (데미지 없음)
     */
    public static void aim(PrintStream out) {
        out.println("저격수-조준 사용");
        out.println("※ 다음 공격이 적의 수비를 무시");
    }

    /**
     * 발사 기술
     * 탄환 소모, 4D20, 스태미나 10 소모
     * @param stat 사용할 스탯
     * @param numBuffs 정조준 패시브
     * @param noBasicAttackUsed 죽음의 탄환 패시브 (기본 공격 미사용 시 150%)
     */
    public static int fire(int stat, int numBuffs, boolean noBasicAttackUsed, PrintStream out) {
        out.println("저격수-발사 사용 (4D20)");
        out.println("※ 탄환 소모");
        out.println("※ 스태미나 10 소모");

        int defaultDamage = Main.dice(4, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 정조준 패시브
        if (numBuffs > 0) {
            double multiplier = 1.0 + (numBuffs * 0.25);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("정조준 패시브 적용: 버프 %d개 → x%.2f = %d%n", numBuffs, multiplier, totalDamage);
        }

        // 죽음의 탄환 패시브
        if (noBasicAttackUsed) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("죽음의 탄환 패시브 적용: x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

}

