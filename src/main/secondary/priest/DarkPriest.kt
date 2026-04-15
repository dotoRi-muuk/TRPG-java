package main.secondary.priest

import main.Main
import main.Result
import java.io.PrintStream
import kotlin.math.roundToInt

/**
 * 어둠의 사제 (Dark Priest)
 *
 * 판정 사용 스탯 : 지능(지혜)
 *
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * 레벨 기반 최종 데미지 기본 계수: (100 + (레벨)^2)%
 * 직업 외 최종 데미지 기본값: 100% (1배)
 *
 * [패시브]
 * - 축복: 아군의 스탯이 2 감소하고, 본인의 스탯이 3 증가합니다. (전투 중 스탯 효과, 계산에 미반영)
 * - 지배: 아군 공격 시 2회 데미지가 150% 증가합니다. (데미지 +150%)
 * - 강탈: 데미지가 (자신의 공격으로 아군이 잃은 체력) x 5% 증가합니다.
 * - 잠식: 자신이 공격한 아군은 다음 턴 수비 불가, 아군 공격 시 데미지만큼 체력 회복, 다음 턴 받는 피해 75% 감소. (전투 효과, 계산에 미반영)
 *
 * [스킬]
 * - 어둠의 기운: (광역) 대상에게 1D4의 피해를 입힙니다. 1회 데미지를 80% 감소시킵니다. (마나 2 소모)
 * - 손아귀: 대상에게 1D8의 피해를 입힙니다. 1턴 행동 불가를 부여합니다. (마나 5 소모, 쿨타임 4턴)
 * - 우즈마니아: 대상에게 4D12의 피해를 입힙니다. (영창 2턴, 마나 8 소모, 쿨타임 6턴)
 * - 엑실리스터: 대상에게 4D20의 피해를 입힙니다. (영창 4턴, 마나 14 소모, 쿨타임 9턴)
 * - 어나이스필레인: 대상에게 7D12의 피해를 입힙니다. (영창 2턴, 마나 18 소모, 쿨타임 12턴)
 * - 엔시아스티켈리아: 대상에게 9D10의 피해를 입힙니다. (영창 5턴, 마나 16 소모, 쿨타임 13턴)
 * - 저주: 모든 아군에게 현재 체력의 절반만큼 피해. [혈류] 획득 → 원할 때 사용, 해당 턴 최종 데미지 2배. (마나 7 소모, 쿨타임 6턴)
 * - 갈취: (죽은 아군의 모든 스탯) / 3 만큼 스탯 증가. (전투 당 1회, 스탯 효과)
 * - 희생양: 다음 4번의 공격이 아군 1명 추가 공격. 최종 데미지 각각 2배/3배/4배/6배 증가. (마나 6 소모, 쿨타임 8턴)
 * - 침식: 현재 [ ]불가 아군 1명당 이번 턴 데미지 80% 증가. 턴 소모 없음. (마나 4 소모, 쿨타임 7턴)
 * - 신앙심: 다음 턴까지 자신 제외 모두에게 행동 불가. 데미지가 (인원수) x 50% 증가. (마나 7 소모, 쿨타임 13턴)
 */
class DarkPriest {

