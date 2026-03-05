package trpg.web;

import main.Result;
import main.hidden.Ninja;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for hidden Ninja damage calculations.
 * Provides endpoints for the hidden Ninja class attacks.
 */
@RestController
@RequestMapping("/api/ninja")
public class NinjaController {

    /**
     * Request body for Ninja skill calculations.
     */
    public static class NinjaRequest {
        public int str;
        public int dex;
        public int speed;
        public int shurikenCount;
        public boolean stealthActive;
        public boolean doppelgangerActive;
        public boolean ideologySealActive;
        public String resistanceType; // "none", "pain", "fear"
    }

    /**
     * Resolve whether doppelganger reduction applies.
     * Ideology Seal removes the doppelganger reduction.
     */
    private static boolean resolveDoppelganger(NinjaRequest req) {
        return req.doppelgangerActive && !req.ideologySealActive;
    }

    /**
     * Resolve resistance type, defaulting to "none" if null.
     */
    private static String resolveResistance(NinjaRequest req) {
        return req.resistanceType != null ? req.resistanceType : "none";
    }

    /**
     * Build standard skill response map from a Result.
     */
    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Ninja strike damage (2D6).
     * POST /api/ninja/strike
     *
     * @param req 요청 본문 (str, stealthActive, doppelgangerActive, ideologySealActive, resistanceType)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/strike")
    public Map<String, Object> strike(@RequestBody NinjaRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.strike(req.str, req.stealthActive, resolveDoppelganger(req),
                req.ideologySealActive, resolveResistance(req), ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Calculate Ninja mangle damage (3D8).
     * POST /api/ninja/mangle
     *
     * @param req 요청 본문 (str, stealthActive, doppelgangerActive, ideologySealActive, resistanceType)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/mangle")
    public Map<String, Object> mangle(@RequestBody NinjaRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.mangle(req.str, req.stealthActive, resolveDoppelganger(req),
                req.ideologySealActive, resolveResistance(req), ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Calculate Ninja throw shuriken damage (D8).
     * POST /api/ninja/throw
     *
     * @param req 요청 본문 (dex, stealthActive, doppelgangerActive, ideologySealActive, resistanceType)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/throw")
    public Map<String, Object> throwShuriken(@RequestBody NinjaRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.throwShuriken(req.dex, req.stealthActive, resolveDoppelganger(req),
                req.ideologySealActive, resolveResistance(req), ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Calculate Ninja phantom dance damage (8D6). Requires stealth. Removes doppelganger reduction.
     * POST /api/ninja/phantom-dance
     *
     * @param req 요청 본문 (dex, stealthActive, ideologySealActive, resistanceType)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/phantom-dance")
    public Map<String, Object> phantomDance(@RequestBody NinjaRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Ninja.phantomDance(req.dex, req.stealthActive,
                req.ideologySealActive, resolveResistance(req), ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Calculate Ninja focused throw damage (D10 * shuriken count). Requires dex + speed verdicts.
     * POST /api/ninja/focused-throw
     *
     * @param req 요청 본문 (dex, speed, shurikenCount, stealthActive, doppelgangerActive, ideologySealActive, resistanceType)
     * @return 계산 결과 (damage, log, succeeded, staminaUsed)
     */
    @PostMapping("/focused-throw")
    public Map<String, Object> focusedThrow(@RequestBody NinjaRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int shurikens = Math.max(1, req.shurikenCount);

        Result result = Ninja.focusedThrow(req.dex, req.speed, shurikens, req.stealthActive,
                resolveDoppelganger(req), req.ideologySealActive, resolveResistance(req), ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
