package trpg.web;

import main.Result;
import main.secondary.priest.DarkPriest;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for DarkPriest (어둠의 사제) skill calculations.
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * 직업 외 최종 데미지 기본값: 100% (1배)
 */
@RestController
@RequestMapping("/api/darkpriest")
public class DarkPriestController {

    /**
     * Request body for DarkPriest skill calculations.
     * <ul>
     *   <li>intelligence      - 지능(지혜) 스탯</li>
     *   <li>level             - 캐릭터 레벨 (최종 데미지 기본 계수: (100 + 레벨²)%)</li>
     *   <li>damageBonus       - 직업 외 데미지 증가 (%, 합연산, 기본값 0)</li>
     *   <li>finalDamageBonus  - 직업 외 최종 데미지 증가 (%, 곱연산, 기본값 100 = 1배)</li>
     *   <li>precision         - 정밀 스탯 (치명타 판정)</li>
     *   <li>domination        - 지배 패시브 적용 여부 (데미지 +150%)</li>
     *   <li>stolenHp          - 강탈 패시브: 아군이 잃은 체력 (데미지 += stolenHp × 5%)</li>
     *   <li>bloodflow         - 혈류 사용 여부 (저주 스킬로 획득, 최종 데미지 ×2)</li>
     *   <li>scapegoatHit      - 희생양 스킬 공격 번호 (0=미적용, 1=×2, 2=×3, 3=×4, 4=×6)</li>
     *   <li>erosionAllies     - 침식 스킬: [ ]불가 상태 아군 수 (1명당 데미지 +80%)</li>
     *   <li>pietyAllies       - 신앙심 스킬: 행동 불가 부여 인원수 (1명당 데미지 +50%)</li>
     * </ul>
     */
    public static class DarkPriestRequest {
        public int intelligence;
        public int level;
        public int damageBonus;
        public int finalDamageBonus = 100;
        public int precision;
        public boolean domination;
        public int stolenHp;
        public boolean bloodflow;
        public int scapegoatHit;
        public int erosionAllies;
        public int pietyAllies;
    }

    private static final DarkPriest DARK_PRIEST = new DarkPriest();

    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("manaUsed", result.manaUsed());
        return response;
    }

    private static PrintStream buildPrintStream(ByteArrayOutputStream baos) {
        return new PrintStream(baos, true, StandardCharsets.UTF_8);
    }

    /**
     * 기본공격 (1D6)
     * POST /api/darkpriest/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.plain(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 어둠의 기운 (1D4, 광역)
     * POST /api/darkpriest/dark-energy
     */
    @PostMapping("/dark-energy")
    public Map<String, Object> darkEnergy(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.darkEnergy(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 손아귀 (1D8) - 마나 5, 쿨타임 4턴
     * POST /api/darkpriest/grip
     */
    @PostMapping("/grip")
    public Map<String, Object> grip(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.grasp(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 우즈마니아 (4D12) - 영창 2턴, 마나 8, 쿨타임 6턴
     * POST /api/darkpriest/uzumania
     */
    @PostMapping("/uzumania")
    public Map<String, Object> uzumania(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.uzmania(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 엑실리스터 (4D20) - 영창 4턴, 마나 14, 쿨타임 9턴
     * POST /api/darkpriest/exilister
     */
    @PostMapping("/exilister")
    public Map<String, Object> exilister(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.exilister(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 어나이스필레인 (7D12) - 영창 2턴, 마나 18, 쿨타임 12턴
     * POST /api/darkpriest/annihilation-plain
     */
    @PostMapping("/annihilation-plain")
    public Map<String, Object> annihilationPlain(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.anaisPhilane(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 엔시아스티켈리아 (9D10) - 영창 5턴, 마나 16, 쿨타임 13턴
     * POST /api/darkpriest/ensiasticalia
     */
    @PostMapping("/ensiasticalia")
    public Map<String, Object> ensiasticalia(@RequestBody DarkPriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = buildPrintStream(baos);
        Result result = DARK_PRIEST.enthiaStickelia(req.intelligence, req.level, req.damageBonus,
                req.finalDamageBonus, req.precision, req.domination, req.stolenHp,
                req.bloodflow, req.scapegoatHit, req.erosionAllies, req.pietyAllies, ps);
        ps.flush();
        return buildResponse(result, baos);
    }
}
