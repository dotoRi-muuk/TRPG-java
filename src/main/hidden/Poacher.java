package main.hidden;

import main.Main;

import java.io.PrintStream;

public class Poacher {

    /**
     * 밀렵꾼 기본공격 - 산탄 패시브 적용
     * 기본 2D4, (스탯-주사위) ≥ 10이면 2D8
     * @param stat 사용할 스탯
     * @param hasDebuff 사냥 패시브 (디버프 적용 시 150%)
     * @param isLoaded 장전 사용 여부 (2D4→2D6 / 2D8→2D12)
     */
    public static int plain(int stat, boolean hasDebuff, boolean isLoaded, PrintStream out) {
        out.println("밀렵꾼-기본공격 사용 (산탄 패시브)");

        // 스탯 판정
        int statCheck = stat - Main.dice(1, 20, out);
        out.printf("스탯 판정: %d - D20 = %d%n", stat, statCheck);

        int defaultDamage;
        if (statCheck >= 10) {
            out.println("판정 성공! 2D8 사용");
            if (isLoaded) {
                out.println("장전 적용: 2D8 → 2D12");
                defaultDamage = Main.dice(2, 12, out);
            } else {
                defaultDamage = Main.dice(2, 8, out);
            }
        } else {
            out.println("기본 2D4 사용");
            if (isLoaded) {
                out.println("장전 적용: 2D4 → 2D6");
                defaultDamage = Main.dice(2, 6, out);
            } else {
                defaultDamage = Main.dice(2, 4, out);
            }
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 사냥 패시브
        if (hasDebuff) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("사냥 패시브 적용 (디버프 대상): x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 머리찍기 기술
     * D8, 스태미나 1 소모
     */
    public static int headChop(int stat, boolean hasDebuff, PrintStream out) {
        out.println("밀렵꾼-머리찍기 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasDebuff) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("사냥 패시브 적용: x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 1 소모");
        return totalDamage;
    }

    /**
     * 덫 깔기 기술
     * D10, 이번 턴 적의 공격 데미지 75%, 스태미나 3 소모
     */
    public static int setTrap(int stat, boolean hasDebuff, PrintStream out) {
        out.println("밀렵꾼-덫 깔기 사용 (D10)");
        out.println("※ 이번 턴 적의 공격 데미지 75%%");
        int defaultDamage = Main.dice(1, 10, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasDebuff) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("사냥 패시브 적용: x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 올가미 탄 기술
     * D8, 다음턴까지 적 행동불가 부여, 스태미나 8 소모
     */
    public static int snareShot(int stat, boolean hasDebuff, PrintStream out) {
        out.println("밀렵꾼-올가미 탄 사용 (D8)");
        out.println("※ 다음턴까지 적 행동불가 부여");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasDebuff) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("사냥 패시브 적용: x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 8 소모");
        return totalDamage;
    }

    /**
     * 헤드샷 기술
     * 2D12, 스태미나 4 소모
     */
    public static int headshot(int stat, boolean hasDebuff, PrintStream out) {
        out.println("밀렵꾼-헤드샷 사용 (2D12)");
        int defaultDamage = Main.dice(2, 12, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasDebuff) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("사냥 패시브 적용: x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 4 소모");
        return totalDamage;
    }

}

