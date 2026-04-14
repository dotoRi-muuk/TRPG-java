package trpg.web;

import main.Result;
import main.secondary.priest.SoulPriest;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for SoulPriest (영혼의 사제) skill calculations.
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * 직업 외 최종 데미지 기본값: 100% (1배)
 */
@RestController
@RequestMapping("/api/soulpriest")
public class SoulPriestController {

    /**
     * Request body for SoulPriest skill calculations.
     * <ul>
     *   <li>intelligence   - 지능(지혜) 스탯</li>
     *   <li>soul           - 현재 영혼 누적 개수 (축복/폐허 패시브 적용)</li>
     *   <li>ruins          - 폐허 스킬 사용 여부 (축복 효과 변경)</li>
     *   <li>soulUse        - 원한 스킬에 소모할 영혼 개수</li>
     *   <li>level          - 캐릭터 레벨 (최종 데미지 기본 계수: (100 + 레벨²)%)</li>
     *   <li>damageBonus    - 직업 외 데미지 증가 (%, 합연산, 기본값 0)</li>
     *   <li>finalDamageBonus - 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)</li>
     *   <li>precision      - 정밀 스탯 (치명타 판정)</li>
     * </ul>
     */
    public static class SoulPriestRequest {
        public int intelligence;
        public int soul;
        public boolean ruins;
        public int soulUse;
        public int level;
        public int damageBonus;
        public int finalDamageBonus = 100;
        public int precision;
    }

    private static final SoulPriest SOUL_PRIEST = new SoulPriest();

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
     * POST /api/soulpriest/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody SoulPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = SOUL_PRIEST.plain(req.intelligence, req.soul, req.ruins,
                req.level, req.damageBonus, req.finalDamageBonus, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 저주 (3D10) - [영혼] 2개 소모, 쿨타임 4턴
     * POST /api/soulpriest/curse
     */
    @PostMapping("/curse")
    public Map<String, Object> curse(@RequestBody SoulPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = SOUL_PRIEST.curse(req.intelligence, req.soul, req.ruins,
                req.level, req.damageBonus, req.finalDamageBonus, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 흉통 (8D4) - [영혼] 3개 소모, 쿨타임 6턴
     * POST /api/soulpriest/chest-pain
     */
    @PostMapping("/chest-pain")
    public Map<String, Object> chestPain(@RequestBody SoulPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = SOUL_PRIEST.chestPain(req.intelligence, req.soul, req.ruins,
                req.level, req.damageBonus, req.finalDamageBonus, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 원한 ([영혼 소모 개수]D20)
     * POST /api/soulpriest/grudge
     */
    @PostMapping("/grudge")
    public Map<String, Object> grudge(@RequestBody SoulPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int soulUse = Math.max(1, req.soulUse);
        Result result = SOUL_PRIEST.grudge(req.intelligence, req.soul, req.ruins,
                soulUse, req.level, req.damageBonus, req.finalDamageBonus, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 흡수 (2D12 회복) - [영혼] 1개 소모
     * POST /api/soulpriest/absorb
     */
    @PostMapping("/absorb")
    public Map<String, Object> absorb(@RequestBody SoulPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = SOUL_PRIEST.absorb(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 수거 (D10 영혼 획득) - 쿨타임 8턴
     * POST /api/soulpriest/collect
     */
    @PostMapping("/collect")
    public Map<String, Object> collect(@RequestBody SoulPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = SOUL_PRIEST.collect(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }
}
