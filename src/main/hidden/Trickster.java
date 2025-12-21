package main.hidden;

import main.Main;

import java.io.PrintStream;

public class Trickster {

    /**
     * 트릭스터 기본공격
     * 난사 패시브: (스탯-주사위) ≥ 3(공격 시행 횟수)라면 1회 추가 공격
     * @param stat 사용할 스탯
     * @param isFocusedFire 일점사 패시브 (5회 이상 공격 시 200%)
     * @param isRepeatCustomer 단골 손님 패시브 (지난 턴 일점사 발동 시 150%)
     */
    public static int plain(int stat, boolean isFocusedFire, boolean isRepeatCustomer, PrintStream out) {
        out.println("트릭스터-기본공격 사용 (난사 패시브)");

        int totalDamage = 0;
        int attackCount = 1;

        while (attackCount <= 10) {
            out.printf("--- %d번째 공격 ---%n", attackCount);
            int defaultDamage = Main.dice(1, 6, out);
            int sideDamage = Main.sideDamage(stat, out);
            int attackDamage = defaultDamage + sideDamage;
            totalDamage += attackDamage;

            // 난사 판정
            int barrageCheck = stat - Main.dice(1, 20, out);
            out.printf("난사 판정: %d - D20 = %d (필요: %d)%n", stat, barrageCheck, 3 * attackCount);

            if (barrageCheck >= 3 * attackCount) {
                out.println("난사 성공! 추가 공격");
                attackCount++;
            } else {
                out.println("난사 실패, 공격 종료");
                break;
            }
        }

        // 일점사 패시브
        if (isFocusedFire) {
            totalDamage *= 2;
            out.printf("일점사 패시브 적용 (5회 이상 공격): x2.0 → %d%n", totalDamage);
        }

        // 단골 손님 패시브
        if (isRepeatCustomer) {
            totalDamage = (int) (totalDamage * 1.5);
            out.printf("단골 손님 패시브 적용: x1.5 → %d%n", totalDamage);
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 페이크 단검 기술
     * D4, 다음 공격 데미지 150%, 스태미나 2 소모
     * 돌발 이벤트 패시브 적용
     */
    public static int fakeDagger(int stat, boolean hasEventBonus, PrintStream out) {
        out.println("트릭스터-페이크 단검 사용 (D4)");
        int defaultDamage = Main.dice(1, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        // 돌발 이벤트 패시브
        if (hasEventBonus) {
            totalDamage = (int) (totalDamage * 1.5);
            out.println("돌발 이벤트 패시브 적용 (기술 후 기본공격): x1.5");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 다음 공격 데미지 150%%");
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 콩알탄 기술
     * 2D6, 스태미나 2 소모
     */
    public static int beanShot(int stat, boolean hasEventBonus, PrintStream out) {
        out.println("트릭스터-콩알탄 사용 (2D6)");
        int defaultDamage = Main.dice(2, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasEventBonus) {
            totalDamage = (int) (totalDamage * 1.5);
            out.println("돌발 이벤트 패시브 적용: x1.5");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 기름통 투척 기술
     * D4, 스태미나 2 소모
     */
    public static int oilBarrel(int stat, boolean hasEventBonus, PrintStream out) {
        out.println("트릭스터-기름통 투척 사용 (D4)");
        int defaultDamage = Main.dice(1, 4, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasEventBonus) {
            totalDamage = (int) (totalDamage * 1.5);
            out.println("돌발 이벤트 패시브 적용: x1.5");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 라이터 투척 기술
     * D8, 이번 턴 기름통 적중 시 3D6 추가, 스태미나 2 소모
     */
    public static int lighterThrow(int stat, boolean hasEventBonus, boolean oilHit, PrintStream out) {
        out.println("트릭스터-라이터 투척 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);

        if (oilHit) {
            int bonusDamage = Main.dice(3, 6, out);
            defaultDamage += bonusDamage;
            out.printf("기름통 적중! 추가 3D6: +%d%n", bonusDamage);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasEventBonus) {
            totalDamage = (int) (totalDamage * 1.5);
            out.println("돌발 이벤트 패시브 적용: x1.5");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 특대형 단검 기술
     * D20, 스태미나 3 소모
     */
    public static int hugeDagger(int stat, boolean hasEventBonus, PrintStream out) {
        out.println("트릭스터-특대형 단검 사용 (D20)");
        int defaultDamage = Main.dice(1, 20, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        if (hasEventBonus) {
            totalDamage = (int) (totalDamage * 1.5);
            out.println("돌발 이벤트 패시브 적용: x1.5");
        }

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

}

