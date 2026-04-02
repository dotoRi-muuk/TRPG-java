package trpg.web;

import main.Result;
import main.secondary.warrior.SpearMaster;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for SpearMaster/Spearman (창술사) damage calculations.
 * Provides endpoints for the secondary SpearMaster class skills.
 */
@RestController
@RequestMapping("/api/spearman")
public class SpearmanController {

    /**
     * Request body for SpearMaster basic attack skill.
     */
    public static class SpearmanBasicRequest {
        public int stat;
        public int precision;
        public int level;
    }

    /**
     * Request body for SpearMaster combo/non-combo skills.
     */
    public static class SpearmanRequest {
        public int stat;
        public int agi;
        public boolean isAdaptationActive;
        public boolean isSplendorActive;
        public int splendorTurns;
        public boolean isAccelerationActive;
        public int linkSuccessCount;
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
     * POST /api/spearman/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.plain(req.stat, req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 돌려 찌르기 (D8)
     * POST /api/spearman/spin-thrust
     */
    @PostMapping("/spin-thrust")
    public Map<String, Object> spinThrust(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.spinStab(req.stat, req.agi, req.precision, req.isSplendorActive,
                req.splendorTurns, req.isAccelerationActive, req.linkSuccessCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 회전 타격 (D10, 연계)
     * POST /api/spearman/spin-strike
     */
    @PostMapping("/spin-strike")
    public Map<String, Object> spinStrike(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.spinStrike(req.stat, req.agi, req.precision, req.isSplendorActive,
                req.splendorTurns, req.isAccelerationActive, req.linkSuccessCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 하단 베기 (D6, 연계)
     * POST /api/spearman/low-slash
     */
    @PostMapping("/low-slash")
    public Map<String, Object> lowSlash(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.lowSlash(req.stat, req.agi, req.precision, req.isSplendorActive,
                req.splendorTurns, req.isAccelerationActive, req.linkSuccessCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * [연계] 정면 찌르기 (2D10)
     * POST /api/spearman/combo-front-thrust
     */
    @PostMapping("/combo-front-thrust")
    public Map<String, Object> comboFrontThrust(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.frontalStab(req.stat, req.agi, req.isAdaptationActive, req.precision,
                req.isSplendorActive, req.splendorTurns, req.isAccelerationActive, req.linkSuccessCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * [연계] 일섬창 (4D8)
     * POST /api/spearman/combo-flash-spear
     */
    @PostMapping("/combo-flash-spear")
    public Map<String, Object> comboFlashSpear(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.flashSpear(req.stat, req.agi, req.isAdaptationActive, req.precision,
                req.isSplendorActive, req.splendorTurns, req.isAccelerationActive, req.linkSuccessCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * [연계] 천뢰격 (5D12)
     * POST /api/spearman/combo-thunder-strike
     */
    @PostMapping("/combo-thunder-strike")
    public Map<String, Object> comboThunderStrike(@RequestBody SpearmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        SpearMaster sm = new SpearMaster();
        Result result = sm.heavenlyThunderStrike(req.stat, req.agi, req.isAdaptationActive, req.precision,
                req.isSplendorActive, req.splendorTurns, req.isAccelerationActive, req.linkSuccessCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
