package main.secondary.priest

import main.Main
import main.Result
import java.io.PrintStream
import kotlin.math.roundToInt

/**
 * 어둠의 사제
 *
 * 판정 사용 스탯 : 지능(지혜)
 *
 * - 기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 *
 * 축복 (패시브) : 아군의 스탯이 2 감소하고, 본인의 스탯이 3 증가합니다.
 * * 지배 (패시브) : 아군 공격 시 다음 턴 데미지가 2배로 증가합니다.
 * 강탈 (패시브) : 모든 아군이 받는 효과를 제거하고, 그 효과의 50%로 자신이 효과를 받습니다. (해제 가능)
 * 잠식 (패시브) : 자신이 공격한 아군은 다음 턴 수비 불가가 됩니다. 아군 공격 시 데미지만큼 체력을 회복합니다. 다음 턴 동안 받는 피해가 75%로 감소합니다.
 *
 * - 어둠의 기운 (스킬) : (광역) 대상에게 D4의 피해를 입힙니다. 다음 턴까지 데미지를 80%로 감소시킵니다. (마나 2 소모)
 * - 손아귀 (스킬) : 대상에게 D8의 피해를 입힙니다. 다음 턴 동안 행동 불가를 부여합니다. (마나 5 소모, 쿨타임 4턴)
 * - 우즈마니아 (스킬) : 대상에게 4D12의 피해를 입힙니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 2턴, 마나 8 소모, 쿨타임 6턴)
 * - 엑실리스터 (스킬) : 대상에게 4D20의 피해를 입힙니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 4턴, 마나 14 소모, 쿨타임 9턴)
 * - 어나이스필레인 (스킬) : 대상에게 7D12의 피해를 입힙니다. 남은 영창 시간이 2턴 이하일 때 모든 아군이 공격 불가 상태가 됩니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 2턴, 마나 18 소모, 쿨타임 12턴)
 * - 엔시아스티켈리아 (스킬) : 대상에게 9D10의 피해를 입힙니다. 발동 시 다음 턴 모든 아군이 받는 데미지가 2배로 증가합니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 5턴, 마나 16 소모, 쿨타임 13턴)
 *
 * * 저주 (스킬) : 모든 아군에게 현재 체력의 절반만큼 피해를 줍니다. 다음 턴 데미지가 2배로 증가합니다. (마나 7 소모, 쿨타임 6턴)
 * 갈취 (스킬) : (죽은 아군의 모든 스탯) / 4 만큼 스탯이 증가합니다. (전투 당 1회)
 * * 희생양 (스킬) : 다음 공격의 대상이 1명이라면 아군 1명까지 추가로 공격합니다. 아군이 받는 데미지는 보정 영향을 받지 않습니다. 해당 공격의 데미지가 3배로 증가합니다. (마나 6 소모, 쿨타임 8턴)
 * * 침식 (스킬) : 현재 ()불가 상태의 아군 1명 당 이번 턴 데미지가 50% 증가합니다. 턴을 소모하지 않습니다. (마나 4 소모, 쿨타임 7턴)
 * * 신앙심 (스킬) : 다음 턴까지 자신 제외 모두에게 행동 불가를 부여합니다. 데미지가 2배로 증가합니다. (마나 7 소모, 쿨타임 13턴)
 */
class DarkPriest {

    /**
     * 어둠의 사제 엔시아스티켈리아 : 대상에게 9D10의 피해를 입힙니다. 발동 시 다음 턴 모든 아군이 받는 데미지가 2배로 증가합니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 5턴, 마나 16 소모, 쿨타임 13턴)
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */

    fun enthiaStickelia(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 9, 10, 16, out)
    }

    /**
     * 어둠의 사제 어나이스필레인 : 대상에게 7D12의 피해를 입힙니다. 남은 영창 시간이 2턴 이하일 때 모든 아군이 공격 불가 상태가 됩니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 2턴, 마나 18 소모, 쿨타임 12턴)
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */

    fun anaisPhilane(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 7, 12, 18, out)
    }

    /**
     * 어둠의 사제 엑실리스터 : 대상에게 4D20의 피해를 입힙니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 4턴, 마나 14 소모, 쿨타임 9턴)
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */

    fun exilister(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 4, 20, 14, out)
    }

    /**
     * 어둠의 사제 우즈마니아 : 대상에게 4D12의 피해를 입힙니다. 발동 대기 후 이외 행동이 가능합니다. (다른 스킬 영창은 불가) (영창 2턴, 마나 8 소모, 쿨타임 6턴)
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */

    fun uzmania(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 4, 12, 8, out)
    }

    /**
     * 어둠의 사제 손아귀 : 대상에게 D8의 피해를 입힙니다. 다음 턴 동안 행동 불가를 부여합니다. (마나 5 소모, 쿨타임 4턴)
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */

    fun grasp(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 1, 8, 5, out)
    }

    /**
     * 어둠의 사제 어둠의 기운 : (광역) 대상에게 D4의 피해를 입힙니다. 다음 턴까지 데미지를 80%로 감소시킵니다. (마나 2 소모)
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */

    fun darkEnergy(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 1, 4, 2, out)
    }

    /**
     * 어둠의 사제 기본공격 : 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */
    fun plain(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        out: PrintStream
    ): Result {
        return normalAttack(stat, domination, curse, scapegoat, erosion, piety, 1, 6, 0, out)
    }

    /**
     * 범용 공격 메소드
     * @param stat 사용할 스탯
     * @param domination 지배 패시브 적용 여부 (데미지 2배)
     * @param curse 저주 스킬 사용 여부 (데미지 2배)
     * @param scapegoat 희생양 스킬 사용 여부 (데미지 3배)
     * @param erosion () 불가 상태의 아군 명수 (1명당 데미지 50%씩 증가) 0으로 비활성화
     * @param piety 신앙심 스킬 사용 여부 (데미지 2배)
     * @return 결과 객체
     */
    fun normalAttack(
        stat: Int,
        domination: Boolean,
        curse: Boolean,
        scapegoat: Boolean,
        erosion: Int,
        piety: Boolean,
        dices: Int,
        sides: Int,
        mana: Int,
        out: PrintStream
    ): Result {
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) {
            return Result(0, 0, false, mana, 0)
        }
        val baseDamage = Main.dice(dices, sides, out)
        var damageMultiplier = 1.0
        if (domination) {
            out.println("지배 패시브 적용: 데미지 2배")
            damageMultiplier *= 2
        }
        if (curse) {
            out.println("저주 스킬 적용: 데미지 2배")
            damageMultiplier *= 2
        }
        if (scapegoat) {
            out.println("희생양 스킬 적용: 데미지 3배")
            damageMultiplier *= 3
        }
        if (erosion > 0) {
            out.println("침식 스킬 적용: 데미지 ${erosion * 0.5 + 1} 배")
            damageMultiplier *= (erosion * 0.5 + 1)
        }
        if (piety) {
            out.println("신앙심 스킬 적용: 데미지 2배")
            damageMultiplier *= 2
        }
        val damage = (baseDamage * damageMultiplier).roundToInt()
        out.printf("배율 적용 데미지 : %d%n", damage)
        val sideDamage = Main.sideDamage(damage, stat, out)
        out.printf("데미지 보정치 : %d%n", sideDamage)
        out.printf("최종 데미지 : %d%n", damage + sideDamage)
        return Result(0, damage + sideDamage, true, mana, 0)
    }

}