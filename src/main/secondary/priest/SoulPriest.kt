package main.secondary.priest

import main.Main
import main.Result
import java.io.PrintStream
import kotlin.math.min

/**
 * 영혼의 사제
 *
 * 판정 사용 스탯 : 지능(지혜)
 */
class SoulPriest {

    /**
     * 영혼의 사제 원한 : ([영혼] 소모 개수)D12의 피해를 입힙니다. (쿨타임 8턴)
     *
     * @param stat 사용할 스탯
     * @param soul 현재 영혼 누적 개수
     * @param ruins 폐허 스킬 사용 여부
     * @param soulUse 사용할 영혼 개수
     * @return 결과 객체
     */
    fun grudge(stat: Int, soul: Int, ruins: Boolean, soulUse: Int, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 원한 사용")
        if (soulUse <= 0) {
            out.println("사용한 영혼의 개수가 올바르지 않습니다.")
            return Result()
        }
        return normalAttack(stat, soulUse, 12, soul, ruins, precision, out)
    }

    /**
     * 영혼의 사제 흉통 : 대상에게 8D4의 피해를 입힙니다. 다음 턴까지 받는 피해를 1.5배로 증가시킵니다. ([영혼] 3개 소모, 쿨타임 6턴)
     *
     * @param stat 사용할 스탯
     * @param soul 현재 영혼 누적 개수
     * @param ruins 폐허 스킬 사용 여부
     * @return 결과 객체
     */
    fun chestPain(stat: Int, soul: Int, ruins: Boolean, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 흉통 사용")
        return normalAttack(stat, 8, 4, soul, ruins, precision, out)
    }

    /**
     * 영혼의 사제 저주 : 대상에게 3D4의 피해를 입힙니다. 다음 턴까지 가하는 피해를 50%로 감소시킵니다. ([영혼] 2개 소모, 쿨타임 4턴)
     *
     * @param stat 사용할 스탯
     * @param soul 현재 영혼 누적 개수
     * @param ruins 폐허 스킬 사용 여부
     * @return 결과 객체
     */
    fun curse(stat: Int, soul: Int, ruins: Boolean, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 저주 사용")
        return normalAttack(stat, 3, 4, soul, ruins, precision, out)
    }

    /**
     * 영혼의 사제 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param soul 현재 영혼 누적 개수
     * @param ruins 폐허 스킬 사용 여부
     * @return 결과 객체
     */
    fun plain(stat: Int, soul: Int, ruins: Boolean, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 기본 공격 사용")
        return normalAttack(stat, 1, 6, soul, ruins, precision, out)
    }

    /**
     * 영혼의 사제 범용 공격 메소드
     *
     * @param stat 사용할 스탯
     * @param soul 현재 영혼 누적 개수
     * @param ruins 폐허 스킬 사용 여부
     * @return 결과 객체
     */
    private fun normalAttack(stat: Int, dices: Int, sides: Int, soul: Int, ruins: Boolean, precision: Int, out: PrintStream): Result {
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) {
            return Result()
        }

        val baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")
        var damageMultiplier = 1.0

        if (ruins) {
            if (soul >= 30) {
                damageMultiplier += min(soul, 40) * 0.3
                out.println("폐허 스킬 적용: 데미지 ${soul * 30}% 증가")
            }
            if (soul >= 20) {
                damageMultiplier = 1.5
                out.println("폐허 스킬 적용: 자신 제외 모두가 받는 피해 150% 증가")
            }
        } else {
            if (soul >= 20) {
                damageMultiplier += min(soul, 30) * 0.2
                out.println("축복 패시브 적용: 데미지 ${soul * 20}% 증가")
            }
        }
        val preCritDamage = (baseDamage * damageMultiplier).toInt()
        out.println("최종 데미지 : $preCritDamage")
        val totalDamage = Main.criticalHit(precision, preCritDamage, out)
        return Result(0, totalDamage, true, 0, 0)
    }
}