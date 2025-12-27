package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 어둠의 사제 (Dark Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 팀킬, 극딜
 */
public class DarkPriest {

    // ===== 패시브 스킬 =====

    /**
     * 지배 (패시브)
     * 아군 공격 시 다음 턴 데미지 200%
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double domination(PrintStream out) {
        out.println("지배: 아군 공격 → 다음 턴 데미지 200%");
        return 2.0;
    }

    /**
     * 강탈 (패시브)
     * 모든 아군이 받는 효과를 제거, 그 효과의 50%로 자신이 효과를 받음
     * (해제 가능)
     *
     * @param out 출력 스트림
     * @return 효과 배율
     */
    public static double plunder(PrintStream out) {
        out.println("강탈: 아군 효과 제거 → 50%로 자신에게 적용");
        return 0.5;
    }

    /**
     * 잠식 (패시브)
     * 자신이 공격한 아군은 다음 턴 수비불가
     * 아군 공격 시 데미지만큼 체력 회복
     * 다음 턴동안 받는 피해 75%
     *
     * @param out 출력 스트림
     * @return 받는 피해 배율
     */
    public static double encroachment(PrintStream out) {
        out.println("잠식: 공격한 아군 다음 턴 수비불가");
        out.println("잠식: 아군 공격 데미지만큼 체력 회복");
        out.println("잠식: 다음 턴 받는 피해 75%");
        return 0.75;
    }

    // ===== 스킬 =====

    /**
     * 어둠의 사제 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("어둠의 사제-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        return defaultDamage + sideDamage;
    }

    /**
     * 어둠의 기운
     * D4
     * 다음 턴까지 데미지 80% 부여
     * 광역
     * 마나2
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int darkEnergy(int intelligence, PrintStream out) {
        out.println("어둠의 사제-어둠의 기운 사용 (D4, 광역)");
        int defaultDamage = Main.dice(1, 4, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 다음 턴까지 대상 데미지 80%");
        out.println("※ 마나 2 소모");
        return defaultDamage + sideDamage;
    }

    public static double darkEnergyDebuff() {
        return 0.8;
    }

    /**
     * 손아귀
     * D8
     * 다음 턴동안 행동불가 부여
     * 마나5 쿨타임 4턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int grip(int intelligence, PrintStream out) {
        out.println("어둠의 사제-손아귀 사용 (D8)");
        int defaultDamage = Main.dice(1, 8, out);
        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", defaultDamage, sideDamage, defaultDamage + sideDamage);
        out.println("※ 다음 턴동안 행동불가 부여");
        out.println("※ 마나 5 소모, 쿨타임 4턴");
        return defaultDamage + sideDamage;
    }

    /**
     * 우즈마니아
     * 4D12
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 2턴 마나8 쿨타임 6턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int uzumania(int intelligence, PrintStream out) {
        out.println("어둠의 사제-우즈마니아 사용 (4D12)");
        out.println("※ 영창 2턴");

        int totalDamage = 0;
        for (int i = 1; i <= 4; i++) {
            int diceResult = Main.dice(1, 12, out);
            out.printf("%d번째 우즈마니아: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 발동 대기 후 이외 행동 가능");
        out.println("※ 마나 8 소모, 쿨타임 6턴");
        return totalDamage + sideDamage;
    }

    /**
     * 엑실리스터
     * 4D20
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 4턴 마나 14 쿨타임 9턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int exilister(int intelligence, PrintStream out) {
        out.println("어둠의 사제-엑실리스터 사용 (4D20)");
        out.println("※ 영창 4턴");

        int totalDamage = 0;
        for (int i = 1; i <= 4; i++) {
            int diceResult = Main.dice(1, 20, out);
            out.printf("%d번째 엑실리스터: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 발동 대기 후 이외 행동 가능");
        out.println("※ 마나 14 소모, 쿨타임 9턴");
        return totalDamage + sideDamage;
    }

    /**
     * 어나이스필레인
     * 7D12
     * 남은 영창 시간이 2턴 이하일 때 모든 아군이 공격 불가
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 2턴 마나18 쿨타임 12턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int annihilationPlain(int intelligence, PrintStream out) {
        out.println("어둠의 사제-어나이스필레인 사용 (7D12)");
        out.println("※ 영창 2턴");

        int totalDamage = 0;
        for (int i = 1; i <= 7; i++) {
            int diceResult = Main.dice(1, 12, out);
            out.printf("%d번째 어나이스필레인: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 남은 영창 2턴 이하 시 모든 아군 공격 불가");
        out.println("※ 마나 18 소모, 쿨타임 12턴");
        return totalDamage + sideDamage;
    }

    /**
     * 엔시아스티켈리아
     * 9D10
     * 발동 시 다음 턴 모든 아군 받는 데미지 200%
     * 발동 대기 후 이외 행동 가능(다른 스킬 영창은 불가)
     * 영창 5턴 마나16 쿨타임 13턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int ensiasticalia(int intelligence, PrintStream out) {
        out.println("어둠의 사제-엔시아스티켈리아 사용 (9D10)");
        out.println("※ 영창 5턴");

        int totalDamage = 0;
        for (int i = 1; i <= 9; i++) {
            int diceResult = Main.dice(1, 10, out);
            out.printf("%d번째 엔시아스티켈리아: %d%n", i, diceResult);
            totalDamage += diceResult;
        }

        int sideDamage = Main.sideDamage(intelligence, out);
        out.printf("총 데미지 : %d + %d = %d%n", totalDamage, sideDamage, totalDamage + sideDamage);
        out.println("※ 발동 시 다음 턴 모든 아군 받는 피해 200%");
        out.println("※ 마나 16 소모, 쿨타임 13턴");
        return totalDamage + sideDamage;
    }

    public static double ensiasticaliaAllyDebuff() {
        return 2.0;
    }

    /**
     * 저주
     * 모든 아군에게 현재 체력의 절반만큼 피해를 줌
     * 다음 턴 데미지 200%
     * 마나7 쿨타임 6턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double curse(PrintStream out) {
        out.println("어둠의 사제-저주 사용");
        out.println("※ 모든 아군에게 현재 체력의 절반만큼 피해");
        out.println("※ 다음 턴 데미지 200%");
        out.println("※ 마나 7 소모, 쿨타임 6턴");
        return 2.0;
    }

    /**
     * 갈취
     * (죽은 아군의 모든 스탯)/4 만큼 스탯 증가
     * 전투 당 1회
     *
     * @param deadAllyTotalStats 죽은 아군의 모든 스탯 합
     * @param out 출력 스트림
     * @return 스탯 증가량
     */
    public static int extortion(int deadAllyTotalStats, PrintStream out) {
        out.println("어둠의 사제-갈취 사용");
        int statGain = deadAllyTotalStats / 4;
        out.printf("죽은 아군 스탯 %d → 스탯 +%d%n", deadAllyTotalStats, statGain);
        out.println("※ 전투 당 1회");
        return statGain;
    }

    /**
     * 희생양
     * 다음 공격의 대상이 1명이라면 아군 1명까지 추가로 공격
     * 아군이 받는 데미지는 보정 영향X
     * 해당 공격의 데미지 300%
     * 마나6 쿨타임 8턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double scapegoat(PrintStream out) {
        out.println("어둠의 사제-희생양 사용");
        out.println("※ 다음 공격: 아군 1명 추가 공격 (보정 영향X)");
        out.println("※ 데미지 300%");
        out.println("※ 마나 6 소모, 쿨타임 8턴");
        return 3.0;
    }

    /**
     * 침식
     * 현재 ()불가 상태의 아군 1명 당 이번 턴 데미지 50% 증가
     * (턴 소모X)
     * 마나4 쿨타임 7턴
     *
     * @param disabledAlliesCount 불가 상태의 아군 수
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double erosion(int disabledAlliesCount, PrintStream out) {
        out.println("어둠의 사제-침식 사용");
        double multiplier = 1.0 + (disabledAlliesCount * 0.5);
        out.printf("불가 상태 아군 %d명 → x%.2f 데미지%n", disabledAlliesCount, multiplier);
        out.println("※ 턴 소모X, 마나 4 소모, 쿨타임 7턴");
        return multiplier;
    }

    /**
     * 신앙심
     * 다음 턴까지 자신 제외 모두 행동불가
     * 데미지 200%
     * 마나7 쿨타임 13턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double faith(PrintStream out) {
        out.println("어둠의 사제-신앙심 사용");
        out.println("※ 다음 턴까지 자신 제외 모두 행동불가");
        out.println("※ 데미지 200%");
        out.println("※ 마나 7 소모, 쿨타임 13턴");
        return 2.0;
    }
}

