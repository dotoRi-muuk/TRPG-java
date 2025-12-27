package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Samurai {

    /**
     * 무사 기본공격
     * @param stat 사용할 스탯
     * @param isMula 물아 모드 (기술 데미지 150%)
     * @param out 출력 스트림
     */
    public static int plain(int stat, boolean isMula, PrintStream out) {
        out.println("무사-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isMula) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("물아 모드 적용: 150%% → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 발검 기술
     * D8, 스태미나 1 소모
     */
    public static int quickDraw(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-발검 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * 발도 기술
     * D12, 스태미나 2 소모
     */
    public static int battou(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-발도 사용 (D12)");
        int defaultDamage = Main.dice(1, 12, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 자법 기술
     * 3D6, 스태미나 3 소모
     */
    public static int jabeop(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-자법 사용 (3D6)");
        int defaultDamage = Main.dice(3, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 일섬 기술
     * 3D6, 스태미나 3 소모
     */
    public static int ilSeom(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-일섬 사용 (3D6)");
        int defaultDamage = Main.dice(3, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 난격 기술
     * 5D4, 스태미나 4 소모
     */
    public static int rangedAttack(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-난격 사용 (5D4)");
        int defaultDamage = Main.dice(5, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

    /**
     * 섬격 기술
     * D12 200% 데미지, 스태미나 5 소모
     */
    public static int flashStrike(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-섬격 사용 (D12 200%)");
        int defaultDamage = Main.dice(1, 12, out);
        int sideDamage = Main.sideDamage(stat, out);
        int baseDamage = defaultDamage + sideDamage;

        int totalDamage = baseDamage * 2;
        out.printf("기본 200%% 데미지: %d → %d%n", baseDamage, totalDamage);

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 5 소모");
        return totalDamage;
    }

    /**
     * 종점 기술
     * 3D20 + 소모한 스태미나
     * 다음 턴까지 행동불가, 그 다음 턴 공격불가
     * 다음 2턴동안 받는 데미지 150%
     * 모든 스태미나 소모
     */
    public static int finalPoint(int stat, int consumedStamina, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-종점 사용 (3D20 + 소모 스태미나)");
        out.println("※ 모든 스태미나 소모");
        out.println("※ 다음 턴까지 행동불가, 그 다음 턴 공격불가");
        out.println("※ 다음 2턴동안 받는 데미지 150%%");

        int defaultDamage = Main.dice(3, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage + consumedStamina;
        out.printf("스태미나 추가: +%d%n", consumedStamina);

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 개화 기술
     * 8D6
     * 다음 2턴동안 공격 불가, 4턴동안 패시브 비활성화
     * 다음 턴 받는데미지 200%
     * 스태미나 8소모
     */
    public static int bloom(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        out.println("무사-개화 사용 (8D6)");
        out.println("※ 스태미나 8 소모");
        out.println("※ 다음 2턴동안 공격 불가, 4턴동안 패시브 비활성화");
        out.println("※ 다음 턴 받는데미지 200%%");

        int defaultDamage = Main.dice(8, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        totalDamage = applyPassives(totalDamage, isMula, kakugo, seishaKetsudan, out);

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 패시브 배율 적용 (곱연산)
     * @param damage 기본 데미지
     * @param isMula 물아 모드 (150%)
     * @param kakugo 각오 (200%)
     * @param seishaKetsudan 생사결단 (300%)
     * @return 최종 데미지
     */
    private static int applyPassives(int damage, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        double multiplier = 1.0;

        if (isMula) {
            multiplier *= 1.5;
            out.println("물아 모드 적용: x1.5");
        }
        if (kakugo) {
            multiplier *= 2.0;
            out.println("각오 적용: x2.0");
        }
        if (seishaKetsudan) {
            multiplier *= 3.0;
            out.println("생사결단 적용: x3.0");
        }

        if (multiplier > 1.0) {
            int finalDamage = (int) (damage * multiplier);
            out.printf("패시브 배율 적용: %d x %.1f = %d%n", damage, multiplier, finalDamage);
            return finalDamage;
        }

        return damage;
    }

}

