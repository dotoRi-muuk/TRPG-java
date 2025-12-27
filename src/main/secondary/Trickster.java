package main.secondary;

import main.Main;

import java.io.PrintStream;

public class Trickster {

    /**
     * 트릭스터 기본공격 (호환성을 위한 오버로드)
     */
    public static int plain(int stat, boolean isFocusedFire, boolean isRepeatCustomer, PrintStream out) {
        return plain(stat, 0, isFocusedFire, isRepeatCustomer, false, false, out);
    }

    /**
     * 트릭스터 기본공격
     * 난사 패시브: (스탯-주사위) ≥ 3(공격 시행 횟수)라면 1회 추가 공격 (최대 10회)
     * 돌발 이벤트 패시브: (이번 턴 사용한 기술 횟수)*50% 만큼 기본 공격 데미지 증가
     * @param stat 사용할 스탯
     * @param skillUsedCount 이번 턴 사용한 기술 횟수 (돌발 이벤트)
     * @param isFocusedFire 일점사 패시브 (5회 이상 공격 시 200%)
     * @param isRepeatCustomer 단골 손님 패시브 (지난 턴 일점사 발동 시 150%)
     * @param isEventPrepared 이벤트 준비 활성화 (돌발 이벤트 50% → 100%)
     * @param isMainEvent 메인 이벤트 활성화 (난사 조건 완화, 최대 공격 제한 제거)
     */
    public static int plain(int stat, int skillUsedCount, boolean isFocusedFire, boolean isRepeatCustomer, boolean isEventPrepared, boolean isMainEvent, PrintStream out) {
        out.println("트릭스터-기본공격 사용 (난사 패시브)");

        // 돌발 이벤트 배율 계산
        double eventBonus = isEventPrepared ? 1.0 : 0.5; // 이벤트 준비 시 100%, 기본 50%
        double eventMultiplier = 1.0 + (skillUsedCount * eventBonus);
        if (skillUsedCount > 0) {
            out.printf("돌발 이벤트 패시브: 기술 %d회 x %.0f%% = +%.0f%% (총 x%.1f)%n",
                    skillUsedCount, eventBonus * 100, skillUsedCount * eventBonus * 100, eventMultiplier);
        }

        int totalDamage = 0;
        int attackCount = 1;
        int maxAttacks = isMainEvent ? Integer.MAX_VALUE : 10;

        while (attackCount <= maxAttacks) {
            out.printf("--- %d번째 공격 ---%n", attackCount);
            int baseDamage = Main.dice(1, 6, out);

            // 돌발 이벤트 적용 후 sideDamage 계산
            int damageAfterEvent = (int) (baseDamage * eventMultiplier);
            if (skillUsedCount > 0) {
                out.printf("돌발 이벤트 적용: x%.1f = %d%n", eventMultiplier, damageAfterEvent);
            }

            // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
            int sideDamage = Main.sideDamage(damageAfterEvent, stat, out);
            int attackDamage = damageAfterEvent + sideDamage;
            totalDamage += attackDamage;

            // 난사 판정
            int barrageCheck = stat - Main.dice(1, 20, out);
            int threshold = isMainEvent ? attackCount : 3 * attackCount;
            out.printf("난사 판정: %d - D20 = %d (필요: %d)%n", stat, barrageCheck, threshold);

            if (barrageCheck >= threshold) {
                out.println("난사 성공! 추가 공격");
                attackCount++;
            } else {
                out.println("난사 실패, 공격 종료");
                break;
            }
        }

        double multiplier = 1.0;

        // 일점사 패시브 (5회 이상 공격 시 200%)
        if (isFocusedFire && attackCount >= 5) {
            multiplier *= 2.0;
            out.printf("일점사 패시브 적용 (%d회 공격): x2.0%n", attackCount);
        }

        // 단골 손님 패시브
        if (isRepeatCustomer) {
            multiplier *= 1.5;
            out.println("단골 손님 패시브 적용: x1.5");
        }

        totalDamage = (int) (totalDamage * multiplier);
        out.printf("총 데미지 : %d%n", totalDamage);
        return totalDamage;
    }

