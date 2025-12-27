package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 정수 탭
 * 직접적으로 데미지를 계산하는 정수만 계산
 * 정수 계열: 빛, 어둠, 시간, 영혼, 번개
 */
public class Essence {

    // ==================== 빛 계열 ====================

    /**
     * 석양 - 대리자의 추억 (헤이타)
     * 다음 턴까지 받는 데미지 150%, 가하는 데미지 250%
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int sunset(int baseDamage, PrintStream out) {
        out.println("정수-석양 사용 (대리자의 추억)");
        int finalDamage = (int) (baseDamage * 2.5);
        out.printf("가하는 데미지 250%%: %d → %d%n", baseDamage, finalDamage);
        out.println("※ 다음 턴까지 받는 데미지 150%%");
        return finalDamage;
    }

    // ==================== 어둠 계열 ====================

    /**
     * 흑염 - 어둠의 소문 (에트라함)
     * D6% 데미지
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int blackFlame(int baseDamage, PrintStream out) {
        out.println("정수-흑염 사용 (어둠의 소문)");
        int roll = Main.dice(1, 6, out);
        int finalDamage = (int) (baseDamage * roll / 100.0);
        out.printf("D6%% 데미지: %d%% → %d%n", roll, finalDamage);
        return finalDamage;
    }

    // ==================== 영혼 계열 ====================

    /**
     * 잔향 - 흩어져가는 길
     * 지난 3턴 동안 적이 입은 피해의 50%만큼 피해 부여
     * @param last3TurnsDamage 지난 3턴 동안 적이 입은 피해
     * @param out 출력 스트림
     * @return 추가 데미지
     */
    public static int afterglow(int last3TurnsDamage, PrintStream out) {
        out.println("정수-잔향 사용 (흩어져가는 길)");
        int additionalDamage = last3TurnsDamage / 2;
        out.printf("지난 3턴 피해의 50%%: %d → %d%n", last3TurnsDamage, additionalDamage);
        return additionalDamage;
    }

    // ==================== 번개 계열 ====================

