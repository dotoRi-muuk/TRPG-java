package main.primary;

import main.Main;

import java.io.PrintStream;

public class Priest {

    /**
     * 사제 기본공격 - 축복 패시브 적용
     * 기본 공격을 지능으로 사용 가능
     * 사용시 지정한 아군의 체력 1 회복
     */
    public static int plain(int intelligence, int precision, int level, PrintStream out) {
        out.println("사제-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(defaultDamage, intelligence, out);
        int totalDamage = defaultDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, totalDamage);
        out.println("※ 지정한 아군의 체력 1 회복");
        int finalDamage = Main.criticalHit(precision, totalDamage, out);
        finalDamage = (int)(finalDamage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), finalDamage);
        return finalDamage;
    }

    /**
     * 복수 스킬
     * D4 x 6
     * 자신 또는 아군의 체력이 5이하가 되는 피해를 받았을때 즉시 발동
     */
    public static int revenge(int intelligence, int precision, int level, PrintStream out) {
        out.println("사제-복수 스킬 사용");
        out.println("※ 공격을 받은 턴까지 해당 아군에게 보호막 10 부여");

        int baseDamage = 0;

        for (int i = 1; i <= 6; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 복수: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        int finalDamage = Main.criticalHit(precision, totalDamage, out);
        finalDamage = (int)(finalDamage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), finalDamage);
        return finalDamage;
    }

    /**
     * 희생 전용 신속
     * 받는 데미지 50% 감소
     */
    public static int sacrifice(int damageTaken, PrintStream out) {
        out.println("사제-희생 전용 신속 사용");
        int reducedDamage = (int) (damageTaken * 0.5);
        out.printf("받는 데미지 50%% 감소: %d → %d%n", damageTaken, reducedDamage);
        out.println("※ 스태미나 5 소모");
        return reducedDamage;
    }

}