    /**
     * 페이크 단검 기술 (호환성을 위한 오버로드)
     */
    public static int fakeDagger(int stat, boolean hasEventBonus, PrintStream out) {
        return fakeDagger(stat, hasEventBonus ? 1 : 0, false, out);
    }

    /**
     * 페이크 단검 기술
     * D4, 이번 턴 이후의 공격 데미지 150%, 스태미나 2 소모
     * @param stat 사용할 스탯
     * @param skillUsedCount 이번 턴 사용한 기술 횟수 (돌발 이벤트)
     * @param isEventPrepared 이벤트 준비 활성화 (돌발 이벤트 50% → 100%)
     */
    public static int fakeDagger(int stat, int skillUsedCount, boolean isEventPrepared, PrintStream out) {
        out.println("트릭스터-페이크 단검 사용 (D4)");
        int baseDamage = Main.dice(1, 4, out);

        double multiplier = 1.0;
        // 돌발 이벤트 패시브
        if (skillUsedCount > 0) {
            double eventBonus = isEventPrepared ? 1.0 : 0.5;
            multiplier = 1.0 + (skillUsedCount * eventBonus);
            out.printf("돌발 이벤트 패시브 적용: 기술 %d회 → x%.1f%n", skillUsedCount, multiplier);
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 이번 턴 이후의 공격 데미지 150%%");
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 콩알탄 기술 (호환성을 위한 오버로드)
     */
    public static int beanShot(int stat, boolean hasEventBonus, PrintStream out) {
        return beanShot(stat, hasEventBonus ? 1 : 0, false, out);
    }

    /**
     * 콩알탄 기술
     * 2D6, 스태미나 2 소모
     */
    public static int beanShot(int stat, int skillUsedCount, boolean isEventPrepared, PrintStream out) {
        out.println("트릭스터-콩알탄 사용 (2D6)");
        int baseDamage = Main.dice(2, 6, out);

        double multiplier = 1.0;
        if (skillUsedCount > 0) {
            double eventBonus = isEventPrepared ? 1.0 : 0.5;
            multiplier = 1.0 + (skillUsedCount * eventBonus);
            out.printf("돌발 이벤트 패시브 적용: x%.1f%n", multiplier);
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 기름통 투척 기술 (호환성을 위한 오버로드)
     */
    public static int oilBarrel(int stat, boolean hasEventBonus, PrintStream out) {
        return oilBarrel(stat, hasEventBonus ? 1 : 0, false, out);
    }

    /**
     * 기름통 투척 기술
     * D4, 스태미나 2 소모
     */
    public static int oilBarrel(int stat, int skillUsedCount, boolean isEventPrepared, PrintStream out) {
        out.println("트릭스터-기름통 투척 사용 (D4)");
        int baseDamage = Main.dice(1, 4, out);

        double multiplier = 1.0;
        if (skillUsedCount > 0) {
            double eventBonus = isEventPrepared ? 1.0 : 0.5;
            multiplier = 1.0 + (skillUsedCount * eventBonus);
            out.printf("돌발 이벤트 패시브 적용: x%.1f%n", multiplier);
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 라이터 투척 기술 (호환성을 위한 오버로드)
     */
    public static int lighterThrow(int stat, boolean hasEventBonus, boolean oilHit, PrintStream out) {
        return lighterThrow(stat, hasEventBonus ? 1 : 0, false, oilHit, out);
    }

    /**
     * 라이터 투척 기술
     * D8, 이번 턴 기름통 적중 시 3D6 추가, 스태미나 2 소모
     */
    public static int lighterThrow(int stat, int skillUsedCount, boolean isEventPrepared, boolean oilHit, PrintStream out) {
        out.println("트릭스터-라이터 투척 사용 (D8)");
        int baseDamage = Main.dice(1, 8, out);

        if (oilHit) {
            int bonusDamage = Main.dice(3, 6, out);
            baseDamage += bonusDamage;
            out.printf("기름통 적중! 추가 3D6: +%d%n", bonusDamage);
        }

        double multiplier = 1.0;
        if (skillUsedCount > 0) {
            double eventBonus = isEventPrepared ? 1.0 : 0.5;
            multiplier = 1.0 + (skillUsedCount * eventBonus);
            out.printf("돌발 이벤트 패시브 적용: x%.1f%n", multiplier);
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 2 소모");
        return totalDamage;
    }

    /**
     * 특대형 단검 기술 (호환성을 위한 오버로드)
     */
    public static int hugeDagger(int stat, boolean hasEventBonus, PrintStream out) {
        return hugeDagger(stat, hasEventBonus ? 1 : 0, false, false, out);
    }

    /**
     * 특대형 단검 기술
     * D20, 스태미나 3 소모
     */
    public static int hugeDagger(int stat, int skillUsedCount, boolean isEventPrepared, boolean isGiantScarActive, PrintStream out) {
        out.println("트릭스터-특대형 단검 사용 (D20)");
        int baseDamage = Main.dice(1, 20, out);

        double multiplier = 1.0;

        if (skillUsedCount > 0) {
            double eventBonus = isEventPrepared ? 1.0 : 0.5;
            multiplier *= 1.0 + (skillUsedCount * eventBonus);
            out.printf("돌발 이벤트 패시브 적용: x%.1f%n", 1.0 + (skillUsedCount * eventBonus));
        }

        // 거대한 상흔 활성화 시 이후 데미지 200%
        if (isGiantScarActive) {
            multiplier *= 2.0;
            out.println("거대한 상흔 적용: 이후 데미지 200%%");
        }

        int damageAfterPassives = (int) (baseDamage * multiplier);

        // sideDamage는 패시브와 배율 적용 후 맨 뒤에 적용
        int sideDamage = Main.sideDamage(damageAfterPassives, stat, out);
        int totalDamage = damageAfterPassives + sideDamage;

        out.printf("총 데미지 : %d%n", totalDamage);
        out.println("※ 스태미나 3 소모");
        return totalDamage;
    }

    /**
     * 파티 타임 스킬
     * 사용 후 최초로 공격한 적에게 그 다음턴까지 가하는 피해 200%
     * 마나 4, 쿨타임 5턴
     */
    public static void partyTime(PrintStream out) {
        out.println("트릭스터-파티 타임 사용");
        out.println("※ 최초로 공격한 적에게 그 다음턴까지 가하는 피해 200%%");
        out.println("※ 마나 4 소모, 쿨타임 5턴");
    }

    /**
     * 이벤트 준비 스킬
     * 이번 턴 공격 불가 (턴 소모X)
     * 다음 턴 돌발 이벤트 데미지 증가 효과 50% → 100%
     * 마나 2, 쿨타임 7턴
     */
    public static void eventPreparation(PrintStream out) {
        out.println("트릭스터-이벤트 준비 사용");
        out.println("※ 이번 턴 공격 불가 (턴 소모 없음)");
        out.println("※ 다음 턴 [돌발 이벤트] 데미지 증가 효과 50%% → 100%%");
        out.println("※ 마나 2 소모, 쿨타임 7턴");
    }

    /**
     * 거대한 상흔 스킬
     * 이번 턴 특대형 단검 적중 시 이후 데미지 200%
     * (턴 소모X) 마나 4, 쿨타임 6턴
     */
    public static void giantScar(PrintStream out) {
        out.println("트릭스터-거대한 상흔 사용");
        out.println("※ 이번 턴 [특대형 단검] 적중 시 이후 데미지 200%%");
        out.println("※ 턴 소모 없음");
        out.println("※ 마나 4 소모, 쿨타임 6턴");
    }

    /**
     * 메인 이벤트 스킬
     * 이번 턴 동안 난사 조건 완화 (3x공격횟수 → 공격횟수)
     * 기본 공격 최대 10회 제한 제거
     * (턴 소모X) 마나 7, 쿨타임 10턴
     */
    public static void mainEvent(PrintStream out) {
        out.println("트릭스터-메인 이벤트 사용");
        out.println("※ 이번 턴 [난사] 조건: 3x공격횟수 → 공격횟수");
        out.println("※ 기본 공격 최대 10회 제한 제거");
        out.println("※ 턴 소모 없음");
        out.println("※ 마나 7 소모, 쿨타임 10턴");
    }

}

