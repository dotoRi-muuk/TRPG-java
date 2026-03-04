package main.secondary.warrior

import main.Main
import main.Result
import java.io.PrintStream

/**
 * 무사
 *
 * 판정 사용 스탯 : 힘(+체력)
 *
 * 패시브
 * 물아일체/무아지경 (물아/무아) : [물아] 기술 데미지 100% 증가 (x2.0). [무아] 받는 데미지 25% 감소 (x0.75).
 * 흩날리는 검무 : 공격 주사위가 αDβ일 때 최종 데미지가 (100+αβ)%로 증가.
 * 일격필살 : HP 40% 이하 시 가하는 데미지 2배, 받는 데미지 1.5배.
 * 엇갈린 검 : 데미지 판정 주사위 기본값이 15 이상이면 다음 턴 상대 공격 불가 부여.
 */
class BladeMaster {

    /**
     * 무사 일섬 : 대상에게 D20의 피해를 입힙니다. (스태미나 3 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 100% 증가, x2.0)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun flash(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 일섬 사용")
        return normalAttack(
            stat,
            1,
            20,
            3,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            despair,
            limitBreak,
            moonHide,
            precision,
            out
        )
    }

    /**
     * 무사 난격 : 대상에게 5D4의 피해를 입힙니다. (스태미나 4 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 100% 증가, x2.0)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun rampage(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
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
            despair,
            limitBreak,
            moonHide,
            precision,
            out
        )
    }

    /**
     * 무사 섬격 : 대상에게 D12의 피해를 입힙니다. 데미지가 2배로 적용됩니다. (스태미나 5 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 100% 증가, x2.0)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun flashStrike(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
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
            true, // 섬격 전용 플래그 (데미지 x2.0)
            despair,
            limitBreak,
            moonHide,
            precision,
            out
        )
    }

    /**
     * 무사 종점 : 대상에게 3D20 + (최근 5턴 소모 스태미나 x 2)의 피해를 입힙니다.
     * 다음 턴 행동 불가, 다음다음 턴 공격 불가. 다음 2턴 동안 받는 데미지 50% 증가. (스태미나 10 소모)
     *
     * @param stat 사용할 스탯
     * @param recentStaminaConsumed 최근 5턴 동안 소모한 스태미나 (데미지 합산용)
     * @param objectI 물아 모드 적용 여부 (기술 데미지 100% 증가, x2.0)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun terminus(
        stat: Int,
        recentStaminaConsumed: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 종점 사용")
        if (Main.verdict(stat, out) <= 0) {
            return Result(0, 0, false, 0, 10)
        }

        var baseDamage = Main.dice(3, 20, out)
        out.println("기본 데미지 : $baseDamage")

        if (baseDamage >= 15) {
            out.println("엇갈린 검 발동: 다음 턴 상대 공격 불가")
        }

        out.println("최근 5턴 소모 스태미나 비례 데미지 추가 : +${recentStaminaConsumed * 2}")
        baseDamage += recentStaminaConsumed * 2

        // 흩날리는 검무: 3D20 기준 (100 + 3*20) / 100.0 = 1.6배
        val scatterMultiplier = (100 + 3 * 20) / 100.0
        out.println("흩날리는 검무 적용: 최종 데미지 ${100 + 3 * 20}%로 증가 (배율 $scatterMultiplier)")
        var damageMultiplier = scatterMultiplier

        if (objectI) {
            out.println("물아 모드 적용: 기술 데미지 100% 증가")
            damageMultiplier *= 2.0
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
        if (limitBreak) {
            out.println("극한돌파 적용: 최종 데미지%의 절반만큼 데미지% 증가")
            damageMultiplier *= 1.5
        }
        if (moonHide) {
            out.println("월은 적용: 데미지 100% 증가")
            damageMultiplier *= 2.0
        }

        val totalDamage = (baseDamage * damageMultiplier).toInt()
        out.println("배율 적용 데미지 : $totalDamage")
        val sideDamage = Main.sideDamage(totalDamage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        val preCritDamage = totalDamage + sideDamage
        out.println("최종 데미지 : $preCritDamage")

        val critDamage = if (despair) {
            out.println("절명 적용 - 치명타 배율 2.5배로 증가")
            val critRoll = Main.dice(1, 20, out)
            if (precision >= critRoll) {
                val cd = (preCritDamage * 2.5).toInt()
                out.printf("치명타 발동! (정밀 %d >= 주사위 %d) %d * 2.5 = %d%n", precision, critRoll, preCritDamage, cd)
                cd
            } else {
                out.printf("치명타 미발동 (정밀 %d < 주사위 %d)%n", precision, critRoll)
                preCritDamage
            }
        } else {
            Main.criticalHit(precision, preCritDamage, out)
        }

        out.println("종점 효과: 다음 턴 행동 불가, 다음다음 턴 공격 불가. 다음 2턴 동안 받는 데미지 50% 증가.")
        return Result(0, critDamage, true, 0, 10)
    }

    /**
     * 무사 개화 : 대상에게 8D6의 피해를 입힙니다.
     * 다음 2턴 동안 공격 불가, 4턴 동안 패시브 비활성화. 다음 턴 받는 데미지 2배 증가. (스태미나 8 소모)
     *
     * @param stat 사용할 스탯
     * @param objectI 물아 모드 적용 여부 (기술 데미지 100% 증가, x2.0)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun blooming(
        stat: Int,
        objectI: Boolean,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 개화 사용")
        val result = normalAttack(
            stat,
            8,
            6,
            8,
            objectI,
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            despair,
            limitBreak,
            moonHide,
            precision,
            out
        )
        if (result.succeeded()) {
            out.println("개화 효과: 다음 2턴 동안 공격 불가, 4턴 동안 패시브 비활성화. 다음 턴 받는 데미지 2배 증가.")
        }
        return result
    }

    /**
     * 무사 각오 : 스스로에게 D6의 피해를 입힙니다. 다음 적중한 공격 데미지가 2배로 증가합니다. (마나 3 소모, 쿨타임 5턴)
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
     * 무사 생사결단 : 최종 데미지 3배, 받는 데미지 5배 증가. (마나 8 소모, 쿨타임 7턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun lifeOrDeath(out: PrintStream): Result {
        out.println("무사 - 생사결단 사용")
        out.println("가하는 데미지 3배 증가, 받는 데미지 5배 증가")
        return Result(0, 0, true, 8, 0)
    }

    /**
     * 무사 절명 : 이번 턴 공격 성공 시 치명타 데미지 배율이 1.5배에서 2.5배로 증가합니다. (마나 3 소모, 쿨타임 4턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun despair(out: PrintStream): Result {
        out.println("무사 - 절명 사용")
        out.println("이번 턴 공격 성공 시 치명타 데미지 배율 1.5배 → 2.5배로 증가")
        return Result(0, 0, true, 3, 0)
    }

    /**
     * 무사 극한돌파 : 다음 공격의 최종 데미지%의 절반만큼 데미지% 증가합니다. (마나 5 소모, 쿨타임 6턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun limitBreak(out: PrintStream): Result {
        out.println("무사 - 극한돌파 사용")
        out.println("다음 공격의 최종 데미지%의 절반만큼 데미지% 증가")
        return Result(0, 0, true, 5, 0)
    }

    /**
     * 무사 월은 : 5턴 동안 데미지 100% 증가. 효과 종료 시 마나 소모 (추가 소모 가능), 소모한 마나의 절반만큼 스태미나 획득. (최소 마나 5 소모, 쿨타임 8턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun moonHide(out: PrintStream): Result {
        out.println("무사 - 월은 사용")
        out.println("5턴 동안 데미지 100% 증가. 효과 종료 시 마나 소모, 추가 소모 가능. 소모한 마나의 절반만큼 스태미나 획득.")
        return Result(0, 0, true, 5, 0)
    }

    /**
     * 무사 빗겨내기 : 이번 턴 받는 데미지가 50%로 감소합니다. (마나 6 소모, 쿨타임 10턴)
     *
     * @param stat 사용할 스탯
     * @param damageTaken 받는 데미지
     * @param notMe 무아 모드 적용 여부 (받는 데미지 25% 감소, x0.75)
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
            out.println("무아 모드 적용: 받는 데미지 25% 감소")
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
            Result((damageMultiplier * damageTaken).toInt(), 0, false, 6, 0)
        } else {
            Result((damageMultiplier * damageTaken * 0.5).toInt(), 0, true, 6, 0)
        }
    }

    /**
     * 무사 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat 사용할 스탯
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    fun plain(
        stat: Int,
        oneStrikeKill: Boolean,
        resolve: Boolean,
        lifeOrDeath: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        out.println("무사 - 기본 공격 사용")
        return normalAttack(
            stat,
            1,
            6,
            0,
            false, // 물아일체는 기술에만 적용
            oneStrikeKill,
            resolve,
            lifeOrDeath,
            false,
            despair,
            limitBreak,
            moonHide,
            precision,
            out
        )
    }

    /**
     * 무사 범용 공격 메소드
     *
     * @param stat 사용할 스탯
     * @param dices 주사위 개수 (α)
     * @param sides 주사위 면수 (β)
     * @param stamina 소모 스태미나
     * @param objectI 물아 모드 적용 여부 (기술 데미지 100% 증가, x2.0)
     * @param oneStrikeKill 일격필살 적용 여부 (남은 체력 40% 이하 데미지 2배)
     * @param resolve 각오 스킬 사용 여부 (데미지 2배)
     * @param lifeOrDeath 생사결단 스킬 사용 여부 (데미지 3배)
     * @param flashStrikeFlag 섬격 기술 여부 (데미지 x2.0)
     * @param despair 절명 스킬 사용 여부 (치명타 배율 2.5배)
     * @param limitBreak 극한돌파 스킬 사용 여부 (데미지% 절반만큼 추가)
     * @param moonHide 월은 스킬 사용 여부 (데미지 100% 증가)
     * @param precision 정밀 스탯 (치명타 판정)
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
        flashStrikeFlag: Boolean,
        despair: Boolean,
        limitBreak: Boolean,
        moonHide: Boolean,
        precision: Int,
        out: PrintStream
    ): Result {
        if (Main.verdict(stat, out) <= 0) {
            return Result(0, 0, false, 0, stamina)
        }
        val baseDamage = Main.dice(dices, sides, out)
        out.println("기본 데미지 : $baseDamage")

        // 엇갈린 검 패시브: 기본값 15 이상 시 다음 턴 상대 공격 불가
        if (baseDamage >= 15) {
            out.println("엇갈린 검 발동: 다음 턴 상대 공격 불가")
        }

        // 흩날리는 검무: αDβ → 최종 데미지 (100+α*β)%로 증가
        val scatterMultiplier = (100 + dices * sides) / 100.0
        out.println("흩날리는 검무 적용: 최종 데미지 ${100 + dices * sides}%로 증가 (배율 $scatterMultiplier)")
        var damageMultiplier = scatterMultiplier

        // 데미지 배율
        if (objectI) {
            out.println("물아 모드 적용: 기술 데미지 100% 증가")
            damageMultiplier *= 2.0
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
        if (flashStrikeFlag) {
            out.println("섬격 기술 패시브 적용: 데미지 2배 증가")
            damageMultiplier *= 2
        }
        if (limitBreak) {
            out.println("극한돌파 적용: 최종 데미지%의 절반만큼 데미지% 증가")
            damageMultiplier *= 1.5
        }
        if (moonHide) {
            out.println("월은 적용: 데미지 100% 증가")
            damageMultiplier *= 2.0
        }

        val totalDamage = (baseDamage * damageMultiplier).toInt()
        out.println("배율 적용 데미지 : $totalDamage")
        val sideDamage = Main.sideDamage(totalDamage, stat, out)
        out.println("데미지 보정치 : $sideDamage")
        val preCritDamage = sideDamage + totalDamage
        out.println("최종 데미지 : $preCritDamage")

        val finalDamage = if (despair) {
            out.println("절명 적용 - 치명타 배율 2.5배로 증가")
            val critRoll = Main.dice(1, 20, out)
            if (precision >= critRoll) {
                val cd = (preCritDamage * 2.5).toInt()
                out.printf("치명타 발동! (정밀 %d >= 주사위 %d) %d * 2.5 = %d%n", precision, critRoll, preCritDamage, cd)
                cd
            } else {
                out.printf("치명타 미발동 (정밀 %d < 주사위 %d)%n", precision, critRoll)
                preCritDamage
            }
        } else {
            Main.criticalHit(precision, preCritDamage, out)
        }

        return Result(0, finalDamage, true, 0, stamina)
    }
}