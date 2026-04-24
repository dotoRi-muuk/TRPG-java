package main.secondary.warrior

import main.Main
import main.Result
import java.io.PrintStream

/**
 * 웨폰마스터
 *
 * 판정 사용 스탯 : 힘(민첩)
 *
 * 패시브
 * 선택 : 여러 가지 무기를 사용할 수 있습니다. 그중 1개의 무기를 선택해 공격할 수 있습니다.
 * 연계 : 무기를 교체하였을 때 그 다음 턴 공격의 데미지가 2배로 증가합니다.
 * 다양성 : 소지한 무기 하나 당 데미지가 10% 감소, 정수를 부여한 무기 하나 당 데미지가 30% 증가.
 * 대리인 : 정수 스킬 사용 시 해당 무기로 1회 더 공격 가능.
 * 과감함 : 무기가 사용 불가 상태가 되었을 때 다음 턴 가하는 데미지가 1.5배로 증가.
 */
class WeaponMaster {

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat              사용할 스탯
     * @param weaponCount       소지한 무기 수 (다양성 패시브: 무기당 데미지 10% 감소)
     * @param essenceWeapons    정수 부여 무기 수 (다양성 패시브: 정수 무기당 데미지 30% 증가)
     * @param weaponSwitched    지난 턴 무기 교체 여부 (연계 패시브: 데미지 2배)
     * @param boldness          과감함 패시브 여부 (무기 사용불가 후 데미지 1.5배)
     * @param precision         정밀 스탯 (치명타 판정)
     * @param out               출력 스트림
     * @return 결과 객체
     */
    fun plain(
        stat: Int,
        weaponCount: Int,
        essenceWeapons: Int,
        weaponSwitched: Boolean,
        boldness: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("웨폰마스터 - 기본 공격 사용")
        return attack(stat, 1, 6, 0, weaponCount, essenceWeapons, weaponSwitched, boldness, false, precision, 0, out)
    }

    /**
     * 마나 웨폰 : 1턴 동안 필요한 무기를 생성해 사용할 수 있습니다.
     * 생성한 무기 1개당 각각의 무기로 1회 추가로 행동할 수 있습니다.
     * 생성된 무기는 사용 가능한 무기 개수에 포함하지 않습니다.
     * (1개당 마나 3 소모, 쿨타임 10턴)
     *
     * @param weaponsGenerated  생성할 무기 수
     * @param out               출력 스트림
     * @return 결과 객체 (마나 소모 = weaponsGenerated * 3)
     */
    fun manaWeapon(weaponsGenerated: Int, out: PrintStream): Result {
        out.println("웨폰마스터 - 마나 웨폰 사용")
        val manaUsed = weaponsGenerated * 3
        out.printf("무기 %d개 생성. 마나 %d 소모 (1개당 마나 3).%n", weaponsGenerated, manaUsed)
        out.printf("이번 턴 생성된 무기 %d개로 각각 1회 추가 행동 가능.%n", weaponsGenerated)
        out.println("생성된 무기는 사용 가능한 무기 개수에 포함되지 않습니다. (쿨타임 10턴)")
        return Result(0, 0, true, manaUsed, 0)
    }

    /**
     * 허상 전환 : 이 스킬 사용 후 다음 행동을 실패한다면 가진 모든 디버프를 제거하고 즉시 턴을 1회 얻습니다.
     * 해당 턴의 최종 데미지가 2배로 증가합니다.
     * (마나 5, 쿨타임 8턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 5 소모)
     */
    fun illusionSwitch(out: PrintStream): Result {
        out.println("웨폰마스터 - 허상 전환 사용")
        out.println("효과: 다음 행동 실패 시 모든 디버프 제거 및 즉시 추가 턴 획득.")
        out.println("해당 추가 턴의 최종 데미지 2배 증가. (마나 5, 쿨타임 8턴)")
        return Result(0, 0, true, 5, 0)
    }

    /**
     * 기회 포착 : 다음 턴까지 현재 사용 불가 상태인 무기당 데미지가 50% 증가합니다.
     * 이 효과가 지속될 때 사용한 무기는 해당 무기의 정수 스킬의 쿨타임만큼 사용 불가능합니다.
     * (마나 5, 쿨타임 8턴)
     *
     * @param unavailableWeapons 현재 사용 불가 무기 수
     * @param out                출력 스트림
     * @return 결과 객체 (마나 5 소모)
     */
    fun opportunityCapture(unavailableWeapons: Int, out: PrintStream): Result {
        out.println("웨폰마스터 - 기회 포착 사용")
        val bonus = unavailableWeapons * 50
        out.printf("사용 불가 무기 %d개 × 데미지 50%% = +%d%% 데미지 (다음 턴까지 적용)%n", unavailableWeapons, bonus)
        out.println("효과 지속 중 사용한 무기는 해당 정수 스킬 쿨타임만큼 사용 불가. (마나 5, 쿨타임 8턴)")
        return Result(0, 0, true, 5, 0)
    }

