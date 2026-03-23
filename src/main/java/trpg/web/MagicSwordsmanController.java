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
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가율)%] x (최종 데미지)% x (주사위 보정)
 */
@RestController
@RequestMapping("/api/magicswordsman")
public class MagicSwordsmanController {

    /**
     * Request body for MagicSwordsman skill calculations.
     */
    public static class MagicSwordsmanRequest {
        /** 지능 스탯 */
        public int intelligence;
        /** 캐릭터 레벨 (마나 오라 패시브: 기본 공격 시 레벨만큼 마나 회복) */
        public int level = 1;
        /** 이전 휴식 이후 소모한 마나 (lastMana - currentMana에 대한 프록시, 마나 축적/플로우 오라 패시브용) */
        public int manaSpentSinceRest;
        /** 이전 휴식 시점의 마나 (lastMana; currentMana와 함께 설정 시 우선 사용) */
        public int lastMana;
        /** 현재 마나 (currentMana; lastMana와 함께 설정 시 우선 사용) */
        public int currentMana;
        /** 이전 턴에 마나를 소모했는지 여부 (마나 오라 패시브 조건) */
        public boolean manaUsedLastTurn;
        /** 오버로드 적용 여부 (최종 데미지 2배, 마나 소모 2배) */
        public boolean overload;
        /** 에테일 솔라 적용 여부 (최종 데미지 3배, 마나 소모 없음) */
        public boolean ethailSolar;
        /** 쉬프트리스터 적용 여부 (마나 대신 스태미나 소모, 소모한 스태미나만큼 마나 회복) */
        public boolean shiftLifter;
        /** 정밀 스탯 (치명타 판정) */
        public int precision;
        /** 받은 데미지 (플로우 오라 전용 수비용) */
        public int damageTaken;
        /**
         * 이전 행동 소모 마나 — manaSpentSinceRest의 별칭 (프론트엔드 하위호환성)
         * manaSpentSinceRest가 0이고 이 값이 설정된 경우 대신 사용합니다.
         */
        public int manaSpentInPreviousAction;
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
     * Resolve mana spent since last rest.
     * Prefers (lastMana - currentMana) if set, otherwise falls back to manaSpentSinceRest / manaSpentInPreviousAction.
     */
    private static int resolveManaSpent(MagicSwordsmanRequest req) {
        if (req.lastMana > 0 || req.currentMana > 0) {
            return Math.max(0, req.lastMana - req.currentMana);
        }
        return req.manaSpentSinceRest > 0 ? req.manaSpentSinceRest : req.manaSpentInPreviousAction;
    }

    /**
     * Resolve [lastMana, currentMana] pair for methods that need both values separately.
     */
    private static int[] resolveMana(MagicSwordsmanRequest req) {
        if (req.lastMana > 0 || req.currentMana > 0) {
            return new int[]{req.lastMana, req.currentMana};
        }
        int spent = req.manaSpentSinceRest > 0 ? req.manaSpentSinceRest : req.manaSpentInPreviousAction;
        return new int[]{spent, 0};
    }

    /**
     * Basic attack (기본 공격): 1D6 damage.
     * 마나 오라 패시브: 이전 턴 마나 소모 시 레벨만큼 마나 회복.
     * POST /api/magicswordsman/plain
     */
    @PostMapping("/plain")
    public Map<String, Object> plain(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.plain(req.intelligence, req.level, mana[0], mana[1],
                req.manaUsedLastTurn, req.overload, req.ethailSolar, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 마나 슬래쉬: 3D6 damage. (마나 3 소모, 쿨타임 2턴)
     * POST /api/magicswordsman/mana-slash
     */
    @PostMapping("/mana-slash")
    public Map<String, Object> manaSlash(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.manaSlash(req.intelligence, mana[0], mana[1],
                req.overload, req.ethailSolar, req.shiftLifter, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 마나 스트라이크: 2D10 damage. (마나 3 소모, 쿨타임 3턴)
     * POST /api/magicswordsman/mana-strike
     */
    @PostMapping("/mana-strike")
    public Map<String, Object> manaStrike(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.manaStrike(req.intelligence, mana[0], mana[1],
                req.overload, req.ethailSolar, req.shiftLifter, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 마나 스피어: 1D20 damage. (마나 3 소모, 쿨타임 3턴)
     * POST /api/magicswordsman/mana-spear
     */
    @PostMapping("/mana-spear")
    public Map<String, Object> manaSpear(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.manaSphere(req.intelligence, mana[0], mana[1],
                req.overload, req.ethailSolar, req.shiftLifter, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 스핀 크라이스트: 4D8 damage. (마나 4 소모, 쿨타임 4턴)
     * POST /api/magicswordsman/spin-chryst
     */
    @PostMapping("/spin-chryst")
    public Map<String, Object> spinChryst(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.spinChrist(req.intelligence, mana[0], mana[1],
                req.overload, req.ethailSolar, req.shiftLifter, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 트리플 슬레인: 3D12 damage. (마나 4 소모, 쿨타임 4턴)
     * POST /api/magicswordsman/triple-slain
     */
    @PostMapping("/triple-slain")
    public Map<String, Object> tripleSlain(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.tripleSlain(req.intelligence, mana[0], mana[1],
                req.overload, req.ethailSolar, req.shiftLifter, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 에테리얼 임페리오: 3D20 damage. (영창 2턴, 마나 6 소모, 쿨타임 7턴)
     * POST /api/magicswordsman/ethereal-imperio
     */
    @PostMapping("/ethereal-imperio")
    public Map<String, Object> etherealImperio(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int[] mana = resolveMana(req);
        Result result = MagicSwordsman.etherealImperio(req.intelligence, mana[0], mana[1],
                req.overload, req.ethailSolar, req.shiftLifter, req.precision, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 스피드레인: 마나를 2D8만큼 회복합니다. (마나 2 소모, 쿨타임 4턴)
     * POST /api/magicswordsman/speed-drain
     */
    @PostMapping("/speed-drain")
    public Map<String, Object> speedDrain(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = MagicSwordsman.speedDrain(req.intelligence, ps);
        ps.flush();
        return buildResponse(result, baos);
    }

    /**
     * 플로우 오라 (전용 수비):
     * (이전 휴식 이후 소모 마나) * 2% 만큼 받는 최종 데미지가 감소합니다.
     * POST /api/magicswordsman/flow-aura
     *
     * @return 계산 결과 (damage = 감소된 후 받는 데미지)
     */
    @PostMapping("/flow-aura")
    public Map<String, Object> flowAura(@RequestBody MagicSwordsmanRequest req) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

        int manaSpent = resolveManaSpent(req);
        Result result = MagicSwordsman.flowAura(manaSpent, req.damageTaken, ps);
        ps.flush();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("damage", result.damageTaken());
        response.put("succeeded", result.succeeded());
        response.put("manaUsed", result.manaUsed());
        return response;
    }
}
