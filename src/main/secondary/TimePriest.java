package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 시간의 사제 (Time Priest)
 * 주 스탯: 지능 (지혜)
 * 특징: 서폿, 디버프, 전장 조절
 */
public class TimePriest {

    // ===== 패시브 스킬 =====

    /**
     * 냉정 (패시브)
     * 행동불가 상태의 적에게 받는 피해 200% 부여
     * 행동횟수가 1+n인 아군에게 가하는 피해 50n% 증가 (자신 제외)
     *
     * @param allyExtraActions 아군의 추가 행동 횟수 (n)
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double calm(int allyExtraActions, PrintStream out) {
        double multiplier = 1.0 + (allyExtraActions * 0.50);
        out.printf("냉정: 추가 행동 %d → 아군 피해 x%.2f%n", allyExtraActions, multiplier);
        out.println("※ 행동불가 적에게 받는 피해 200%");
        return multiplier;
    }

    public static double calmEnemyDebuff() {
        return 2.0;
    }

    /**
     * 침착 (패시브)
     * 아군이 행동불가 상태일 때 받는 피해 50%
     *
     * @param out 출력 스트림
     * @return 받는 피해 배율
     */
    public static double composure(PrintStream out) {
        out.println("침착: 행동불가 아군 받는 피해 50%");
        return 0.5;
    }

    // ===== 스킬 =====

    /**
     * 시간의 사제 기본공격
     */
    public static int plain(int intelligence, PrintStream out) {
        out.println("시간의 사제-기본공격 사용");
        int baseDamage = Main.dice(1, 6, out);
        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        return totalDamage;
    }

    /**
     * 유예
     * 1명의 대상에게 다음 턴 동안 공격불가
     * 받는 데미지 150% 부여
     * 마나9 쿨타임 4턴
     *
     * @param out 출력 스트림
     * @return 받는 피해 배율
     */
    public static double suspension(PrintStream out) {
        out.println("시간의 사제-유예 사용");
        out.println("※ 대상 다음 턴 공격불가");
        out.println("※ 대상 받는 피해 150%");
        out.println("※ 마나 9 소모, 쿨타임 4턴");
        return 1.5;
    }

    /**
     * 감속
     * 1명의 대상에게 다음 턴 동안 수비불가
     * 받는 데미지 200% 부여
     * 마나8 쿨타임 5턴
     *
     * @param out 출력 스트림
     * @return 받는 피해 배율
     */
    public static double deceleration(PrintStream out) {
        out.println("시간의 사제-감속 사용");
        out.println("※ 대상 다음 턴 수비불가");
        out.println("※ 대상 받는 피해 200%");
        out.println("※ 마나 8 소모, 쿨타임 5턴");
        return 2.0;
    }

    /**
     * 부식
     * 자유 영창, 영창하는 동안 적 1명에게 매턴 영창 1회 당 4D8
     * 마나15 쿨타임 10턴
     *
     * @param intelligence 지능 스탯
     * @param out 출력 스트림
     * @return 총 데미지
     */
    public static int corrosion(int intelligence, PrintStream out) {
        out.println("시간의 사제-부식 사용 (4D8)");
        out.println("※ 자유 영창: 매턴 영창 1회당 4D8");

        int baseDamage = 0;
        for (int i = 1; i <= 4; i++) {
            int diceResult = Main.dice(1, 8, out);
            out.printf("%d번째 부식: %d%n", i, diceResult);
            baseDamage += diceResult;
        }

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(baseDamage, intelligence, out);
        int totalDamage = baseDamage + sideDamage;
        out.printf("총 데미지 : %d + %d = %d%n", baseDamage, sideDamage, totalDamage);
        out.println("※ 마나 15 소모, 쿨타임 10턴");
        return totalDamage;
    }

    /**
     * 시간의 틈새
     * 자유 영창, 영창하는 동안 자신과 적1명 행동불가
     * 매턴 마나 7소모
     *
     * @param out 출력 스트림
     * @return 매턴 소모 마나
     */
    public static int timeGap(PrintStream out) {
        out.println("시간의 사제-시간의 틈새 사용");
        out.println("※ 자유 영창: 자신과 적 1명 행동불가");
        out.println("※ 매턴 마나 7 소모");
        return 7;
    }

    /**
     * 강탈
     * 전투 시작 시 자신 외 아군 1명 이상 존재 시 사용 가능
     * 매턴 50% 확률로 자신 제외 모든 아군 행동불가
     * 영창 2개 동시에 진행 가능, [축복]영창 중 사용 가능
     * 전투 내 영구 지속, 취소 불가
     * 마나10
     *
     * @param out 출력 스트림
     * @return 행동불가 발동 여부
     */
    public static boolean plunder(PrintStream out) {
        out.println("시간의 사제-강탈 사용");
        int diceRoll = Main.dice(1, 20, out);
        boolean success = diceRoll <= 10; // 50% 확률
        out.printf("50%% 확률 판정 (D20 <= 10): %s%n", success ? "아군 행동불가 발동" : "발동 안 함");
        out.println("※ 영창 2개 동시 진행 가능");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 10 소모");
        return success;
    }

    /**
     * 성급함
     * 전투 시작 시 자신 외 아군 1명 이상 존재 시 사용 가능
     * [냉정] -> 행동횟수 n+1일 때 자신이 가하는 피해 100n% 증가
     * 아군 받는 피해 200%
     * 전투 내 영구 지속, 취소 불가
     * 마나10
     *
     * @param extraActions 추가 행동 횟수 (n)
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double impatience(int extraActions, PrintStream out) {
        out.println("시간의 사제-성급함 사용");
        double multiplier = 1.0 + (extraActions * 1.0);
        out.printf("추가 행동 %d → 자신 피해 x%.2f%n", extraActions, multiplier);
        out.println("※ 냉정 효과 변경");
        out.println("※ 아군 받는 피해 200%");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 10 소모");
        return multiplier;
    }

    public static double impatienceAllyDebuff() {
        return 2.0;
    }

    /**
     * 기복
     * 전투 시작 시 자신 외 아군 1명 이상 존재 시 사용 가능
     * [침착]효과 제거
     * 자신은 행동불가 효과 무시
     * 행동불가 아군 받는 피해 200%
     * 자신이 행동불가를 부여하는 공격을 받을 때 받는 피해 25%
     * 전투 내 영구 지속, 취소 불가
     * 마나15
     *
     * @param out 출력 스트림
     */
    public static void upsAndDowns(PrintStream out) {
        out.println("시간의 사제-기복 사용");
        out.println("※ 침착 효과 제거");
        out.println("※ 자신 행동불가 무시");
        out.println("※ 행동불가 아군 받는 피해 200%");
        out.println("※ 행동불가 공격 받을 때 피해 25%");
        out.println("※ 전투내 영구 지속, 취소 불가");
        out.println("※ 마나 15 소모");
    }

    public static double upsAndDownsAllyDebuff() {
        return 2.0;
    }

    public static double upsAndDownsSelfDefense() {
        return 0.25;
    }

    /**
     * 신앙심
     * 다음 턴 모든 아군 행동 횟수 1회 추가
     * 모든 적 행동불가, 모든 아군 데미지 200%
     * 마나10 쿨타임 9턴
     *
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double faith(PrintStream out) {
        out.println("시간의 사제-신앙심 사용");
        out.println("※ 다음 턴 모든 아군 행동 +1회");
        out.println("※ 모든 적 행동불가");
        out.println("※ 모든 아군 데미지 200%");
        out.println("※ 마나 10 소모, 쿨타임 9턴");
        return 2.0;
    }
}

