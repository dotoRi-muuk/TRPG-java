package main.primary;

import main.Main;

import java.io.PrintStream;

public class Rogue {

    /**
     * 도적 기본공격 - 직업병 패시브 적용 (D4 x 2)
     */
    public static int plain(int stat, boolean useTwoDice, int precision, PrintStream out) {
        if (useTwoDice) {
            out.println("도적-기본공격 사용 (D4 x 2)");
            int dice1 = Main.dice(1, 4, out);
            int dice2 = Main.dice(1, 4, out);
            int defaultDamage = dice1 + dice2;
            int sideDamage = Main.sideDamage(defaultDamage, stat, out);
            int totalDamage = defaultDamage + sideDamage;
            out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, totalDamage);
            return Main.criticalHit(precision, totalDamage, out);
        } else {
            out.println("도적-기본공격 사용 (D6)");
            int defaultDamage = Main.dice(1, 6, out);
            int sideDamage = Main.sideDamage(defaultDamage, stat, out);
            int totalDamage = defaultDamage + sideDamage;
            out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, totalDamage);
            return Main.criticalHit(precision, totalDamage, out);
        }
    }

    /**
     * 쑤시기 기술
     * 기본 공격 + 다음 턴에 추가 3데미지 (메시지만)
     */
    public static int stab(int stat, int precision, PrintStream out) {
        out.println("도적-쑤시기 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(defaultDamage, stat, out);
        int totalDamage = defaultDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, totalDamage);
        out.println("※ 다음 턴에 상대에게 추가 3데미지");
        return Main.criticalHit(precision, totalDamage, out);
    }

    /**
     * 투척/속공 기술
     * D6 + 신속 판정 성공시 기본공격 1회 추가
     * 신속 판정: (신속 스탯 - 1D20) > 0이면 성공
     */
    public static int throwAttack(int dexterity, int swiftness, int precision, PrintStream out) {
        out.println("도적-투척/속공 사용");

        // 첫 번째 공격 (D6)
        int firstAttack = Main.dice(1, 6, out);
        int firstSideDamage = Main.sideDamage(firstAttack, dexterity, out);
        int totalDamage = firstAttack + firstSideDamage;

        // 신속 판정
        out.println("신속 판정 시도");
        int swiftCheck = swiftness - Main.dice(1, 20, out);
        out.printf("신속 판정 결과: %d - D20 = %d ", swiftness, swiftCheck);

        if (swiftCheck > 0) {
            out.println("(성공!)");
            out.println("추가 기본공격 발동");
            int secondAttack = Main.dice(1, 6, out);
            int secondSideDamage = Main.sideDamage(secondAttack, dexterity, out);
            totalDamage += secondAttack + secondSideDamage;
            out.printf("총 데미지 : %d + %d = %d%n", firstAttack + firstSideDamage, secondAttack + secondSideDamage, totalDamage);
        } else {
            out.println("(실패)");
            out.printf("총 데미지 : %d%n", totalDamage);
        }

        return Main.criticalHit(precision, totalDamage, out);
    }

}

