package main.secondary;

import main.Main;

import java.io.PrintStream;

/**
 * 결계술사 (Barrier Mage)
 * 주 스탯: 지능 (지혜)
 * 특징: 서포터. 아마도?
 */
public class BarrierMage {

    // ===== 패시브 스킬 =====

    /**
     * 결계 확장 (패시브)
     * '결계'스킬 자유 영창 가능
     * (해당 스킬의 영창 시간)명의 대상에게 결계 효과를 50%로 추가부여
     *
     * @param chantTime 결계 영창 시간
     * @param out 출력 스트림
     * @return 추가 대상 수
     */
    public static int barrierExpansion(int chantTime, PrintStream out) {
        out.printf("결계 확장: %d명의 추가 대상에게 50%% 효과 부여%n", chantTime);
        return chantTime;
    }

    public static double barrierExpansionEffectMultiplier() {
        return 0.5;
    }

    /**
     * 영역 (패시브)
     * (전개된 결계의 영창 시간) x 50% 데미지 증가
     *
     * @param totalBarrierChantTime 전개된 결계들의 총 영창 시간
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double territory(int totalBarrierChantTime, PrintStream out) {
        double multiplier = 1.0 + (totalBarrierChantTime * 0.50);
        out.printf("영역 패시브: 결계 영창 %d턴 → x%.2f 데미지%n", totalBarrierChantTime, multiplier);
        return multiplier;
    }

    // ===== 결계 스킬 =====

    /**
     * 출력 결계
     * 힘, 지능 스탯 +4
     * 매턴 마나 1소모
     */
    public static void powerBarrier(PrintStream out) {
        out.println("결계술사-출력 결계 사용");
        out.println("※ 힘, 지능 스탯 +4");
        out.println("※ 매턴 마나 1 소모");
    }

    public static int powerBarrierManaCostPerTurn() {
        return 1;
    }

    /**
     * 신속 결계
     * 신속, 민첩 스탯 +4
     * 매턴 마나 1소모
     */
    public static void swiftnessBarrier(PrintStream out) {
        out.println("결계술사-신속 결계 사용");
        out.println("※ 신속, 민첩 스탯 +4");
        out.println("※ 매턴 마나 1 소모");
    }

    public static int swiftnessBarrierManaCostPerTurn() {
        return 1;
    }

    /**
     * 강화 결계
     * (결계 영창시간) x 40% 데미지 증가
     * (휴식 시 제거)
     * 매턴 마나 4소모
     *
     * @param chantTime 결계 영창 시간
     * @param out 출력 스트림
     * @return 데미지 배율
     */
    public static double enhancementBarrier(int chantTime, PrintStream out) {
        out.println("결계술사-강화 결계 사용");
        double multiplier = 1.0 + (chantTime * 0.40);
        out.printf("영창 %d턴 → 데미지 x%.2f%n", chantTime, multiplier);
        out.println("※ 휴식 시 제거");
        out.println("※ 매턴 마나 4 소모");
        return multiplier;
    }

    public static int enhancementBarrierManaCostPerTurn() {
        return 4;
    }

    /**
     * 중력 결계
     * 결계 지속 중 행동 불가
     * 받는 피해 400% 증가
     * 매턴 마나 5소모
     */
    public static void gravityBarrier(PrintStream out) {
        out.println("결계술사-중력 결계 사용");
        out.println("※ 대상 행동 불가");
        out.println("※ 대상 받는 피해 400%");
        out.println("※ 매턴 마나 5 소모");
    }

    public static double gravityBarrierDamageMultiplier() {
        return 4.0;
    }

    public static int gravityBarrierManaCostPerTurn() {
        return 5;
    }

    /**
     * 봉인 결계
     * 기술, 스킬 봉인
     * 데미지 200% 증가
     * 매턴 마나 4소모
     */
    public static void sealingBarrier(PrintStream out) {
        out.println("결계술사-봉인 결계 사용");
        out.println("※ 대상 기술, 스킬 봉인");
        out.println("※ 대상 받는 피해 200%");
        out.println("※ 매턴 마나 4 소모");
    }

    public static double sealingBarrierDamageMultiplier() {
        return 2.0;
    }

    public static int sealingBarrierManaCostPerTurn() {
        return 4;
    }

    /**
     * 분신 결계
     * 매턴 2회 추가 행동
     * 데미지 50% 감소
     * 매턴 마나 2소모
     */
    public static void cloneBarrier(PrintStream out) {
        out.println("결계술사-분신 결계 사용");
        out.println("※ 매턴 2회 추가 행동");
        out.println("※ 데미지 50%");
        out.println("※ 매턴 마나 2 소모");
    }

    public static double cloneBarrierDamageMultiplier() {
        return 0.5;
    }

    public static int cloneBarrierManaCostPerTurn() {
        return 2;
    }

    /**
     * 역장 결계
     * 매턴 D6 보호막 생성
     * 매턴 마나 3소모
     *
     * @param out 출력 스트림
     * @return 보호막 양
     */
    public static int forceFieldBarrier(PrintStream out) {
        out.println("결계술사-역장 결계 사용");
        int shield = Main.dice(1, 6, out);
        out.printf("보호막 %d 생성%n", shield);
        out.println("※ 매턴 마나 3 소모");
        return shield;
    }

    public static int forceFieldBarrierManaCostPerTurn() {
        return 3;
    }

    /**
     * 결계 잔영
     * 이전 사용한 결계를 선택해 다음턴까지 자신과 1명의 대상에게 발동
     * (복수의 결계 선택 가능)
     * 마나[선택한 결계의 수] x 3 쿨타임6턴
     *
     * @param selectedBarrierCount 선택한 결계 개수
     * @param out 출력 스트림
     * @return 소모 마나
     */
    public static int barrierAfterimage(int selectedBarrierCount, PrintStream out) {
        out.println("결계술사-결계 잔영 사용");
        int manaCost = selectedBarrierCount * 3;
        out.printf("선택한 결계 %d개 → 다음턴까지 자신과 대상 1명에게 발동%n", selectedBarrierCount);
        out.printf("마나 %d 소모, 쿨타임 6턴%n", manaCost);
        return manaCost;
    }

    /**
     * 기운 회수
     * 1개의 결계를 해제
     * 해당 결계에 소모된 마나 50% 회수
     * 마나2 쿨타임 7턴
     *
     * @param manaSpentOnBarrier 결계에 소모된 마나
     * @param out 출력 스트림
     * @return 회수할 마나
     */
    public static int energyRecovery(int manaSpentOnBarrier, PrintStream out) {
        out.println("결계술사-기운 회수 사용");
        int manaRecovered = manaSpentOnBarrier / 2;
        out.printf("결계 해제 → 마나 %d 회수%n", manaRecovered);
        out.println("※ 마나 2 소모, 쿨타임 7턴");
        return manaRecovered;
    }
}

