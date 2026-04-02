package trpg.web;

import main.primary.Rogue;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Rogue (도적) damage calculations.
 * Provides endpoints for the primary Rogue class skills.
 */
@RestController
@RequestMapping("/api/rogue")
public class RogueController {

    /**
     * Request body for Rogue skill calculations.
     */
    public static class RogueRequest {
        public int stat;
        public int dexterity;
        public int swiftness;
        public boolean useTwoDice;
        public int precision;
        public int level;
    }

    /**
     * Build standard skill response map.
     */
    private static Map<String, Object> buildResponse(int damage, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", damage);
        return response;
    }

    /**
     * 기본공격 - 직업병 패시브 적용 (D4 x 2 또는 D6)
     * POST /api/rogue/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody RogueRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Rogue.plain(req.stat, req.useTwoDice, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 쑤시기 기술
     * POST /api/rogue/stab
     */
    @PostMapping("/stab")
    public Map<String, Object> stab(@RequestBody RogueRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Rogue.stab(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 투척/속공 기술
     * POST /api/rogue/throw
     */
    @PostMapping("/throw")
    public Map<String, Object> throwAttack(@RequestBody RogueRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Rogue.throwAttack(req.dexterity, req.swiftness, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }
}
