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
     * Calculate Sniper plain attack damage (1D6).
     * POST /api/sniper/plain
     *
     * @param stat             사용할 스탯
     * @param turnsSinceAttack 마지막으로 공격받은 이후 턴 수 (5 이상 시 급소조준 발동)
     * @param assemble         조립 기술 적용 여부
     * @param aim              조준 기술 적용 여부
     * @param sureHit          필즉 스킬 적용 여부
     * @param stabilize        안정화 스킬 적용 여부
     * @param immersion        몰입 스킬 적용 여부
     * @param conviction       확신 스킬 적용 여부
     * @param heightenedSenses 신경 극대화 스킬 적용 여부
     * @param precision        정밀 스탯
     * @return 계산 결과 (output, damageDealt, succeeded, staminaUsed)
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(
            @RequestParam int stat,
            @RequestParam int turnsSinceAttack,
            @RequestParam(defaultValue = "false") boolean assemble,
            @RequestParam(defaultValue = "false") boolean aim,
            @RequestParam(defaultValue = "false") boolean sureHit,
            @RequestParam(defaultValue = "false") boolean stabilize,
            @RequestParam(defaultValue = "false") boolean immersion,
            @RequestParam(defaultValue = "false") boolean conviction,
            @RequestParam(defaultValue = "false") boolean heightenedSenses,
            @RequestParam int precision) {

        boolean vitalAim = turnsSinceAttack >= 5;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Sniper.plain(stat, vitalAim, assemble, aim, sureHit, stabilize,
                immersion, conviction, heightenedSenses, precision, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("output", baos.toString(StandardCharsets.UTF_8));
        response.put("damageDealt", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * Calculate Sniper fire attack damage (5D20, consumes bullet).
     * POST /api/sniper/fire
     *
     * @param stat             사용할 스탯
     * @param turnsSinceAttack 마지막으로 공격받은 이후 턴 수 (5 이상 시 급소조준 발동)
     * @param deathBullet      죽음의 탄환 적용 여부 (전투 중 기본 공격 미사용 시)
     * @param assemble         조립 기술 적용 여부
     * @param aim              조준 기술 적용 여부
     * @param sureHit          필즉 스킬 적용 여부
     * @param stabilize        안정화 스킬 적용 여부
     * @param immersion        몰입 스킬 적용 여부
     * @param conviction       확신 스킬 적용 여부
     * @param heightenedSenses 신경 극대화 스킬 적용 여부
     * @param precision        정밀 스탯
     * @return 계산 결과 (output, damageDealt, succeeded, staminaUsed)
     */
    @PostMapping("/fire")
    public Map<String, Object> fire(
            @RequestParam int stat,
            @RequestParam int turnsSinceAttack,
            @RequestParam(defaultValue = "false") boolean deathBullet,
            @RequestParam(defaultValue = "false") boolean assemble,
            @RequestParam(defaultValue = "false") boolean aim,
            @RequestParam(defaultValue = "false") boolean sureHit,
            @RequestParam(defaultValue = "false") boolean stabilize,
            @RequestParam(defaultValue = "false") boolean immersion,
            @RequestParam(defaultValue = "false") boolean conviction,
            @RequestParam(defaultValue = "false") boolean heightenedSenses,
            @RequestParam int precision) {

        boolean vitalAim = turnsSinceAttack >= 5;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = Sniper.fire(stat, vitalAim, deathBullet, assemble, aim, sureHit, stabilize,
                immersion, conviction, heightenedSenses, precision, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("output", baos.toString(StandardCharsets.UTF_8));
        response.put("damageDealt", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }
}
