package main.primary;

import main.Main;

import java.io.PrintStream;

public class Mage {

    /**
     * 마법사 기본공격 - 매지션 패시브 적용 (일반 D6)
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("마법사-기본공격 사용 (D6)");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
        int totalDamage = defaultDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, totalDamage);
        return totalDamage;
    }

    /**
     * 마법사 기본공격 - 매지션 패시브 적용 (마나 소모 D8)
     */
    public static int plain(int intelligence, boolean useMana, PrintStream out) {
        if (useMana) {
            out.println("마법사-기본공격 사용 (마나 1 소모, D8)");
            out.println("※ 마나 1 소모");
            int defaultDamage = Main.dice(1, 8, out);
            int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
            int totalDamage = defaultDamage + sideDamage;
            out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, totalDamage);
            return totalDamage;
        } else {
            return plain(intelligence, out);
        }
    }

    /**
     * 마탄 스킬
     * D8 x 3
     */
    public static int magicBullet(int intelligence, PrintStream out) {
        out.println("마법사-마탄 스킬 사용");
        int baseDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 8, out);
            out.printf("%d번째 마탄: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        return totalDamage;
    }

    /**
     * 마나 블래스트 스킬
     * D6 x 6 + 소모한 추가 마나
     */
    public static int manaBlast(int intelligence, int additionalMana, PrintStream out) {
        out.println("마법사-마나 블래스트 스킬 사용");
        out.printf("추가 소모 마나: %d (기본 8 + 추가 %d = 총 %d)%n", additionalMana, additionalMana, 8 + additionalMana);

        int baseDamage = 0;

        for (int i = 1; i <= 6; i++) {
            int diceResult = Main.dice(1, 6, out);
            out.printf("%d번째 블래스트: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        // 추가 마나도 기본 데미지에 포함
        baseDamage += additionalMana;
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d(추가 마나 포함) + %d = %d%n", baseDamage - additionalMana, additionalMana, sideDamage, totalDamage);
        return totalDamage;
    }

    /**
     * 매직가드 전용 수비
     * 받는 데미지 50%
     */
    public static int magicGuard(int damageTaken, PrintStream out) {
        out.println("마법사-매직가드 사용");
        int reducedDamage = (int) (damageTaken * 0.5);
        out.printf("받는 데미지 50%% 감소: %d → %d%n", damageTaken, reducedDamage);
        out.println("※ 마나 3 소모");
        return reducedDamage;
    }

}

