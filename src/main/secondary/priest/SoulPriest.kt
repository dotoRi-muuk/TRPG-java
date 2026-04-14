package main.secondary.priest

import main.Main
import main.Result
import java.io.PrintStream
import kotlin.math.min

/**
 * 영혼의 사제
 *
 * 판정 사용 스탯 : 지능(지혜)
 *
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * 직업 외 최종 데미지 기본값: 100% (1배)
 *
 * [패시브]
 * - 축복: [영혼] 누적에 따라 효과를 얻습니다.
 *   - 20개: 데미지 20×[영혼]% 증가 (최대 600%)
 * - 폐허(스킬): 축복 효과를 변경합니다.
 *   - 30개: 최종 데미지 30×[영혼]%로 증가 (최대 1200%)
 */
class SoulPriest {

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat             사용할 스탯
     * @param soul             현재 영혼 누적 개수
     * @param ruins            폐허 스킬 사용 여부
     * @param level            캐릭터 레벨 (최종 데미지 기본 계수: (100 + 레벨²)%)
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    fun plain(stat: Int, soul: Int, ruins: Boolean, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 기본 공격 사용")
        return damageAttack(stat, 1, 6, soul, ruins, level, damageBonus, finalDamageBonus, precision, 0, out)
    }

    /**
     * 저주 (스킬) : 대상에게 3D10의 피해를 입힙니다. 다음 턴까지 가하는 피해를 50%로 감소시킵니다. ([영혼] 2개 소모, 쿨타임 4턴)
     *
     * @param stat             사용할 스탯
     * @param soul             현재 영혼 누적 개수
     * @param ruins            폐허 스킬 사용 여부
     * @param level            캐릭터 레벨
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    fun curse(stat: Int, soul: Int, ruins: Boolean, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 저주 사용")
        return damageAttack(stat, 3, 10, soul, ruins, level, damageBonus, finalDamageBonus, precision, 0, out)
    }

    /**
     * 흉통 (스킬) : 대상에게 8D4의 피해를 입힙니다. 다음 턴까지 받는 데미지를 70% 증가시킵니다. ([영혼] 3개 소모, 쿨타임 6턴)
     *
     * @param stat             사용할 스탯
     * @param soul             현재 영혼 누적 개수
     * @param ruins            폐허 스킬 사용 여부
     * @param level            캐릭터 레벨
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    fun chestPain(stat: Int, soul: Int, ruins: Boolean, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 흉통 사용")
        return damageAttack(stat, 8, 4, soul, ruins, level, damageBonus, finalDamageBonus, precision, 0, out)
    }

    /**
     * 원한 (스킬) : ([영혼] 소모 개수)D20의 피해를 입힙니다.
     *
     * @param stat             사용할 스탯
     * @param soul             현재 영혼 누적 개수
     * @param ruins            폐허 스킬 사용 여부
     * @param soulUse          사용할 영혼 개수 (주사위 개수)
     * @param level            캐릭터 레벨
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    fun grudge(stat: Int, soul: Int, ruins: Boolean, soulUse: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 원한 사용")
        if (soulUse <= 0) {
            out.println("사용한 영혼의 개수가 올바르지 않습니다.")
            return Result()
        }
        out.println("원한: ${soulUse}D20 피해")
        return damageAttack(stat, soulUse, 20, soul, ruins, level, damageBonus, finalDamageBonus, precision, 0, out)
    }

    /**
     * 흡수 (스킬) : 체력을 2D12만큼 회복합니다. ([영혼] 1개 소모)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체 (damageDealt = 회복량)
     */
    fun absorb(stat: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 흡수 사용")
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) return Result(0, 0, false, 0, 0)
        val heal = Main.dice(2, 12, out)
        out.println("회복량 : $heal")
        return Result(0, heal, true, 0, 0)
    }

    /**
     * 수거 (스킬) : 영혼을 D10개 획득합니다. (쿨타임 8턴)
     *
     * @param stat 사용할 스탯
     * @param out  출력 스트림
     * @return 결과 객체 (damageDealt = 획득한 영혼 수)
     */
    fun collect(stat: Int, out: PrintStream): Result {
        out.println("영혼의 사제 - 수거 사용")
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) return Result(0, 0, false, 0, 0)
        val souls = Main.dice(1, 10, out)
        out.println("획득한 영혼 : $souls")
        return Result(0, souls, true, 0, 0)
    }

    /**
     * 범용 데미지 공격 메소드
     *
     * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
     *
     * 축복 패시브 (폐허 비활성):
     *   - soul >= 20: 데미지 증가 += min(soul, 30) × 20% (최대 600%)
     * 폐허 스킬 (ruins = true):
     *   - soul >= 30: 최종 데미지 × min(soul, 40) × 30% (최대 1200%)
     */
    private fun damageAttack(
        stat: Int, dices: Int, sides: Int,
        soul: Int, ruins: Boolean,
        level: Int, damageBonus: Int, finalDamageBonus: Int,
        precision: Int, mana: Int, out: PrintStream
    ): Result {
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) return Result(0, 0, false, mana, 0)

        val baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")

        // 총 데미지 증가 (합산)
        var totalDamageBonus = damageBonus

        // 축복 패시브 데미지 증가 (폐허 비활성 시)
        if (!ruins && soul >= 20) {
            val blessingBonus = min(soul, 30) * 20
            out.println("축복 패시브 적용: 데미지 ${blessingBonus}% 증가 (영혼 ${soul}개)")
            totalDamageBonus += blessingBonus
        }

        // 레벨 기반 최종 데미지 계수: (100 + level²)%
        val safeLevel = maxOf(0, level)
        val levelCoeff = 100.0 + safeLevel * safeLevel
        out.printf("레벨 %d 기반 최종 데미지 계수: (100 + %d^2) = %.1f%%%n", safeLevel, safeLevel, levelCoeff)

        // 총 최종 데미지 배율
        var totalFinalMult = (levelCoeff / 100.0) * (finalDamageBonus / 100.0)
        out.printf("직업 외 최종 데미지 배율: %d%% (×%.4f)%n", finalDamageBonus, finalDamageBonus / 100.0)

        // 폐허 최종 데미지 (폐허 활성 + 영혼 30개 이상 시)
        if (ruins && soul >= 30) {
            val ruinsFinalPct = min(soul, 40) * 30
            val ruinsMult = ruinsFinalPct / 100.0
            out.printf("폐허 스킬 적용: 최종 데미지 %d%% (영혼 %d개, ×%.2f)%n", ruinsFinalPct, soul, ruinsMult)
            totalFinalMult *= ruinsMult
        }

        // 주사위 보정: 1.0 + max(0, verdict) × 0.1
        // (판정 결과가 양수일수록 데미지 10%씩 추가 증가)
        val diceModifier = 1.0 + maxOf(0, verdict) * 0.1

        val damage = (baseDamage * ((100.0 + totalDamageBonus) / 100.0) * totalFinalMult * diceModifier).toInt()
        out.printf(
            "데미지 계산: [(기본 %d) x (100 + %d)%%] x (최종 %.4f배) x (주사위 보정 %.2f) = %d%n",
            baseDamage, totalDamageBonus, totalFinalMult, diceModifier, damage
        )

        val finalDamage = Main.criticalHit(precision, damage, out)
        out.printf("최종 데미지 : %d%n", finalDamage)
        return Result(0, finalDamage, true, mana, 0)
    }
}