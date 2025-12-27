package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Sniper {

    /**
     * 저격수 기본공격
     * @param stat 사용할 스탯
     * @param numBuffs 정조준 패시브 (버프 개수당 25% 증가)
     * @param notAttackedFor5Turns 급소조준 패시브 (5턴 이상 미공격 시 200%)
     */
    public static int plain(int stat, int numBuffs, boolean notAttackedFor5Turns, PrintStream out) {
        out.println("저격수-기본공격 사용");
        int defaultDamage = Main.dice(1, 6, out);
        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        double multiplier = 1.0;

        // 정조준 패시브: 버프 1개당 25% 증가
        if (numBuffs > 0) {
            multiplier *= 1.0 + (numBuffs * 0.25);
            out.printf("정조준 패시브 적용: 버프 %d개 → x%.2f%n", numBuffs, 1.0 + (numBuffs * 0.25));
        }

        // 급소조준 패시브: 5턴 이상 미공격 시 200%
        if (notAttackedFor5Turns) {
            multiplier *= 2.0;
            out.println("급소조준 패시브 적용: x2.0");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 확보 기술 (버프)
     * 공격 전까지 광역 공격을 받지 않음, 스태미나 2 소모
     * (데미지 없음)
     */
    public static void secure(PrintStream out) {
        out.println("저격수-확보 사용 (버프)");
        out.println("※ 공격 전까지 광역 공격을 받지 않음");
        out.println("※ 스태미나 2 소모");
    }

    /**
     * 조립 기술 (버프)
     * 다음 공격 데미지 150%
     * (데미지 없음)
     */
    public static void assemble(PrintStream out) {
        out.println("저격수-조립 사용 (버프)");
        out.println("※ 다음 공격 데미지 150%%");
    }

    /**
     * 장전 기술 (버프)
     * 탄환 얻음, 스태미나 3 소모
     * (데미지 없음)
     */
    public static void load(PrintStream out) {
        out.println("저격수-장전 사용 (버프)");
        out.println("※ 탄환 획득");
        out.println("※ 스태미나 3 소모");
    }

    /**
     * 조준 기술 (버프)
     * 다음 공격이 적의 수비를 무시
     * (데미지 없음)
     */
    public static void aim(PrintStream out) {
        out.println("저격수-조준 사용 (버프)");
        out.println("※ 다음 공격이 적의 수비를 무시");
    }

    /**
     * 안정화 기술 (버프)
     * 다음 공격 데미지 75%, 주사위 복제 (결과 2배)
     * 마나 4, 쿨타임 10턴
     */
    public static void stabilize(PrintStream out) {
        out.println("저격수-안정화 사용 (버프)");
        out.println("※ 다음 공격 데미지 75%%");
        out.println("※ 주사위 복제 (결과 2배)");
        out.println("※ 마나 4 소모, 쿨타임 10턴");
    }

    /**
     * 몰입 기술 (버프)
     * 다음 공격 데미지 200%, 공격 전까지 수비 불가
     * 마나 4, 쿨타임 10턴
     */
    public static void immerse(PrintStream out) {
        out.println("저격수-몰입 사용 (버프)");
        out.println("※ 다음 공격 데미지 200%%");
        out.println("※ 공격 전까지 수비 불가");
        out.println("※ 마나 4 소모, 쿨타임 10턴");
    }

    /**
     * 확신 기술 (버프)
     * 2턴동안 공격 불가, 다음 공격 데미지 250%
     * 마나 3, 쿨타임 10턴
     */
    public static void confidence(PrintStream out) {
        out.println("저격수-확신 사용 (버프)");
        out.println("※ 2턴동안 공격 불가");
        out.println("※ 다음 공격 데미지 250%%");
        out.println("※ 마나 3 소모, 쿨타임 10턴");
    }

    /**
     * 신경 극대화 기술 (버프)
     * 공격 전까지 받는 데미지 200%, 피격 시 모든 버프 효과/고유 키워드 제거
     * 다음 공격 데미지 400%
     * 마나 8, 쿨타임 10턴
     */
    public static void nerveMax(PrintStream out) {
        out.println("저격수-신경 극대화 사용 (버프)");
        out.println("※ 공격 전까지 받는 데미지 200%%");
        out.println("※ 피격 시 모든 버프 효과/고유 키워드 제거");
        out.println("※ 다음 공격 데미지 400%%");
        out.println("※ 마나 8 소모, 쿨타임 10턴");
    }

    /**
     * 발사 기술 (호환성을 위한 오버로드)
     */
    public static int fire(int stat, int numBuffs, boolean noBasicAttackUsed, PrintStream out) {
        return fire(stat, numBuffs, noBasicAttackUsed, false, false, false, false, false, out);
    }

    /**
     * 발사 기술
     * 탄환 소모, 4D20, 스태미나 10 소모
     * @param stat 사용할 스탯
     * @param numBuffs 정조준 패시브 (버프 개수당 25% 증가)
     * @param noBasicAttackUsed 죽음의 탄환 패시브 (기본 공격 미사용 시 150%)
     * @param isAssembled 조립 버프 (150%)
     * @param isStabilized 안정화 버프 (75%, 주사위 복제)
     * @param isImmersed 몰입 버프 (200%)
     * @param isConfident 확신 버프 (250%)
     * @param isNerveMax 신경 극대화 버프 (400%)
     */
    public static int fire(int stat, int numBuffs, boolean noBasicAttackUsed,
                           boolean isAssembled, boolean isStabilized, boolean isImmersed,
                           boolean isConfident, boolean isNerveMax, PrintStream out) {
        out.println("저격수-발사 사용 (4D20)");
        out.println("※ 탄환 소모");
        out.println("※ 스태미나 10 소모");

        int defaultDamage = Main.dice(4, 20, out);

        // 안정화 버프: 주사위 복제 (결과 2배)
        if (isStabilized) {
            defaultDamage *= 2;
            out.printf("안정화 버프 적용 (주사위 복제): x2 = %d%n", defaultDamage);
        }

        int sideDamage = Main.sideDamage(stat, out);
        int totalDamage = defaultDamage + sideDamage;

        double multiplier = 1.0;

        // 정조준 패시브: 버프 1개당 25% 증가
        if (numBuffs > 0) {
            multiplier *= 1.0 + (numBuffs * 0.25);
            out.printf("정조준 패시브 적용: 버프 %d개 → x%.2f%n", numBuffs, 1.0 + (numBuffs * 0.25));
        }

        // 죽음의 탄환 패시브
        if (noBasicAttackUsed) {
            multiplier *= 1.5;
            out.println("죽음의 탄환 패시브 적용: x1.5");
        }

        // 조립 버프
        if (isAssembled) {
            multiplier *= 1.5;
            out.println("조립 버프 적용: x1.5");
        }

        // 안정화 버프: 데미지 75%
        if (isStabilized) {
            multiplier *= 0.75;
            out.println("안정화 버프 적용 (데미지 75%%): x0.75");
        }

        // 몰입 버프
        if (isImmersed) {
            multiplier *= 2.0;
            out.println("몰입 버프 적용: x2.0");
        }

        // 확신 버프
        if (isConfident) {
            multiplier *= 2.5;
            out.println("확신 버프 적용: x2.5");
        }

        // 신경 극대화 버프
        if (isNerveMax) {
            multiplier *= 4.0;
            out.println("신경 극대화 버프 적용: x4.0");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("최종 배율: x%.2f%n", multiplier);
        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

}

