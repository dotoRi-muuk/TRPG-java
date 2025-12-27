package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Crossbowman {

    /**
     * 석궁사수 기본공격 - 장전/발사 패시브 적용
     * 모인 화살의 데미지: (화살 개수)D6
     * @param stat 사용할 스탯
     * @param arrows 장전된 화살 개수
     * @param focusedAttack 집중 공격 패시브 적용 여부 (화살 1개당 30% 증가)
     * @param out 출력 스트림
     */
    public static int plain(int stat, int arrows, boolean focusedAttack, PrintStream out) {
        out.println("석궁사수-기본공격 사용 (장전/발사)");
        out.printf("장전된 화살: %d개%n", arrows);

        if (arrows <= 0) {
            out.println("장전된 화살이 없습니다!");
            return 0;
        }

        int defaultDamage = Main.dice(arrows, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 집중 공격 패시브
        if (focusedAttack) {
            double multiplier = 1.0 + (arrows * 0.3);
            totalDamage = (int) (totalDamage * multiplier);
            out.printf("집중 공격 패시브 적용: 화살 %d개 → x%.1f = %d%n", arrows, multiplier, totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.printf("※ 화살 %d개 소모%n", arrows);

        if (arrows >= 4) {
            out.println("※ 무차별 난사 발동: 기본 공격이 광역으로 변경");
        }

        return totalDamage;
    }

    /**
     * 던지기 기술
     * D6, 스태미나 0
     */
    public static int throwAttack(int stat, PrintStream out) {
        out.println("석궁사수-던지기 사용 (D6)");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 빠른 장전 기술
     * 화살 1개 추가 장전, 스태미나 1 소모
     * (데미지 없음, 메시지만 출력)
     */
    public static void quickLoad(PrintStream out) {
        out.println("석궁사수-빠른 장전 사용");
        out.println("※ 화살 1개 추가 장전");
        out.println("※ 스태미나 1 소모");
    }

    /**
     * 단일사격 기술
     * 화살을 1개만 소모, D10, 스태미나 2 소모
     */
    public static int singleShot(int stat, PrintStream out) {
        out.println("석궁사수-단일사격 사용 (D10)");
        int defaultDamage = Main.dice(1, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 2 소모");
        out.println("※ 화살 1개만 소모");
        return defaultDamage + sideDamage;
    }

    /**
     * 발광 화살 기술
     * 화살 2개 소모, 2D8, 다음턴까지 받는 데미지 150% 부여, 스태미나 4 소모
     */
    public static int rageArrow(int stat, PrintStream out) {
        out.println("석궁사수-발광 화살 사용 (2D8)");
        int defaultDamage = Main.dice(2, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 4 소모");
        out.println("※ 화살 2개 소모");
        out.println("※ 다음턴까지 적이 받는 데미지 150%%");
        return defaultDamage + sideDamage;
    }

    /**
     * 마비 화살 기술
     * 화살 1개 소모, 2D8, 다음턴까지 적의 모든 스탯 -2 D6, 스태미나 6 소모
     */
    public static int paralyzeArrow(int stat, PrintStream out) {
        out.println("석궁사수-마비 화살 사용 (2D8)");
        int defaultDamage = Main.dice(2, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 스태미나 6 소모");
        out.println("※ 화살 1개 소모");
        out.println("※ 다음턴까지 적의 모든 스탯 -2 D6");
        return defaultDamage + sideDamage;
    }

    /**
     * 화살 꺾기 (전용수비)
     * 6 * 소모 화살 만큼 받는 데미지 감소, 스태미나 3 소모
     * @param damageTaken 받는 데미지
     * @param arrowsToBreak 꺾을 화살 개수
     * @param out 출력 스트림
     * @return 감소된 데미지
     */
    public static int breakArrows(int damageTaken, int arrowsToBreak, PrintStream out) {
        out.println("석궁사수-화살 꺾기 사용 (전용수비)");
        int reduction = 6 * arrowsToBreak;
        int finalDamage = Math.max(0, damageTaken - reduction);
        out.printf("받는 데미지 감소: %d - %d (화살 %d개) = %d%n", damageTaken, reduction, arrowsToBreak, finalDamage);
        out.println("※ 스태미나 3 소모");
        out.printf("※ 화살 %d개 소모%n", arrowsToBreak);
        return finalDamage;
    }

    /**
     * 이럴 때 일수록! (전용수비)
     * (받는 데미지/4) 만큼 화살 장전, 스태미나 9 소모
     * @param damageTaken 받는 데미지
     * @param out 출력 스트림
     * @return 장전할 화살 개수
     */
    public static int desperateLoad(int damageTaken, PrintStream out) {
        out.println("석궁사수-이럴 때 일수록! 사용 (전용수비)");
        int arrowsLoaded = damageTaken / 4;
        out.printf("화살 장전: %d / 4 = %d개%n", damageTaken, arrowsLoaded);
        out.println("※ 스태미나 9 소모");
        return arrowsLoaded;
    }

}

