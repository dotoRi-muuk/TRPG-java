package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 저격수
 * <p>
 * 판정 사용 스탯 : 힘, 민첩
 */
public class Sniper {

    /**
     * 발사: 탄환을 소모하여 대상에게 4D20의 피해를 입힙니다.
     * @param stat 사용할 스탯
     * @param vitalAim 급소조준 적용 여부 (5턴 이상 공격X 시 데미지 2배)
     * @param deathBullet 죽음의 탄환 적용 여부 (기본 공격 사용X 시 데미지 1.5배)
     * @param assemble 조립 기술 적용 여부 (데미지 1.5배)
     * @param aim 조준 기술 적용 여부 (수비 무시)
     * @param sureHit 필즉 스킬 적용 여부 (확정 명중)
     * @param stabilize 안정화 스킬 적용 여부 (데미지 75%로 감소, 주사위 복제)
     * @param focus 몰입 스킬 적용 여부 (데미지 2배)
     * @param conviction 확신 스킬 적용 여부 (데미지 2.5배)
     * @param heightenedSenses 신경 극대화 스킬 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result fire(int stat, boolean vitalAim, boolean deathBullet, boolean assemble, boolean aim, boolean sureHit, boolean stabilize, boolean focus, boolean conviction, boolean heightenedSenses, PrintStream out) {
        out.println("저격수-발사 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            if (sureHit) {
                out.println("필즉 스킬 적용: 확정 명중");
            } else {
                return new Result(0, 0, false, 0, 10);
            }
        }

        out.print("발사 스킬 적용: 4D20 데미지\n");
        double damageMultiplier = 1.0;

        int dices = 4;
        if (stabilize) {
            out.print("안정화 스킬 적용: 데미지 75%로 감소, 주사위 복제%n");
            dices *= 2;
            damageMultiplier *= 0.75;

        }
        int damage = Main.dice(dices, 20, out);
        out.printf("기본 데미지 : %d\n", damage);

        int enabled = 0;
        if (sureHit) enabled++;
        if (stabilize) enabled++;
        if (vitalAim) {
            out.print("급소조준 패시브 적용: 데미지 배율 2배\n");
            damageMultiplier *= 2.0;
        }
        if (deathBullet) {
            out.print("죽음의 탄환 패시브 적용: 데미지 배율 1.5배\n");
            damageMultiplier *= 1.5;
        }
        if (assemble) {
            out.print("조립 기술 적용: 데미지 배율 1.5배\n");
            damageMultiplier *= 1.5;
        }
        if (focus) {
            out.print("몰입 스킬 적용: 데미지 배율 2배\n");
            damageMultiplier *= 2.0;
            enabled++;
        }
        if (conviction) {
            out.print("확신 스킬 적용: 데미지 배율 2.5배\n");
            damageMultiplier *= 2.5;
            enabled++;
        }
        if (heightenedSenses) {
            out.print("신경 극대화 스킬 적용: 데미지 배율 4배\n");
            damageMultiplier *= 4.0;
            enabled++;
        }
        if (enabled > 0) {
            out.printf("정조준 패시브 적용: 버프 스킬 %d개당 데미지 25%% 증가\n", enabled);
            damageMultiplier *= (1.0 + 0.25 * enabled);
        }
        damage = (int) (damage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", damage);
        if (aim) {
            out.println("조준 기술 적용: 수비를 무시합니다.");
        }
        return new Result(0, damage, true, 0, 10);
    }

    /**
     * 기본공격: 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 사용할 스탯
     * @param vitalAim 급소조준 적용 여부 (5턴 이상 공격X 시 데미지 2배)
     * @param assemble 조립 기술 적용 여부 (데미지 1.5배)
     * @param aim 조준 기술 적용 여부 (수비 무시)
     * @param sureHit 필즉 스킬 적용 여부 (확정 명중)
     * @param stabilize 안정화 스킬 적용 여부 (데미지 75%로 감소, 주사위 복제)
     * @param focus 몰입 스킬 적용 여부 (데미지 2배)
     * @param conviction 확신 스킬 적용 여부 (데미지 2.5배)
     * @param heightenedSenses 신경 극대화 스킬 적용 여부 (데미지 4배)
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, boolean vitalAim, boolean assemble, boolean aim, boolean sureHit, boolean stabilize, boolean focus, boolean conviction, boolean heightenedSenses, PrintStream out) {

        out.println("저격수-기본공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) {
            if (sureHit) {
                out.println("필즉 스킬 적용: 확정 명중");
            } else {
                return new Result(0, 0, false, 0, 0);
            }
        }

        double damageMultiplier = 1.0;

        int dices = 1;
        if (stabilize) {
            out.print("안정화 스킬 적용: 데미지 75%로 감소, 주사위 복제%n");
            dices *= 2;
            damageMultiplier *= 0.75;
        }
        int damage = Main.dice(dices, 6, out);
        out.printf("기본 데미지 : %d\n", damage);

        int enabled = 0;
        if (sureHit) enabled++;
        if (stabilize) enabled++;
        if (vitalAim) {
            out.print("급소조준 패시브 적용: 데미지 배율 2배\n");
            damageMultiplier *= 2.0;
        }
        if (assemble) {
            out.print("조립 기술 적용: 데미지 배율 1.5배\n");
            damageMultiplier *= 1.5;
        }
        if (focus) {
            out.print("몰입 스킬 적용: 데미지 배율 2배\n");
            damageMultiplier *= 2.0;
            enabled++;
        }
        if (conviction) {
            out.print("확신 스킬 적용: 데미지 배율 2.5배\n");
            damageMultiplier *= 2.5;
            enabled++;
        }
        if (heightenedSenses) {
            out.print("신경 극대화 스킬 적용: 데미지 배율 4배\n");
            damageMultiplier *= 4.0;
            enabled++;
        }
        if (enabled > 0) {
            out.printf("정조준 패시브 적용: 버프 스킬 %d개당 데미지 25%% 증가\n", enabled);
            damageMultiplier *= (1.0 + 0.25 * enabled);
        }
        damage = (int) (damage * damageMultiplier);
        out.printf("배율 적용 데미지 : %d\n", damage);
        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d\n", sideDamage);
        out.printf("최종 데미지 : %d\n", damage);
        if (aim) {
            out.println("조준 기술 적용: 수비를 무시합니다.");
        }
        return new Result(0, damage, true, 0, 0);

    }
}
