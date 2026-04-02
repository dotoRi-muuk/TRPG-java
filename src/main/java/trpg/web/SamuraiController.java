package trpg.web;

import main.Result;
import main.secondary.warrior.BladeMaster;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for BladeMaster/Samurai (무사) damage calculations.
 * Provides endpoints for the secondary BladeMaster class skills.
 */
@RestController
@RequestMapping("/api/samurai")
public class SamuraiController {

    /**
     * Request body for BladeMaster skill calculations.
     * isMula      → objectI   (물아 모드)
     * kakugo      → resolve   (각오)
     * seishaKetsudan → lifeOrDeath (생사결단)
     * currentHP / maxHP → oneStrikeKill check (체력 40% 이하)
     */
    public static class SamuraiRequest {
        public int stat;
        public boolean isMula;
        public boolean kakugo;
        public boolean seishaKetsudan;
        public boolean scatteringSwordDance;
        public int currentHP;
        public int maxHP;
        public int consumedStamina;
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

    private static boolean isOneStrikeKill(SamuraiRequest req) {
        return req.maxHP > 0 && req.currentHP <= req.maxHP * 0.4;
    }

    /**
     * 기본공격 (D6)
     * POST /api/samurai/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody SamuraiRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        BladeMaster bm = new BladeMaster();
        Result result = bm.plain(req.stat, isOneStrikeKill(req), req.kakugo, req.seishaKetsudan,
                false, false, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 일섬 (D20)
     * POST /api/samurai/il-seom
     */
    @PostMapping("/il-seom")
    public Map<String, Object> ilSeom(@RequestBody SamuraiRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        BladeMaster bm = new BladeMaster();
        Result result = bm.flash(req.stat, req.isMula, isOneStrikeKill(req), req.kakugo, req.seishaKetsudan,
                false, false, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 난격 (5D4)
     * POST /api/samurai/ranged-attack
     */
    @PostMapping("/ranged-attack")
    public Map<String, Object> rangedAttack(@RequestBody SamuraiRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        BladeMaster bm = new BladeMaster();
        Result result = bm.rampage(req.stat, req.isMula, isOneStrikeKill(req), req.kakugo, req.seishaKetsudan,
                false, false, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 섬격 (D12 x2)
     * POST /api/samurai/flash-strike
     */
    @PostMapping("/flash-strike")
    public Map<String, Object> flashStrike(@RequestBody SamuraiRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        BladeMaster bm = new BladeMaster();
        Result result = bm.flashStrike(req.stat, req.isMula, isOneStrikeKill(req), req.kakugo, req.seishaKetsudan,
                false, false, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 종점 (3D20 + 소모 스태미나 × 2)
     * POST /api/samurai/final-point
     */
    @PostMapping("/final-point")
    public Map<String, Object> finalPoint(@RequestBody SamuraiRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        BladeMaster bm = new BladeMaster();
        Result result = bm.terminus(req.stat, req.consumedStamina, req.isMula, isOneStrikeKill(req),
                req.kakugo, req.seishaKetsudan, false, false, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 개화 (8D6)
     * POST /api/samurai/bloom
     */
    @PostMapping("/bloom")
    public Map<String, Object> bloom(@RequestBody SamuraiRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        BladeMaster bm = new BladeMaster();
        Result result = bm.blooming(req.stat, req.isMula, isOneStrikeKill(req), req.kakugo, req.seishaKetsudan,
                false, false, false, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
