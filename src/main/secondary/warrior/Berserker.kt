package main.secondary.warrior

import main.Main
import java.io.PrintStream
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * 버서커
 *
 * 판정 사용 스탯 : 힘, 체력(+민첩)
 *
 * 기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 *
 * 분노 (패시브) : 감소한 체력 1당 가하는 데미지가 2% 증가합니다. (최대 100%)
 * 희열 (패시브) : 받는 데미지가 2배, 가하는 데미지가 2배로 증가합니다.
 *
 * 찍어내리기 (기술) : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
 * 부수기 (기술) : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
 * 일격 (기술) : 대상에게 D20의 피해를 입힙니다. (스태미나 5 소모)
 * 무지성 난타 (기술) : 대상에게 4D8의 피해를 입힙니다. (스태미나 8 소모)
 * 흉폭한 맹공 (기술) : 대상에게 5D12의 피해를 입힙니다. (스태미나 12 소모)
 * 최후의 일격 (기술) : 대상에게 4D20의 피해를 입힙니다. 체력이 10% 이하로 남았을 때 사용 가능합니다. 모든 스태미나를 소모합니다.

 * 저항 (스킬) : 다음 턴까지 받는 데미지가 50% 감소합니다. (마나 5 소모, 쿨타임 10턴)
 *
 * 동귀어진 (전용 수비) : 받는 데미지가 2배로 증가합니다. 입은 피해의 4배만큼 적에게 피해를 입힙니다. (스태미나 6 소모, 쿨타임 10턴)
 */
class Berserker {

    /**
     * 버서커 동귀어진 : 받는 데미지가 2배로 증가합니다. 입은 피해의 4배만큼 적에게 피해를 입힙니다. (스태미나 6 소모, 쿨타임 10턴)
     *
     * @param stat 사용할 스탯
     * @param damageTaken 받은 데미지
     * @param resistance 저항 스킬 적용 여부 (받는 데미지 50% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun mutualDestruction(
        stat: Int,
        damageTaken: Int,
        resistance: Boolean,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 동귀어진 사용")
        var damage = damageTaken
        if (resistance) {
            damage = (damage * 0.5).roundToInt()
            out.println("저항 스킬 적용: 받는 데미지 50% 감소")
        }
        damage *= 2
        out.println("희열 적용: 받는 데미지 2배 증가")

        if (main.Main.verdict(stat, out) <= 0) {
            return main.Result(damage, 0, false, 0, 0)
        }
        val counterDamage = damage * 4
        out.println("적에게 입히는 반격 데미지 : $counterDamage")
        return main.Result(damage*2, counterDamage, true, 0, 0)
    }

    /**
     * 버서커 찍어내리기 : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param euphoria 감소한 체력 값
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun smash(
        stat: Int,
        euphoria: Int,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 찍어내리기 사용")
        return normalAttack(stat, 1, 8, euphoria, 1, out)
    }

    /**
     * 버서커 부수기 : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat 사용할 스탯
     * @param euphoria 감소한 체력 값
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun crush(
        stat: Int,
        euphoria: Int,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 부수기 사용")
        return normalAttack(stat, 2, 6, euphoria, 2, out)
    }

    /**
     * 버서커 일격 : 대상에게 D20의 피해를 입힙니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param euphoria 감소한 체력 값
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun strike(
        stat: Int,
        euphoria: Int,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 일격 사용")
        return normalAttack(stat, 1, 20, euphoria, 5, out)
    }

    /**
     * 버서커 무지성 난타 : 대상에게 4D8의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat 사용할 스탯
     * @param euphoria 감소한 체력 값
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun mindlessThrashing(
        stat: Int,
        euphoria: Int,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 무지성 난타 사용")
        return normalAttack(stat, 4, 8, euphoria, 8, out)
    }

    /**
     * 버서커 흉폭한 맹공 : 대상에게 5D12의 피해를 입힙니다. (스태미나 12 소모)
     *
     * @param stat 사용할 스탯
     * @param euphoria 감소한 체력 값
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun ferociousAssault(
        stat: Int,
        euphoria: Int,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 흉폭한 맹공 사용")
        return normalAttack(stat, 5, 12, euphoria, 12, out)
    }

    /**
     * 버서커 최후의 일격 : 대상에게 4D20의 피해를 입힙니다. 체력이 10% 이하로 남았을 때 사용 가능합니다. 모든 스태미나를 소모합니다.
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param currentStamina 현재 스태미나
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun finalStrike(
        stat: Int,
        currentHp: Int,
        maxHp: Int,
        currentStamina: Int,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 최후의 일격 사용")
        if (currentHp > maxHp * 0.1) {
            out.println("체력이 10% 이하가 아닙니다. 기술 사용 실패.")
            return main.Result(0, 0, false, 0, 0)
        }

        if (Main.verdict(stat, out) <= 0) {
            return main.Result(0, 0, false, 0, currentStamina)
        }

        var damage = Main.dice(4, 20, out)
        out.println("기본 데미지 : $damage")

        val euphoria = maxHp - currentHp
        var damageMultiplier = (1.0 + min(euphoria * 0.02, 1.0)) * 2 // 희열 패시브 + 분노 패시브
        out.println("분노 패시브 적용: 가하는 데미지 ${"%.2f".format(1.0 + min(euphoria * 0.02, 1.0))} 배")
        out.println("희열 패시브 적용: 가하는 데미지 2배")

        damage = (damage * damageMultiplier).roundToInt()
        out.println("비율 적용 데미지 : $damage")

        val sideDamage = Main.sideDamage(damage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        damage += sideDamage
        out.println("최종 데미지 : $damage")

        return main.Result(0, damage, true, 0, currentStamina)
    }

    /**
     * 버서커 범용 공격 메소드
     *
     * @param stat 사용할 스탯
     * @param dices 주사위 개수
     * @param sides 주사위 면수
     * @param euphoria 감소한 체력 값
     * @param stamina 현재 스태미나
     * @param out 출력 스트림
     * @return 결과 객체
     */
    private fun normalAttack(
        stat: Int,
        dices: Int,
        sides: Int,
        euphoria: Int,
        stamina: Int,
        out: PrintStream
    ): main.Result {
        if (Main.verdict(stat, out) <= 0) {
            return main.Result(0, 0, false, 0, stamina)
        }
        var damage = Main.dice(dices, sides, out)

        out.println("기본 데미지 : $damage")
        val damageMultiplier = (1.0 + min(euphoria * 0.02, 1.0)) * 2 // 희열 패시브
        out.println("분노 패시브 적용: 가하는 데미지 ${"%.2f".format(1.0 + min(euphoria * 0.02, 1.0))} 배")
        out.println("희열 패시브 적용: 가하는 데미지 2배")
        damage = (damage * damageMultiplier).roundToInt()
        out.println("비율 적용 데미지 : $damage")
        val sideDamage = Main.sideDamage(damage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        damage += sideDamage
        out.println("최종 데미지 : $damage")
        return main.Result(0, damage, true, 0, stamina)

    }

}