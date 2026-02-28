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
 * 분노 (패시브) : 감소한 체력 1당 가하는 데미지가 5% 증가합니다. (최대 300%)
 * 광기 (패시브) : 가한 데미지의 10%만큼 체력을 회복합니다.
 * 희열 (패시브) : 체력이 20% 이하일 때 활성화됩니다.
 *   - 턴당 2회 행동 가능 (휴식 사용 시 1회로 고정)
 *   - 스태미나 소모 50% 감소
 *   - 같은 스킬 연속 2회 사용 가능
 *   - 받는 데미지 100% 증가
 *   - 가하는 데미지 150% 증가
 * 피의 축제 (패시브) : HP 피해를 받을 때 해당 피해의 50%만큼 다음 턴까지 지속되는 쉴드를 획득합니다.
 *   (주의: 동귀어진 외에 피해를 받는 경우 호출자가 직접 계산해야 합니다.)
 *
 * 기본 공격 : 대상에게 1D6의 피해를 입힙니다.
 * 찍어내리기 (기술) : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
 * 부수기 (기술) : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
 * 일격 (기술) : 대상에게 D20의 피해를 입힙니다. (스태미나 5 소모)
 * 무지성 난타 (기술) : 대상에게 4D8의 피해를 입힙니다. (스태미나 8 소모)
 * 흉폭한 맹공 (기술) : 대상에게 5D12의 피해를 입힙니다. (스태미나 12 소모)
 * 최후의 일격 (기술) : 대상에게 4D20의 피해를 입힙니다. 체력이 10% 이하로 남았을 때 사용 가능합니다. 모든 스태미나를 소모합니다.
 * 아드레날린 (스킬) : 최대 체력의 10%를 소모합니다. 스킬 4회 사용 후 기본 공격을 1회 사용할 수 있습니다. (중첩 불가, 마나 4 소모, 쿨타임 6턴)
 * 극복 (스킬) : 체력이 5% 이하일 때 사용 가능합니다. 이번 턴 가한 피해만큼 체력을 회복합니다. 턴을 소모하지 않습니다. (마나 8 소모, 쿨타임 15턴)
 * 공포 새기기 (스킬) : 체력이 15% 이하일 때 사용 가능합니다. 대상은 다음 2턴 동안 공격할 수 없습니다. (마나 6 소모, 쿨타임 8턴)
 * 저항 (스킬) : 다음 턴까지 받는 데미지가 50% 감소합니다. 다음 턴 가한 피해만큼 그 다음 턴까지 지속되는 쉴드를 획득합니다. (마나 5 소모, 쿨타임 10턴)
 *
 * 동귀어진 (전용 수비) : 받는 데미지가 100% 증가합니다. 입은 피해의 400%만큼 적에게 피해를 입힙니다. (스태미나 6 소모, 쿨타임 10턴)
 */
class Berserker {

