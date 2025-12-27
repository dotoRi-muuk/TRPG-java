package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 수비 탭
 * 각각의 사용 스탯 - D20이 전부 1 이상일 때 성공
 * 실패 시 데미지 반환
 */
public class Defense {

    /**
     * 스탯 판정
     * @param stat 스탯 값
     * @param out 출력 스트림
     * @return 스탯 - D20 값 (성공: >= 1)
     */
    private static int rollCheck(int stat, String statName, PrintStream out) {
        int roll = Main.dice(1, 20, out);
        int result = stat - roll;
        out.printf("%s 판정: %d - %d = %d%n", statName, stat, roll, result);
        return result;
    }

    /**
     * 방어 (사용 스탯: 힘)
     * 성공: {30 + 5*(스탯-주사위)}% 데미지 경감, 다음 공격 데미지 150%
     * 실패: {20 + 5*(스탯-주사위)} 만큼 데미지 경감
     * 스태미나 3 소모
     * @param damageTaken 받는 데미지
     * @param strength 힘 스탯
     * @param out 출력 스트림
     * @return 최종 받는 데미지
     */
    public static int defend(int damageTaken, int strength, PrintStream out) {
        out.println("수비-방어 사용 (힘)");
        out.println("※ 스태미나 3 소모");

        int check = rollCheck(strength, "힘", out);

        int finalDamage;
        if (check >= 1) {
            // 성공: {30 + 5*(스탯-주사위)}% 데미지 경감
            int reductionPercent = 30 + 5 * check;
            finalDamage = (int) (damageTaken * (100 - reductionPercent) / 100.0);
            out.printf("방어 성공! %d%% 데미지 경감: %d → %d%n", reductionPercent, damageTaken, finalDamage);
            out.println("※ 다음 공격 데미지 150%%");
        } else {
            // 실패: {20 + 5*(스탯-주사위)} 만큼 데미지 경감 (음수이므로 실제로는 감소량이 적어짐)
            int reductionFlat = 20 + 5 * check;
            if (reductionFlat < 0) reductionFlat = 0;
            finalDamage = Math.max(0, damageTaken - reductionFlat);
            out.printf("방어 실패. %d 데미지 경감: %d → %d%n", reductionFlat, damageTaken, finalDamage);
        }

        out.printf("최종 받는 데미지: %d%n", finalDamage);
        return finalDamage;
    }

    /**
     * 회피 (사용 스탯: 민첩)
     * 성공: 기본 공격으로 반격 가능
     * 실패: 데미지 그대로 받음
     * 스태미나 4 소모
     * @param damageTaken 받는 데미지
     * @param dexterity 민첩 스탯
     * @param out 출력 스트림
     * @return 최종 받는 데미지 (성공: 0, 실패: damageTaken)
     */
    public static int evade(int damageTaken, int dexterity, PrintStream out) {
        out.println("수비-회피 사용 (민첩)");
        out.println("※ 스태미나 4 소모");

        int check = rollCheck(dexterity, "민첩", out);

        if (check >= 1) {
            out.println("회피 성공! 데미지 무효");
            out.println("※ 기본 공격으로 반격 가능");
            return 0;
        } else {
            out.printf("회피 실패. 데미지 그대로 받음: %d%n", damageTaken);
            return damageTaken;
        }
    }

    /**
     * 흘리기 (사용 스탯: 힘, 민첩)
     * 성공: 약한 반격 사용 가능 (데미지 50%), 다음 공격 데미지 200%, 스태미나 2 소모
     * 실패: 스태미나 3 소모
     * @param damageTaken 받는 데미지
     * @param strength 힘 스탯
     * @param dexterity 민첩 스탯
     * @param out 출력 스트림
     * @return 최종 받는 데미지 (성공: 0, 실패: damageTaken)
     */
    public static int deflect(int damageTaken, int strength, int dexterity, PrintStream out) {
        out.println("수비-흘리기 사용 (힘, 민첩)");

        int strengthCheck = rollCheck(strength, "힘", out);
        int dexterityCheck = rollCheck(dexterity, "민첩", out);

        if (strengthCheck >= 1 && dexterityCheck >= 1) {
            out.println("흘리기 성공! 데미지 무효");
            out.println("※ 약한 반격 사용 가능 (데미지 50%%)");
            out.println("※ 다음 공격 데미지 200%%");
            out.println("※ 스태미나 2 소모");
            return 0;
        } else {
            out.printf("흘리기 실패. 데미지 그대로 받음: %d%n", damageTaken);
            out.println("※ 스태미나 3 소모");
            return damageTaken;
        }
    }

