package main.secondary.warrior

import main.Main
import main.Main.verdict
import main.Result
import java.io.PrintStream

/**
 * 기사
 *
 * 판정 사용 스탯 : 힘(+체력, 신속)
 */
class Knight {

    /**
     * 기사 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun plain(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 기본 공격 사용")
        return normalAttack(stat, blessing, 0, 1, 6, out)
    }

    /**
     * 내려치기 : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun downwardStrike(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 내려치기 사용")
        return normalAttack(stat, blessing, 1, 1, 8, out)
    }

    /**
     * 후려치기 : 대상에게 2D4의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun bash(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 후려치기 사용")
        return normalAttack(stat, blessing, 1, 2, 4, out)
    }

    /**
     * 머리치기 : 대상에게 D6의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun headStrike(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 머리치기 사용")
        return normalAttack(stat, blessing, 8, 1, 6, out)
    }

    /**
     * 수비파괴 : 대상에게 D6의 피해를 입힙니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun defenseBreak(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 수비파괴 사용")
        return normalAttack(stat, blessing, 5, 1, 6, out)
    }

    /**
     * 기절시키기 : 대상에게 D8의 피해를 입힙니다. (스태미나 10 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun stun(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 기절시키기 사용")
        return normalAttack(stat, blessing, 10, 1, 8, out)
    }

    /**
     * 일격 : 대상에게 3D12의 피해를 입힙니다. (스태미나 6 소모)
     *
     * @param stat 사용할 스탯
     * @param blessing 축복 스킬 적용 여부 (가하는 데미지 30% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun strike(stat: Int, blessing: Boolean, out: PrintStream): Result {
        out.println("기사 - 일격 사용")
        return normalAttack(stat, blessing, 6, 3, 12, out)
    }

    private fun normalAttack(
        stat: Int,
        blessing: Boolean,
        stamina: Int,
        dices: Int,
        sides: Int,
        out: PrintStream
    ): Result {
        val verdict = verdict(stat, out)
        if (verdict <= 0) {
            return Result(0, 0, false, 0, stamina)
        }

        var baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")
        if (blessing) {
            out.println("축복 스킬 적용: 가하는 데미지 30% 감소")
            baseDamage = (baseDamage * 0.7).toInt()
            out.println("계수 적용 후 데미지: $baseDamage")
        }
        val sideDamage = Main.sideDamage(baseDamage, stat, out)
        out.println("데미지 보정치: $sideDamage")
        out.println("최종 데미지: ${sideDamage + baseDamage}")
        return Result(0, sideDamage + baseDamage, true, 0, stamina)


    }
}