    /**
     * 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat             사용할 스탯 (지능/지혜)
     * @param level            캐릭터 레벨 (최종 데미지 기본 계수: (100 + 레벨²)%)
     * @param damageBonus      직업 외 데미지 증가 (%)
     * @param finalDamageBonus 직업 외 최종 데미지 증가 (%, 기본값 100 = 1배)
     * @param precision        정밀 스탯
     * @param domination       지배 패시브 적용 여부 (데미지 +150%)
     * @param stolenHp         강탈 패시브: 아군이 잃은 체력 (데미지 += stolenHp × 5%)
     * @param bloodflow        혈류 사용 여부 (저주 스킬 획득, 최종 데미지 ×2)
     * @param scapegoatHit     희생양 스킬 적용 공격 번호 (0=미적용, 1=×2, 2=×3, 3=×4, 4=×6)
     * @param erosionAllies    침식 스킬: [ ]불가 상태 아군 수 (1명당 데미지 +80%)
     * @param pietyAllies      신앙심 스킬: 행동 불가 부여 인원수 (1명당 데미지 +50%)
     * @param out              출력 스트림
     * @return 결과 객체
     */
    fun plain(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 기본 공격 사용")
        return normalAttack(stat, 1, 6, 0, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 어둠의 기운 : (광역) 대상에게 1D4의 피해를 입힙니다. 1회 데미지를 80% 감소시킵니다.
     * (마나 2 소모)
     */
    fun darkEnergy(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 어둠의 기운 사용 (광역, 1회 데미지 80% 감소)")
        return normalAttack(stat, 1, 4, 2, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 손아귀 : 대상에게 1D8의 피해를 입힙니다. 1턴 행동 불가를 부여합니다.
     * (마나 5 소모, 쿨타임 4턴)
     */
    fun grasp(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 손아귀 사용")
        return normalAttack(stat, 1, 8, 5, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 우즈마니아 : 대상에게 4D12의 피해를 입힙니다.
     * (영창 2턴, 마나 8 소모, 쿨타임 6턴)
     */
    fun uzmania(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 우즈마니아 사용")
        return normalAttack(stat, 4, 12, 8, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 엑실리스터 : 대상에게 4D20의 피해를 입힙니다.
     * (영창 4턴, 마나 14 소모, 쿨타임 9턴)
     */
    fun exilister(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 엑실리스터 사용")
        return normalAttack(stat, 4, 20, 14, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 어나이스필레인 : 대상에게 7D12의 피해를 입힙니다.
     * 남은 영창 시간이 2턴 이하일 때 모든 아군이 공격 불가 상태가 됩니다.
     * (영창 2턴, 마나 18 소모, 쿨타임 12턴)
     */
    fun anaisPhilane(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 어나이스필레인 사용")
        return normalAttack(stat, 7, 12, 18, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 엔시아스티켈리아 : 대상에게 9D10의 피해를 입힙니다.
     * 발동 시 1회 모든 아군이 받는 데미지가 100% 증가합니다.
     * (영창 5턴, 마나 16 소모, 쿨타임 13턴)
     */
    fun enthiaStickelia(
        stat: Int, level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        out.println("어둠의 사제 - 엔시아스티켈리아 사용")
        return normalAttack(stat, 9, 10, 16, level, damageBonus, finalDamageBonus, precision,
            domination, stolenHp, bloodflow, scapegoatHit, erosionAllies, pietyAllies, out)
    }

    /**
     * 범용 데미지 공격 메소드
     *
     * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
     *
     * - 데미지 증가 (합산):
     *   직업 외 damageBonus + 지배(+150%) + 강탈(stolenHp×5%) + 침식(erosionAllies×80%) + 신앙심(pietyAllies×50%)
     * - 최종 데미지 (곱산):
     *   레벨 계수 (100+레벨²)% × 직업 외 finalDamageBonus × 혈류(×2) × 희생양(×2/3/4/6)
     */
    private fun normalAttack(
        stat: Int, dices: Int, sides: Int, mana: Int,
        level: Int, damageBonus: Int, finalDamageBonus: Int, precision: Int,
        domination: Boolean, stolenHp: Int, bloodflow: Boolean, scapegoatHit: Int,
        erosionAllies: Int, pietyAllies: Int, out: PrintStream
    ): Result {
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) return Result(0, 0, false, mana, 0)

        val baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")

        // ── 데미지 증가 (합산) ──
        var totalDamageBonus = damageBonus

        if (domination) {
            out.println("지배 패시브 적용: 데미지 +150%")
            totalDamageBonus += 150
        }
        if (stolenHp > 0) {
            val stolenBonus = stolenHp * 5
            out.println("강탈 패시브 적용: 데미지 +${stolenBonus}% (아군 잃은 체력 ${stolenHp} × 5%)")
            totalDamageBonus += stolenBonus
        }
        if (erosionAllies > 0) {
            val erosionBonus = erosionAllies * 80
            out.println("침식 스킬 적용: 데미지 +${erosionBonus}% ([ ]불가 아군 ${erosionAllies}명 × 80%)")
            totalDamageBonus += erosionBonus
        }
        if (pietyAllies > 0) {
            val pietyBonus = pietyAllies * 50
            out.println("신앙심 스킬 적용: 데미지 +${pietyBonus}% (행동 불가 ${pietyAllies}명 × 50%)")
            totalDamageBonus += pietyBonus
        }

        // ── 최종 데미지 (곱산) ──
        val safeLevel = maxOf(0, level)
        val levelCoeff = 100.0 + safeLevel.toDouble() * safeLevel
        out.printf("레벨 %d 기반 최종 데미지 계수: (100 + %d²) = %.1f%%%n", safeLevel, safeLevel, levelCoeff)

        var totalFinalMult = (levelCoeff / 100.0) * (finalDamageBonus / 100.0)
        out.printf("직업 외 최종 데미지 배율: %d%% (×%.4f)%n", finalDamageBonus, finalDamageBonus / 100.0)

        if (bloodflow) {
            out.println("혈류 적용: 최종 데미지 ×2")
            totalFinalMult *= 2.0
        }

        if (scapegoatHit in 1..4) {
            val scapegoatMult = when (scapegoatHit) {
                1 -> 2.0
                2 -> 3.0
                3 -> 4.0
                else -> 6.0
            }
            out.printf("희생양 스킬 적용: 최종 데미지 ×%.0f (%d번째 공격)%n", scapegoatMult, scapegoatHit)
            totalFinalMult *= scapegoatMult
        }

        // ── 주사위 보정 ──
        val diceModifier = 1.0 + maxOf(0, verdict) * 0.1

        val damage = (baseDamage * ((100.0 + totalDamageBonus) / 100.0) * totalFinalMult * diceModifier).roundToInt()
        out.printf(
            "데미지 계산: [(기본 %d) x (100 + %d)%%] x (최종 %.4f배) x (주사위 보정 %.2f) = %d%n",
            baseDamage, totalDamageBonus, totalFinalMult, diceModifier, damage
        )

        val finalDamage = Main.criticalHit(precision, damage, out)
        out.printf("최종 데미지 : %d%n", finalDamage)
        return Result(0, finalDamage, true, mana, 0)
    }
}