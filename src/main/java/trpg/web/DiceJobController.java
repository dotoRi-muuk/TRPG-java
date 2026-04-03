package trpg.web;

import main.Main;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Dice Job (주사위 직업) damage calculations.
 * Allows users to set nDn (number of dice and sides) and roll for damage.
 */
@RestController
@RequestMapping("/api/dicejob")
public class DiceJobController {

    /**
     * Request body for Dice Job skill calculations.
     */
    public static class DiceJobRequest {
        public int stat;
        public int dices;
        public int sides;
    }

    /**
     * 주사위 판정 시도 (nDn)
     * POST /api/dicejob/roll
     *
     * Performs a verdict check (1D20 vs stat). On success, rolls nDn dice
     * and applies sideDamage bonus. Returns total damage and calculation log.
     *
     * @param req 요청 본문 (stat, dices, sides)
     * @return 계산 결과 (damage, log, succeeded)
     */
    @PostMapping("/roll")
    public Map<String, Object> roll(@RequestBody DiceJobRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        ps.printf("주사위 직업 - %dD%d 판정 시도%n", req.dices, req.sides);

        int verdictResult = Main.verdict(req.stat, ps);
        boolean succeeded = verdictResult >= 0;
        int damage = 0;

        if (succeeded) {
            int diceRoll = req.stat - verdictResult;
            damage = Main.normalCalculation(req.stat, ps, req.dices, req.sides, diceRoll);
        }

        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", damage);
        response.put("succeeded", succeeded);
        return response;
    }
}
