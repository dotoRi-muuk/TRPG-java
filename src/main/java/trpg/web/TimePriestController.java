package trpg.web;

import main.Result;
import main.secondary.priest.TimePriest;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for TimePriest (시간의 사제) skill calculations.
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * 직업 외 최종 데미지 기본값: 100% (1배). 최종 데미지는 곱연산으로 적용.
 */
@RestController
@RequestMapping("/api/timepriest")
public class TimePriestController {

    /**
     * Request body for TimePriest skill calculations.
     * <ul>
     *   <li>intelligence     - 지능(지혜) 스탯</li>
     *   <li>level            - 캐릭터 레벨 (최종 데미지 기본 계수: (100 + 레벨²)%)</li>
     *   <li>damageBonus      - 직업 외 데미지 증가 (%, 합연산, 기본값 0)</li>
     *   <li>finalDamageBonus - 직업 외 최종 데미지 증가 (%, 곱연산, 기본값 100 = 1배)</li>
     *   <li>precision        - 정밀 스탯 (치명타 판정)</li>
     *   <li>chantTurns       - 영창 턴 수 (부식 전용)</li>
     *   <li>impatience       - 성급함 스킬 적용 여부 (행동 횟수 n+1일 때 자신 피해 +100n%)</li>
     *   <li>turns            - 행동 횟수 (성급함 스킬용)</li>
     *   <li>piety            - 신앙심 스킬 발동 여부 (아군 데미지 +75%)</li>
     * </ul>
     */
    public static class TimePriestRequest {
        public int intelligence;
        public int level;
        public int damageBonus;
        public int finalDamageBonus = 100;
        public int precision;
        public int chantTurns = 1;
        public boolean impatience;
        public int turns = 1;
        public boolean piety;
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
     * POST /api/timepriest/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.plain(req.intelligence, req.impatience, req.turns, req.piety,
                req.damageBonus, req.finalDamageBonus, req.level, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 부식 (자유 영창, 영창 턴수×D12) - 마나 8, 쿨타임 10턴
     * POST /api/timepriest/corrosion
     */
    @PostMapping("/corrosion")
    public Map<String, Object> corrosion(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int chantTurns = Math.max(1, req.chantTurns);
        Result result = TimePriest.corrosion(req.intelligence, chantTurns, req.impatience, req.turns, req.piety,
                req.damageBonus, req.finalDamageBonus, req.level, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 가속 (아군 1명에게 1회 추가 행동 부여) - 마나 8, 쿨타임 3턴
     * POST /api/timepriest/acceleration
     */
    @PostMapping("/acceleration")
    public Map<String, Object> acceleration(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.acceleration(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 정지 (대상 다음 턴 행동 불가) - 마나 7, 쿨타임 5턴
     * POST /api/timepriest/stop
     */
    @PostMapping("/stop")
    public Map<String, Object> stop(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.stop(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 유예 (대상 다음 턴 공격 불가 + 다음 피격 데미지 +75%) - 마나 9, 쿨타임 4턴
     * POST /api/timepriest/suspension
     */
    @PostMapping("/suspension")
    public Map<String, Object> suspension(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.suspension(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 감속 (대상 다음 턴 수비 불가 + 다음 피격 최종 데미지 ×2) - 마나 13, 쿨타임 5턴
     * POST /api/timepriest/deceleration
     */
    @PostMapping("/deceleration")
    public Map<String, Object> deceleration(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.deceleration(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 시간의 틈새 (자유 영창, 판정 없음 - 자신+적 1명 행동 불가) - 매 턴 마나 7 소모
     * POST /api/timepriest/time-gap
     */
    @PostMapping("/time-gap")
    public Map<String, Object> timeGap(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.timeGap(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 강탈 (매 턴 50% 확률 아군 행동 불가, 영구 지속) - 마나 10
     * POST /api/timepriest/seizure
     */
    @PostMapping("/seizure")
    public Map<String, Object> seizure(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.seizure(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 자기중심 (아군 스탯 -3, 버프 대상 자신 고정, 자신 지능/지혜 +5, 영구 지속) - 마나 13
     * POST /api/timepriest/self-centered
     */
    @PostMapping("/self-centered")
    public Map<String, Object> selfCentered(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.selfCentered(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 성급함 ([냉정] 변경 - 자신 피해 +100n%, 아군 행동 제한, 영구 지속) - 마나 10
     * POST /api/timepriest/impatience-skill
     */
    @PostMapping("/impatience-skill")
    public Map<String, Object> impatienceSkill(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.impatienceSkill(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 기복 ([침착] 제거, 자신 행동 불가 무시, 아군 피해 ×2, 영구 지속) - 마나 15
     * POST /api/timepriest/fluctuation
     */
    @PostMapping("/fluctuation")
    public Map<String, Object> fluctuation(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.fluctuation(ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 신앙심 (모든 아군 행동 +1, 모든 적 행동 불가, 아군 데미지 +75%) - 마나 10, 쿨타임 9턴
     * POST /api/timepriest/piety-skill
     */
    @PostMapping("/piety-skill")
    public Map<String, Object> pietySkill(@RequestBody TimePriestRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = TimePriest.pietySkill(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }
}
