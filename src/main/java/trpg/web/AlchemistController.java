package trpg.web;

import main.Result;
import main.secondary.mage.Alchemist;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller for Alchemist (연금술사) damage calculations.
 * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정) + 레벨 보너스
 */
@RestController
@RequestMapping("/api/alchemist")
public class AlchemistController {

    /**
     * 연금술사 기본 요청 본문.
     */
    public static class AlchemistRequest {
        /** 지능 스탯 */
        public int stat;
        /** 캐릭터 레벨 (레벨 보너스: 100 + 레벨^2) */
        public int level;
        /** 현재 플라스크 개수 (연성 패시브: 개당 최종 데미지 25% 증가) */
        public int flasks;
        /** 속성 융합 성공 횟수 (변이 신체 패시브: 회당 최종 데미지 20% 증가) */
        public int fusionCount;
        /** 부식 계열 데미지 증가 누적량 (%) */
        public int corrosionAmpBonus;
        /** 맹독 계열 데미지 증가 누적량 (%) */
        public int poisonAmpBonus;
        /** 화염 계열 데미지 증가 누적량 (%) */
        public int fireAmpBonus;
        /** 빙결 계열 데미지 증가 누적량 (%) */
        public int iceAmpBonus;
        /** 전체 스킬 데미지 증가 누적량 (%) - 오블리테라 맬리티아 효과 등 */
        public int allAmpBonus;
        /** 외부 데미지 증가 (%) */
        public int externalDmgIncrease;
        /** 외부 최종 데미지 증가 (%) */
        public int externalFinalDmgIncrease;
        /** 정밀 스탯 */
        public int precision;
    }

    /**
     * 융합 스킬 요청 본문 - 원소 목록 포함.
     */
    public static class FusionRequest extends AlchemistRequest {
        /** 융합할 원소 이름 목록 (예: ["corrosion", "poison"]) */
        public List<String> elements = new ArrayList<>();
    }

    private Map<String, Object> buildResponse(Result result, ByteArrayOutputStream baos) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageDealt());
        response.put("succeeded", result.succeeded());
        response.put("staminaUsed", result.staminaUsed());
        return response;
    }

    /**
     * 기본 공격 (1D6)
     * POST /api/alchemist/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.plain(req.stat, req.level, req.flasks, req.fusionCount,
                req.allAmpBonus, req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 비트리올 플라스크 (3D8, [부식] 부여, 마나 3)
     * POST /api/alchemist/vitriolic-flask
     */
    @PostMapping("/vitriolic-flask")
    public Map<String, Object> vitriolicFlask(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.vitriolicFlask(req.stat, req.level, req.flasks, req.fusionCount,
                req.corrosionAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 블라이트 플라스크 (6D4, [맹독] 부여, 마나 3)
     * POST /api/alchemist/blight-flask
     */
    @PostMapping("/blight-flask")
    public Map<String, Object> blightFlask(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.blightFlask(req.stat, req.level, req.flasks, req.fusionCount,
                req.poisonAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 이그니스 플라스크 (3D12, [화염] 부여, 마나 3)
     * POST /api/alchemist/ignis-flask
     */
    @PostMapping("/ignis-flask")
    public Map<String, Object> ignisFlask(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.ignisFlask(req.stat, req.level, req.flasks, req.fusionCount,
                req.fireAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 앱솔루트 플라스크 (2D8, [빙결] 부여, 마나 4)
     * POST /api/alchemist/absolute-flask
     */
    @PostMapping("/absolute-flask")
    public Map<String, Object> absoluteFlask(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.absoluteFlask(req.stat, req.level, req.flasks, req.fusionCount,
                req.iceAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 아케인 플라스크 (2D20, [증폭] 부여, 마나 5)
     * POST /api/alchemist/arcane-flask
     */
    @PostMapping("/arcane-flask")
    public Map<String, Object> arcaneFlask(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.arcaneFlask(req.stat, req.level, req.flasks, req.fusionCount,
                req.allAmpBonus, req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 리액션 - 2가지 속성 융합 (마나 3)
     * POST /api/alchemist/reaction
     * 요청 본문에 elements 배열 포함 필요 (2개 선택)
     */
    @PostMapping("/reaction")
    public Map<String, Object> reaction(@RequestBody FusionRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.reaction(req.stat, req.level, req.flasks, req.fusionCount,
                req.corrosionAmpBonus, req.poisonAmpBonus, req.fireAmpBonus, req.iceAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, req.elements, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 체인 디스토션 - 3가지 속성 융합 (마나 4, 쿨타임 5턴)
     * POST /api/alchemist/chain-distortion
     * 요청 본문에 elements 배열 포함 필요 (3개 선택)
     */
    @PostMapping("/chain-distortion")
    public Map<String, Object> chainDistortion(@RequestBody FusionRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.chainDistortion(req.stat, req.level, req.flasks, req.fusionCount,
                req.corrosionAmpBonus, req.poisonAmpBonus, req.fireAmpBonus, req.iceAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, req.elements, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 매터 디재스티아 - 5가지 속성 모두 융합 (마나 6, 쿨타임 12턴)
     * → 오블리테라 맬리티아 (10D6 + D200)
     * POST /api/alchemist/matter-disaster
     */
    @PostMapping("/matter-disaster")
    public Map<String, Object> matterDisaster(@RequestBody AlchemistRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        Result result = Alchemist.matterDisaster(req.stat, req.level, req.flasks, req.fusionCount,
                req.corrosionAmpBonus, req.poisonAmpBonus, req.fireAmpBonus, req.iceAmpBonus, req.allAmpBonus,
                req.externalDmgIncrease, req.externalFinalDmgIncrease, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }
}
