package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Crossbowman {

    /**
     * 석궁사수 기본공격 (호환성을 위한 오버로드)
     */
    public static int plain(int stat, int arrows, boolean focusedAttack, PrintStream out) {
        return plain(stat, arrows, focusedAttack, false, false, false, 0, out);
    }

    /**
     * 석궁사수 기본공격 - 장전/발사 패시브 적용
     * 모인 화살의 데미지: (화살 개수)D6
     * @param stat 사용할 스탯
     * @param arrows 장전된 화살 개수
     * @param focusedAttack 집중 공격 패시브 적용 여부 (화살 1개당 30% 증가)
     * @param isErrorRemoval 오차 제거 활성화 여부 (집중 공격 30% → 50%)
     * @param isDistanceCalc 비거리 계산 패시브 적용 여부 (2회 판정 성공 시 200%)
     * @param isExecutionArrow 처형 화살 활성화 여부
     * @param executionArrows 처형 화살 개수
     * @param out 출력 스트림
     */
    public static int plain(int stat, int arrows, boolean focusedAttack, boolean isErrorRemoval, boolean isDistanceCalc, boolean isExecutionArrow, int executionArrows, PrintStream out) {
        out.println("석궁사수-기본공격 사용 (장전/발사)");
        out.printf("장전된 화살: %d개%n", arrows);

        if (arrows <= 0) {
            out.println("장전된 화살이 없습니다!");
            return 0;
        }

        // 비거리 계산 패시브: 2회 판정 성공 시 200%
        if (isDistanceCalc) {
            out.println("비거리 계산 패시브: 2회 판정 필요");
            int check1 = stat - Main.dice(1, 20, out);
            out.printf("1차 판정: %d - D20 = %d%n", stat, check1);
            int check2 = stat - Main.dice(1, 20, out);
            out.printf("2차 판정: %d - D20 = %d%n", stat, check2);

            if (check1 < 1 || check2 < 1) {
                out.println("비거리 계산 실패! 공격 실패");
                return 0;
            }
            out.println("비거리 계산 성공! 데미지 200%");
        }

        int baseDamage = Main.dice(arrows, 6, out);

        double multiplier = 1.0;

        // 비거리 계산 성공 시 200%
        if (isDistanceCalc) {
            multiplier *= 2.0;
        }

        // 집중 공격 패시브 (오차 제거 시 50%, 그 외 30%)
        if (focusedAttack) {
            double focusBonus = isErrorRemoval ? 0.5 : 0.3;
            double focusMultiplier = 1.0 + (arrows * focusBonus);
            multiplier *= focusMultiplier;
            out.printf("집중 공격 패시브 적용: 화살 %d개 → x%.1f%n", arrows, focusMultiplier);
            if (isErrorRemoval) {
                out.println("오차 제거 활성화: 집중 공격 30% → 50%");
            }
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        out.printf("※ 화살 %d개 소모%n", arrows);

        if (arrows >= 4 && !isErrorRemoval) {
            out.println("※ 무차별 난사 발동: 기본 공격이 광역으로 변경");
        } else if (arrows >= 4 && isErrorRemoval) {
            out.println("※ 오차 제거로 무차별 난사 비활성화");
        }

        // 처형 화살 판정
        if (isExecutionArrow && executionArrows > 0) {
            double execProb = executionArrows * 0.2;
            // 5% 단위로 내림
            int execProbFloor = (int) (execProb / 5) * 5;
            out.printf("처형 화살 판정: %d개 → %.1f%% → %d%% (5%% 단위 내림)%n", executionArrows, execProb, execProbFloor);
            if (execProbFloor > 0) {
                int roll = Main.dice(1, 20, out);
                int threshold = execProbFloor / 5; // 5% = 1, 10% = 2, ...
                out.printf("D20 판정: %d <= %d (%d%% 확률)?%n", roll, threshold, execProbFloor);
                if (roll <= threshold) {
                    out.println("★ 처형 발동! ★");
                }
            }
        }

        return totalDamage;
    }

    /**
     * 던지기 기술
     * D6, 스태미나 0
     */
    public static int throwAttack(int stat, PrintStream out) {
        out.println("석궁사수-던지기 사용 (D6)");
        int baseDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        return totalDamage;
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
        int baseDamage = Main.dice(1, 10, out);
        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 스태미나 2 소모");
        out.println("※ 화살 1개만 소모");
        return totalDamage;
    }

    /**
     * 발광 화살 기술
     * 화살 2개 소모, 2D8, 다음턴까지 받는 데미지 150% 부여, 스태미나 4 소모
     */
    public static int rageArrow(int stat, PrintStream out) {
        out.println("석궁사수-발광 화살 사용 (2D8)");
        int baseDamage = Main.dice(2, 8, out);
        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 스태미나 4 소모");
        out.println("※ 화살 2개 소모");
        out.println("※ 다음턴까지 적이 받는 데미지 150%%");
        return totalDamage;
    }

    /**
     * 마비 화살 기술
     * 화살 1개 소모, 2D8, 다음턴까지 적의 모든 스탯 -2 D6, 스태미나 6 소모
     */
    public static int paralyzeArrow(int stat, PrintStream out) {
        out.println("석궁사수-마비 화살 사용 (2D8)");
        int baseDamage = Main.dice(2, 8, out);
        int sideDamage = Main.sideDamage(baseDamage, stat, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 스태미나 6 소모");
        out.println("※ 화살 1개 소모");
        out.println("※ 다음턴까지 적의 모든 스탯 -2 D6");
        return totalDamage;
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

