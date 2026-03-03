package trpg.web;

import main.Result;
import main.hidden.Sniper;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for Sniper damage calculations.
 * Provides endpoints for the hidden Sniper class attacks.
 */
@RestController
@RequestMapping("/api/sniper")
public class DamageController {

    /**
     * Request body for Sniper skill calculations.
     */
    public static class SniperRequest {
        public int stat;
        public int turnsSinceAttack;
        public int numBuffs;
        public boolean deathBullet;
        public boolean secure;
        public boolean assemble;
        public boolean load;
        public boolean aim;
        public boolean sureHit;
        public boolean stabilize;
        public boolean immersion;
        public boolean conviction;
        public boolean heightenedSenses;
        public int precision;
    }

    /**
     * Calculate Sniper plain attack damage (1D6).
     * POST /api/sniper/plain
     *
     * @param req 요청 본문 (stat, turnsSinceAttack, numBuffs, secure, assemble, load, aim, sureHit,
     *            stabilize, immersion, conviction, heightenedSenses, precision)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody SniperRequest req) {
        boolean vitalAim = req.turnsSinceAttack >= 5;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Sniper.plain(req.stat, vitalAim, req.secure, req.assemble, req.load, req.aim, req.sureHit,
                req.stabilize, req.immersion, req.conviction, req.heightenedSenses, req.numBuffs, req.precision, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Sniper fire attack damage (5D20, consumes bullet).
     * POST /api/sniper/fire
     *
     * @param req 요청 본문 (stat, turnsSinceAttack, numBuffs, deathBullet, secure, assemble, load, aim, sureHit,
     *            stabilize, immersion, conviction, heightenedSenses, precision)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/fire")
    public Map<String, Object> fire(@RequestBody SniperRequest req) {
        boolean vitalAim = req.turnsSinceAttack >= 5;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Sniper.fire(req.stat, vitalAim, req.deathBullet, req.secure, req.assemble, req.load, req.aim,
                req.sureHit, req.stabilize, req.immersion, req.conviction, req.heightenedSenses, req.numBuffs, req.precision, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }
}
