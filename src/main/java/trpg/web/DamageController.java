package trpg.web;

import main.Main;
import main.normal.*;
import main.hidden.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DamageController {

    /**
     * 주사위 굴리기 API
     */
    @PostMapping("/dice")
    public Map<String, Object> rollDice(@RequestBody Map<String, Integer> request) {
        int dices = request.getOrDefault("dices", 1);
        int sides = request.getOrDefault("sides", 6);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int result = Main.dice(dices, sides, ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("result", result);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        response.put("dices", dices);
        response.put("sides", sides);
        return response;
    }

    // ===== 기본 직업 (Normal Jobs) =====
    
    /**
     * 전사 - 기본공격
     */
    @PostMapping("/warrior/plain")
    public Map<String, Object> warriorPlain(@RequestBody Map<String, Integer> request) {
        int power = request.getOrDefault("power", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int curHealth = request.getOrDefault("curHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Warrior.plain(power, maxHealth, curHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 전사 - 강타
     */
    @PostMapping("/warrior/strike")
    public Map<String, Object> warriorStrike(@RequestBody Map<String, Integer> request) {
        int power = request.getOrDefault("power", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int curHealth = request.getOrDefault("curHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Warrior.strike(power, maxHealth, curHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 전사 - 가로베기
     */
    @PostMapping("/warrior/side")
    public Map<String, Object> warriorSide(@RequestBody Map<String, Integer> request) {
        int power = request.getOrDefault("power", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int curHealth = request.getOrDefault("curHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Warrior.side(power, maxHealth, curHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 전사 - 육참골단 (전용 수비)
     */
    @PostMapping("/warrior/shield")
    public Map<String, Object> warriorShield(@RequestBody Map<String, Integer> request) {
        int damageTaken = request.getOrDefault("damageTaken", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Warrior.shield(damageTaken, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 궁수 (Archer) =====
    
    /**
     * 궁수 - 기본공격 (단일 스탯)
     */
    @PostMapping("/archer/plain")
    public Map<String, Object> archerPlain(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Archer.plain(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 궁수 - 기본공격 (힘+민첩)
     */
    @PostMapping("/archer/plain-dual")
    public Map<String, Object> archerPlainDual(@RequestBody Map<String, Integer> request) {
        int strength = request.getOrDefault("strength", 10);
        int dexterity = request.getOrDefault("dexterity", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Archer.plain(strength, dexterity, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 궁수 - 퀵샷
     */
    @PostMapping("/archer/quickshot")
    public Map<String, Object> archerQuickShot(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Archer.quickShot(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 궁수 - 대쉬 (단일 스탯)
     */
    @PostMapping("/archer/dash")
    public Map<String, Object> archerDash(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Archer.dash(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 궁수 - 대쉬 (힘+민첩)
     */
    @PostMapping("/archer/dash-dual")
    public Map<String, Object> archerDashDual(@RequestBody Map<String, Integer> request) {
        int strength = request.getOrDefault("strength", 10);
        int dexterity = request.getOrDefault("dexterity", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Archer.dash(strength, dexterity, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 궁수 - 사냥감 (단일 스탯)
     */
    @PostMapping("/archer/hunt")
    public Map<String, Object> archerHunt(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int consecutiveHits = (Integer) request.getOrDefault("consecutiveHits", 1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Archer.hunt(stat, consecutiveHits, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 도적 (Rogue) =====
    
    /**
     * 도적 - 기본공격
     */
    @PostMapping("/rogue/plain")
    public Map<String, Object> roguePlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean useTwoDice = (Boolean) request.getOrDefault("useTwoDice", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Rogue.plain(stat, useTwoDice, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 도적 - 쑤시기
     */
    @PostMapping("/rogue/stab")
    public Map<String, Object> rogueStab(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Rogue.stab(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 도적 - 투척/속공
     */
    @PostMapping("/rogue/throw")
    public Map<String, Object> rogueThrow(@RequestBody Map<String, Integer> request) {
        int dexterity = request.getOrDefault("dexterity", 10);
        int swiftness = request.getOrDefault("swiftness", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Rogue.throwAttack(dexterity, swiftness, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 마법사 (Mage) =====
    
    /**
     * 마법사 - 기본공격
     */
    @PostMapping("/mage/plain")
    public Map<String, Object> magePlain(@RequestBody Map<String, Object> request) {
        int intelligence = (Integer) request.getOrDefault("intelligence", 10);
        boolean useMana = (Boolean) request.getOrDefault("useMana", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage;
        if (useMana) {
            damage = Mage.plain(intelligence, true, ps);
        } else {
            damage = Mage.plain(intelligence, ps);
        }
        
        return createResponse(damage, baos);
    }

    /**
     * 마법사 - 마탄
     */
    @PostMapping("/mage/magic-bullet")
    public Map<String, Object> mageMagicBullet(@RequestBody Map<String, Integer> request) {
        int intelligence = request.getOrDefault("intelligence", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Mage.magicBullet(intelligence, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 마법사 - 마나 블래스트
     */
    @PostMapping("/mage/mana-blast")
    public Map<String, Object> mageManaBlast(@RequestBody Map<String, Integer> request) {
        int intelligence = request.getOrDefault("intelligence", 10);
        int additionalMana = request.getOrDefault("additionalMana", 0);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Mage.manaBlast(intelligence, additionalMana, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 마법사 - 매직가드
     */
    @PostMapping("/mage/magic-guard")
    public Map<String, Object> mageMagicGuard(@RequestBody Map<String, Integer> request) {
        int damageTaken = request.getOrDefault("damageTaken", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int reducedDamage = Mage.magicGuard(damageTaken, ps);
        
        return createResponse(reducedDamage, baos);
    }

    // ===== 사제 (Priest) =====
    
    /**
     * 사제 - 기본공격
     */
    @PostMapping("/priest/plain")
    public Map<String, Object> priestPlain(@RequestBody Map<String, Integer> request) {
        int intelligence = request.getOrDefault("intelligence", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Priest.plain(intelligence, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 사제 - 복수
     */
    @PostMapping("/priest/revenge")
    public Map<String, Object> priestRevenge(@RequestBody Map<String, Integer> request) {
        int intelligence = request.getOrDefault("intelligence", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Priest.revenge(intelligence, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 사제 - 희생
     */
    @PostMapping("/priest/sacrifice")
    public Map<String, Object> priestSacrifice(@RequestBody Map<String, Integer> request) {
        int damageTaken = request.getOrDefault("damageTaken", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int reducedDamage = Priest.sacrifice(damageTaken, ps);
        
        return createResponse(reducedDamage, baos);
    }

    // ===== 히든 직업 - 무사 (Samurai) =====
    
    /**
     * 무사 - 기본공격
     */
    @PostMapping("/samurai/plain")
    public Map<String, Object> samuraiPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.plain(stat, isMula, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 발검
     */
    @PostMapping("/samurai/quick-draw")
    public Map<String, Object> samuraiQuickDraw(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.quickDraw(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 발도
     */
    @PostMapping("/samurai/battou")
    public Map<String, Object> samuraiBattou(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.battou(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 자법
     */
    @PostMapping("/samurai/jabeop")
    public Map<String, Object> samuraiJabeop(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.jabeop(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 일섬
     */
    @PostMapping("/samurai/il-seom")
    public Map<String, Object> samuraiIlSeom(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.ilSeom(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 난격
     */
    @PostMapping("/samurai/ranged-attack")
    public Map<String, Object> samuraiRangedAttack(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.rangedAttack(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 섬격
     */
    @PostMapping("/samurai/flash-strike")
    public Map<String, Object> samuraiFlashStrike(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.flashStrike(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 종점
     */
    @PostMapping("/samurai/final-point")
    public Map<String, Object> samuraiFinalPoint(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int consumedStamina = (Integer) request.getOrDefault("consumedStamina", 0);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.finalPoint(stat, consumedStamina, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 무사 - 개화
     */
    @PostMapping("/samurai/bloom")
    public Map<String, Object> samuraiBloom(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isMula = (Boolean) request.getOrDefault("isMula", false);
        boolean kakugo = (Boolean) request.getOrDefault("kakugo", false);
        boolean seishaKetsudan = (Boolean) request.getOrDefault("seishaKetsudan", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Samurai.bloom(stat, isMula, kakugo, seishaKetsudan, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 버서커 (Berserker) =====
    
    /**
     * 버서커 - 기본공격
     */
    @PostMapping("/berserker/plain")
    public Map<String, Object> berserkerPlain(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.plain(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 버서커 - 찍어내리기
     */
    @PostMapping("/berserker/chop-down")
    public Map<String, Object> berserkerChopDown(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.chopDown(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 버서커 - 부수기
     */
    @PostMapping("/berserker/smash")
    public Map<String, Object> berserkerSmash(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.smash(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 버서커 - 일격
     */
    @PostMapping("/berserker/strike")
    public Map<String, Object> berserkerStrike(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.strike(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 버서커 - 무지성 난타
     */
    @PostMapping("/berserker/mindless-barrage")
    public Map<String, Object> berserkerMindlessBarrage(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.mindlessBarrage(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 버서커 - 흉폭한 맹공
     */
    @PostMapping("/berserker/savage-assault")
    public Map<String, Object> berserkerSavageAssault(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.savageAssault(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 버서커 - 최후의 일격
     */
    @PostMapping("/berserker/last-strike")
    public Map<String, Object> berserkerLastStrike(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        int maxHealth = request.getOrDefault("maxHealth", 100);
        int currentHealth = request.getOrDefault("currentHealth", 100);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Berserker.lastStrike(stat, maxHealth, currentHealth, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 겜블러 (Gambler) =====
    
    /**
     * 겜블러 - 기본공격
     */
    @PostMapping("/gambler/plain")
    public Map<String, Object> gamblerPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int reducedLuck = (Integer) request.getOrDefault("reducedLuck", 0);
        boolean jackpotActive = (Boolean) request.getOrDefault("jackpotActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gambler.plain(stat, reducedLuck, jackpotActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 겜블러 - 코인 토스
     */
    @PostMapping("/gambler/coin-toss")
    public Map<String, Object> gamblerCoinToss(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int luck = (Integer) request.getOrDefault("luck", 10);
        int reducedLuck = (Integer) request.getOrDefault("reducedLuck", 0);
        boolean jackpotActive = (Boolean) request.getOrDefault("jackpotActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gambler.coinToss(stat, luck, reducedLuck, jackpotActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 겜블러 - 조커 카드
     */
    @PostMapping("/gambler/joker-card")
    public Map<String, Object> gamblerJokerCard(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int luck = (Integer) request.getOrDefault("luck", 10);
        int reducedLuck = (Integer) request.getOrDefault("reducedLuck", 0);
        boolean jackpotActive = (Boolean) request.getOrDefault("jackpotActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gambler.jokerCard(stat, luck, reducedLuck, jackpotActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 겜블러 - 블랙잭
     */
    @PostMapping("/gambler/blackjack")
    public Map<String, Object> gamblerBlackjack(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int luck = (Integer) request.getOrDefault("luck", 10);
        int reducedLuck = (Integer) request.getOrDefault("reducedLuck", 0);
        boolean jackpotActive = (Boolean) request.getOrDefault("jackpotActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gambler.blackjack(stat, luck, reducedLuck, jackpotActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 겜블러 - 야추 다이스
     */
    @PostMapping("/gambler/yatzy-dice")
    public Map<String, Object> gamblerYatzyDice(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int luck = (Integer) request.getOrDefault("luck", 10);
        int reducedLuck = (Integer) request.getOrDefault("reducedLuck", 0);
        boolean jackpotActive = (Boolean) request.getOrDefault("jackpotActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gambler.yatzyDice(stat, luck, reducedLuck, jackpotActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 겜블러 - 로얄 플러쉬
     */
    @PostMapping("/gambler/royal-flush")
    public Map<String, Object> gamblerRoyalFlush(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int luck = (Integer) request.getOrDefault("luck", 10);
        int reducedLuck = (Integer) request.getOrDefault("reducedLuck", 0);
        boolean jackpotActive = (Boolean) request.getOrDefault("jackpotActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gambler.royalFlush(stat, luck, reducedLuck, jackpotActive, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 암살자 (Assassin) =====
    
    /**
     * 암살자 - 기본공격
     */
    @PostMapping("/assassin/plain")
    public Map<String, Object> assassinPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isReturnTurn = (Boolean) request.getOrDefault("isReturnTurn", false);
        boolean isFirstAssault = (Boolean) request.getOrDefault("isFirstAssault", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Assassin.plain(stat, isReturnTurn, isFirstAssault, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 암살자 - 암살
     */
    @PostMapping("/assassin/assassinate")
    public Map<String, Object> assassinAssassinate(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isReturnTurn = (Boolean) request.getOrDefault("isReturnTurn", false);
        boolean isFirstAssault = (Boolean) request.getOrDefault("isFirstAssault", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Assassin.assassinate(stat, isReturnTurn, isFirstAssault, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 암살자 - 급소 찌르기
     */
    @PostMapping("/assassin/critical-stab")
    public Map<String, Object> assassinCriticalStab(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isReturnTurn = (Boolean) request.getOrDefault("isReturnTurn", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Assassin.criticalStab(stat, isReturnTurn, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 암살자 - 목 긋기
     */
    @PostMapping("/assassin/throat-slit")
    public Map<String, Object> assassinThroatSlit(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isReturnTurn = (Boolean) request.getOrDefault("isReturnTurn", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Assassin.throatSlit(stat, isReturnTurn, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 암살자 - 손목 긋기
     */
    @PostMapping("/assassin/wrist-slit")
    public Map<String, Object> assassinWristSlit(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isReturnTurn = (Boolean) request.getOrDefault("isReturnTurn", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Assassin.wristSlit(stat, isReturnTurn, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 암살자 - 후방 공격
     */
    @PostMapping("/assassin/rear-attack")
    public Map<String, Object> assassinRearAttack(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isReturnTurn = (Boolean) request.getOrDefault("isReturnTurn", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Assassin.rearAttack(stat, isReturnTurn, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 기사 (Knight) =====
    
    /**
     * 기사 - 기본공격
     */
    @PostMapping("/knight/plain")
    public Map<String, Object> knightPlain(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.plain(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 기사 - 내려치기
     */
    @PostMapping("/knight/smash-down")
    public Map<String, Object> knightSmashDown(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.smashDown(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 기사 - 후려치기
     */
    @PostMapping("/knight/sweep")
    public Map<String, Object> knightSweep(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.sweep(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 기사 - 머리치기
     */
    @PostMapping("/knight/head-strike")
    public Map<String, Object> knightHeadStrike(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.headStrike(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 기사 - 수비파괴
     */
    @PostMapping("/knight/defense-break")
    public Map<String, Object> knightDefenseBreak(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.defenseBreak(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 기사 - 기절시키기
     */
    @PostMapping("/knight/stun")
    public Map<String, Object> knightStun(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.stun(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 기사 - 일격
     */
    @PostMapping("/knight/critical-strike")
    public Map<String, Object> knightCriticalStrike(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Knight.criticalStrike(stat, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 닌자 (Ninja) =====
    
    /**
     * 닌자 - 기본공격
     */
    @PostMapping("/ninja/plain")
    public Map<String, Object> ninjaPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isIllusionTurn = (Boolean) request.getOrDefault("isIllusionTurn", false);
        boolean isCloneActive = (Boolean) request.getOrDefault("isCloneActive", false);
        boolean isReflexActive = (Boolean) request.getOrDefault("isReflexActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Ninja.plain(stat, isIllusionTurn, isCloneActive, isReflexActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 닌자 - 일격
     */
    @PostMapping("/ninja/strike")
    public Map<String, Object> ninjaStrike(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isIllusionTurn = (Boolean) request.getOrDefault("isIllusionTurn", false);
        boolean isCloneActive = (Boolean) request.getOrDefault("isCloneActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Ninja.strike(stat, isIllusionTurn, isCloneActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 닌자 - 난도
     */
    @PostMapping("/ninja/chaos")
    public Map<String, Object> ninjaChaos(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isIllusionTurn = (Boolean) request.getOrDefault("isIllusionTurn", false);
        boolean isCloneActive = (Boolean) request.getOrDefault("isCloneActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Ninja.chaos(stat, isIllusionTurn, isCloneActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 닌자 - 투척 표창
     */
    @PostMapping("/ninja/throw-shuriken")
    public Map<String, Object> ninjaThrowShuriken(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isCloneActive = (Boolean) request.getOrDefault("isCloneActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Ninja.throwShuriken(stat, isCloneActive, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 닌자 - 환영난무
     */
    @PostMapping("/ninja/illusion-barrage")
    public Map<String, Object> ninjaIllusionBarrage(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isIllusionTurn = (Boolean) request.getOrDefault("isIllusionTurn", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Ninja.illusionBarrage(stat, isIllusionTurn, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 닌자 - 일점투척
     */
    @PostMapping("/ninja/focus-throw")
    public Map<String, Object> ninjaFocusThrow(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int shurikenCount = (Integer) request.getOrDefault("shurikenCount", 1);
        boolean isCloneActive = (Boolean) request.getOrDefault("isCloneActive", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Ninja.focusThrow(stat, shurikenCount, isCloneActive, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 건슬링거 (Gunslinger) =====
    
    /**
     * 건슬링거 - 기본공격
     */
    @PostMapping("/gunslinger/plain")
    public Map<String, Object> gunslingerPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isFirstShot = (Boolean) request.getOrDefault("isFirstShot", false);
        boolean dodgedLastTurn = (Boolean) request.getOrDefault("dodgedLastTurn", false);
        boolean isJudgeTurn = (Boolean) request.getOrDefault("isJudgeTurn", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gunslinger.plain(stat, isFirstShot, dodgedLastTurn, isJudgeTurn, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 건슬링거 - 더블샷
     */
    @PostMapping("/gunslinger/double-shot")
    public Map<String, Object> gunslingerDoubleShot(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gunslinger.doubleShot(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 건슬링거 - 헤드샷
     */
    @PostMapping("/gunslinger/headshot")
    public Map<String, Object> gunslingerHeadshot(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gunslinger.headshot(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 건슬링거 - 퀵드로우
     */
    @PostMapping("/gunslinger/quick-draw")
    public Map<String, Object> gunslingerQuickDraw(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isFirstShot = (Boolean) request.getOrDefault("isFirstShot", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gunslinger.quickDraw(stat, isFirstShot, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 건슬링거 - 일점사
     */
    @PostMapping("/gunslinger/focus-fire")
    public Map<String, Object> gunslingerFocusFire(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gunslinger.focusFire(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 건슬링거 - 백스탭 (전용 수비)
     */
    @PostMapping("/gunslinger/backstab")
    public Map<String, Object> gunslingerBackstab(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Gunslinger.backstab(stat, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 저격수 (Sniper) =====
    
    /**
     * 저격수 - 기본공격
     */
    @PostMapping("/sniper/plain")
    public Map<String, Object> sniperPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int numBuffs = (Integer) request.getOrDefault("numBuffs", 0);
        boolean notAttackedFor5Turns = (Boolean) request.getOrDefault("notAttackedFor5Turns", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Sniper.plain(stat, numBuffs, notAttackedFor5Turns, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 저격수 - 확보
     */
    @PostMapping("/sniper/secure")
    public Map<String, Object> sniperSecure() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        Sniper.secure(ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("damage", 0);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }

    /**
     * 저격수 - 조립
     */
    @PostMapping("/sniper/assemble")
    public Map<String, Object> sniperAssemble() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        Sniper.assemble(ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("damage", 0);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }

    /**
     * 저격수 - 장전
     */
    @PostMapping("/sniper/load")
    public Map<String, Object> sniperLoad() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        Sniper.load(ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("damage", 0);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }

    /**
     * 저격수 - 조준
     */
    @PostMapping("/sniper/aim")
    public Map<String, Object> sniperAim() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        Sniper.aim(ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("damage", 0);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }

    /**
     * 저격수 - 발사
     */
    @PostMapping("/sniper/fire")
    public Map<String, Object> sniperFire(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int numBuffs = (Integer) request.getOrDefault("numBuffs", 0);
        boolean noBasicAttackUsed = (Boolean) request.getOrDefault("noBasicAttackUsed", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Sniper.fire(stat, numBuffs, noBasicAttackUsed, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 명궁 (MasterArcher) =====
    
    /**
     * 명궁 - 기본공격
     */
    @PostMapping("/masterarcher/plain")
    public Map<String, Object> masterArcherPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isHeavyString = (Boolean) request.getOrDefault("isHeavyString", false);
        boolean isFirstTarget = (Boolean) request.getOrDefault("isFirstTarget", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = MasterArcher.plain(stat, isHeavyString, isFirstTarget, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 명궁 - 파위샷
     */
    @PostMapping("/masterarcher/power-shot")
    public Map<String, Object> masterArcherPowerShot(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isHeavyString = (Boolean) request.getOrDefault("isHeavyString", false);
        boolean isFirstTarget = (Boolean) request.getOrDefault("isFirstTarget", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = MasterArcher.powerShot(stat, isHeavyString, isFirstTarget, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 명궁 - 폭탄 화살
     */
    @PostMapping("/masterarcher/explosive-arrow")
    public Map<String, Object> masterArcherExplosiveArrow(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isHeavyString = (Boolean) request.getOrDefault("isHeavyString", false);
        boolean isFirstTarget = (Boolean) request.getOrDefault("isFirstTarget", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = MasterArcher.explosiveArrow(stat, isHeavyString, isFirstTarget, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 명궁 - 분열 화살
     */
    @PostMapping("/masterarcher/split-arrow")
    public Map<String, Object> masterArcherSplitArrow(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isHeavyString = (Boolean) request.getOrDefault("isHeavyString", false);
        boolean isFirstTarget = (Boolean) request.getOrDefault("isFirstTarget", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = MasterArcher.splitArrow(stat, isHeavyString, isFirstTarget, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 명궁 - 관통 화살
     */
    @PostMapping("/masterarcher/piercing-arrow")
    public Map<String, Object> masterArcherPiercingArrow(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isFirstTarget = (Boolean) request.getOrDefault("isFirstTarget", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = MasterArcher.piercingArrow(stat, isFirstTarget, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 명궁 - 더블 샷
     */
    @PostMapping("/masterarcher/double-shot")
    public Map<String, Object> masterArcherDoubleShot(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isHeavyString = (Boolean) request.getOrDefault("isHeavyString", false);
        boolean isFirstTarget = (Boolean) request.getOrDefault("isFirstTarget", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = MasterArcher.doubleShot(stat, isHeavyString, isFirstTarget, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 석궁사수 (Crossbowman) =====
    
    /**
     * 석궁사수 - 기본공격
     */
    @PostMapping("/crossbowman/plain")
    public Map<String, Object> crossbowmanPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        int arrows = (Integer) request.getOrDefault("arrows", 1);
        boolean focusedAttack = (Boolean) request.getOrDefault("focusedAttack", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Crossbowman.plain(stat, arrows, focusedAttack, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 석궁사수 - 던지기
     */
    @PostMapping("/crossbowman/throw")
    public Map<String, Object> crossbowmanThrow(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Crossbowman.throwAttack(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 석궁사수 - 빠른 장전
     */
    @PostMapping("/crossbowman/quick-load")
    public Map<String, Object> crossbowmanQuickLoad() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        Crossbowman.quickLoad(ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("damage", 0);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }

    /**
     * 석궁사수 - 단일사격
     */
    @PostMapping("/crossbowman/single-shot")
    public Map<String, Object> crossbowmanSingleShot(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Crossbowman.singleShot(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 석궁사수 - 발광 화살
     */
    @PostMapping("/crossbowman/rage-arrow")
    public Map<String, Object> crossbowmanRageArrow(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Crossbowman.rageArrow(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 석궁사수 - 마비 화살
     */
    @PostMapping("/crossbowman/paralyze-arrow")
    public Map<String, Object> crossbowmanParalyzeArrow(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Crossbowman.paralyzeArrow(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 석궁사수 - 화살 꺾기 (전용수비)
     */
    @PostMapping("/crossbowman/break-arrows")
    public Map<String, Object> crossbowmanBreakArrows(@RequestBody Map<String, Integer> request) {
        int damageTaken = request.getOrDefault("damageTaken", 10);
        int arrowsToBreak = request.getOrDefault("arrowsToBreak", 1);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Crossbowman.breakArrows(damageTaken, arrowsToBreak, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 석궁사수 - 이럴 때 일수록! (전용수비)
     */
    @PostMapping("/crossbowman/desperate-load")
    public Map<String, Object> crossbowmanDesperateLoad(@RequestBody Map<String, Integer> request) {
        int damageTaken = request.getOrDefault("damageTaken", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int arrowsLoaded = Crossbowman.desperateLoad(damageTaken, ps);
        
        Map<String, Object> response = new HashMap<>();
        response.put("damage", arrowsLoaded);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }

    // ===== 히든 직업 - 창술사 (Spearman) =====
    
    /**
     * 창술사 - 기본공격
     */
    @PostMapping("/spearman/plain")
    public Map<String, Object> spearmanPlain(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.plain(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 창술사 - 돌려 찌르기
     */
    @PostMapping("/spearman/spin-thrust")
    public Map<String, Object> spearmanSpinThrust(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.spinThrust(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 창술사 - 회전 타격
     */
    @PostMapping("/spearman/spin-strike")
    public Map<String, Object> spearmanSpinStrike(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.spinStrike(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 창술사 - 하단 베기
     */
    @PostMapping("/spearman/low-slash")
    public Map<String, Object> spearmanLowSlash(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.lowSlash(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 창술사 - [연계]정면 찌르기
     */
    @PostMapping("/spearman/combo-front-thrust")
    public Map<String, Object> spearmanComboFrontThrust(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.comboFrontThrust(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 창술사 - [연계]일섬창
     */
    @PostMapping("/spearman/combo-flash-spear")
    public Map<String, Object> spearmanComboFlashSpear(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.comboFlashSpear(stat, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 창술사 - [연계]천뢰격
     */
    @PostMapping("/spearman/combo-thunder-strike")
    public Map<String, Object> spearmanComboThunderStrike(@RequestBody Map<String, Integer> request) {
        int stat = request.getOrDefault("stat", 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Spearman.comboThunderStrike(stat, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 트릭스터 (Trickster) =====
    
    /**
     * 트릭스터 - 기본공격
     */
    @PostMapping("/trickster/plain")
    public Map<String, Object> tricksterPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean isFocusedFire = (Boolean) request.getOrDefault("isFocusedFire", false);
        boolean isRepeatCustomer = (Boolean) request.getOrDefault("isRepeatCustomer", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Trickster.plain(stat, isFocusedFire, isRepeatCustomer, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 트릭스터 - 페이크 단검
     */
    @PostMapping("/trickster/fake-dagger")
    public Map<String, Object> tricksterFakeDagger(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasEventBonus = (Boolean) request.getOrDefault("hasEventBonus", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Trickster.fakeDagger(stat, hasEventBonus, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 트릭스터 - 콩알탄
     */
    @PostMapping("/trickster/bean-shot")
    public Map<String, Object> tricksterBeanShot(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasEventBonus = (Boolean) request.getOrDefault("hasEventBonus", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Trickster.beanShot(stat, hasEventBonus, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 트릭스터 - 기름통 투척
     */
    @PostMapping("/trickster/oil-barrel")
    public Map<String, Object> tricksterOilBarrel(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasEventBonus = (Boolean) request.getOrDefault("hasEventBonus", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Trickster.oilBarrel(stat, hasEventBonus, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 트릭스터 - 라이터 투척
     */
    @PostMapping("/trickster/lighter-throw")
    public Map<String, Object> tricksterLighterThrow(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasEventBonus = (Boolean) request.getOrDefault("hasEventBonus", false);
        boolean oilHit = (Boolean) request.getOrDefault("oilHit", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Trickster.lighterThrow(stat, hasEventBonus, oilHit, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 트릭스터 - 특대형 단검
     */
    @PostMapping("/trickster/huge-dagger")
    public Map<String, Object> tricksterHugeDagger(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasEventBonus = (Boolean) request.getOrDefault("hasEventBonus", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Trickster.hugeDagger(stat, hasEventBonus, ps);
        
        return createResponse(damage, baos);
    }

    // ===== 히든 직업 - 밀렵꾼 (Poacher) =====
    
    /**
     * 밀렵꾼 - 기본공격
     */
    @PostMapping("/poacher/plain")
    public Map<String, Object> poacherPlain(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasDebuff = (Boolean) request.getOrDefault("hasDebuff", false);
        boolean isLoaded = (Boolean) request.getOrDefault("isLoaded", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Poacher.plain(stat, hasDebuff, isLoaded, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 밀렵꾼 - 머리찍기
     */
    @PostMapping("/poacher/head-chop")
    public Map<String, Object> poacherHeadChop(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasDebuff = (Boolean) request.getOrDefault("hasDebuff", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Poacher.headChop(stat, hasDebuff, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 밀렵꾼 - 덫 깔기
     */
    @PostMapping("/poacher/set-trap")
    public Map<String, Object> poacherSetTrap(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasDebuff = (Boolean) request.getOrDefault("hasDebuff", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Poacher.setTrap(stat, hasDebuff, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 밀렵꾼 - 올가미 탄
     */
    @PostMapping("/poacher/snare-shot")
    public Map<String, Object> poacherSnareShot(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasDebuff = (Boolean) request.getOrDefault("hasDebuff", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Poacher.snareShot(stat, hasDebuff, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * 밀렵꾼 - 헤드샷
     */
    @PostMapping("/poacher/headshot")
    public Map<String, Object> poacherHeadshot(@RequestBody Map<String, Object> request) {
        int stat = (Integer) request.getOrDefault("stat", 10);
        boolean hasDebuff = (Boolean) request.getOrDefault("hasDebuff", false);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);
        
        int damage = Poacher.headshot(stat, hasDebuff, ps);
        
        return createResponse(damage, baos);
    }

    /**
     * Helper method to create response
     */
    private Map<String, Object> createResponse(int damage, ByteArrayOutputStream baos) {
        Map<String, Object> response = new HashMap<>();
        response.put("damage", damage);
        response.put("log", baos.toString(StandardCharsets.UTF_8));
        return response;
    }
}
