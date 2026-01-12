package main;

import java.util.Map;

/**
 * 스킬 실행 결과를 나타내는 레코드 클래스
 */
public record Result (
        int damageTaken,
        int damageDealt,
        boolean succeeded,

        int manaUsed,
        int staminaUsed,
        Map<Stat, Integer> statChanges
){
    public Result() {
        this(0, 0, false, 0, 0, Map.of());
    }

    public Result(int damageTaken, int damageDealt, boolean succeeded, int manaChange, int staminaChange) {
        this(damageTaken, damageDealt, succeeded, manaChange, staminaChange, Map.of());
    }
}
