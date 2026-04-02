package trpg.web;

import main.Result;
import main.secondary.warrior.Berserker;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Berserker (버서커) damage calculations.
 * Provides endpoints for the secondary Berserker class skills.
 */
@RestController
@RequestMapping("/api/berserker")
public class BerserkerController {

    /**
     * Request body for Berserker attack skill calculations.
     */
    public static class BerserkerRequest {
        public int stat;
        public int maxHealth;
        public int currentHealth;
        public int precision;
        public int level;
    }

    /**
     * Request body for Berserker final strike skill.
     * Includes currentStamina which is consumed entirely.
     */
    public static class BerserkerFinalStrikeRequest {
        public int stat;
        public int maxHealth;
        public int currentHealth;
        public int currentStamina;
        public int precision;
        public int level;
    }

    /**
     * Request body for Berserker mutual destruction (동귀어진) defense skill.
     */
    public static class BerserkerDefenseRequest {
        public int stat;
        public int damageTaken;
        public int currentHealth;
        public int maxHealth;
        public boolean resistance;
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
     * POST /api/berserker/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.plain(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 찍어내리기 (D8)
     * POST /api/berserker/chop-down
     */
    @PostMapping("/chop-down")
    public Map<String, Object> chopDown(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.smash(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 부수기 (2D6)
     * POST /api/berserker/smash
     */
    @PostMapping("/smash")
    public Map<String, Object> smash(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.crush(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 일격 (D20)
     * POST /api/berserker/strike
     */
    @PostMapping("/strike")
    public Map<String, Object> strike(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.strike(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 무지성 난타 (4D8)
     * POST /api/berserker/mindless-barrage
     */
    @PostMapping("/mindless-barrage")
    public Map<String, Object> mindlessBarrage(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.mindlessThrashing(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 흉폭한 맹공 (5D12)
     * POST /api/berserker/savage-assault
     */
    @PostMapping("/savage-assault")
    public Map<String, Object> savageAssault(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.ferociousAssault(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 파멸의 일격 (6D8)
     * POST /api/berserker/devastating-blow
     */
    @PostMapping("/devastating-blow")
    public Map<String, Object> devastatingBlow(@RequestBody BerserkerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.devastatingBlow(req.stat, req.currentHealth, req.maxHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 최후의 일격 (4D20) - 체력 10% 이하 시 사용 가능
     * POST /api/berserker/last-strike
     */
    @PostMapping("/last-strike")
    public Map<String, Object> lastStrike(@RequestBody BerserkerFinalStrikeRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.finalStrike(req.stat, req.currentHealth, req.maxHealth,
                req.currentStamina, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 동귀어진 (전용 수비)
     * POST /api/berserker/mutual-destruction
     */
    @PostMapping("/mutual-destruction")
    public Map<String, Object> mutualDestruction(@RequestBody BerserkerDefenseRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Berserker berserker = new Berserker();
        Result result = berserker.mutualDestruction(req.stat, req.damageTaken, req.currentHealth,
                req.maxHealth, req.resistance, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
