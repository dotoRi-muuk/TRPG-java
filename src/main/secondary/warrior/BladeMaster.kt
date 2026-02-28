package main.secondary.warrior

import main.Main
import main.Result
import java.io.PrintStream

/**
 * 무사
 *
 * 판정 사용 스탯 : 힘(+체력)
 */
class BladeMaster {

    /**
     * 무사 발검 : 대상에게 D8의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun drawBlade(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 발검 사용")
        return normalAttack(
            stat,
            1,
            8,
            1,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 발도 : 대상에게 D12의 피해를 입힙니다. (스태미나 2 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun drawSword(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 발도 사용")
        return normalAttack(
            stat,
            1,
            12,
            2,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 자법 : 대상에게 3D6의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun cut(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 자법 사용")
        return normalAttack(
            stat,
            3,
            6,
            3,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 일섬 : 대상에게 3D6의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun flash(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 일섬 사용")
        return normalAttack(
            stat,
            3,
            6,
            3,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 난격 : 대상에게 5D4의 피해를 입힙니다. (스태미나 4 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun rampage(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 난격 사용")
        return normalAttack(
            stat,
            5,
            4,
            4,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 섬격 : 대상에게 D12의 피해를 입힙니다. 데미지가 2배로 적용됩니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun flashStrike(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 섬격 사용")
        return normalAttack(
            stat,
            1,
            12,
            5,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            true, // 섬격 전용 플래그
            precision,
            out
        )
    }

    /**
     * 무사 종점 : 대상에게 3D20 + (소모한 스태미나)의 피해를 입힙니다. 모든 스태미나를 소모합니다.
     *
     * @param stat 사용할 스탯
     * @param currentStamina 현재 보유 스태미나 (전량 소모 및 데미지 합산)
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun terminus(
        stat: Int,
        currentStamina: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 종점 사용")
        if (Main.verdict(stat, out) <= 0) {
            return Result(0, 0, false, 0, currentStamina)
        }

        var baseDamage = Main.dice(3, 20, out)
        out.println("기본 데미지 : $baseDamage")

        out.println("소모 스태미나 비례 데미지 추가 : +$currentStamina")
        baseDamage += currentStamina

        out.println("흩날리는 검무 적용: 주사위당(3개) 추가 데미지 D4")
        val additionalDamage = Main.dice(3, 4, out)
        out.println("추가 데미지 : $additionalDamage")
        baseDamage += additionalDamage

        var damageMultiplier = 1.0

        if (objectI) {
            out.println("물아 모드 적용: 기술 데미지 150% 증가")
            damageMultiplier *= 1.5
        }
        if (oneStrikeKill) {
            out.println("일격필살 적용: 남은 체력 40% 이하, 데미지 2배 증가")
            damageMultiplier *= 2
        }
        if (resolve) {
            out.println("각오 사용: 공격 데미지 2배 증가")
            damageMultiplier *= 2
        }
        if (lifeOrDeath) {
            out.println("생사결단 사용: 가하는 데미지 3배 증가")
            damageMultiplier *= 3
        }

        val totalDamage = (baseDamage * damageMultiplier).toInt()
        out.println("배율 적용 데미지 : $totalDamage")
        val sideDamage = Main.sideDamage(totalDamage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        out.println("최종 데미지 : ${sideDamage + totalDamage}")

        val preCritDamage = totalDamage + sideDamage
        val critDamage = Main.criticalHit(precision, preCritDamage, out)
        return Result(0, critDamage, true, 0, currentStamina)
    }

    /**
     * 무사 개화 : 대상에게 8D6의 피해를 입힙니다. (스태미나 8 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun blooming(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 개화 사용")
        return normalAttack(
            stat,
            8,
            6,
            8,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 각오 : 스스로에게 D6의 피해를 입힙니다. 다음 적중한 공격 데미지가 2배로 증가합니다. (마나 3 소모)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (받은 피해량 포함)
     */
    fun resolve(out: PrintStream): Result {
        out.println("무사 - 각오 사용")
        val selfDamage = Main.dice(1, 6, out)
        out.println("각오 자해 데미지 : $selfDamage")
        return Result(selfDamage, 0, true, 3, 0)
    }

