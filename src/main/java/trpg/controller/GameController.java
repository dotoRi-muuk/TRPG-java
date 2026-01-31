package trpg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import trpg.service.GameService;

/**
 * Controller for handling TRPG game web requests.
 * Provides endpoints for dice rolling, damage calculation, and verdict checks.
 */
@Controller
public class GameController {

    private final GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Display the main game page.
     */
    @GetMapping("/")
    public String index() {
        return "game";
    }

    /**
     * Roll dice and return result.
     */
    @PostMapping("/roll-dice")
    @ResponseBody
    public GameService.DiceResult rollDice(@RequestParam int dices, @RequestParam int sides) {
        return gameService.diceWithOutput(dices, sides);
    }

    /**
     * Calculate normal damage.
     */
    @PostMapping("/calculate-damage")
    @ResponseBody
    public GameService.NormalCalculationResult calculateDamage(
            @RequestParam int stat,
            @RequestParam int dices,
            @RequestParam int sides) {
        return gameService.normalCalculation(stat, dices, sides);
    }

    /**
     * Perform a stat verdict check.
     */
    @PostMapping("/verdict")
    @ResponseBody
    public GameService.VerdictResult verdict(@RequestParam int stat) {
        return gameService.verdict(stat);
    }

    /**
     * Calculate side damage.
     */
    @PostMapping("/side-damage")
    @ResponseBody
    public GameService.SideDamageResult sideDamage(
            @RequestParam int finalDamage,
            @RequestParam int stat) {
        return gameService.sideDamage(finalDamage, stat);
    }
}
