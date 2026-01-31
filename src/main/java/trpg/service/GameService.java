package trpg.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Game logic service for TRPG damage calculations.
 * Extracted from main.Main class to follow Spring Boot best practices.
 */
@Service
public class GameService {

    /**
     * Roll dice and return the total.
     * @param dices Number of dice to roll
     * @param sides Number of sides on each die
     * @return Total of all dice rolls
     */
    public int dice(int dices, int sides) {
        int total = 0;
        for (int i = 0; i < dices; i++) {
            total += (int)(Math.random() * sides) + 1;
        }
        return total;
    }

    /**
     * Roll dice with detailed output.
     * @param dices Number of dice to roll
     * @param sides Number of sides on each die
     * @return Result containing total and message
     */
    public DiceResult diceWithOutput(int dices, int sides) {
        int total = dice(dices, sides);
        String message = "주사위 값 : " + total;
        return new DiceResult(total, message);
    }

    /**
     * Calculate side damage based on final damage and stat.
     * sideDamage = (최종 데미지) * Math.Max((스탯-D20), 0) * 0.1
     * @param finalDamage Final damage after passive/multiplier
     * @param stat Stat to use
     * @return Result containing side damage and messages
     */
    public SideDamageResult sideDamage(int finalDamage, int stat) {
        List<String> messages = new ArrayList<>();
        int diceRoll = dice(1, 20);
        messages.add("주사위 값 : " + diceRoll);
        
        int statBonus = Math.max(0, stat - diceRoll);
        int damage = (int)(finalDamage * statBonus * 0.1);
        messages.add(String.format("데미지 보정 : %d * %d * 0.1 = %d", finalDamage, statBonus, damage));
        
        return new SideDamageResult(damage, diceRoll, statBonus, messages);
    }

    /**
     * Perform a stat verdict check.
     * @param stat Stat value to check against
     * @return Result containing dice roll, success/failure, and messages
     */
    public VerdictResult verdict(int stat) {
        List<String> messages = new ArrayList<>();
        messages.add("판정 시도!");
        
        int diceRoll = dice(1, 20);
        messages.add("주사위 값 : " + diceRoll);
        
        boolean success = stat >= diceRoll;
        if (success) {
            messages.add(String.format("판정 성공! (스탯 %d >= 주사위 %d)", stat, diceRoll));
        } else {
            messages.add(String.format("판정 실패... (스탯 %d < 주사위 %d)", stat, diceRoll));
        }
        
        int difference = diceRoll - stat;
        return new VerdictResult(diceRoll, success, difference, messages);
    }

    /**
     * Calculate normal damage with dice roll and side damage.
     * @param stat Stat to use for side damage
     * @param dices Number of dice to roll
     * @param sides Number of sides on each die
     * @return Result containing total damage and calculation details
     */
    public NormalCalculationResult normalCalculation(int stat, int dices, int sides) {
        List<String> messages = new ArrayList<>();
        
        int damage = dice(dices, sides);
        messages.add("주사위 값 : " + damage);
        messages.add(String.format("기본 데미지 : %d", damage));
        
        SideDamageResult sideDamageResult = sideDamage(damage, stat);
        int sideDamageValue = sideDamageResult.getDamage();
        messages.addAll(sideDamageResult.getMessages());
        
        damage += sideDamageValue;
        messages.add(String.format("데미지 보정치 : %d", sideDamageValue));
        messages.add(String.format("최종 데미지 : %d", damage));
        
        return new NormalCalculationResult(damage, sideDamageValue, messages);
    }

    // Result classes
    public static class DiceResult {
        private final int total;
        private final String message;

        public DiceResult(int total, String message) {
            this.total = total;
            this.message = message;
        }

        public int getTotal() { return total; }
        public String getMessage() { return message; }
    }

    public static class SideDamageResult {
        private final int damage;
        private final int diceRoll;
        private final int statBonus;
        private final List<String> messages;

        public SideDamageResult(int damage, int diceRoll, int statBonus, List<String> messages) {
            this.damage = damage;
            this.diceRoll = diceRoll;
            this.statBonus = statBonus;
            this.messages = messages;
        }

        public int getDamage() { return damage; }
        public int getDiceRoll() { return diceRoll; }
        public int getStatBonus() { return statBonus; }
        public List<String> getMessages() { return messages; }
    }

    public static class VerdictResult {
        private final int diceRoll;
        private final boolean success;
        private final int difference;
        private final List<String> messages;

        public VerdictResult(int diceRoll, boolean success, int difference, List<String> messages) {
            this.diceRoll = diceRoll;
            this.success = success;
            this.difference = difference;
            this.messages = messages;
        }

        public int getDiceRoll() { return diceRoll; }
        public boolean isSuccess() { return success; }
        public int getDifference() { return difference; }
        public List<String> getMessages() { return messages; }
    }

    public static class NormalCalculationResult {
        private final int totalDamage;
        private final int sideDamage;
        private final List<String> messages;

        public NormalCalculationResult(int totalDamage, int sideDamage, List<String> messages) {
            this.totalDamage = totalDamage;
            this.sideDamage = sideDamage;
            this.messages = messages;
        }

        public int getTotalDamage() { return totalDamage; }
        public int getSideDamage() { return sideDamage; }
        public List<String> getMessages() { return messages; }
    }
}
