package main.hidden;

import main.Main;

import java.io.PrintStream;

public class MasterArcher {

    /**
     * 명궁 기본공격
     * @param stat 사용할 스탯 (오차 제거 패시브: +3)
     * @param isHeavyString 무거운 시위 패시브 (2D8, 스태미나 1 소모)
     * @param isFirstTarget 포착 패시브 (첫 대상 200%, 그외 50%)
     */
    public static int plain(int stat, boolean isHeavyString, boolean isFirstTarget, PrintStream out) {
        out.println("명궁-기본공격 사용");

        // 오차 제거 패시브: 스탯 +3
        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int defaultDamage;
        if (isHeavyString) {
            out.println("무거운 시위 패시브 (2D8, 스태미나 1 소모)");
            defaultDamage = Main.dice(2, 8, out);
        } else {
            out.println("기본 (D6)");
            defaultDamage = Main.dice(1, 6, out);
        }

        int sideDamage = Main.sideDamage(effectiveStat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 포착 패시브
        if (isFirstTarget) {
            totalDamage *= 2;
            out.printf("포착 패시브 적용 (첫 대상): x2.0 → %d%n", totalDamage);
        } else {
            totalDamage = (int) (totalDamage * 0.5);
            out.printf("포착 패시브 적용 (그외 대상): x0.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 파위샷 기술
     * 이번 턴 기본 공격 D6→D8 / 2D8→2D12
     * 스태미나 1 소모
     */
    public static int powerShot(int stat, boolean isHeavyString, boolean isFirstTarget, PrintStream out) {
        out.println("명궁-파위샷 사용");
        out.println("※ 스태미나 1 소모");

        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int defaultDamage;
        if (isHeavyString) {
            out.println("무거운 시위 + 파위샷: 2D8 → 2D12");
            defaultDamage = Main.dice(2, 12, out);
        } else {
            out.println("파위샷: D6 → D8");
            defaultDamage = Main.dice(1, 8, out);
        }

        int sideDamage = Main.sideDamage(effectiveStat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isFirstTarget) {
            totalDamage *= 2;
            out.printf("포착 패시브: x2.0 → %d%n", totalDamage);
        } else {
            totalDamage = (int) (totalDamage * 0.5);
            out.printf("포착 패시브: x0.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 폭탄 화살 기술
     * 이번 턴 기본 공격 데미지 200%
     * 스태미나 2 소모
     */
    public static int explosiveArrow(int stat, boolean isHeavyString, boolean isFirstTarget, PrintStream out) {
        out.println("명궁-폭탄 화살 사용 (데미지 200%)");
        out.println("※ 스태미나 2 소모");

        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int defaultDamage;
        if (isHeavyString) {
            out.println("무거운 시위 (2D8)");
            defaultDamage = Main.dice(2, 8, out);
        } else {
            out.println("기본 (D6)");
            defaultDamage = Main.dice(1, 6, out);
        }

        int sideDamage = Main.sideDamage(effectiveStat, out);
        int totalDamage = (defaultDamage + sideDamage) * 2;
        out.printf("폭탄 화살 200%%: %d%n", totalDamage);

        if (isFirstTarget) {
            totalDamage *= 2;
            out.printf("포착 패시브: x2.0 → %d%n", totalDamage);
        } else {
            totalDamage = (int) (totalDamage * 0.5);
            out.printf("포착 패시브: x0.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 분열 화살 기술
     * 이번 턴 기본 공격 D6→2D4 / 2D8→4D4
     * 스태미나 1 소모
     */
    public static int splitArrow(int stat, boolean isHeavyString, boolean isFirstTarget, PrintStream out) {
        out.println("명궁-분열 화살 사용");
        out.println("※ 스태미나 1 소모");

        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int defaultDamage;
        if (isHeavyString) {
            out.println("무거운 시위 + 분열: 2D8 → 4D4");
            defaultDamage = Main.dice(4, 4, out);
        } else {
            out.println("분열 화살: D6 → 2D4");
            defaultDamage = Main.dice(2, 4, out);
        }

        int sideDamage = Main.sideDamage(effectiveStat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isFirstTarget) {
            totalDamage *= 2;
            out.printf("포착 패시브: x2.0 → %d%n", totalDamage);
        } else {
            totalDamage = (int) (totalDamage * 0.5);
            out.printf("포착 패시브: x0.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 관통 화살 기술
     * 이번 턴 기본 공격 2D8→D20
     * 스태미나 0 소모
     * (무거운 시위 사용 필수)
     */
    public static int piercingArrow(int stat, boolean isFirstTarget, PrintStream out) {
        out.println("명궁-관통 화살 사용");
        out.println("※ 무거운 시위 필수 (2D8 → D20)");

        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(effectiveStat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (isFirstTarget) {
            totalDamage *= 2;
            out.printf("포착 패시브: x2.0 → %d%n", totalDamage);
        } else {
            totalDamage = (int) (totalDamage * 0.5);
            out.printf("포착 패시브: x0.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 더블 샷 기술
     * 이번 턴 기본 공격 2회 사용
     * 스태미나 3 소모
     */
    public static int doubleShot(int stat, boolean isHeavyString, boolean isFirstTarget, PrintStream out) {
        out.println("명궁-더블 샷 사용 (기본 공격 2회)");
        out.println("※ 스태미나 3 소모");

        int effectiveStat = stat + 3;
        out.printf("오차 제거 패시브: 스탯 %d → %d%n", stat, effectiveStat);

        int totalDamage = 0;

        for (int i = 1; i <= 2; i++) {
            out.printf("--- %d번째 공격 ---%n", i);
            int defaultDamage;
            if (isHeavyString) {
                defaultDamage = Main.dice(2, 8, out);
            } else {
                defaultDamage = Main.dice(1, 6, out);
            }

            int sideDamage = Main.sideDamage(effectiveStat, out);
            int attackDamage = defaultDamage + sideDamage;

            if (isFirstTarget) {
                attackDamage *= 2;
                out.printf("포착 패시브: x2.0 → %d%n", attackDamage);
            } else {
                attackDamage = (int) (attackDamage * 0.5);
                out.printf("포착 패시브: x0.5 → %d%n", attackDamage);
            }

            totalDamage += attackDamage;
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

}