    /**
     * 무사 빗겨내기 : 이번 턴 받는 데미지가 25%로 감소합니다. 이후 기술 반격이 가능합니다. (마나 6 소모, 쿨타임 10턴)
     *
     * @param stat 사용할 스탯
     * @param damageTaken 받는 데미지
     * @param notMe 무아 모드 적용 여부 (받는 데미지 75% 감소)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 받는 데미지 1.5배)
     * @param terminus 종점 기술 사용 여부 (받는 데미지 1.5배 증가)
     * @param blooming 개화 기술 사용 여부 (받는 데미지 2배 증가)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (받는 데미지 5배 증가)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun deflect(
        stat: Int,
        damageTaken: Int,
        notMe: Boolean,
        oneStrikeKill: Boolean,
        terminus: Boolean,
        blooming: Boolean,
        lifeOrDeath: Boolean,
        out: PrintStream
    ): Result {
        out.println("무사 - 빗겨내기 사용")

        var damageMultiplier = 1.0
        if (notMe) {
            out.println("무아 모드 적용: 받는 데미지 75% 감소")
            damageMultiplier *= 0.75
        }
        if (oneStrikeKill) {
            out.println("일격필살 적용: 남은 체력 40% 이하, 받는 데미지 1.5배 증가")
            damageMultiplier *= 1.5
        }
        if (terminus) {
            out.println("종점 적용: 받는 데미지 1.5배 증가")
            damageMultiplier *= 1.5
        }
        if (blooming) {
            out.println("개화 적용: 받는 데미지 2배 증가")
            damageMultiplier *= 2
        }
        if (lifeOrDeath) {
            out.println("생사결단 사용: 받는 데미지 5배 증가")
            damageMultiplier *= 5
        }
        return if (Main.verdict(stat, out) <= 0) {
            Result((damageMultiplier * damageTaken).toInt(), 0, false, 7, 0)
        } else {
            Result((damageMultiplier * damageTaken * 0.25).toInt(), 0, true, 7, 0)
        }
    }

    /**
     * 무사 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @return 결과 객체
     */
    fun plain(
        stat: Int,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 기본 공격 사용")
        return normalAttack(
            stat,
            1,
            6,
            0,
            false, // 기술에만 적용
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            precision,
            out
        )
    }

    /**
     * 무사 범용 공격 메소드
     *
     * @param stat 사용할 스탯
     * @param dices 주사위 개수
     * @param sides 주사위 면수
     * @param stamina 소모 스태미나
     * @param objectI 물아 모드 적용 여부 (기술 데미지 150%)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param flashStrike 섬격 기술 여부 (데미지 2배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    private fun normalAttack(
        stat: Int,
        dices: Int,
        sides: Int,
        stamina: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        flashStrike: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        if (Main.verdict(stat, out) <= 0) {
            return Result(0, 0, false, 0, stamina)
        }
        var baseDamage = Main.dice(dices, sides, out)
        var damageMultiplier = 1.0

        out.println("기본 데미지 : $baseDamage")

        out.println("흩날리는 검무 적용: 주사위당 추가 데미지 D4")
        val additionalDamage = Main.dice(dices, 4, out)
        out.println("추가 데미지 : $additionalDamage")
        baseDamage += additionalDamage

        // 데미지 배율
        if (objectI) {
            out.println("물아 모드 적용: 기술 데미지 150% 증가")
            damageMultiplier *= 1.5
        }
        if (oneStrikeKill) {
            out.println("일격필살 적용: 남은 체력 40% 이하, 데미지 2배 증가")
            damageMultiplier *= 2
        }
        if (resolve) {
            out.println("각오 사용: 공격 데미지 2배 증가")
            damageMultiplier *= 2
        }
        if (lifeOrDeath) {
            out.println("생사결단 사용: 가하는 데미지 3배 증가")
            damageMultiplier *= 3
        }
        if (flashStrike) {
            out.println("섬격 기술 패시브 적용: 데미지 2배 증가")
            damageMultiplier *= 2
        }
        val totalDamage = (baseDamage * damageMultiplier).toInt()
        out.println("배율 적용 데미지 : $totalDamage")
        val sideDamage = Main.sideDamage(totalDamage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        val preCritDamage = sideDamage + totalDamage
        out.println("최종 데미지 : $preCritDamage")
        val finalDamage = Main.criticalHit(precision, preCritDamage, out)
        return Result(0, finalDamage, true, 0, stamina)

    }
}