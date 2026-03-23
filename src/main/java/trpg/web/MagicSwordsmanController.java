package trpg.web;

import main.Result;
import main.secondary.mage.MagicSwordsman;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * REST API Controller for MagicSwordsman (마검사) damage calculations.
 * <p>
 * 판정 사용 스탯: 지능
 * <p>
 * 데미지 공식: [(기본 데미지) × (100 + 데미지 증가 수치)%] × (최종 데미지)% × (주사위 보정)
 */
@RestController
@RequestMapping("/api/magicswordsman")
public class MagicSwordsmanController {

    /**
     * Request body for MagicSwordsman skill calculations.
     * <p>
     * 전달 값:
     * <ul>
     *   <li>intelligence: 지능 스탯 (판정 및 데미지 기준)</li>
     *   <li>manaSpentInPreviousAction: 이전 휴식 이후 소모한 총 마나
     *       (마나 오라 패시브 체크, 마나 축적 패시브, 플로우 오라 수비에 사용)</li>
     *   <li>damageTaken: 받은 데미지 (플로우 오라 수비 전용)</li>
     * </ul>
     */
    public static class MagicSwordsmanRequest {
        public int intelligence;
        public int manaSpentInPreviousAction;
        public int damageTaken;
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
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * 마나 슬래쉬 : 대상에게 3D6의 피해를 입힙니다. (마나 3 소모, 쿨타임 2턴)
     * POST /api/magicswordsman/mana-slash
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/mana-slash")
    public Map<String, Object> manaSlash(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        // manaSpentInPreviousAction is treated as (lastMana - currentMana):
        // lastMana = manaSpentInPreviousAction, currentMana = 0
        Result result = MagicSwordsman.manaSlash(
                req.intelligence,
                req.manaSpentInPreviousAction, 0,
                false, false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 마나 스트라이크 : 대상에게 2D10의 피해를 입힙니다. (마나 3 소모, 쿨타임 3턴)
     * POST /api/magicswordsman/mana-strike
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/mana-strike")
    public Map<String, Object> manaStrike(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.manaStrike(
                req.intelligence,
                req.manaSpentInPreviousAction, 0,
                false, false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 마나 스피어 : 대상에게 1D20의 피해를 입힙니다. (마나 3 소모, 쿨타임 3턴)
     * POST /api/magicswordsman/mana-spear
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/mana-spear")
    public Map<String, Object> manaSpear(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.manaSpear(
                req.intelligence,
                req.manaSpentInPreviousAction, 0,
                false, false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 스핀 크라이스트 : 대상에게 4D8의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     * POST /api/magicswordsman/spin-chryst
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/spin-chryst")
    public Map<String, Object> spinChryst(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.spinChrist(
                req.intelligence,
                req.manaSpentInPreviousAction, 0,
                false, false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 트리플 슬레인 : 대상에게 3D12의 피해를 입힙니다. (마나 4 소모, 쿨타임 4턴)
     * POST /api/magicswordsman/triple-slain
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/triple-slain")
    public Map<String, Object> tripleSlain(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.tripleSlain(
                req.intelligence,
                req.manaSpentInPreviousAction, 0,
                false, false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 에테리얼 임페리오 : 대상에게 3D20의 피해를 입힙니다. (영창 2턴, 마나 6 소모, 쿨타임 7턴)
     * POST /api/magicswordsman/ethereal-imperio
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/ethereal-imperio")
    public Map<String, Object> etherealImperio(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.etherealImperio(
                req.intelligence,
                req.manaSpentInPreviousAction, 0,
                false, false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 스피드레인 : 마나를 2D8만큼 회복합니다. (마나 2 소모, 쿨타임 4턴)
     * POST /api/magicswordsman/speed-drain
     *
     * @param req 요청 본문 (intelligence)
     * @return 계산 결과 (damage = 회복된 마나량, log, succeeded, manaUsed = 소모 마나 - 회복 마나)
     */
    @PostMapping("/speed-drain")
    public Map<String, Object> speedDrain(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.speedDrain(req.intelligence, false, ps);
        ps.flush();

        return buildResponse(result, baos);
    }

    /**
     * 플로우 오라 (전용 수비) : (이전 휴식 시점의 마나 - 현재의 마나) × 2% 만큼 받는 최종 데미지가 감소합니다.
     * POST /api/magicswordsman/flow-aura
     *
     * @param req 요청 본문 (damageTaken, manaSpentInPreviousAction)
     * @return 계산 결과 (damage = 최종 받는 피해량, log, succeeded)
     */
    @PostMapping("/flow-aura")
    public Map<String, Object> flowAura(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        // manaSpentInPreviousAction = lastMana - currentMana
        Result result = MagicSwordsman.flowAura(
                req.damageTaken,
                req.manaSpentInPreviousAction, 0,
                ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageTaken());
        response.put("succeeded", result.succeeded());
        response.put("manaUsed", result.manaUsed());
        return response;
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     * POST /api/magicswordsman/plain
     *
     * @param req 요청 본문 (intelligence, manaSpentInPreviousAction)
     * @return 계산 결과 (damage, log, succeeded, manaUsed)
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        // level defaulted to 1 (no level input in the UI form)
        Result result = MagicSwordsman.plain(
                req.intelligence,
                1,
                req.manaSpentInPreviousAction, 0,
                false, false, 0, ps);
        ps.flush();

        return buildResponse(result, baos);
    }
}
