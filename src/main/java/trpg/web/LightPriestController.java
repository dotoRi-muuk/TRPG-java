package trpg.web;

import main.Result;
import main.secondary.priest.LightPriest;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for LightPriest (빛의 사제) skill calculations.
 * <p>
 * 회복량 계산 공식: [(기본 회복량) x (100 + 회복량 증가)%] x (최종 회복량 증가)% x (주사위 보정)
 * 레벨 기반 최종 회복량 기본 계수: (100 + (레벨)^2 / 2)%
 */
@RestController
@RequestMapping("/api/lightpriest")
public class LightPriestController {

    /**
     * Request body for LightPriest skill calculations.
     * <ul>
     *   <li>intelligence   - 지능(지혜) 스탯</li>
     *   <li>hasAttacked    - 전투 내 공격 이력 (true 이면 자비 패시브 비활성화)</li>
     *   <li>favoritism     - 편애 특수 스킬 적용 여부 (회복량 +50%)</li>
     *   <li>transfer       - 양도 특수 스킬 적용 여부 (회복량 +50%)</li>
     *   <li>piety          - 신앙심 특수 스킬 적용 여부 (회복량 +100%)</li>
     *   <li>healBonus      - 직업 스킬 외 추가 회복량 증가 (%)</li>
     *   <li>finalHealBonus - 직업 스킬 외 추가 최종 회복량 증가 (%)</li>
     *   <li>level          - 캐릭터 레벨 (최종 회복량 기본 계수 산출)</li>
     *   <li>precision      - 정밀 스탯 (공격 스킬에 사용)</li>
     *   <li>chantTurns     - 영창 턴수 (기도 스킬에 사용)</li>
     * </ul>
     */
    public static class LightPriestRequest {
        public int intelligence;
        public boolean hasAttacked;
        public boolean favoritism;
        public boolean transfer;
        public boolean piety;
        public int healBonus;
        public int finalHealBonus;
        public int level;
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
     * 기본공격 (1D6) – 공격 스킬
     * POST /api/lightpriest/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightPriest.plain(req.intelligence, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 힐 (1D6) – 회복 스킬 (마나 1 소모)
     * POST /api/lightpriest/heal
     */
    @PostMapping("/heal")
    public Map<String, Object> heal(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        boolean mercy = !req.hasAttacked;
        Result result = LightPriest.heal(req.intelligence, mercy, req.favoritism, req.transfer,
                req.piety, req.healBonus, req.finalHealBonus, req.level, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 치유의 바람 (2D6) – 공격 스킬 (마나 2 소모, 쿨타임 2턴)
     * POST /api/lightpriest/healing-wind
     */
    @PostMapping("/healing-wind")
    public Map<String, Object> healingWind(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightPriest.healingWind(req.intelligence, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 빛의 성배 (5D4) – 회복 스킬 (마나 3 소모, 쿨타임 4턴)
     * POST /api/lightpriest/chalice-of-light
     */
    @PostMapping("/chalice-of-light")
    public Map<String, Object> chaliceOfLight(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        boolean mercy = !req.hasAttacked;
        Result result = LightPriest.holyGrailOfLight(req.intelligence, mercy, req.favoritism,
                req.transfer, req.piety, req.healBonus, req.finalHealBonus, req.level, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 기원 (5D8 보호막) – 보호막 스킬 (마나 4 소모, 쿨타임 6턴)
     * POST /api/lightpriest/invocation
     */
    @PostMapping("/invocation")
    public Map<String, Object> invocation(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = LightPriest.invocation(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 기도 (영창 턴수 x D6) – 회복 스킬 (마나 4 소모, 쿨타임 8턴)
     * POST /api/lightpriest/prayer
     */
    @PostMapping("/prayer")
    public Map<String, Object> prayer(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        boolean mercy = !req.hasAttacked;
        int chantTurns = Math.max(1, req.chantTurns);
        int level = Math.max(0, req.level);
        Result result = LightPriest.prayer(req.intelligence, mercy, req.favoritism, req.transfer,
                req.piety, chantTurns, req.healBonus, req.finalHealBonus, level, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 헤븐즈 도어 (4D10 부활) – 부활 스킬 (마나 15 소모, 영창 3턴, 쿨타임 20턴)
     * POST /api/lightpriest/heavens-door
     */
    @PostMapping("/heavens-door")
    public Map<String, Object> heavensDoor(@RequestBody LightPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        boolean mercy = !req.hasAttacked;
        Result result = LightPriest.heavensDoor(req.intelligence, mercy, req.favoritism, req.transfer,
                req.piety, req.healBonus, req.finalHealBonus, req.level, ps);
        ps.flush();
        return buildResponse(result, baos);
    }
}
