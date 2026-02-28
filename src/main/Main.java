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

    public static int verdict(int stat, PrintStream out){
        out.println("판정 시도!");
        int dice = dice(1,20, out);
        if (stat >= dice) {
            out.printf("판정 성공! (스탯 %d > 주사위 %d)\n", stat, dice);
        } else {
            out.printf("판정 실패... (스탯 %d <= 주사위 %d)\n", stat, dice);
        }
        out.printf("판정 결과 값 : %d - %d = %d\n", stat, dice, stat - dice);
        return stat-dice;
    }


    public static int normalCalculation(int stat, PrintStream out, int dices, int sides) {
        int damage = dice(dices, sides, out);
        out.printf("기본 데미지 : %d\n", damage);
        int sideDamage = sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", damage);
        return damage;
    }

    /**
     * 새로운 데미지 계산 공식
     * 최종 데미지 = 기본 주사위 데미지 * { (100 + 추가 고정 데미지) * 배율 } %
     * 
     * @param baseDamage 주사위로 굴린 기본 데미지
     * @param flatBonus  추가할 고정 데미지 (현재는 0, 추후 스킬/아이템 등에서 추가)
     * @param multiplier 스킬/패시브 배율 (기본 1.0)
     * @param out        출력 스트림
     * @return 최종 계산된 데미지
     */
    public static int calculateDamage(int baseDamage, int flatBonus, double multiplier, PrintStream out) {
        double percent = (100 + flatBonus) * multiplier;
        int finalDamage = (int)(baseDamage * (percent / 100.0));
        out.printf("데미지 계산: %d * { (100 + %d) * %.2f }%% = %d\n", 
                baseDamage, flatBonus, multiplier, finalDamage);
        return finalDamage;
    }

    /**
     * 정밀 판정 - 공격 시 치명타 여부를 결정
     * 정밀 스탯 >= D20 판정 성공 시 최종 데미지 1.5배 (치명타)
     *
     * @param precision 정밀 스탯
     * @param finalDamage 판정 전 최종 데미지
     * @param out 출력 스트림
     * @return 치명타 적용 후 데미지
     */
    public static int criticalHit(int precision, int finalDamage, PrintStream out) {
        out.println("정밀 판정 시도!");
        int diceRoll = dice(1, 20, out);
        if (precision >= diceRoll) {
            int critDamage = (int)(finalDamage * 1.5);
            out.printf("정밀 판정 성공! (정밀 %d >= 주사위 %d) 치명타 발동: %d * 1.5 = %d%n", precision, diceRoll, finalDamage, critDamage);
            return critDamage;
        } else {
            out.printf("정밀 판정 실패 (정밀 %d < 주사위 %d)%n", precision, diceRoll);
            return finalDamage;
        }
    }

    public static void main(String[] args) {
    }
}
