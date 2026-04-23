package trpg.web;

import main.Result;
import main.secondary.archer.MasterArcher;
import main.secondary.archer.MasterArcherPassive;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/masterarcher")
public class MasterArcherController {

    public static class MasterArcherRequest {
        public int stat;
        public boolean isHeavyString;
        public boolean isFirstTarget = true;
        public boolean isEmergencyShot;
        public boolean preyEnabled;
        public int arrowOverheatCount;
        public boolean calm;
        public boolean cracking;
        public int stageTurn;
        public int precision;
        public int level = 1;
        public int damageIncrease;
        public int finalDamageIncrease = 100;
        public int actionCount = 1;
        public int weaponCount = 1;
        public boolean nextActionFailed;
    }

    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    private static Map<String, Object> execute(MasterArcherRequest req, MasterArcherPassive passive, boolean finaleArrow) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result;
        if (finaleArrow) {
            result = MasterArcher.finaleArrow(
                    req.stat, req.isHeavyString, req.isFirstTarget, req.isEmergencyShot,
                    req.preyEnabled, req.arrowOverheatCount, req.calm, req.cracking, req.stageTurn,
                    req.precision, req.level, req.damageIncrease, req.finalDamageIncrease, req.actionCount, ps
            );
        } else {
            result = MasterArcher.plain(
                    req.stat, req.isHeavyString, req.isFirstTarget, req.isEmergencyShot, passive,
                    req.preyEnabled, req.arrowOverheatCount, req.calm, req.cracking, req.stageTurn,
                    req.precision, req.level, req.damageIncrease, req.finalDamageIncrease, false, 0, ps
            );
        }
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.NONE, false);
    }

    @PostMapping("/emergency-shot")
    public Map<String, Object> emergencyShot(@RequestBody MasterArcherRequest req) {
        req.isEmergencyShot = true;
        return execute(req, MasterArcherPassive.NONE, false);
    }

    @PostMapping("/power-shot")
    public Map<String, Object> powerShot(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.POWER_SHOT, false);
    }

    @PostMapping("/explosive-arrow")
    public Map<String, Object> explosiveArrow(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.EXPLOSIVE_ARROW, false);
    }

    @PostMapping("/split-arrow")
    public Map<String, Object> splitArrow(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.SPLIT_ARROW, false);
    }

    @PostMapping("/piercing-arrow")
    public Map<String, Object> piercingArrow(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.PENETRATING_ARROW, false);
    }

    @PostMapping("/double-shot")
    public Map<String, Object> doubleShot(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.DOUBLE_SHOT, false);
    }

    @PostMapping("/twilight-meteor")
    public Map<String, Object> twilightMeteor() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = MasterArcher.twilightMeteor(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    @PostMapping("/finale-arrow")
    public Map<String, Object> finaleArrow(@RequestBody MasterArcherRequest req) {
        return execute(req, MasterArcherPassive.NONE, true);
    }

    @PostMapping("/mana-weapon")
    public Map<String, Object> manaWeapon(@RequestBody MasterArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = MasterArcher.manaWeapon(req.weaponCount, ps);
        ps.flush();
        Map<String, Object> response = buildResponse(result, baos);
        response.put("manaUsed", result.manaUsed());
        return response;
    }

    @PostMapping("/phantom-shift")
    public Map<String, Object> phantomShift(@RequestBody MasterArcherRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = MasterArcher.phantomShift(req.nextActionFailed, ps);
        ps.flush();
        Map<String, Object> response = buildResponse(result, baos);
        response.put("manaUsed", result.manaUsed());
        return response;
    }
}