    /**
     * 순환 : 모든 사용 불가 상태의 무기를 사용 가능으로 변경합니다.
     * 이때 효과를 부여한 무기 하나 당 다음 턴까지 데미지가 30% 감소합니다.
     * (마나 2, 쿨타임 2턴)
     *
     * @param revivedEssenceWeapons 소생된 정수 부여 무기 수 (데미지 30% 감소)
     * @param out                   출력 스트림
     * @return 결과 객체 (마나 2 소모)
     */
    fun cycle(revivedEssenceWeapons: Int, out: PrintStream): Result {
        out.println("웨폰마스터 - 순환 사용")
        val penalty = revivedEssenceWeapons * 30
        out.println("모든 사용 불가 무기를 사용 가능 상태로 변경.")
        out.printf("소생된 정수 부여 무기 %d개 × 30%% = 다음 턴까지 데미지 -%d%% 감소.%n", revivedEssenceWeapons, penalty)
        return Result(0, 0, true, 2, 0)
    }

    /**
     * 순간 교체 (민첩 판정) : 턴 사용 후 휴식 없이 무기를 교체할 수 있습니다.
     * 이후 1회 더 턴을 사용할 수 있습니다. 턴을 소모하지 않습니다.
     * (마나 4, 쿨타임 5턴)
     *
     * @param dexterity 민첩 스탯 (판정용)
     * @param out       출력 스트림
     * @return 결과 객체 (마나 4 소모)
     */
    fun instantSwap(dexterity: Int, out: PrintStream): Result {
        out.println("웨폰마스터 - 순간 교체 사용")
        val verdict = Main.verdict(dexterity, out)
        if (verdict <= 0) {
            out.println("판정 실패: 순간 교체에 실패했습니다.")
            return Result(0, 0, false, 4, 0)
        }
        out.println("판정 성공: 휴식 없이 무기를 교체하고 추가 턴을 획득합니다. (마나 4, 쿨타임 5턴)")
        return Result(0, 0, true, 4, 0)
    }

    /**
     * 공통 공격 메소드
     */
    private fun attack(
        stat: Int,
        dices: Int,
        sides: Int,
        mana: Int,
        weaponCount: Int,
        essenceWeapons: Int,
        weaponSwitched: Boolean,
        boldness: Boolean,
        instantSwapUsed: Boolean,
        precision: Int,
        stamina: Int,
        out: PrintStream
    ): Result {
        val verdict = Main.verdict(stat, out)
        if (verdict <= 0) {
            return Result(0, 0, false, mana, stamina)
        }
        val diceRoll = stat - verdict
        val baseDamage = Main.dice(dices, sides, out)
        out.printf("기본 데미지 : %d%n", baseDamage)

        // 다양성 패시브: 무기당 -10%, 정수 부여 무기당 +30%
        var flatBonus = 0
        if (weaponCount > 0) {
            val diversityPenalty = weaponCount * 10
            out.printf("다양성 패시브 적용: 무기 %d개 × 10%% = -%d%% 데미지 감소%n", weaponCount, diversityPenalty)
            flatBonus -= diversityPenalty
        }
        if (essenceWeapons > 0) {
            val essenceBonus = essenceWeapons * 30
            out.printf("다양성 패시브 적용: 정수 부여 무기 %d개 × 30%% = +%d%% 데미지 증가%n", essenceWeapons, essenceBonus)
            flatBonus += essenceBonus
        }

        // 최종 데미지 배율 보정
        var finalMultiplier = 1.0

        // 연계 패시브: 지난 턴 무기 교체 시 데미지 2배
        if (weaponSwitched) {
            out.println("연계 패시브 적용: 지난 턴 무기 교체 → 데미지 2배")
            finalMultiplier *= 2.0
        }

        // 과감함 패시브
        if (boldness) {
            out.println("과감함 패시브 적용: 무기 사용불가 후 데미지 1.5배")
            finalMultiplier *= 1.5
        }

        val damage = Main.calculateDamage(baseDamage, flatBonus, finalMultiplier, out)
        val sideDmg = Main.sideDamage(damage, stat, out, diceRoll)
        val totalDamage = damage + sideDmg
        out.printf("데미지 보정치 : %d%n", sideDmg)

        val critDamage = Main.criticalHit(precision, totalDamage, out)
        out.printf("최종 데미지 : %d%n", critDamage)

        return Result(0, critDamage, true, mana, stamina)
    }
}
