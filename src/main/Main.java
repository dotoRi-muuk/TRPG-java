package main;

import java.io.PrintStream;

public class Main {

    public static int dice(int dices, int sides, PrintStream out){
        int total = 0;
        for (int i = 0; i < dices; i++) {
            total += (int)(Math.random() * sides) + 1;
        }
        out.println("주사위 값 : "+total);
        return total;
    }

    public static int sideDamage(int stat, PrintStream out){
        int damage = Math.max(0, stat - dice(1, 20, out));
        out.printf("데미지 보정 : %d\n", damage);
        return damage/10;
    }

    public static void main(String[] args) {
    }
}
