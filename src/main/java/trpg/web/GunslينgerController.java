package trpg.web;

import main.Result;
import main.secondary.archer.Gunslinger;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Gunslinger (건슬링거) damage calculations.
 * Provides endpoints for the secondary Gunslinger class skills.
 */
@RestController
@RequestMapping("/api/gunslinger")
public class GunslينgerController {

    /**
     * Request body for Gunslinger skill calculations.
     */
    public static class GunslينgerRequest {
        public int stat;
        public int swiftness;
        public boolean isFirstShot;
        public boolean dodgedLastTurn;
        public boolean isJudgeTurn;
        public boolean isJudgementTarget;
        public boolean isBackstabActive;
        public int damageTaken;
        public int baseDamage;
        public int precision;
        public int level;
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
     * POST /api/gunslinger/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.plain(req.stat, req.isFirstShot, req.dodgedLastTurn,
                req.isJudgeTurn, req.isJudgementTarget, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 더블샷 (2D6)
     * POST /api/gunslinger/double-shot
     */
    @PostMapping("/double-shot")
    public Map<String, Object> doubleShot(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.doubleShot(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 헤드샷 (D20)
     * POST /api/gunslinger/headshot
     */
    @PostMapping("/headshot")
    public Map<String, Object> headshot(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.HeadShot(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 퀵드로우 (D8/4D8)
     * POST /api/gunslinger/quick-draw
     */
    @PostMapping("/quick-draw")
    public Map<String, Object> quickDraw(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.quickDraw(req.stat, req.isFirstShot, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 일점사 (6D6)
     * POST /api/gunslinger/focus-fire
     */
    @PostMapping("/focus-fire")
    public Map<String, Object> focusFire(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.focusedFire(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 백스탭 반격 데미지
     * POST /api/gunslinger/backstab
     */
    @PostMapping("/backstab")
    public Map<String, Object> backstab(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.backStab(req.stat, req.damageTaken, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 경고 (반격 300%) - plain with warning=true
     * POST /api/gunslinger/warning
     */
    @PostMapping("/warning")
    public Map<String, Object> warning(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.plain(req.stat, false, false, false, false, true, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 예고장 (반격 200%) - opportunity
     * POST /api/gunslinger/notice
     */
    @PostMapping("/notice")
    public Map<String, Object> notice(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.opportunity(req.stat, req.baseDamage, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 활약 기회 (신속 판정)
     * POST /api/gunslinger/active-opportunity
     */
    @PostMapping("/active-opportunity")
    public Map<String, Object> activeOpportunity(@RequestBody GunslينgerRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Gunslinger.opportunity(req.swiftness, req.baseDamage, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