    /**
     * 버서커 동귀어진 : 받는 데미지가 100% 증가합니다. 입은 피해의 400%만큼 적에게 피해를 입힙니다. (스태미나 6 소모, 쿨타임 10턴)
     *
     * @param stat 사용할 스탯
     * @param damageTaken 받은 데미지
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param resistance 저항 스킬 적용 여부 (받는 데미지 50% 감소)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun mutualDestruction(
        stat: Int,
        damageTaken: Int,
        currentHp: Int,
        maxHp: Int,
        resistance: Boolean,
        out: PrintStream
    ): main.Result {
        out.println("버서커 - 동귀어진 사용")
        var damage = damageTaken
        if (resistance) {
            damage = (damage * 0.5).roundToInt()
            out.println("저항 스킬 적용: 받는 데미지 50% 감소")
        }

        val euphoriaActive = currentHp <= maxHp * 0.20
        // 동귀어진 +100%, 희열 패시브 +100% (if active)
        var incomingBonus = 100
        out.println("동귀어진 적용: 받는 데미지 100% 증가")
        if (euphoriaActive) {
            incomingBonus += 100
            out.println("희열 패시브 적용: 받는 데미지 100% 증가")
        }
        damage = (damage * (100 + incomingBonus) / 100.0).roundToInt()
        out.println("최종 받는 데미지: $damage")

        // 피의 축제 패시브: HP 피해의 50%만큼 쉴드 획득
        val shield = (damage * 0.5).roundToInt()
        out.println("피의 축제 패시브 적용: 다음 턴까지 쉴드 $shield 획득")

        if (main.Main.verdict(stat, out) <= 0) {
            return main.Result(damage, 0, false, 0, 6)
        }

        // 반격 데미지: 받은 피해의 400% + 분노/희열 패시브 적용
        val missingHp = maxHp - currentHp
        val rageBonus = min(missingHp * 5, 300)
        out.println("분노 패시브 적용: 가하는 데미지 $rageBonus% 증가 (감소 체력 $missingHp * 5%, 최대 300%)")
        val euphoriaOutBonus = if (euphoriaActive) 150 else 0
        if (euphoriaActive) out.println("희열 패시브 적용: 가하는 데미지 150% 증가")

        // calculateDamage(damage, 300 + bonuses, 1.0) = damage * (400 + bonuses) / 100 = 400% + bonuses
        val counterDamage = Main.calculateDamage(damage, 300 + rageBonus + euphoriaOutBonus, 1.0, out)
        out.println("반격 데미지: $counterDamage")

        // 광기 패시브: 가한 데미지의 10% 회복
        val heal = (counterDamage * 0.1).roundToInt()
        out.println("광기 패시브 적용: $heal HP 회복")

        return main.Result(damage, counterDamage, true, 0, 6)
    }

    /**
     * 버서커 기본 공격 : 대상에게 1D6의 피해를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun plain(stat: Int, currentHp: Int, maxHp: Int, precision: Int, out: PrintStream): main.Result {
        out.println("버서커 - 기본 공격 사용")
        return normalAttack(stat, 1, 6, currentHp, maxHp, 0, precision, out)
    }

    /**
     * 버서커 찍어내리기 : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun smash(stat: Int, currentHp: Int, maxHp: Int, precision: Int, out: PrintStream): main.Result {
        out.println("버서커 - 찍어내리기 사용")
        return normalAttack(stat, 1, 8, currentHp, maxHp, 1, precision, out)
    }

    /**
     * 버서커 부수기 : 대상에게 2D6의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun crush(stat: Int, currentHp: Int, maxHp: Int, precision: Int, out: PrintStream): main.Result {
        out.println("버서커 - 부수기 사용")
        return normalAttack(stat, 2, 6, currentHp, maxHp, 2, precision, out)
    }

    /**
     * 버서커 일격 : 대상에게 D20의 피해를 입힙니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun strike(stat: Int, currentHp: Int, maxHp: Int, precision: Int, out: PrintStream): main.Result {
        out.println("버서커 - 일격 사용")
        return normalAttack(stat, 1, 20, currentHp, maxHp, 5, precision, out)
    }

    /**
     * 버서커 무지성 난타 : 대상에게 4D8의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun mindlessThrashing(stat: Int, currentHp: Int, maxHp: Int, precision: Int, out: PrintStream): main.Result {
        out.println("버서커 - 무지성 난타 사용")
        return normalAttack(stat, 4, 8, currentHp, maxHp, 8, precision, out)
    }

    /**
     * 버서커 흉폭한 맹공 : 대상에게 5D12의 피해를 입힙니다. (스태미나 12 소모)
     *
     * @param stat 사용할 스탯
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun ferociousAssault(stat: Int, currentHp: Int, maxHp: Int, precision: Int, out: PrintStream): main.Result {
        out.println("버서커 - 흉폭한 맹공 사용")
        return normalAttack(stat, 5, 12, currentHp, maxHp, 12, precision, out)
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
        precision: Int,
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

        val baseDamage = Main.dice(4, 20, out)
        out.println("기본 데미지 : $baseDamage")

        val missingHp = maxHp - currentHp
        val rageBonus = min(missingHp * 5, 300)
        out.println("분노 패시브 적용: 가하는 데미지 $rageBonus% 증가 (감소 체력 $missingHp * 5%, 최대 300%)")
        val euphoriaActive = currentHp <= maxHp * 0.20
        val euphoriaOutBonus = if (euphoriaActive) 150 else 0
        if (euphoriaActive) out.println("희열 패시브 적용: 가하는 데미지 150% 증가")

        val damageAfterPassives = Main.calculateDamage(baseDamage, rageBonus + euphoriaOutBonus, 1.0, out)
        val sideDamage = Main.sideDamage(damageAfterPassives, stat, out)
        val totalDamage = damageAfterPassives + sideDamage
        out.println("데미지 보정치 : $sideDamage")
        out.println("최종 데미지 : $totalDamage")
        val finalDamage = Main.criticalHit(precision, totalDamage, out)

        // 광기 패시브: 가한 데미지의 10% 회복
        val heal = (finalDamage * 0.1).roundToInt()
        out.println("광기 패시브 적용: $heal HP 회복")

        return main.Result(0, finalDamage, true, 0, currentStamina)
    }

    /**
     * 버서커 아드레날린 : 최대 체력의 10%를 소모합니다. 스킬 4회 사용 후 기본 공격을 1회 사용할 수 있습니다. (중첩 불가, 마나 4 소모, 쿨타임 6턴)
     *
     * @param maxHp 최대 체력
     * @param out 출력 스트림
     * @return 결과 객체 (damageTaken = 자해 피해량)
     */
    private fun adrenaline(maxHp: Int, out: PrintStream): main.Result {
        out.println("버서커 - 아드레날린 사용")
        val selfDamage = (maxHp * 0.1).roundToInt()
        out.println("아드레날린 자해 데미지 (최대 체력의 10%): $selfDamage")
        out.println("스킬 4회 사용 후 기본 공격 1회 사용 가능 (중첩 불가)")
        return main.Result(selfDamage, 0, true, 4, 0)
    }

