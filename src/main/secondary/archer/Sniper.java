package main.secondary.archer;

import main.Main;
import main.Result;

import java.io.PrintStream;

public class Sniper {

    /*
    저격수

    판정 사용 스탯 : 힘, 민첩

-   기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.

    암시야 (패시브) : 전투 시작 시 공격하는 순간까지 은신 상태가 됩니다. 이때 휴식을 할 수 없습니다. 공격 전까지 같은 기술, 스킬은 1회만 사용할 수 있습니다.
*   정조준 (패시브) : 자신에게 적용된 버프 스킬 1개당 데미지가 25% 증가합니다.
*   급소조준 (패시브) : 5턴 이상 공격하지 않았다면 데미지가 2배로 증가합니다.
*   죽음의 탄환 (패시브) : 전투 중 기본 공격을 사용한 적이 없다면 '발사'의 데미지가 1.5배로 증가합니다.

    확보 (기술) : 공격 전까지 광역 공격을 받지 않습니다. (스태미나 2 소모)
*   조립 (기술) : 다음 공격 데미지가 1.5배로 증가합니다.
    장전 (기술) : 탄환을 얻습니다. (스태미나 3 소모)
*   조준 (기술) : 다음 공격이 적의 수비를 무시합니다.
-   발사 (기술) : 탄환을 소모하여 대상에게 4D20의 피해를 입힙니다. (스태미나 10 소모)

*   필즉 (스킬) : 다음 공격이 확정적으로 성공합니다. 다음 턴 공격이 불가능합니다. (마나 3 소모, 쿨타임 10턴)
*   안정화 (스킬) : 다음 공격 데미지가 75%로 감소하지만, 주사위를 복제합니다. (마나 4 소모, 쿨타임 10턴)
*   몰입 (스킬) : 다음 공격 데미지가 2배로 증가합니다. 공격 전까지 수비가 불가능합니다. (마나 4 소모, 쿨타임 10턴)
*   확신 (스킬) : 2턴 동안 공격이 불가능합니다. 다음 공격 데미지가 2.5배로 증가합니다. (마나 3 소모, 쿨타임 10턴)
*   신경 극대화 (스킬) : 공격 전까지 받는 데미지가 2배로 증가합니다. 피격 시 모든 버프 효과와 고유 키워드가 제거됩니다. 다음 공격 데미지가 4배로 증가합니다. (마나 8 소모, 쿨타임 10턴)
    도주 (스킬) : 다음 턴 행동이 불가능합니다. 2턴 후까지 은신 상태가 됩니다. (마나 3 소모, 쿨타임 10턴)
     */

    /**
     * 저격수 발사: 탄환을 소모하여 대상에게 4D20의 피해를 입힙니다.
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
     * 저격수 기본공격: 대상에게 1D6의 데미지를 입힙니다.
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
