package trpg.web;

import main.primary.Archer;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Archer (궁수) damage calculations.
 * Provides endpoints for the primary Archer class skills.
 */
@RestController
@RequestMapping("/api/archer")
public class ArcherController {

    /**
     * Request body for Archer skill calculations.
     */
    public static class ArcherRequest {
        public int stat;
        public int strength;
        public int dexterity;
        public int consecutiveHits;
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
     * 기본공격 - 단일 스탯 사용 (D4)
     * POST /api/archer/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.plain(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 기본공격 - 힘과 민첩 동시 사용 (D6)
     * POST /api/archer/plain-dual
     */
    @PostMapping("/plain-dual")
    public Map<String, Object> plainDual(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.plain(req.strength, req.dexterity, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 퀵샷 (3xD4)
     * POST /api/archer/quickshot
     */
    @PostMapping("/quickshot")
    public Map<String, Object> quickShot(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.quickShot(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 대쉬 - 단일 스탯 사용 (D4 * 1.5)
     * POST /api/archer/dash
     */
    @PostMapping("/dash")
    public Map<String, Object> dash(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.dash(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 대쉬 - 힘과 민첩 동시 사용 (D6 * 1.5)
     * POST /api/archer/dash-dual
     */
    @PostMapping("/dash-dual")
    public Map<String, Object> dashDual(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.dash(req.strength, req.dexterity, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 사냥감 - 단일 스탯 사용 (D4)
     * POST /api/archer/hunt
     */
    @PostMapping("/hunt")
    public Map<String, Object> hunt(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.hunt(req.stat, req.consecutiveHits, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }

    /**
     * 사냥감 - 힘과 민첩 동시 사용 (D6)
     * POST /api/archer/hunt-dual
     */
    @PostMapping("/hunt-dual")
    public Map<String, Object> huntDual(@RequestBody ArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int damage = Archer.hunt(req.strength, req.dexterity, req.consecutiveHits, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(damage, baos);
    }
}
