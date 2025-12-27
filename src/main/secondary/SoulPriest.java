package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 영혼의 사제 (Soul Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 디버프, 딜, 왕귀
 */
public class SoulPriest {

    // ===== 패시브 스킬 =====

    /**
     * 축복 (패시브)
     * [영혼] 누적에 따라 아래 효과 얻음
     * 3개 적의 모든 스탯 -2
     * 5개 적이 가하는 피해 75%
     * 10개 받는 피해 75%
     * 20개 데미지 영혼 x 20% 증가 (최대 600%)
     *
     * @param souls 영혼 개수
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double blessingDamageBonus(int souls, PrintStream out) {
        out.printf("축복: 영혼 %d개%n", souls);
        if (souls >= 3) out.println("  - 적 모든 스탯 -2");
        if (souls >= 5) out.println("  - 적 피해 75%");
        if (souls >= 10) out.println("  - 받는 피해 75%");
        if (souls >= 20) {
            double bonus = Math.min(souls * 20.0, 600.0);
            out.printf("  - 데미지 +%.0f%%%n", bonus);
            return 1.0 + (bonus / 100.0);
        }
        return 1.0;
    }

    public static double blessingEnemyDamageReduction(int souls) {
        return (souls >= 5) ? 0.75 : 1.0;
    }

    public static double blessingDefense(int souls) {
        return (souls >= 10) ? 0.75 : 1.0;
    }

    /**
     * 신비한 육체 (패시브)
     * 마나 0으로 고정
     * (마나)/5 만큼 전투 시작 시 영혼 획득
     * (소수점 내림)
     *
     * @param mana 마나 (전투 시작 시)
     * @param out 출력 스트림
     * @return 영혼 개수
     */
    public static int mysteriousBody(int mana, PrintStream out) {
        int souls = mana / 5;
        out.printf("신비한 육체: 마나 %d → 영혼 %d개 획득%n", mana, souls);
        out.println("※ 마나 0으로 고정");
        return souls;
    }

    // ===== 스킬 =====

    /**
     * 영혼의 사제 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("영혼의 사제-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 흡수
     * 2D4 회복
     * [영혼] 1개 소모
     *
     * @param out 출력 스트림
     * @return 회복량
     */
    public static int absorb(PrintStream out) {
        out.println("영혼의 사제-흡수 사용 (2D4 회복)");
        int totalHeal = 0;

        for (int i = 1; i <= 2; i++) {
            int healRoll = Main.dice(1, 4, out);
            out.printf("%d번째 흡수: %d%n", i, healRoll);
            totalHeal += healRoll;
        }

        out.printf("총 회복량: %d%n", totalHeal);
        out.println("※ 영혼 1개 소모");
        return totalHeal;
    }

    /**
     * 저주
     * 3D4
     * 다음 턴까지 가하는 피해 50% 부여
     * [영혼]2개 소모
     * 쿨타임 4턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int curse(int intelligence, PrintStream out) {
        out.println("영혼의 사제-저주 사용 (3D4)");
        int totalDamage = 0;

        for (int i = 1; i <= 3; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 저주: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 다음 턴까지 대상 피해 50%");
        out.println("※ 영혼 2개 소모, 쿨타임 4턴");
        return totalDamage + sideDamage;
    }

    public static double curseDebuff() {
        return 0.5;
    }

    /**
     * 흉통
     * 8D4
     * 다음 턴까지 받는 피해 150% 부여
     * [영혼] 3개 소모
     * 쿨타임 6턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int chestPain(int intelligence, PrintStream out) {
        out.println("영혼의 사제-흉통 사용 (8D4)");
        int totalDamage = 0;

        for (int i = 1; i <= 8; i++) {
            int diceResult = Main.dice(1, 4, out);
            out.printf("%d번째 흉통: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 다음 턴까지 대상 받는 피해 150%");
        out.println("※ 영혼 3개 소모, 쿨타임 6턴");
        return totalDamage + sideDamage;
    }

    public static double chestPainDebuff() {
        return 1.5;
    }

    /**
     * 원한
     * ([영혼] 소모 개수)D12
     * 쿨타임 8턴
     *
     * @param soulsSpent 소모한 영혼 개수
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int grudge(int soulsSpent, int intelligence, PrintStream out) {
        out.printf("영혼의 사제-원한 사용 (%dD12)%n", soulsSpent);
        int totalDamage = 0;

        for (int i = 1; i <= soulsSpent; i++) {
            int diceResult = Main.dice(1, 12, out);
            out.printf("%d번째 원한: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.printf("※ 영혼 %d개 소모, 쿨타임 8턴%n", soulsSpent);
        return totalDamage + sideDamage;
    }

    /**
     * 수거
     * 영혼 D4개 획득
     * 쿨타임 6턴
     *
     * @param out 출력 스트림
     * @return 획득할 영혼 개수
     */
    public static int collect(PrintStream out) {
        out.println("영혼의 사제-수거 사용");
        int souls = Main.dice(1, 4, out);
        out.printf("영혼 %d개 획득%n", souls);
        out.println("※ 쿨타임 6턴");
        return souls;
    }

    /**
     * 구속
     * 1명의 적에게 ([영혼] 소모 개수)/2턴 후까지 행동불가 부여(소수점 내림)
     * 쿨타임 10턴
     *
     * @param soulsSpent 소모한 영혼 개수
     * @param out 출력 스트림
     * @return 행동불가 턴 수
     */
    public static int restraint(int soulsSpent, PrintStream out) {
        out.println("영혼의 사제-구속 사용");
        int turns = soulsSpent / 2;
        out.printf("영혼 %d개 소모 → %d턴 행동불가 부여%n", soulsSpent, turns);
        out.println("※ 쿨타임 10턴");
        return turns;
    }

    /**
     * 폐허
     * [축복]효과 변경
     * 10개 자신 제외 모두의 스탯 -5
     * 15개 자신 제외 모두의 가하는 피해 50%
     * 20개 자신 받는 피해 50% / 자신 제외 모두가 받는 피해 150%
     * 30개 데미지 영혼 x 30% 증가 (최대 1200%)
     * 전투 내 영구 지속, 취소 불가
     * [영혼]5개 소모
     *
     * @param souls 영혼 개수
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double ruinsDamageBonus(int souls, PrintStream out) {
        out.println("영혼의 사제-폐허 사용");
        out.printf("영혼 %d개%n", souls);
        out.println("※ 축복 효과 변경:");
        if (souls >= 10) out.println("  - 자신 제외 모두 스탯 -5");
        if (souls >= 15) out.println("  - 자신 제외 모두 피해 50%");
        if (souls >= 20) out.println("  - 자신 받는 피해 50%, 자신 제외 모두 받는 피해 150%");
        if (souls >= 30) {
            double bonus = Math.min(souls * 30.0, 1200.0);
            out.printf("  - 데미지 +%.0f%%%n", bonus);
            out.println("※ 영혼 5개 소모, 전투내 영구 지속");
            return 1.0 + (bonus / 100.0);
        }
        out.println("※ 영혼 5개 소모, 전투내 영구 지속");
        return 1.0;
    }

    public static double ruinsOtherDamage(int souls) {
        return (souls >= 15) ? 0.5 : 1.0;
    }

    public static double ruinsSelfDefense(int souls) {
        return (souls >= 20) ? 0.5 : 1.0;
    }

    public static double ruinsOtherDefense(int souls) {
        return (souls >= 20) ? 1.5 : 1.0;
    }

    /**
     * 수명 단축
     * 모든 아군 최대 체력이 50%로 감소
     * 자신의 모든 스킬 쿨타임 50%
     * 전투 내 영구 지속, 취소 불가
     * [영혼] 4개 소모
     *
     * @param out 출력 스트림
     * @return 체력/쿨타임 감소 배율
     */
    public static double lifeShortening(PrintStream out) {
        out.println("영혼의 사제-수명 단축 사용");
        out.println("※ 모든 아군 최대 체력 50%");
        out.println("※ 자신 모든 스킬 쿨타임 50%");
        out.println("※ 영혼 4개 소모, 전투내 영구 지속");
        return 0.5;
    }

    /**
     * 처형
     * 아군 1명 즉시 처형
     * [영혼] 3개 획득
     * (해당 아군 전투 내 부활 불가)
     * (영구적으로 데미지 150%)
     *
     * @param out 출력 스트림
     * @return 획득 영혼 개수
     */
    public static int execution(PrintStream out) {
        out.println("영혼의 사제-처형 사용");
        out.println("※ 아군 1명 즉시 처형");
        out.println("※ 영혼 3개 획득");
        out.println("※ 해당 아군 전투 내 부활 불가");
        out.println("※ 영구적으로 데미지 150%");
        return 3;
    }

    public static double executionDamageBonus() {
        return 1.5;
    }

    /**
     * 신앙심
     * 사망 시 발동, 다음 턴까지 자신 제외 모두 행동불가
     * [영혼]이 20개 이상이라면 절반을 소모
     * (소모량) x 5% 체력으로 부활
     * 전투 당 1회
     *
     * @param souls 영혼 개수
     * @param out 출력 스트림
     * @return 부활 체력 퍼센트
     */
    public static double faithReviveHp(int souls, PrintStream out) {
        out.println("영혼의 사제-신앙심 발동 (사망 시)");
        if (souls < 20) {
            out.printf("영혼 %d개 (20개 미만) → 부활 불가%n", souls);
            return 0;
        }
        int consumed = souls / 2;
        double hpPercent = consumed * 5.0;
        out.printf("영혼 %d개 소모 → 체력 %.0f%%로 부활%n", consumed, hpPercent);
        out.println("※ 다음 턴까지 자신 제외 모두 행동불가");
        out.println("※ 전투 당 1회");
        return hpPercent;
    }
}