    /**
     * 패링 (사용 스탯: 힘, 민첩, 신속)
     * 성공: 기본 공격 반격 가능 (데미지 150%), 상대 다음 턴까지 수비 불가, 다음 공격 데미지 200%
     * 실패: 다음 자신의 턴까지 받는 데미지 150%, 스태미나 5 소모
     * @param damageTaken 받는 데미지
     * @param strength 힘 스탯
     * @param dexterity 민첩 스탯
     * @param swiftness 신속 스탯
     * @param out 출력 스트림
     * @return 최종 받는 데미지 (성공: 0, 실패: damageTaken * 1.5)
     */
    public static int parry(int damageTaken, int strength, int dexterity, int swiftness, PrintStream out) {
        out.println("수비-패링 사용 (힘, 민첩, 신속)");

        int strengthCheck = rollCheck(strength, "힘", out);
        int dexterityCheck = rollCheck(dexterity, "민첩", out);
        int swiftnessCheck = rollCheck(swiftness, "신속", out);

        if (strengthCheck >= 1 && dexterityCheck >= 1 && swiftnessCheck >= 1) {
            out.println("패링 성공! 데미지 무효");
            out.println("※ 기본 공격 반격 가능 (데미지 150%%)");
            out.println("※ 상대 다음 턴까지 수비 불가");
            out.println("※ 다음 공격 데미지 200%%");
            return 0;
        } else {
            int finalDamage = (int) (damageTaken * 1.5);
            out.printf("패링 실패. 받는 데미지 150%%: %d → %d%n", damageTaken, finalDamage);
            out.println("※ 스태미나 5 소모");
            return finalDamage;
        }
    }

    /**
     * 카운터 패링 (사용 스탯: 힘, 민첩, 신속, 치명타)
     * 성공: 기본 공격 반격 가능 (데미지 250%), 상대 다음 2턴 공격/수비 불가, 다음 공격 데미지 300%
     * 실패: 다음 자신의 턴까지 행동 불가, 받는 데미지 200%, 스태미나 8 소모
     * @param damageTaken 받는 데미지
     * @param strength 힘 스탯
     * @param dexterity 민첩 스탯
     * @param swiftness 신속 스탯
     * @param critical 치명타 스탯
     * @param out 출력 스트림
     * @return 최종 받는 데미지 (성공: 0, 실패: damageTaken * 2)
     */
    public static int counterParry(int damageTaken, int strength, int dexterity, int swiftness, int critical, PrintStream out) {
        out.println("수비-카운터 패링 사용 (힘, 민첩, 신속, 치명타)");

        int strengthCheck = rollCheck(strength, "힘", out);
        int dexterityCheck = rollCheck(dexterity, "민첩", out);
        int swiftnessCheck = rollCheck(swiftness, "신속", out);
        int criticalCheck = rollCheck(critical, "치명타", out);

        if (strengthCheck >= 1 && dexterityCheck >= 1 && swiftnessCheck >= 1 && criticalCheck >= 1) {
            out.println("카운터 패링 성공! 데미지 무효");
            out.println("※ 기본 공격 반격 가능 (데미지 250%%)");
            out.println("※ 상대 다음 2턴 공격/수비 불가");
            out.println("※ 다음 공격 데미지 300%%");
            return 0;
        } else {
            int finalDamage = damageTaken * 2;
            out.printf("카운터 패링 실패. 받는 데미지 200%%: %d → %d%n", damageTaken, finalDamage);
            out.println("※ 다음 자신의 턴까지 행동 불가");
            out.println("※ 스태미나 8 소모");
            return finalDamage;
        }
    }

}
