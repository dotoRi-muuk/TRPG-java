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
