package main;

import java.util.Map;

public record Result (
        int damageTaken,
        int damageDealt,
        boolean succeeded,

        int manaChange,
        int staminaChange,
        Map<Stat, Integer> statChanges
){
    public Result() {
        this(0, 0, false, 0, 0, Map.of());
    }

    public Result(int damageTaken, int damageDealt, boolean succeeded, int manaChange, int staminaChange) {
        this(damageTaken, damageDealt, succeeded, manaChange, staminaChange, Map.of());
    }
}
