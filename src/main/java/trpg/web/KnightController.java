package trpg.web;

import main.Result;
import main.secondary.warrior.Knight;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Knight (기사) damage calculations.
 * Provides endpoints for the secondary Knight class skills.
 */
@RestController
@RequestMapping("/api/knight")
public class KnightController {

    /**
     * Request body for Knight skill calculations.
     */
    public static class KnightRequest {
        public int stat;
        public boolean blessing;
        public int precision;
        public int level;
        public int damageIncrease;
        public int finalDamageIncrease = 100;
        public int maxHealth = 100;
        public int swiftness;
    }

    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * 기본공격 (D6)
     * POST /api/knight/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.plain(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 내려치기 (D8)
     * POST /api/knight/smash-down
     */
    @PostMapping("/smash-down")
    public Map<String, Object> smashDown(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.downwardStrike(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 후려치기 (2D4)
     * POST /api/knight/sweep
     */
    @PostMapping("/sweep")
    public Map<String, Object> sweep(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.bash(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 머리치기 (D6)
     * POST /api/knight/head-strike
     */
    @PostMapping("/head-strike")
    public Map<String, Object> headStrike(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.headStrike(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 수비파괴 (D6)
     * POST /api/knight/defense-break
     */
    @PostMapping("/defense-break")
    public Map<String, Object> defenseBreak(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.defenseBreak(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 기절시키기 (D8)
     * POST /api/knight/stun
     */
    @PostMapping("/stun")
    public Map<String, Object> stun(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.stun(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 일격 (3D12)
     * POST /api/knight/critical-strike
     */
    @PostMapping("/critical-strike")
    public Map<String, Object> criticalStrike(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.strike(req.stat, req.blessing, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 태양 갑옷
     * POST /api/knight/solar-armor
     */
    @PostMapping("/solar-armor")
    public Map<String, Object> solarArmor(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        Result result = knight.solarArmor(req.stat, req.maxHealth, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 노을빛 수호
     * POST /api/knight/sunset-guardian
     */
    @PostMapping("/sunset-guardian")
    public Map<String, Object> sunsetGuardian(@RequestBody KnightRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Knight knight = new Knight();
        int swiftness = req.swiftness > 0 ? req.swiftness : req.stat;
        Result result = knight.sunsetGuardian(swiftness, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
