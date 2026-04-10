package trpg.web;

import main.Result;
import main.secondary.priest.LightningPriest;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for LightningPriest (번개의 사제) skill calculations.
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지 증가)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 */
@RestController
@RequestMapping("/api/lightningpriest")
public class LightningPriestController {

    /**
     * Request body for LightningPriest skill calculations.
     * <ul>
     *   <li>intelligence   - 지능(지혜) 스탯</li>
     *   <li>level          - 캐릭터 레벨 (최종 데미지 기본 계수: (100 + 레벨²)%)</li>
     *   <li>damageBonus    - 직업 외 데미지 증가 (%)</li>
     *   <li>finalDamageBonus - 직업 외 최종 데미지 증가 (%)</li>
     *   <li>monopoly       - 독점 스킬 적용 여부 (아군 데미지 n% 증가를 본인 최종 데미지 n+100% 증가로 변경)</li>
     *   <li>monopolyAmount - 독점 적용 시 n 값 (%)</li>
     *   <li>piety          - 신앙심 스킬 적용 여부 (아군 데미지 50% 증가)</li>
     *   <li>precision      - 정밀 스탯 (치명타 판정)</li>
     *   <li>chantTurns     - 영창 턴 수 (일렉트릭 필드 전용)</li>
     * </ul>
     */
    public static class LightningPriestRequest {
        public int intelligence;
        public int level;
        public int damageBonus;
        public int finalDamageBonus;
        public boolean monopoly;
        public int monopolyAmount;
        public boolean piety;
        public int precision;
        public int chantTurns;
    }

    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("manaUsed", result.manaUsed());
        return response;
    }

    /**
     * 기본공격 (1D6)
     * POST /api/lightningpriest/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.plain(req.intelligence, req.monopoly, req.monopolyAmount,
                req.piety, req.damageBonus, req.finalDamageBonus, req.level, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 스파크 (4D6) - 마나 3
     * POST /api/lightningpriest/spark
     */
    @PostMapping("/spark")
    public Map<String, Object> spark(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.spark(req.intelligence, req.monopoly, req.monopolyAmount,
                req.piety, req.damageBonus, req.finalDamageBonus, req.level, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 체인 라이트닝 - 적 피해 (4D8) - 마나 5, 쿨타임 3턴
     * POST /api/lightningpriest/chain-lightning-damage
     */
    @PostMapping("/chain-lightning-damage")
    public Map<String, Object> chainLightningDamage(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.chainLightning(req.intelligence, req.monopoly, req.monopolyAmount,
                req.piety, req.damageBonus, req.finalDamageBonus, req.level, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 체인 라이트닝 - 아군 보호막 (2D4) - 마나 5, 쿨타임 3턴
     * POST /api/lightningpriest/chain-lightning-shield
     */
    @PostMapping("/chain-lightning-shield")
    public Map<String, Object> chainLightningShield(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.chainLightningShield(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 일렉트릭 필드 (영창 n턴 × D12) - 마나 8, 쿨타임 7턴
     * POST /api/lightningpriest/electric-field
     */
    @PostMapping("/electric-field")
    public Map<String, Object> electricField(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int chantTurns = Math.max(1, req.chantTurns);
        Result result = LightningPriest.electricField(req.intelligence, chantTurns, req.monopoly,
                req.monopolyAmount, req.piety, req.damageBonus, req.finalDamageBonus, req.level,
                req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 스트라이크 (3D20) - 마나 5, 쿨타임 5턴
     * POST /api/lightningpriest/strike
     */
    @PostMapping("/strike")
    public Map<String, Object> strike(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.strike(req.intelligence, req.monopoly, req.monopolyAmount,
                req.piety, req.damageBonus, req.finalDamageBonus, req.level, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 일레이스터 (아군 1명 공격 데미지 100% 증가 버프) - 마나 5, 쿨타임 5턴
     * POST /api/lightningpriest/elraister
     */
    @PostMapping("/elraister")
    public Map<String, Object> elraister(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.elraister(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 신뇌격 (4D20) - 영창 2턴, 마나 4
     * POST /api/lightningpriest/divine-lightning
     */
    @PostMapping("/divine-lightning")
    public Map<String, Object> divineLightning(@RequestBody LightningPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightningPriest.divineThunderStrike(req.intelligence, req.monopoly,
                req.monopolyAmount, req.piety, req.damageBonus, req.finalDamageBonus, req.level,
                req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }
}