    /**
     * 버서커 극복 : 체력이 5% 이하일 때 사용 가능합니다. 이번 턴 가한 피해만큼 체력을 회복합니다. 턴을 소모하지 않습니다. (마나 8 소모, 쿨타임 15턴)
     *
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param damageDealtThisTurn 이번 턴에 가한 피해량
     * @param out 출력 스트림
     * @return 결과 객체 (damageTaken 음수 = 회복량)
     */
    private fun overcome(currentHp: Int, maxHp: Int, damageDealtThisTurn: Int, out: PrintStream): main.Result {
        out.println("버서커 - 극복 사용")
        if (currentHp > maxHp * 0.05) {
            out.println("체력이 5% 이하가 아닙니다. 기술 사용 실패.")
            return main.Result(0, 0, false, 0, 0)
        }
        out.println("이번 턴 가한 피해만큼 체력 회복: $damageDealtThisTurn HP")
        return main.Result(-damageDealtThisTurn, 0, true, 8, 0)
    }

    /**
     * 버서커 공포 새기기 : 체력이 15% 이하일 때 사용 가능합니다. 대상은 다음 2턴 동안 공격할 수 없습니다. (마나 6 소모, 쿨타임 8턴)
     *
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param out 출력 스트림
     * @return 결과 객체
     */
    private fun instillFear(currentHp: Int, maxHp: Int, out: PrintStream): main.Result {
        out.println("버서커 - 공포 새기기 사용")
        if (currentHp > maxHp * 0.15) {
            out.println("체력이 15% 이하가 아닙니다. 기술 사용 실패.")
            return main.Result(0, 0, false, 0, 0)
        }
        out.println("적은 다음 2턴 동안 공격할 수 없습니다.")
        return main.Result(0, 0, true, 6, 0)
    }

    /**
     * 버서커 저항 : 다음 턴까지 받는 데미지가 50% 감소합니다. 다음 턴 가한 피해만큼 그 다음 턴까지 지속되는 쉴드를 획득합니다. (마나 5 소모, 쿨타임 10턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    private fun resistance(out: PrintStream): main.Result {
        out.println("버서커 - 저항 사용")
        out.println("다음 턴까지 받는 데미지 50% 감소")
        out.println("다음 턴 가한 피해만큼 그 다음 턴까지 지속되는 쉴드 획득")
        return main.Result(0, 0, true, 5, 0)
    }

    /**
     * 버서커 범용 공격 메소드
     *
     * @param stat 사용할 스탯
     * @param dices 주사위 개수
     * @param sides 주사위 면수
     * @param currentHp 현재 체력
     * @param maxHp 최대 체력
     * @param stamina 스태미나 소모량
     * @param out 출력 스트림
     * @return 결과 객체
     */
    private fun normalAttack(
        stat: Int,
        dices: Int,
        sides: Int,
        currentHp: Int,
        maxHp: Int,
        stamina: Int,
        precision: Int,
        out: PrintStream
    ): main.Result {
        if (Main.verdict(stat, out) <= 0) {
            return main.Result(0, 0, false, 0, stamina)
        }
        val baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")

        val missingHp = maxHp - currentHp
        val rageBonus = min(missingHp * 5, 300)
        out.println("분노 패시브 적용: 가하는 데미지 $rageBonus% 증가 (감소 체력 $missingHp * 5%, 최대 300%)")

        val euphoriaActive = currentHp <= maxHp * 0.20
        val euphoriaOutBonus = if (euphoriaActive) 150 else 0
        if (euphoriaActive) out.println("희열 패시브 적용: 가하는 데미지 150% 증가")

        // 희열 패시브: 스태미나 소모 50% 감소
        val effectiveStamina = if (euphoriaActive) (stamina * 0.5).roundToInt() else stamina
        if (euphoriaActive) out.println("희열 패시브 적용: 스태미나 소모 50% 감소 ($stamina -> $effectiveStamina)")

        val damageAfterPassives = Main.calculateDamage(baseDamage, rageBonus + euphoriaOutBonus, 1.0, out)
        val sideDamage = Main.sideDamage(damageAfterPassives, stat, out)
        val totalDamage = damageAfterPassives + sideDamage
        out.println("데미지 보정치 : $sideDamage")
        out.println("최종 데미지 : $totalDamage")
        val finalDamage = Main.criticalHit(precision, totalDamage, out)

        // 광기 패시브: 가한 데미지의 10% 회복
        val heal = (finalDamage * 0.1).roundToInt()
        out.println("광기 패시브 적용: $heal HP 회복")

        return main.Result(0, finalDamage, true, 0, effectiveStamina)
    }

}