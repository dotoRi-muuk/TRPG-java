package main.secondary.warrior

import main.Main
import main.Result

/**
 * 창술사
 *
 * 판정 사용 스탯 : 힘(민첩)
 *
 * 기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 *
 * 약점파악 (패시브) : 기본 기술 데미지가 50%로 감소합니다. [연계] 기술 데미지가 2배로 증가합니다.
 * 빈틈 (패시브) : [연계] 발동 성공 후 민첩 판정 성공 시 기본 공격으로 1회 추가 공격이 가능합니다.
 *
 * 돌려 찌르기 (기술) : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
 * 회전 타격 (기술) : 대상에게 D10의 피해를 입힙니다. [연계]를 획득합니다. (스태미나 2 소모)
 * 하단 베기 (기술) : 대상에게 D6의 피해를 입힙니다. 이번 턴 상대에게 수비 불가를 부여합니다. [연계]를 획득합니다. (스태미나 2 소모)
 * [연계] 정면 찌르기 (기술) : 대상에게 2D10의 피해를 입힙니다. (스태미나 1 소모)
 * [연계] 일섬창 (기술) : 대상에게 4D8의 피해를 입힙니다. (스태미나 3 소모)
 * [연계] 천뢰격 (기술) : 대상에게 5D12의 피해를 입힙니다. (스태미나 5 소모)
 *
 * 적응 (스킬) : 다음 3턴 동안 [연계] 데미지가 4배로 증가합니다. (마나 10 소모, 쿨타임 8턴)
 *
 * 회전 회피 (전용 수비) : 민첩, 신속 판정 모두 성공해야 합니다. 회피 성공 시 [연계]를 획득합니다. (스태미나 3 소모)
 */
class SpearMaster {

    fun spinEvasion(
        agi: Int,
        speed: Int,
        damageTaken: Int,
        out: java.io.PrintStream
    ): Result {
        if (Main.verdict(agi, out) > 0 && Main.verdict(speed, out) > 0) {
            out.println("창술사 - 회전 회피 성공! [연계] 획득")
            return Result(0, 0, true, 0, 3)
        } else {
            out.println("창술사 - 회전 회피 실패!")
            return Result(damageTaken, 0, false, 0, 3)
        }
    }

    /**
     * 창술사 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun plain(
        stat: Int,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - 기본 공격 사용")
        val damage = Main.dice(1, 6, out)
        out.println("기본 데미지 : $damage")
        val sideDamage = Main.sideDamage(damage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        out.println("최종 데미지 : ${damage + sideDamage}")
        return main.Result(0, damage+sideDamage, false, 0, 0)
    }

    /**
     * 창술사 돌려 찌르기 : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param agi 민첩 스탯 (빈틈 패시브용)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun spinStab(
        stat: Int,
        agi: Int,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - 돌려 찌르기 사용")
        return normalAttack(stat, agi, 1, 8, false, false, 1, out)
    }

    /**
     * 창술사 회전 타격 : 대상에게 D10의 피해를 입힙니다. [연계]를 획득합니다. (스태미나 2 소모)
     *
     * @param stat 사용할 스탯
     * @param agi 민첩 스탯 (빈틈 패시브용)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun spinStrike(
        stat: Int,
        agi: Int,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - 회전 타격 사용")
        out.println("[연계] 획득")
        return normalAttack(stat, agi, 1, 10, false, false, 2, out)
    }

    /**
     * 창술사 하단 베기 : 대상에게 D6의 피해를 입힙니다. 이번 턴 상대에게 수비 불가를 부여합니다. [연계]를 획득합니다. (스태미나 2 소모)
     *
     * @param stat 사용할 스탯
     * @param agi 민첩 스탯 (빈틈 패시브용)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun lowSlash(
        stat: Int,
        agi: Int,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - 하단 베기 사용")
        out.println("상대에게 수비 불가 부여")
        out.println("[연계] 획득")
        return normalAttack(stat, agi, 1, 6, false, false, 2, out)
    }

    /**
     * 창술사 [연계] 정면 찌르기 : 대상에게 2D10의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param agi 민첩 스탯 (빈틈 패시브용)
     * @param adaptation 적응 스킬 적용 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun frontalStab(
        stat: Int,
        agi: Int,
        adaptation: Boolean,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - [연계] 정면 찌르기 사용")
        return normalAttack(stat, agi, 2, 10, true, adaptation, 1, out)
    }

    /**
     * 창술사 [연계] 일섬창 : 대상에게 4D8의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat 사용할 스탯
     * @param agi 민첩 스탯 (빈틈 패시브용)
     * @param adaptation 적응 스킬 적용 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun flashSpear(
        stat: Int,
        agi: Int,
        adaptation: Boolean,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - [연계] 일섬창 사용")
        return normalAttack(stat, agi, 4, 8, true, adaptation, 3, out)
    }

    /**
     * 창술사 [연계] 천뢰격 : 대상에게 5D12의 피해를 입힙니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param agi 민첩 스탯 (빈틈 패시브용)
     * @param adaptation 적응 스킬 적용 여부
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun heavenlyThunderStrike(
        stat: Int,
        agi: Int,
        adaptation: Boolean,
        out: java.io.PrintStream
    ): main.Result {
        out.println("창술사 - [연계] 천뢰격 사용")
        return normalAttack(stat, agi, 5, 12, true, adaptation, 5, out)
    }

    private fun normalAttack(
        stat: Int,
        agi: Int,
        dices: Int,
        sides: Int,
        combo: Boolean,
        adaptation: Boolean,
        staminaCost: Int,
        out: java.io.PrintStream
    ): main.Result {
        if (main.Main.verdict(stat, out) <= 0) {
            return main.Result(0, 0, false, 0, staminaCost)
        }
        var damage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $damage")
        if (combo) {
            damage = (damage * 2)
            out.println("약점파악 패시브 적용: [연계] 데미지 2배 증가")
            if (adaptation) {
                damage = (damage * 4)
                out.println("적응 스킬 적용: [연계] 데미지 4배 증가")
            }
        } else {
            damage = (damage * 0.5).toInt()
            out.println("약점파악 패시브 적용: 기본 기술 데미지 50% 감소")
        }
        if (combo && Main.verdict(agi, out) > 0) {
            val extraDamage = plain(stat, out).damageDealt
            out.println("빈틈 패시브 적용: 민첩 판정 성공으로 추가 공격 가능")
            out.println("추가 공격 데미지 : $extraDamage")
            damage += extraDamage
        }
        val sideDamage = Main.sideDamage(damage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        out.println("최종 데미지 : ${damage + sideDamage}")
        return main.Result(0, damage + sideDamage, true, 0, staminaCost)
    }
}