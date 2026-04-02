package trpg.web;

import main.primary.Warrior;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Warrior (전사) damage calculations.
 * Provides endpoints for the primary Warrior class skills.
 */
@RestController
@RequestMapping("/api/warrior")
public class WarriorController {

    /**
     * Request body for Warrior attack skill calculations.
     */
    public static class WarriorRequest {
        public int power;
        public int maxHealth;
        public int curHealth;
        public int precision;
        public int level;
    }

    /**
     * Request body for Warrior shield skill calculation.
     */
    public static class WarriorShieldRequest {
        public int damageTaken;
    }

    private static Map<String, Object> buildResponse(int damage, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", damage);
        return response;
    }

    /**
     * 기본공격 (D6)
     * POST /api/warrior/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody WarriorRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Warrior.plain(req.power, req.maxHealth, req.curHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 강타 (D10)
     * POST /api/warrior/strike
     */
    @PostMapping("/strike")
    public Map<String, Object> strike(@RequestBody WarriorRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Warrior.strike(req.power, req.maxHealth, req.curHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 가로베기 (D8)
     * POST /api/warrior/side
     */
    @PostMapping("/side")
    public Map<String, Object> side(@RequestBody WarriorRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Warrior.side(req.power, req.maxHealth, req.curHealth, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 육참골단 (전용 수비)
     * POST /api/warrior/shield
     */
    @PostMapping("/shield")
    public Map<String, Object> shield(@RequestBody WarriorShieldRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Warrior.shield(req.damageTaken, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }
}
