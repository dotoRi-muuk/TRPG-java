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

    /**
     * sideDamage 계산 - 패시브와 배율 적용 후 맨 뒤에 적용
     * sideDamage = (최종 데미지) * Math.Max((스탯-D20), 0) * 0.1
     * @param finalDamage 패시브/배율 적용 후 최종 데미지
     * @param stat 사용할 스탯
     * @param out 출력 스트림
     * @return 추가할 sideDamage 값
     */
    public static int sideDamage(int finalDamage, int stat, PrintStream out){
        int diceRoll = dice(1, 20, out);
        int statBonus = Math.max(0, stat - diceRoll);
        int damage = (int)(finalDamage * statBonus * 0.1);
        out.printf("데미지 보정 : %d * %d * 0.1 = %d\n", finalDamage, statBonus, damage);
        return damage;
    }

    public static void main(String[] args) {
    }
}
