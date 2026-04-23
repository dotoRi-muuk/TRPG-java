package trpg.web;

import main.Result;
import main.secondary.archer.Crossbowman;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/crossbowman")
public class CrossbowmanController {

    public static class CrossbowmanRequest {
        public int stat;
        public int arrows = 1;
        public int arrowsToBreak = 1;
        public int damageTaken = 0;
        public int precision;
        public int level = 1;
        public int damageIncrease;
        public int finalDamageIncrease = 100;
        public boolean focusedAttack;
        public boolean isErrorRemoval;
        public boolean isDistanceCalc;
        public boolean isExecutionArrow;
        public boolean scarletRainActive;
        public int arrowsFirst = 1;
        public int arrowsSecond = 1;
        public int arrowsThird = 1;
        public int arrowsFourth = 1;
    }

    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.plain(
                req.stat, req.arrows, req.isErrorRemoval || req.focusedAttack, req.isExecutionArrow, req.isDistanceCalc, req.precision,
                req.level, req.damageIncrease, req.finalDamageIncrease, req.scarletRainActive, ps
        );
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/throw")
    public Map<String, Object> throwWeapon(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.throwWeapon(
                req.stat, req.precision, req.level, req.damageIncrease, req.finalDamageIncrease, req.scarletRainActive, ps
        );
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/quick-load")
    public Map<String, Object> quickLoad() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.quickReload(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/single-shot")
    public Map<String, Object> singleShot(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.singleShot(
                req.stat, req.arrows, req.precision, req.level, req.damageIncrease, req.finalDamageIncrease, req.scarletRainActive, ps
        );
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/rage-arrow")
    public Map<String, Object> rageArrow(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.flareArrow(
                req.stat, req.arrows, req.precision, req.level, req.damageIncrease, req.finalDamageIncrease, req.scarletRainActive, ps
        );
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/paralyze-arrow")
    public Map<String, Object> paralyzeArrow(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.paralysisArrow(
                req.stat, req.arrows, req.precision, req.level, req.damageIncrease, req.finalDamageIncrease, req.scarletRainActive, ps
        );
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/break-arrows")
    public Map<String, Object> breakArrows(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.breakArrow(req.arrowsToBreak, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/desperate-load")
    public Map<String, Object> desperateLoad() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.inTheseTimes(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/scarlet-rain")
    public Map<String, Object> scarletRain() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.scarletRain(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/final-reload")
    public Map<String, Object> finalReload(@RequestBody CrossbowmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Crossbowman.finalReloadAttack(
                req.stat, req.arrowsFirst, req.arrowsSecond, req.arrowsThird, req.arrowsFourth,
                req.isErrorRemoval || req.focusedAttack, req.isDistanceCalc, req.precision, req.level,
                req.damageIncrease, req.finalDamageIncrease, req.scarletRainActive, ps
        );
        ps.flush();
        return buildResponse(result, baos);
    }
}
