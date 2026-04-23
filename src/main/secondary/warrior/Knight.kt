package main.secondary.warrior

import main.Main
import main.Main.verdict
import main.Result
import java.io.PrintStream
import kotlin.math.roundToInt

/**
 * 기사
 *
 * 판정 사용 스탯 : 힘(+체력, 신속)
 */
class Knight {
    companion object {
        private const val DEFAULT_FINAL_DAMAGE_PERCENT = 100
    }

    /**
     * 기사 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun plain(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return plain(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun plain(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 기본 공격 사용")
        return normalAttack(stat, blessing, 0, 1, 6, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    /**
     * 내려치기 : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun downwardStrike(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return downwardStrike(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun downwardStrike(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 내려치기 사용")
        return normalAttack(stat, blessing, 1, 1, 8, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    /**
     * 후려치기 : 대상에게 2D4의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun bash(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return bash(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun bash(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 후려치기 사용")
        return normalAttack(stat, blessing, 1, 2, 4, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    /**
     * 머리치기 : 대상에게 D6의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun headStrike(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return headStrike(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun headStrike(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 머리치기 사용")
        return normalAttack(stat, blessing, 8, 1, 6, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    /**
     * 수비파괴 : 대상에게 D6의 피해를 입힙니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun defenseBreak(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return defenseBreak(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun defenseBreak(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 수비파괴 사용")
        return normalAttack(stat, blessing, 5, 1, 6, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    /**
     * 기절시키기 : 대상에게 D8의 피해를 입힙니다. (스태미나 10 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun stun(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return stun(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun stun(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 기절시키기 사용")
        return normalAttack(stat, blessing, 10, 1, 8, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    /**
     * 일격 : 대상에게 3D12의 피해를 입힙니다. (스태미나 6 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun strike(stat: Int, blessing: Boolean, precision: Int, level: Int, out: PrintStream): Result {
        return strike(stat, blessing, precision, level, 0, DEFAULT_FINAL_DAMAGE_PERCENT, out)
    }

    fun strike(stat: Int, blessing: Boolean, precision: Int, level: Int, damageIncreasePercent: Int, finalDamagePercent: Int, out: PrintStream): Result {
        out.println("기사 - 일격 사용")
        return normalAttack(stat, blessing, 6, 3, 12, precision, level, damageIncreasePercent, finalDamagePercent, out)
    }

    fun solarArmor(stat: Int, maxHp: Int, out: PrintStream): Result {
        out.println("기사 - 태양 갑옷 사용")
        val verdictResult = verdict(stat, out)
        if (verdictResult <= 0) {
            return Result(0, 0, false, 7, 0)
        }
        val shield = Main.dice(2, 10, out)
        val healAfterTwoTurns = (maxHp * (shield / 100.0)).roundToInt()
        out.printf("태양 갑옷: 보호막 %d 획득%n", shield)
        out.printf("2턴 후 자신의 턴 종료 시 보호막 %d%%만큼 회복: %d HP%n", shield, healAfterTwoTurns)
        return Result(-healAfterTwoTurns, 0, true, 7, 0)
    }

    fun sunsetGuardian(swiftness: Int, out: PrintStream): Result {
        out.println("기사 - 노을빛 수호 사용")
        val verdictResult = verdict(swiftness, out)
        if (verdictResult <= 0) {
            out.println("신속 판정 실패: 보호막 획득 실패")
            return Result(0, 0, false, 6, 0)
        }
        val shield = Main.dice(1, 8, out)
        out.printf("노을빛 수호 발동: 보호막 %d 획득 (전투 내 영구 지속)%n", shield)
        return Result(0, 0, true, 6, 0)
    }

    private fun normalAttack(
        stat: Int,
        blessing: Boolean,
        stamina: Int,
        dices: Int,
        sides: Int,
        precision: Int,
        level: Int,
        externalDamageIncreasePercent: Int,
        externalFinalDamagePercent: Int,
        out: PrintStream
    ): Result {
        val verdict = verdict(stat, out)
        if (verdict <= 0) {
            return Result(0, 0, false, 0, stamina)
        }
        val diceRoll = stat - verdict

        var baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")
        if (blessing) {
            out.println("축복 스킬 적용: 가하는 데미지 30% 감소")
            baseDamage = (baseDamage * 0.7).toInt()
            out.println("계수 적용 후 데미지: $baseDamage")
        }
        val levelMultiplierPercent = 100 + level * level
        val classFinalMultiplier = levelMultiplierPercent / 100.0
        val externalFinalMultiplier = externalFinalDamagePercent / 100.0
        val combinedFinalMultiplier = classFinalMultiplier * externalFinalMultiplier

        out.printf("레벨 최종 배율: (100 + %d^2)%% = %d%%%n", level, levelMultiplierPercent)
        out.printf("외부 최종 데미지 배율: %d%%%n", externalFinalDamagePercent)
        out.printf("적용 데미지 증가: +%d%%%n", externalDamageIncreasePercent)

        val damageAfterFormula = Main.calculateDamage(baseDamage, externalDamageIncreasePercent, combinedFinalMultiplier, out)
        val sideDamage = Main.sideDamage(damageAfterFormula, stat, out, diceRoll)
        out.println("데미지 보정치: $sideDamage")
        val totalDamage = sideDamage + damageAfterFormula
        out.println("최종 데미지: $totalDamage")
        val critDamage = Main.criticalHit(precision, totalDamage, out)
        return Result(0, critDamage, true, 0, stamina)


    }
}