    /**
     * 천둥 - 천둥에 다다랐던 전사 (호아킨)
     * 이번 턴 적 공격 불가, 가하는 데미지 200% (턴 소모X)
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int thunder(int baseDamage, PrintStream out) {
        out.println("정수-천둥 사용 (천둥에 다다랐던 전사)");
        int finalDamage = baseDamage * 2;
        out.printf("가하는 데미지 200%%: %d → %d%n", baseDamage, finalDamage);
        out.println("※ 이번 턴 적 공격 불가");
        out.println("※ 턴 소모 없음");
        return finalDamage;
    }

    /**
     * 격동 - 멈추지 않는 투지
     * (효과 지속 턴*50)% 데미지 추가, 첫 피격 시 피해 300%
     * @param baseDamage 기본 데미지
     * @param durationTurns 효과 지속 턴 (최대 5)
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int surge(int baseDamage, int durationTurns, PrintStream out) {
        out.println("정수-격동 사용 (멈추지 않는 투지)");
        int bonusPercent = durationTurns * 50;
        int finalDamage = (int) (baseDamage * (100 + bonusPercent) / 100.0);
        out.printf("지속 턴 %d → %d%% 추가: %d → %d%n", durationTurns, bonusPercent, baseDamage, finalDamage);
        out.println("※ 사용 후 처음으로 맞는 공격의 피해 300%%");
        out.println("※ 피격 시 효과 해제");
        return finalDamage;
    }

    /**
     * 섬광 - 미련 없는 전우
     * 모든 적 받는 데미지 200%, 수비 불가 부여
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int flash(int baseDamage, PrintStream out) {
        out.println("정수-섬광 사용 (미련 없는 전우)");
        int finalDamage = baseDamage * 2;
        out.printf("모든 적 받는 데미지 200%%: %d → %d%n", baseDamage, finalDamage);
        out.println("※ 다음 턴까지 모든 적 수비 불가 부여");
        return finalDamage;
    }

    /**
     * 폭주 - 사라진 이성 속의 인연
     * D6 < (6-지속턴) 성공 시 데미지 250%
     * @param baseDamage 기본 데미지
     * @param durationTurns 효과 지속 턴 (최대 6)
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int rampage(int baseDamage, int durationTurns, PrintStream out) {
        out.println("정수-폭주 사용 (사라진 이성 속의 인연)");
        int roll = Main.dice(1, 6, out);
        int threshold = 6 - durationTurns;
        out.printf("폭주 판정: D6=%d < %d (6-%d)?%n", roll, threshold, durationTurns);

        if (roll < threshold) {
            int finalDamage = (int) (baseDamage * 2.5);
            out.printf("폭주 성공! 데미지 250%%: %d → %d%n", baseDamage, finalDamage);
            return finalDamage;
        } else {
            out.println("폭주 실패. 데미지 증가 없음");
            if (durationTurns < 4) {
                out.println("※ 다음 턴까지 행동불가");
            }
            return baseDamage;
        }
    }

    // ==================== 대리자 계열 ====================

    /**
     * 빛(대리자) - 대리자의 추억(빛)
     * 데미지 200%, 적 행동불가
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int lightProxy(int baseDamage, PrintStream out) {
        out.println("정수-빛(대리자) 사용");
        int finalDamage = baseDamage * 2;
        out.printf("데미지 200%%: %d → %d%n", baseDamage, finalDamage);
        out.println("※ 전투 당 1회 아군 1명의 잃은 체력 모두 회복");
        out.println("※ 이번 턴 1개의 스탯 100%% 증가");
        out.println("※ 적 행동불가");
        return finalDamage;
    }

    /**
     * 어둠(대리자) - 대리자의 추억(어둠)
     * 아군에게 D20% 데미지(보정X), 자신 데미지 200%
     * @param baseDamage 기본 데미지
     * @param allyDamage 아군에게 줄 데미지 기준값
     * @param out 출력 스트림
     * @return 최종 데미지 (적에게 가할 데미지)
     */
    public static int darkProxy(int baseDamage, int allyDamage, PrintStream out) {
        out.println("정수-어둠(대리자) 사용");
        int allyDamageRoll = Main.dice(1, 20, out);
        int allyFinalDamage = (int) (allyDamage * allyDamageRoll / 100.0);
        out.printf("자신 제외 모든 아군 D20%% 데미지: %d%% → %d (보정X)%n", allyDamageRoll, allyFinalDamage);

        int finalDamage = baseDamage * 2;
        out.printf("자신 데미지 200%%: %d → %d%n", baseDamage, finalDamage);
        out.println("※ 1명의 대상의 턴을 자신의 턴으로 변경");
        return finalDamage;
    }

    /**
     * 영혼(대리자) - 대리자의 추억(영혼)
     * 5턴간 입힌 데미지의 50% 추가 데미지
     * @param baseDamage 기본 데미지
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int soulProxy(int baseDamage, PrintStream out) {
        out.println("정수-영혼(대리자) 사용");
        out.printf("기본 데미지: %d%n", baseDamage);
        out.println("※ 5턴 후까지 입힌 데미지의 50%% 추가 데미지");
        out.println("※ 2턴 후까지 적의 영혼을 소환, 그 시간동안 적은 행동불가 부여");
        return baseDamage;
    }

    /**
     * 번개(대리자) - 대리자의 추억(번개)
     * D8 < (8-지속턴) 성공 시 (효과지속턴*50)% 데미지 증가
     * @param baseDamage 기본 데미지
     * @param durationTurns 효과 지속 턴 (최대 8)
     * @param out 출력 스트림
     * @return 최종 데미지
     */
    public static int lightningProxy(int baseDamage, int durationTurns, PrintStream out) {
        out.println("정수-번개(대리자) 사용");
        int roll = Main.dice(1, 8, out);
        int threshold = 8 - durationTurns;
        out.printf("번개 판정: D8=%d < %d (8-%d)?%n", roll, threshold, durationTurns);

        if (roll < threshold) {
            int bonusPercent = durationTurns * 50;
            int finalDamage = (int) (baseDamage * (100 + bonusPercent) / 100.0);
            out.printf("번개 성공! %d%% 데미지 증가: %d → %d%n", bonusPercent, baseDamage, finalDamage);
            return finalDamage;
        } else {
            out.println("번개 실패. 데미지 증가 없음");
            if (durationTurns < 5) {
                out.println("※ 이번 턴 행동불가");
            }
            return baseDamage;
        }
    }

}
