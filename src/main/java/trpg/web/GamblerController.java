package trpg.web;

import main.Result;
import main.hidden.Gambler;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for hidden Gambler damage calculations.
 * Provides endpoints for the hidden Gambler class attacks.
 */
@RestController
@RequestMapping("/api/gambler")
public class GamblerController {

    /**
     * Request body for Gambler skill calculations.
     */
    public static class GamblerRequest {
        public int stat;
        public int luck;
        public int reducedLuck;
        public boolean jackpotActive;
    }

    /**
     * Calculate Gambler plain attack damage (1D6).
     * POST /api/gambler/plain
     *
     * @param req 요청 본문 (stat, reducedLuck, jackpotActive)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody GamblerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gambler.plain(req.stat, req.reducedLuck, req.jackpotActive, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Gambler coin toss damage (1D4 + 1D12 on luck success).
     * POST /api/gambler/coinToss
     *
     * @param req 요청 본문 (stat, luck, reducedLuck, jackpotActive)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/coinToss")
    public Map<String, Object> coinToss(@RequestBody GamblerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gambler.coinToss(req.stat, req.luck, req.reducedLuck, req.jackpotActive, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Gambler joker card damage (1D6 + 2D12 on luck success).
     * POST /api/gambler/jokerCard
     *
     * @param req 요청 본문 (stat, luck, reducedLuck, jackpotActive)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/jokerCard")
    public Map<String, Object> jokerCard(@RequestBody GamblerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gambler.jokerCard(req.stat, req.luck, req.reducedLuck, req.jackpotActive, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Gambler blackjack damage (1D6 + 3D8 on luck success).
     * POST /api/gambler/blackjack
     *
     * @param req 요청 본문 (stat, luck, reducedLuck, jackpotActive)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/blackjack")
    public Map<String, Object> blackjack(@RequestBody GamblerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gambler.blackjack(req.stat, req.luck, req.reducedLuck, req.jackpotActive, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Gambler yatzy dice damage (1D8 + 2D20 on 2 luck successes).
     * POST /api/gambler/yatzyDice
     *
     * @param req 요청 본문 (stat, luck, reducedLuck, jackpotActive)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/yatzyDice")
    public Map<String, Object> yatzyDice(@RequestBody GamblerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gambler.yatzyDice(req.stat, req.luck, req.reducedLuck, req.jackpotActive, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Gambler royal flush damage (1D4 + 4D20 on 3 luck successes).
     * POST /api/gambler/royalFlush
     *
     * @param req 요청 본문 (stat, luck, reducedLuck, jackpotActive)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/royalFlush")
    public Map<String, Object> royalFlush(@RequestBody GamblerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gambler.royalFlush(req.stat, req.luck, req.reducedLuck, req.jackpotActive, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }
}
