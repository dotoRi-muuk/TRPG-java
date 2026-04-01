package main.hidden;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 저격수 (숨겨진 직업)
 * <p>
 * 판정 사용 스탯 : 힘, 민첩
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 */
public class Sniper {

    /**
     * 데미지 계산 공식 적용
     * [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param dices            주사위 수
     * @param sides            주사위 면 수
     * @param stat             사용할 스탯
     * @param vitalAim         급소조준 적용 여부 (5턴 이상 공격받지 않을 시 최종 데미지 x2)
     * @param deathBullet      죽음의 탄환 적용 여부 (기본 공격 미사용 시 발사 데미지 +400%)
     * @param secure           확보 기술 적용 여부 (정조준 버프 카운트)
     * @param assemble         조립 기술 적용 여부 (최종 데미지 x1.5, 정조준 버프 카운트)
     * @param load             장전 기술 적용 여부 (정조준 버프 카운트)
     * @param aim              조준 기술 적용 여부 (수비 무시, 정조준 버프 카운트)
     * @param sureHit          필즉 스킬 적용 여부 (확정 명중)
     * @param stabilize        안정화 스킬 적용 여부 (데미지 -50%, 주사위 복제)
     * @param immersion        몰입 스킬 적용 여부 (데미지 +100%)
     * @param conviction       확신 스킬 적용 여부 (최종 데미지 x2.5)
     * @param heightenedSenses 신경 극대화 스킬 적용 여부 (데미지 +1000%)
     * @param numBuffs         추가 외부 버프 수 (정조준 패시브에 반영)
     * @param precision        정밀 스탯
     * @param staminaUsed      소모 스태미나
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result calculate(int dices, int sides, int stat, boolean vitalAim, boolean deathBullet,
                                   boolean secure, boolean assemble, boolean load, boolean aim,
                                   boolean sureHit, boolean stabilize,
                                   boolean immersion, boolean conviction, boolean heightenedSenses,
                                   int numBuffs, int precision, int staminaUsed, int level, PrintStream out) {
        // 정조준: 버프 스킬 1개당 데미지 +50%
        int buffCount = numBuffs;
        if (secure) buffCount++;
        if (assemble) buffCount++;
        if (load) buffCount++;
        if (aim) buffCount++;
        if (sureHit) buffCount++;
        if (stabilize) buffCount++;
        if (immersion) buffCount++;
        if (conviction) buffCount++;
        if (heightenedSenses) buffCount++;

        // 안정화: 주사위 복제
        int actualDices = dices;
        if (stabilize) {
            out.println("안정화 스킬 적용: 주사위 복제");
            actualDices *= 2;
        }

        int damage = Main.dice(actualDices, sides, out);
        out.printf("기본 데미지 : %d\n", damage);

        // (100 + 데미지)% 계산
        int flatBonus = 0;
        if (stabilize) {
            out.println("안정화 스킬 적용: 데미지 -50%");
            flatBonus -= 50;
        }
        if (immersion) {
            out.println("몰입 스킬 적용: 데미지 +100%");
            flatBonus += 100;
        }
        if (heightenedSenses) {
            out.println("신경 극대화 스킬 적용: 데미지 +1000%");
            flatBonus += 1000;
        }
        if (deathBullet) {
            out.println("죽음의 탄환 패시브 적용: 발사 데미지 +400%");
            flatBonus += 400;
        }
        if (buffCount > 0) {
            out.printf("정조준 패시브 적용: 버프 %d개 x 50%% = +%d%%\n", buffCount, buffCount * 50);
            flatBonus += buffCount * 50;
        }

        // (최종 데미지)% 계산
        double finalDamageMultiplier = 1.0;
        if (vitalAim) {
            out.println("급소조준 패시브 적용: 최종 데미지 x2");
            finalDamageMultiplier *= 2.0;
        }
        if (assemble) {
            out.println("조립 기술 적용: 최종 데미지 x1.5");
            finalDamageMultiplier *= 1.5;
        }
        if (conviction) {
            out.println("확신 스킬 적용: 최종 데미지 x2.5");
            finalDamageMultiplier *= 2.5;
        }

        // [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)%
        damage = Main.calculateDamage(damage, flatBonus, finalDamageMultiplier, out);

        // 주사위 보정
        int sideDmg = Main.sideDamage(damage, stat, out);
        damage += sideDmg;
        out.printf("데미지 보정치 : %d\n", sideDmg);

        damage = Main.criticalHit(precision, damage, out);
        out.printf("최종 데미지 : %d\n", damage);
        damage = (int)(damage * Main.levelMultiplier(level));
        out.printf("레벨 보정 (레벨 %d): %.0f%% 적용 → %d%n", level, (100.0 + (double)level*level), damage);

        if (aim) {
            out.println("조준 기술 적용: 수비를 무시합니다.");
        }

        return new Result(0, damage, true, 0, staminaUsed);
    }

    /**
     * 기본공격: 대상에게 1D6의 피해를 입힙니다.
     *
     * @param stat             사용할 스탯
     * @param vitalAim         급소조준 적용 여부 (5턴 이상 공격받지 않을 시 최종 데미지 x2)
     * @param secure           확보 기술 적용 여부 (정조준 버프 카운트)
     * @param assemble         조립 기술 적용 여부 (최종 데미지 x1.5, 정조준 버프 카운트)
     * @param load             장전 기술 적용 여부 (정조준 버프 카운트)
     * @param aim              조준 기술 적용 여부 (수비 무시, 정조준 버프 카운트)
     * @param sureHit          필즉 스킬 적용 여부 (확정 명중)
     * @param stabilize        안정화 스킬 적용 여부 (데미지 -50%, 주사위 복제)
     * @param immersion        몰입 스킬 적용 여부 (데미지 +100%)
     * @param conviction       확신 스킬 적용 여부 (최종 데미지 x2.5)
     * @param heightenedSenses 신경 극대화 스킬 적용 여부 (데미지 +1000%)
     * @param numBuffs         추가 외부 버프 수 (정조준 패시브에 반영)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean vitalAim, boolean secure, boolean assemble, boolean load, boolean aim,
                               boolean sureHit, boolean stabilize, boolean immersion,
                               boolean conviction, boolean heightenedSenses, int numBuffs, int precision, int level, PrintStream out) {
        out.println("저격수-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            if (sureHit) {
                out.println("필즉 스킬 적용: 확정 명중");
            } else {
                return new Result(0, 0, false, 0, 0);
            }
        }

        return calculate(1, 6, stat, vitalAim, false, secure, assemble, load, aim,
                sureHit, stabilize, immersion, conviction, heightenedSenses, numBuffs, precision, 0, level, out);
    }

    /**
     * 발사: 탄환을 소모하여 대상에게 5D20의 피해를 입힙니다. (스태미나 10 소모)
     *
     * @param stat             사용할 스탯
     * @param vitalAim         급소조준 적용 여부 (5턴 이상 공격받지 않을 시 최종 데미지 x2)
     * @param deathBullet      죽음의 탄환 적용 여부 (기본 공격 미사용 시 발사 데미지 +400%)
     * @param secure           확보 기술 적용 여부 (정조준 버프 카운트)
     * @param assemble         조립 기술 적용 여부 (최종 데미지 x1.5, 정조준 버프 카운트)
     * @param load             장전 기술 적용 여부 (정조준 버프 카운트)
     * @param aim              조준 기술 적용 여부 (수비 무시, 정조준 버프 카운트)
     * @param sureHit          필즉 스킬 적용 여부 (확정 명중)
     * @param stabilize        안정화 스킬 적용 여부 (데미지 -50%, 주사위 복제)
     * @param immersion        몰입 스킬 적용 여부 (데미지 +100%)
     * @param conviction       확신 스킬 적용 여부 (최종 데미지 x2.5)
     * @param heightenedSenses 신경 극대화 스킬 적용 여부 (데미지 +1000%)
     * @param numBuffs         추가 외부 버프 수 (정조준 패시브에 반영)
     * @param precision        정밀 스탯
     * @param out              출력 스트림
     * @return 결과 객체
     */
    public static Result fire(int stat, boolean vitalAim, boolean deathBullet, boolean secure, boolean assemble,
                              boolean load, boolean aim, boolean sureHit, boolean stabilize, boolean immersion,
                              boolean conviction, boolean heightenedSenses, int numBuffs, int precision, int level, PrintStream out) {
        out.println("저격수-발사 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            if (sureHit) {
                out.println("필즉 스킬 적용: 확정 명중");
            } else {
                return new Result(0, 0, false, 0, 10);
            }
        }

        out.println("발사 스킬 적용: 5D20 데미지");
        return calculate(5, 20, stat, vitalAim, deathBullet, secure, assemble, load, aim,
                sureHit, stabilize, immersion, conviction, heightenedSenses, numBuffs, precision, 10, level, out);
    }
}
