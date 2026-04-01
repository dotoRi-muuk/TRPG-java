package trpg.web;

import main.Result;
import main.secondary.mage.BarrierMage;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for BarrierMage (결계술사) damage calculations.
 * Provides endpoints for the BarrierMage class skills.
 */
@RestController
@RequestMapping("/api/barrier-mage")
public class BarrierMageController {

    /**
     * Request body for BarrierMage skill calculations.
     * <p>
     * Selectable buff flags: outputBarrier, swiftBarrier, reinforceBarrier, sealBarrier, cloneBarrier.
     * Note: barrierExpansion is excluded from the DTO as it is always active (passive).
     */
    public static class BarrierMageRequest {
        public int stat;
        public int castSum;
        public int reinforceBarrierCast;
        public int precision;
        public boolean outputBarrier;   // 출력 결계: 대상 힘·지능 +4 (매 턴 마나 1 소모), 데미지 계산에 영향 없음
        public boolean swiftBarrier;    // 신속 결계: 대상 신속·민첩 +4 (매 턴 마나 1 소모), 데미지 계산에 영향 없음
        public boolean reinforceBarrier;
        public boolean sealBarrier;
        public boolean cloneBarrier;
        public int selectedCount;
        public int totalManaSpent;
        public int level = 1;
    }

    /**
     * Build standard skill response map from a Result.
     */
    private static Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("manaUsed", result.manaUsed());
        return response;
    }

    /**
     * Basic attack (기본공격): 1D6 damage.
     * Damage formula: [(Base Damage) x (100 + Damage)%] x (Final Damage)% x (Dice Bonus)
     * POST /api/barrier-mage/plain
     *
     * @param req 요청 본문 (stat, castSum, reinforceBarrier, reinforceBarrierCast, sealBarrier, cloneBarrier, precision)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody BarrierMageRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = BarrierMage.plain(req.stat, req.castSum,
                req.reinforceBarrier, req.reinforceBarrierCast,
                req.sealBarrier, req.cloneBarrier,
                req.precision, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Force Field (역장 결계): Generate 1D6 shield per turn (3 mana/turn).
     * POST /api/barrier-mage/force-field
     *
     * @param req 요청 본문 (stat, castSum)
     * @return 계산 결과 (log, succeeded, manaUsed)
     */
    @PostMapping("/force-field")
    public Map<String, Object> forceField(@RequestBody BarrierMageRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = BarrierMage.forceField(req.stat, req.castSum, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Barrier Afterimage (결계 잔영): Reapply selected previous barriers to self + 1 target.
     * Cost: selectedCount * 3 mana, CD: 6 turns.
     * POST /api/barrier-mage/barrier-afterimage
     *
     * @param req 요청 본문 (stat, selectedCount)
     * @return 계산 결과 (log, succeeded, manaUsed)
     */
    @PostMapping("/barrier-afterimage")
    public Map<String, Object> barrierAfterimage(@RequestBody BarrierMageRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = BarrierMage.barrierAfterimage(req.stat, req.selectedCount, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Over Deployment (초과 전개): Deploy another barrier while one is active.
     * ([전개], [영역] effects excluded) (2 extra mana/turn)
     * POST /api/barrier-mage/over-deployment
     *
     * @param req 요청 본문 (stat)
     * @return 계산 결과 (log, succeeded, manaUsed)
     */
    @PostMapping("/over-deployment")
    public Map<String, Object> overDeployment(@RequestBody BarrierMageRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = BarrierMage.overDeployment(req.stat, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * Mana Recovery (기운 회수): Remove all barriers, recover 50% of spent mana.
     * Cost: 2 mana, CD: 7 turns.
     * POST /api/barrier-mage/mana-recovery
     *
     * @param req 요청 본문 (stat, totalManaSpent)
     * @return 계산 결과 (log, succeeded, manaUsed)
     */
    @PostMapping("/mana-recovery")
    public Map<String, Object> manaRecovery(@RequestBody BarrierMageRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = BarrierMage.manaRecovery(req.stat, req.totalManaSpent, req.level, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
