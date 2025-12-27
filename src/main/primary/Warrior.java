package main.primary;

import main.Main;

import java.io.PrintStream;

public class Warrior {

    public static int strike(int power, int maxHealth, int curHealth, PrintStream out) {
        out.println("전사-강타 사용");
        int defaultDamage = Main.dice(1, 10, out);
        int frenzyDamage = frenzy(maxHealth, curHealth, out);
        int baseDamage = defaultDamage + frenzyDamage;
        int sideDamage = Main.sideDamage(baseDamage, power, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d + %d = %d%n", defaultDamage, frenzyDamage, sideDamage, totalDamage);
        return totalDamage;
    }

    private static int frenzy(int maxHealth, int curHealth, PrintStream out) {
        out.println("전사-광란 적용");
        out.printf("시작 체력 : %d%n", maxHealth);
        out.printf("현재 체력 : %d%n", curHealth);
        out.printf("광란 추가 데미지 : (%d-%d)/5 = %d%n", maxHealth, curHealth, (maxHealth-curHealth)/5);
        return (maxHealth-curHealth)/5;
    }

    public static int side(int power, int maxHealth, int curHealth, PrintStream out) {
        out.println("전사-가로베기 사용");
        int defaultDamage = Main.dice(1, 8, out);
        int frenzyDamage = frenzy(maxHealth, curHealth, out);
        int baseDamage = defaultDamage + frenzyDamage;
        int sideDamage = Main.sideDamage(baseDamage, power, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d + %d = %d%n", defaultDamage, frenzyDamage, sideDamage, totalDamage);
        return totalDamage;
    }

    public static int plain(int power, int maxHealth, int curHealth, PrintStream out) {
        out.println("전사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int frenzyDamage = frenzy(maxHealth, curHealth, out);
        int baseDamage = defaultDamage + frenzyDamage;
        int sideDamage = Main.sideDamage(baseDamage, power, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d + %d = %d%n", defaultDamage, frenzyDamage, sideDamage, totalDamage);
        return totalDamage;
    }

    public static int shield(int damageTaken, PrintStream out) {
        out.printf("받은 데미지 1.5배 : %d%n", ((int) (damageTaken * 1.5)));
        out.printf("반격 데미지 2배 : %d%n", damageTaken*2);
        return damageTaken * 2;
    }

}
