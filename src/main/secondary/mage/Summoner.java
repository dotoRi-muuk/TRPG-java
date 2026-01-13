package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 소환술사
 * <p>
 * 판정 사용 스탯 : 지능
 *
 * -    기본 공격(기본 공격) : 대상에게 1D6의 데미지를 입힙니다.
 *
 * 사역 (패시브) : 소환수를 처치할 시 길들일 수 있습니다. 소환한 소환수는 매 턴 랜덤한 스킬을 사용합니다.
 * *    유대감 (패시브) : 길들인 소환수의 수와 등급에 따라 데미지가 증가합니다. (보유한 소환수 1마리의 등급당 데미지 증가량 : 초급 5% / 중등 10% / 고위 15% / 특급 20% / 초월 25% / 전능 50%)
 * 소환패 (패시브) : 길들인 소환수를 소환할 수 있는 [소환패]를 5개 가지고 전투를 시작합니다.
 *
 * 소환 (스킬) : 소환수를 소환합니다. 길들인 소환수를 소환할 시, [소환패] 1개를 소모합니다. 전장의 소환수는 매 턴 소환술사의 마나를 소모합니다. (하급 0, 중급 1, 돌파 2, 특급 3, 초월 4, 전능 6) (마나 3 소모, 쿨타임 8턴)
 * -    소환수를 이기는 주먹 (기술) : 대상에게 D10의 피해를 입힙니다. (스태미나 3 소모)
 * -    말을 잘 듣게 하는 주먹 (기술) : 대상에게 D10의 피해를 입힙니다. (스태미나 2 소모)
 *
 * -        소환해제 (스킬) : 소환한 소환수를 소환 해제시킵니다. 이후 지혜 판정 성공 시 [소환패]를 1개 얻습니다.
 * 잠깐 (스킬) : 소환수가 사용할 스킬을 다시 랜덤하게 선택합니다. (마나 3 소모, 쿨타임 3턴)
 * 지휘자 (스킬) : 소환수가 사용할 스킬을 선택할 수 있습니다. (마나 5 소모, 쿨타임 5턴)
 * *    소환 증폭 (스킬) : 전투 내 영구적으로 해당 소환수가 가하는 피해가 (200/(현재 소환된 소환수의 수))% 증가합니다. (중첩 불가) (마나 5 소모, 쿨타임 5턴)
 * *    결속의 끈 (스킬) : 소환 후 5턴이 지난 소환수에게 사용 가능합니다. 전투 내에서 영구적으로 소환수가 가하는 피해가 2배로 증가합니다. (마나 7 소모, 쿨타임 7턴)
 *
 * -    원호 방어 (전용 신속) : 지능 또는 지혜 판정을 진행합니다. 성공 시, 자신이나 아군이 받을 공격을 소환수가 대신 맞게 할 수 있습니다.
 */
public class Summoner {



    /**
     * 말을 잘 듣게 하는 주먹 : 대상에게 D10의 피해를 입힙니다. (스태미나 2 소모)
     * @param stat 사용할 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result fistOfObedience(int stat, PrintStream out) {
        out.println("소환술사-말을 잘 듣게 하는 주먹 사용");
        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 2);

        int damage = Main.dice(1, 10, out);
        out.printf("기본 데미지 : %d%n", damage);

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(damage, 0, true, 0, 2);
    }

    /**
     * 소환수를 이기는 주먹 : 대상에게 D10의 피해를 입힙니다. (스태미나 3 소모)
     * @param stat 사용할 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result fistBeatingSummon(int stat, PrintStream out) {
        out.println("소환술사-소환수를 이기는 주먹 사용");
        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 3);

        int damage = Main.dice(1, 10, out);
        out.printf("기본 데미지 : %d%n", damage);

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(damage, 0, true, 0, 3);
    }

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     * @param stat 사용할 스탯
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, PrintStream out) {
        out.println("소환술사-기본공격 사용");

        int verdict = Main.verdict(stat, out);

        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int damage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", damage);

        int sideDamage = Main.sideDamage(damage, stat, out);
        damage += sideDamage;
        out.printf("데미지 보정치 : %d%n", sideDamage);
        out.printf("최종 데미지 : %d%n", damage);

        return new Result(damage, 0, true, 0, 0);
    }

}
