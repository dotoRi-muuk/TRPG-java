package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Samurai {

    /**
     * 무사 기본공격 (호환성을 위한 오버로드)
     */
    public static int plain(int stat, boolean isMula, PrintStream out) {
        return plain(stat, isMula, 100, 100, false, out);
    }

    /**
     * 무사 기본공격
     * @param stat 사용할 스탯
     * @param isMula 물아 모드 (기술 데미지 150%)
     * @param currentHP 현재 체력 (일격필살용)
     * @param maxHP 최대 체력 (일격필살용)
     * @param scatteringSwordDance 흩날리는 검무 활성화 여부
     * @param out 출력 스트림
     */
    public static int plain(int stat, boolean isMula, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-기본공격 사용");
        int diceCount = 1; // D6 = 1개
        int baseDamage = Main.dice(diceCount, 6, out);

        // 흩날리는 검무 패시브: 공격 판정에 사용된 주사위 개수만큼 D4 추가
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        double multiplier = 1.0;

        if (isMula) {
            multiplier *= 1.5;
            out.println("물아 모드 적용: x1.5");
        }

        // 일격필살 패시브: 체력 40% 이하일 때 데미지 200%
        if (currentHP <= maxHP * 0.4) {
            multiplier *= 2.0;
            out.println("일격필살 적용 (체력 40% 이하): x2.0");
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);
        if (multiplier > 1.0) {
            out.printf("패시브 배율 적용: %d x %.1f = %d%n", baseDamage, multiplier, damageAfterPassives);
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 발검 기술 (호환성을 위한 오버로드)
     */
    public static int quickDraw(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return quickDraw(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 발검 기술
     * D8, 스태미나 1 소모
     */
    public static int quickDraw(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-발검 사용 (D8)");
        int diceCount = 1; // D8 = 1개
        int baseDamage = Main.dice(diceCount, 8, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        out.println("※ 스태미나 1 소모");
        return damageAfterPassives;
    }

    /**
     * 발도 기술 (호환성을 위한 오버로드)
     */
    public static int battou(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return battou(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 발도 기술
     * D12, 스태미나 2 소모
     */
    public static int battou(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-발도 사용 (D12)");
        int diceCount = 1; // D12 = 1개
        int baseDamage = Main.dice(diceCount, 12, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        out.println("※ 스태미나 2 소모");
        return damageAfterPassives;
    }

    /**
     * 자법 기술 (호환성을 위한 오버로드)
     */
    public static int jabeop(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return jabeop(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 자법 기술
     * 3D6, 스태미나 3 소모
     */
    public static int jabeop(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-자법 사용 (3D6)");
        int diceCount = 3; // 3D6 = 3개
        int baseDamage = Main.dice(diceCount, 6, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        out.println("※ 스태미나 3 소모");
        return damageAfterPassives;
    }

    /**
     * 일섬 기술 (호환성을 위한 오버로드)
     */
    public static int ilSeom(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return ilSeom(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 일섬 기술
     * 3D6, 스태미나 3 소모
     */
    public static int ilSeom(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-일섬 사용 (3D6)");
        int diceCount = 3; // 3D6 = 3개
        int baseDamage = Main.dice(diceCount, 6, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        out.println("※ 스태미나 3 소모");
        return damageAfterPassives;
    }

    /**
     * 난격 기술 (호환성을 위한 오버로드)
     */
    public static int rangedAttack(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return rangedAttack(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 난격 기술
     * 5D4, 스태미나 4 소모
     */
    public static int rangedAttack(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-난격 사용 (5D4)");
        int diceCount = 5; // 5D4 = 5개
        int baseDamage = Main.dice(diceCount, 4, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        out.println("※ 스태미나 4 소모");
        return damageAfterPassives;
    }

    /**
     * 섬격 기술 (호환성을 위한 오버로드)
     */
    public static int flashStrike(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return flashStrike(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 섬격 기술
     * D12 200% 데미지, 스태미나 5 소모
     */
    public static int flashStrike(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-섬격 사용 (D12 200%)");
        int diceCount = 1; // D12 = 1개
        int baseDamage = Main.dice(diceCount, 12, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        // 기본 200% 데미지 적용
        int boostedDamage = baseDamage * 2;
        out.printf("기본 200%% 데미지: %d → %d%n", baseDamage, boostedDamage);

        int damageAfterPassives = applyPassives(boostedDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        out.println("※ 스태미나 5 소모");
        return damageAfterPassives;
    }

    /**
     * 종점 기술 (호환성을 위한 오버로드)
     */
    public static int finalPoint(int stat, int consumedStamina, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return finalPoint(stat, consumedStamina, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 종점 기술
     * 3D20 + 소모한 스태미나
     * 다음 턴까지 행동불가, 그 다음 턴 공격불가
     * 다음 2턴동안 받는 데미지 150%
     * 모든 스태미나 소모
     */
    public static int finalPoint(int stat, int consumedStamina, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-종점 사용 (3D20 + 소모 스태미나)");
        out.println("※ 모든 스태미나 소모");
        out.println("※ 다음 턴까지 행동불가, 그 다음 턴 공격불가");
        out.println("※ 다음 2턴동안 받는 데미지 150%%");

        int diceCount = 3; // 3D20 = 3개
        int baseDamage = Main.dice(diceCount, 20, out);
        baseDamage += consumedStamina;
        out.printf("스태미나 추가: +%d%n", consumedStamina);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        return damageAfterPassives;
    }

    /**
     * 개화 기술 (호환성을 위한 오버로드)
     */
    public static int bloom(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, PrintStream out) {
        return bloom(stat, isMula, kakugo, seishaKetsudan, 100, 100, false, out);
    }

    /**
     * 개화 기술
     * 8D6
     * 다음 2턴동안 공격 불가, 4턴동안 패시브 비활성화
     * 다음 턴 받는데미지 200%
     * 스태미나 8소모
     */
    public static int bloom(int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, boolean scatteringSwordDance, PrintStream out) {
        out.println("무사-개화 사용 (8D6)");
        out.println("※ 스태미나 8 소모");
        out.println("※ 다음 2턴동안 공격 불가, 4턴동안 패시브 비활성화");
        out.println("※ 다음 턴 받는데미지 200%%");

        int diceCount = 8; // 8D6 = 8개
        int baseDamage = Main.dice(diceCount, 6, out);

        // 흩날리는 검무 패시브
        if (scatteringSwordDance) {
            int extraDamage = Main.dice(diceCount, 4, out);
            out.printf("흩날리는 검무: %dD4 = %d%n", diceCount, extraDamage);
            baseDamage += extraDamage;
        }

        int damageAfterPassives = applyPassives(baseDamage, stat, isMula, kakugo, seishaKetsudan, currentHP, maxHP, out);

        out.printf("총 데미지 : %d%n", damageAfterPassives);
        return damageAfterPassives;
    }

    /**
     * 패시브 배율 적용 (곱연산) 후 sideDamage 적용
     * sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
     * @param baseDamage 기본 데미지
     * @param stat 스탯 (sideDamage용)
     * @param isMula 물아 모드 (150%)
     * @param kakugo 각오 (200%)
     * @param seishaKetsudan 생사결단 (300%)
     * @param currentHP 현재 체력 (일격필살용)
     * @param maxHP 최대 체력 (일격필살용)
     * @return 최종 데미지
     */
    private static int applyPassives(int baseDamage, int stat, boolean isMula, boolean kakugo, boolean seishaKetsudan, int currentHP, int maxHP, PrintStream out) {
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
        // 일격필살 패시브: 체력 40% 이하일 때 데미지 200%
        if (currentHP <= maxHP * 0.4) {
            multiplier *= 2.0;
            out.println("일격필살 적용 (체력 40% 이하): x2.0");
        }

        int damageAfterMultiplier = baseDamage;
        if (multiplier > 1.0) {
            damageAfterMultiplier = (int) (baseDamage * multiplier);
            out.printf("패시브 배율 적용: %d x %.1f = %d%n", baseDamage, multiplier, damageAfterMultiplier);
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterMultiplier, stat, out);
        return damageAfterMultiplier + sideDamage;
    }

}

