package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;

/**
 * 소환술사의 소환수 클래스
 * <p>
 * 소환술사가 소환할 수 있는 소환수들과 그 스킬들을 구현합니다.
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 * <p>
 * 소환수 목록:
 * <ul>
 *   <li>[초급] 코볼린 (HP: 120), 로룸 (HP: 150), 무므르 (HP: 90)</li>
 *   <li>[중등] 샤데린 (HP: 140), 파그리온 (HP: 170), 루벨린 (HP: 160)</li>
 *   <li>[고위] 골렘 나이트 (HP: 270), 바실로스 (HP: 320)</li>
 *   <li>[특급] 텐브락스 (HP: 860), 에레블리온 (HP: 970), 모르드라크 (HP: 1620), 켈프로스 (HP: 1080)</li>
 *   <li>[초월] 카오폴리스 (HP: 1860), 라그나로크 (HP: 1480), 케로베로스 (HP: 2320),
 *             데스바크 (HP: 2760), 모노슬래셔 (HP: 4680), 카르나이츠 (HP: 2820)</li>
 *   <li>[전능] 루인 (HP: 4820)</li>
 * </ul>
 */
public class SummonerMinion {

    /**
     * 소환수 등급
     */
    public enum Rank {
        NOVICE("초급"),
        INTERMEDIATE("중등"),
        ADVANCED("고위"),
        SPECIAL("특급"),
        TRANSCENDENT("초월"),
        OMNIPOTENT("전능");

        private final String korName;

        Rank(String korName) {
            this.korName = korName;
        }

        public String getKorName() {
            return korName;
        }

        /**
         * 유대감 패시브 - 등급별 데미지 증가량(%) 반환
         * (초급 5% / 중등 10% / 고위 15% / 특급 20% / 초월 25% / 전능 50%)
         */
        public int getBondBonus() {
            return switch (this) {
                case NOVICE -> 5;
                case INTERMEDIATE -> 10;
                case ADVANCED -> 15;
                case SPECIAL -> 20;
                case TRANSCENDENT -> 25;
                case OMNIPOTENT -> 50;
            };
        }

        /**
         * 소환수 마나 유지 비용 (매 턴) 반환
         * (초급 0, 중등 1, 고위 2, 특급 3, 초월 3, 전능 5)
         */
        public int getManaUpkeep() {
            return switch (this) {
                case NOVICE -> 0;
                case INTERMEDIATE -> 1;
                case ADVANCED -> 2;
                case SPECIAL -> 3;
                case TRANSCENDENT -> 3;
                case OMNIPOTENT -> 5;
            };
        }
    }

    // ═══════════════════════════════════════════════════════════════
    // [초급] 소환수
    // ═══════════════════════════════════════════════════════════════

    // ─── 코볼린 (HP: 120) ───────────────────────────────────────────

    /**
     * 코볼린 - 돌맹이 투척 (D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result koblinStoneThrow(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("코볼린-돌맹이 투척 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "돌맹이 투척", precision, out, diceRoll);
    }

    /**
     * 코볼린 - 허약한 반격 (공격 받을 시 D4)
     * 공격을 받은 직후 반응으로 발동됩니다.
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result koblinWeakCounterattack(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("코볼린-허약한 반격 발동 (공격 받을 시)");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 4, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "허약한 반격", precision, out, diceRoll);
    }

    /**
     * 코볼린 - 작은 방패 (다음 턴까지 받는 최종 데미지 50%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result koblinSmallShield(PrintStream out) {
        out.println("코볼린-작은 방패 발동");
        out.println("효과: 다음 턴까지 받는 최종 데미지 50%");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 로룸 (HP: 150) ─────────────────────────────────────────────

    /**
     * 로룸 - 균형 타격 (2D4)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result rorumBalancedStrike(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("로룸-균형 타격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 4, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "균형 타격", precision, out, diceRoll);
    }

    /**
     * 로룸 - 보호의 기류 (모든 아군 받는 데미지 20% 감소, 중첩 불가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result rorumProtectiveAura(PrintStream out) {
        out.println("로룸-보호의 기류 발동");
        out.println("효과: 모든 아군이 받는 데미지 20% 감소 (중첩 불가)");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 로룸 - 단단한 몸 (다음 턴까지 받는 최종 데미지 50%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result rorumSturdyBody(PrintStream out) {
        out.println("로룸-단단한 몸 발동");
        out.println("효과: 다음 턴까지 받는 최종 데미지 50%");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 무므르 (HP: 90) ─────────────────────────────────────────────

    /**
     * 무므르 - 음침한 노크 (3D4)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result mumruSinisterKnock(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("무므르-음침한 노크 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 4, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "음침한 노크", precision, out, diceRoll);
    }

    /**
     * 무므르 - 미약한 흡기 (적의 모든 스탯 -1, 중첩 불가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result mumruWeakAbsorption(PrintStream out) {
        out.println("무므르-미약한 흡기 발동");
        out.println("효과: 적의 모든 스탯 -1 (중첩 불가)");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 무므르 - 사소한 방해 (다음 턴까지 적의 판정 스탯 -2)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result mumruMinorDisruption(PrintStream out) {
        out.println("무므르-사소한 방해 발동");
        out.println("효과: 다음 턴까지 적의 판정 스탯 -2");
        return new Result(0, 0, true, 0, 0);
    }

    // ═══════════════════════════════════════════════════════════════
    // [중등] 소환수
    // ═══════════════════════════════════════════════════════════════

    // ─── 샤데린 (HP: 140) ───────────────────────────────────────────

    /**
     * 샤데린 - 그림자 연속참 (3D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result shaderlinShadowSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("샤데린-그림자 연속참 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "그림자 연속참", precision, out, diceRoll);
    }

    /**
     * 샤데린 - 추적흔적 (공격한 적에게 다음 턴까지 가하는 데미지 200% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result shaderlinTrackingMark(PrintStream out) {
        out.println("샤데린-추적흔적 발동");
        out.println("효과: 공격한 적에게 다음 턴까지 가하는 데미지 200% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 샤데린 - 어둠의 걸음 (다음 턴까지 적이 가하는 데미지 50%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result shaderlinDarkStep(PrintStream out) {
        out.println("샤데린-어둠의 걸음 발동");
        out.println("효과: 다음 턴까지 적이 가하는 데미지 50%");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 파그리온 (HP: 170) ─────────────────────────────────────────

    /**
     * 파그리온 - 파편 탄성 (2D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result pagrionFragmentBounce(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("파그리온-파편 탄성 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "파편 탄성", precision, out, diceRoll);
    }

    /**
     * 파그리온 - 파편 연쇄 (5D4)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result pagrionFragmentChain(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("파그리온-파편 연쇄 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(5, 4, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "파편 연쇄", precision, out, diceRoll);
    }

    /**
     * 파그리온 - 파편 폭발 (6D4)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result pagrionFragmentExplosion(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("파그리온-파편 폭발 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(6, 4, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "파편 폭발", precision, out, diceRoll);
    }

    // ─── 루벨린 (HP: 160) ───────────────────────────────────────────

    /**
     * 루벨린 - 이중 할퀴기 (2D12)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result ruvelinDoubleSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루벨린-이중 할퀴기 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 12, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "이중 할퀴기", precision, out, diceRoll);
    }

    /**
     * 루벨린 - 야수의 위협 (다음 턴까지 적의 스탯 -2)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result ruvelinBeastThreat(PrintStream out) {
        out.println("루벨린-야수의 위협 발동");
        out.println("효과: 다음 턴까지 적의 스탯 -2");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 루벨린 - 돌진 분쇄 (5D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result ruvelinRushCrush(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루벨린-돌진 분쇄 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(5, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "돌진 분쇄", precision, out, diceRoll);
    }

    // ═══════════════════════════════════════════════════════════════
    // [고위] 소환수
    // ═══════════════════════════════════════════════════════════════

    // ─── 골렘 나이트 (HP: 270) ───────────────────────────────────────

    /**
     * 골렘 나이트 - 강철 망치 강타 (D20)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result golemKnightHammerStrike(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("골렘 나이트-강철 망치 강타 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 20, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "강철 망치 강타", precision, out, diceRoll);
    }

    /**
     * 골렘 나이트 - 급속 파편 분출 (3D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result golemKnightRapidFragmentBurst(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("골렘 나이트-급속 파편 분출 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "급속 파편 분출", precision, out, diceRoll);
    }

    /**
     * 골렘 나이트 - 고체 압축 (다음 턴 가하는 데미지 150% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result golemKnightSolidCompression(PrintStream out) {
        out.println("골렘 나이트-고체 압축 발동");
        out.println("효과: 다음 턴 가하는 데미지 150% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 골렘 나이트 - 과부화 충격 (다음 턴 받는 피해 150% 증가, 가하는 데미지 200%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result golemKnightOverloadShock(PrintStream out) {
        out.println("골렘 나이트-과부화 충격 발동");
        out.println("효과: 다음 턴 받는 피해 150% 증가, 가하는 데미지 200%");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 바실로스 (HP: 320) ─────────────────────────────────────────

    /**
     * 바실로스 - 맹독 절단 (3D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result basilosPoisonCut(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("바실로스-맹독 절단 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "맹독 절단", precision, out, diceRoll);
    }

    /**
     * 바실로스 - 석화 충격 (4D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result basilosPetrifyShock(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("바실로스-석화 충격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "석화 충격", precision, out, diceRoll);
    }

    /**
     * 바실로스 - 굴절 참격 (2D10)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result basilosRefractSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("바실로스-굴절 참격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 10, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "굴절 참격", precision, out, diceRoll);
    }

    /**
     * 바실로스 - 왕의 권위 (다음 3턴까지 소환사의 모든 스탯 +2)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result basilosKinglyAuthority(PrintStream out) {
        out.println("바실로스-왕의 권위 발동");
        out.println("효과: 다음 3턴까지 소환사의 모든 스탯 +2");
        return new Result(0, 0, true, 0, 0);
    }

    // ═══════════════════════════════════════════════════════════════
    // [특급] 소환수
    // ═══════════════════════════════════════════════════════════════

    // ─── 텐브락스 (HP: 860) ─────────────────────────────────────────

    /**
     * 텐브락스 - 어둠 암살 (전 턴에 공격하지 않았다면 공격 발동, 3D12)
     *
     * @param stat                 판정 스탯
     * @param damageBonus          데미지 증가 % (덧셈 보정)
     * @param finalMult            최종 데미지 배율 (곱셈 보정)
     * @param didNotAttackLastTurn 전 턴에 공격하지 않았는지 여부
     * @param precision            정밀 스탯
     * @param out                  출력 스트림
     * @return 결과 객체
     */
    public static Result tenbraksDarkAssassinate(int stat, int damageBonus, double finalMult,
                                                 boolean didNotAttackLastTurn, int precision, PrintStream out) {
        out.println("텐브락스-어둠 암살 사용");
        if (!didNotAttackLastTurn) {
            out.println("발동 실패: 전 턴에 공격을 수행했습니다.");
            return new Result(0, 0, false, 0, 0);
        }
        out.println("전 턴 공격 없음: 어둠 암살 발동!");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 12, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "어둠 암살", precision, out, diceRoll);
    }

    /**
     * 텐브락스 - 절멸하는 중력 (4D6, 다음 턴 적 행동 불가)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result tenbraksCrushingGravity(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("텐브락스-절멸하는 중력 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 6, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "절멸하는 중력", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴 적 행동 불가");
        return result;
    }

    /**
     * 텐브락스 - 심연의 파열 (다음 턴 가하는 데미지 200% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result tenbraksAbyssRupture(PrintStream out) {
        out.println("텐브락스-심연의 파열 발동");
        out.println("효과: 다음 턴 가하는 데미지 200% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 텐브락스 - 흑염 개방 (다음 턴 받는 데미지 150% 증가, 가하는 피해 300% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result tenbraksBlackFlameRelease(PrintStream out) {
        out.println("텐브락스-흑염 개방 발동");
        out.println("효과: 다음 턴 받는 데미지 150% 증가, 가하는 피해 300% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 에레블리온 (HP: 970) ───────────────────────────────────────

    /**
     * 에레블리온 - 침식일섬 (2D20)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result erevlionErosionSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("에레블리온-침식일섬 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 20, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "침식일섬", precision, out, diceRoll);
    }

    /**
     * 에레블리온 - 암흑흡혈 (3D12, 데미지의 50% 체력 회복)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체 (damageDealt = 적에게 입힌 피해; 회복량은 damageDealt / 2)
     */
    public static Result erevlionDarkBloodSuck(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("에레블리온-암흑흡혈 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 12, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "암흑흡혈", precision, out, diceRoll);
        int healAmount = result.damageDealt() / 2;
        out.printf("암흑흡혈 체력 회복: 데미지 %d의 50%% = %d%n", result.damageDealt(), healAmount);
        return result;
    }

    /**
     * 에레블리온 - 심야속의 포효 (다음 턴까지 적의 판정 스탯 -3)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result erevlionMidnightRoar(PrintStream out) {
        out.println("에레블리온-심야속의 포효 발동");
        out.println("효과: 다음 턴까지 적의 판정 스탯 -3");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 에레블리온 - 심연과의 계약 (받는 데미지 100% 증가, 가하는 데미지 300% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result erevlionAbyssContract(PrintStream out) {
        out.println("에레블리온-심연과의 계약 발동");
        out.println("효과: 받는 데미지 100% 증가, 가하는 데미지 300% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 모르드라크 (HP: 1620) ──────────────────────────────────────

    /**
     * 모르드라크 - 분해흉참 (4D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result mordrakeDecomposeSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모르드라크-분해흉참 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "분해흉참", precision, out, diceRoll);
    }

    /**
     * 모르드라크 - 저주의 숨결 (5D4, 다음 턴까지 적이 받는 데미지 150% 증가)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result mordrakeCurseBreath(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모르드라크-저주의 숨결 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(5, 4, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "저주의 숨결", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴까지 적이 받는 데미지 150% 증가");
        return result;
    }

    /**
     * 모르드라크 - 파열 행진 (6D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result mordrakeRuptureMarch(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모르드라크-파열 행진 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(6, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "파열 행진", precision, out, diceRoll);
    }

    /**
     * 모르드라크 - 흉조 방출 (4D10, 다음 턴 가하는 피해 50% 감소)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result mordrakeOmenRelease(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모르드라크-흉조 방출 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 10, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "흉조 방출", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴 가하는 피해 50% 감소");
        return result;
    }

    // ─── 켈프로스 (HP: 1080) ────────────────────────────────────────

    /**
     * 켈프로스 - 불안정한 참극 (4D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result kelprosUnstableMassacre(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("켈프로스-불안정한 참극 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "불안정한 참극", precision, out, diceRoll);
    }

    /**
     * 켈프로스 - 균열 분출 (6D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result kelprosFissureBlast(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("켈프로스-균열 분출 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(6, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "균열 분출", precision, out, diceRoll);
    }

    /**
     * 켈프로스 - 파편 동요 (다음 턴 가하는 데미지 200% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result kelprosFragmentAgitation(PrintStream out) {
        out.println("켈프로스-파편 동요 발동");
        out.println("효과: 다음 턴 가하는 데미지 200% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 켈프로스 - 조율 파멸 (다음 턴까지 적의 모든 데미지 보정 제거)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result kelprosHarmonyRuin(PrintStream out) {
        out.println("켈프로스-조율 파멸 발동");
        out.println("효과: 다음 턴까지 적의 모든 데미지 보정 제거");
        return new Result(0, 0, true, 0, 0);
    }

    // ═══════════════════════════════════════════════════════════════
    // [초월] 소환수
    // ═══════════════════════════════════════════════════════════════

    // ─── 카오폴리스 (HP: 1860) ──────────────────────────────────────

    /**
     * 카오폴리스 - 혼돈 충돌 (6D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result chaopolisChaoticCollision(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카오폴리스-혼돈 충돌 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(6, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "혼돈 충돌", precision, out, diceRoll);
    }

    /**
     * 카오폴리스 - 압파 (4D10)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result chaopolisWaveForce(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카오폴리스-압파 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 10, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "압파", precision, out, diceRoll);
    }

    /**
     * 카오폴리스 - 난폭진동 (3D12, 다음 턴까지 적이 가하는 피해 50% 감소)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result chaopolisViolentVibration(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카오폴리스-난폭진동 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 12, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "난폭진동", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴까지 적이 가하는 피해 50% 감소");
        return result;
    }

    /**
     * 카오폴리스 - 혼돈 벼락 (4D8, 다음 턴까지 적이 받는 피해 150% 증가)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result chaopolisChaoticLightning(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카오폴리스-혼돈 벼락 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(4, 8, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "혼돈 벼락", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴까지 적이 받는 피해 150% 증가");
        return result;
    }

    /**
     * 카오폴리스 - 파멸 폭발 (8D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result chaopolisRuinExplosion(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카오폴리스-파멸 폭발 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(8, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "파멸 폭발", precision, out, diceRoll);
    }

    /**
     * 카오폴리스 - 혼돈의 진조 (다음 턴까지 가하는 최종 데미지 300%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result chaopolisChaoticOmen(PrintStream out) {
        out.println("카오폴리스-혼돈의 진조 발동");
        out.println("효과: 다음 턴까지 가하는 최종 데미지 300%");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 라그나로크 (HP: 1480) ──────────────────────────────────────

    /**
     * 라그나로크 - 멸망참격 (2D20)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result ragnarokAnnihilationSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("라그나로크-멸망참격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(2, 20, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "멸망참격", precision, out, diceRoll);
    }

    /**
     * 라그나로크 - 종말의 폭발 (5D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result ragnarokDoomExplosion(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("라그나로크-종말의 폭발 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(5, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "종말의 폭발", precision, out, diceRoll);
    }

    /**
     * 라그나로크 - 파멸의 굉음 (3D10, 다음 턴까지 적이 받는 데미지 150%)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result ragnarokRuinRoar(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("라그나로크-파멸의 굉음 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(3, 10, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "파멸의 굉음", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴까지 적이 받는 데미지 150%");
        return result;
    }

    /**
     * 라그나로크 - 붕괴 폭주 (영구적으로 받는 최종 데미지 150%, 가하는 데미지 200%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result ragnarokCollapseRunaway(PrintStream out) {
        out.println("라그나로크-붕괴 폭주 발동");
        out.println("효과: 영구적으로 받는 최종 데미지 150%, 가하는 데미지 200%");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 라그나로크 - 라그나로크 (사용시 소환수 사망, (5D20) x 10, 모든 적에게 피해)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체 (사용 후 소환수 사망)
     */
    public static Result ragnarokUltimate(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("라그나로크-라그나로크 사용! (소환수 사망)");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(5, 20, out) * 10;
        out.printf("라그나로크 기본 데미지: (5D20) x 10 = %d%n", baseDamage);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "라그나로크", precision, out, diceRoll);
        out.println("해당 공격은 모든 적에게 피해를 입힙니다. 사용 후 소환수 사망.");
        return result;
    }

    /**
     * 라그나로크 - 멸망의 존재 (자신을 제외한 모두에게 행동불가 부여)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result ragnarokExistenceOfRuin(PrintStream out) {
        out.println("라그나로크-멸망의 존재 발동");
        out.println("효과: 자신을 제외한 모두에게 행동불가 부여");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 케로베로스 (HP: 2320) ──────────────────────────────────────

    /**
     * 케로베로스 - 삼두절단 (3D10 + 3D12 + 3D20, 다음 턴 최종 데미지 30% 감소)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result cerberusTripleHeadSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("케로베로스-삼두절단 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int d1 = Main.dice(3, 10, out);
        int d2 = Main.dice(3, 12, out);
        int d3 = Main.dice(3, 20, out);
        int baseDamage = d1 + d2 + d3;
        out.printf("삼두절단 기본 데미지: 3D10(%d) + 3D12(%d) + 3D20(%d) = %d%n", d1, d2, d3, baseDamage);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "삼두절단", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴 최종 데미지 30% 감소");
        return result;
    }

    /**
     * 케로베로스 - 지옥의 포효 (전장의 모두에게 최종 데미지 200% 부여, 전장의 모두가 다음 턴 피아식별 불가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result cerberusHellRoar(PrintStream out) {
        out.println("케로베로스-지옥의 포효 발동");
        out.println("효과: 전장의 모두에게 최종 데미지 200% 부여, 전장의 모두가 다음 턴 피아식별 불가");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 케로베로스 - 연속돌진 (D6 + D8 + D10 + D12 + D20 + D20)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result cerberusRapidCharge(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("케로베로스-연속돌진 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int d1 = Main.dice(1, 6, out);
        int d2 = Main.dice(1, 8, out);
        int d3 = Main.dice(1, 10, out);
        int d4 = Main.dice(1, 12, out);
        int d5 = Main.dice(1, 20, out);
        int d6 = Main.dice(1, 20, out);
        int baseDamage = d1 + d2 + d3 + d4 + d5 + d6;
        out.printf("연속돌진 기본 데미지: D6(%d)+D8(%d)+D10(%d)+D12(%d)+D20(%d)+D20(%d) = %d%n",
                d1, d2, d3, d4, d5, d6, baseDamage);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "연속돌진", precision, out, diceRoll);
    }

    /**
     * 케로베로스 - 심문 (랜덤 대상에게 적용, 이전 턴에 피해를 입혔다면 행동불가 2회 부여)
     *
     * @param targetAttackedLastTurn 이전 턴에 대상이 피해를 입혔는지 여부
     * @param out                    출력 스트림
     * @return 결과 객체 (succeeded = 행동불가 효과 부여 여부)
     */
    public static Result cerberusInterrogation(boolean targetAttackedLastTurn, PrintStream out) {
        out.println("케로베로스-심문 발동 (랜덤 대상에게 적용)");
        if (targetAttackedLastTurn) {
            out.println("대상이 이전 턴에 피해를 입힘: 행동불가 2회 부여");
        } else {
            out.println("대상이 이전 턴에 피해를 입히지 않음: 효과 없음");
        }
        return new Result(0, 0, targetAttackedLastTurn, 0, 0);
    }

    /**
     * 케로베로스 - 물고 찢고 절단하기 (4D4 + 2D10 + 2D20)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result cerberusBiteTearCut(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("케로베로스-물고 찢고 절단하기 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int d1 = Main.dice(4, 4, out);
        int d2 = Main.dice(2, 10, out);
        int d3 = Main.dice(2, 20, out);
        int baseDamage = d1 + d2 + d3;
        out.printf("물고 찢고 절단하기 기본 데미지: 4D4(%d) + 2D10(%d) + 2D20(%d) = %d%n", d1, d2, d3, baseDamage);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "물고 찢고 절단하기", precision, out, diceRoll);
    }

    /**
     * 케로베로스 - 지옥의 파수꾼 (재사용시까지 피아식별 불가 공격의 대상으로 지정)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result cerberusHellGuardian(PrintStream out) {
        out.println("케로베로스-지옥의 파수꾼 발동");
        out.println("효과: 재사용시까지 피아식별 불가 공격의 대상으로 지정됨");
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 데스바크 (HP: 2760) ────────────────────────────────────────

    /**
     * 데스바크 - 부식갈퀴 (D10 + 15D4)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result deathbarkCorrosiveClaw(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("데스바크-부식갈퀴 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int d1 = Main.dice(1, 10, out);
        int d2 = Main.dice(15, 4, out);
        int baseDamage = d1 + d2;
        out.printf("부식갈퀴 기본 데미지: D10(%d) + 15D4(%d) = %d%n", d1, d2, baseDamage);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "부식갈퀴", precision, out, diceRoll);
    }

    /**
     * 데스바크 - 사령흡수 (8D6, 데미지만큼 자신의 체력 회복)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체 (damageDealt = 적 피해 및 소환수 회복량)
     */
    public static Result deathbarkDeathAbsorb(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("데스바크-사령흡수 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(8, 6, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "사령흡수", precision, out, diceRoll);
        out.printf("사령흡수 체력 회복: %d (데미지만큼 회복)%n", result.damageDealt());
        return result;
    }

    /**
     * 데스바크 - 암흑확산 (자신 제외 모두에게 다음 턴 행동불가, 자신은 다음 턴 피아식별 불가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result deathbarkDarkSpread(PrintStream out) {
        out.println("데스바크-암흑확산 발동");
        out.println("효과: 자신 제외 모두에게 다음 턴 행동불가 부여, 자신은 다음 턴 피아식별 불가 상태가 됨");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 데스바크 - 영혼찢기 (9D6, 잃은 체력 x 3% 데미지 증가)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param lostHp      소환수가 현재까지 잃은 체력
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result deathbarkSoulRend(int stat, int damageBonus, double finalMult, int lostHp, int precision, PrintStream out) {
        out.println("데스바크-영혼찢기 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(9, 6, out);
        int lostHpBonus = lostHp * 3;
        out.printf("영혼찢기 데미지 증가: 잃은 체력 %d x 3%% = %d%%%n", lostHp, lostHpBonus);
        return applyDamage(baseDamage, damageBonus + lostHpBonus, finalMult, stat, "영혼찢기", precision, out, diceRoll);
    }

    /**
     * 데스바크 - 망령울음 (전투 내 사망한 유닛이 있다면 이번 턴 최종 데미지 500%로 부활)
     *
     * @param deadUnitsInBattle 전투 내 사망한 유닛 수
     * @param out               출력 스트림
     * @return 결과 객체 (succeeded = 효과 발동 여부)
     */
    public static Result deathbarkWraithCry(int deadUnitsInBattle, PrintStream out) {
        out.println("데스바크-망령울음 발동");
        if (deadUnitsInBattle > 0) {
            out.printf("전투 내 %d명 사망 확인: 이번 턴 최종 데미지 500%%로 부활%n", deadUnitsInBattle);
            return new Result(0, 0, true, 0, 0);
        } else {
            out.println("전투 내 사망한 유닛 없음: 효과 없음");
            return new Result(0, 0, false, 0, 0);
        }
    }

    /**
     * 데스바크 - 죽음의 울림 (다음 턴 최종 데미지 (사망한 유닛 수)배)
     *
     * @param deadUnitsInBattle 전투 내 사망한 유닛 수
     * @param out               출력 스트림
     * @return 결과 객체
     */
    public static Result deathbarkDeathResonance(int deadUnitsInBattle, PrintStream out) {
        out.println("데스바크-죽음의 울림 발동");
        out.printf("효과: 다음 턴 최종 데미지 %d배 (전투 내 사망한 유닛 수: %d)%n", deadUnitsInBattle, deadUnitsInBattle);
        return new Result(0, 0, true, 0, 0);
    }

    // ─── 모노슬래셔 (HP: 4680) ──────────────────────────────────────

    /**
     * 모노슬래셔 - 단독연참 (10D4 + D45)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result monoslasherSoloSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모노슬래셔-단독연참 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int d1 = Main.dice(10, 4, out);
        int d2 = Main.dice(1, 45, out);
        int baseDamage = d1 + d2;
        out.printf("단독연참 기본 데미지: 10D4(%d) + D45(%d) = %d%n", d1, d2, baseDamage);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "단독연참", precision, out, diceRoll);
    }

    /**
     * 모노슬래셔 - 고속섬경 (9D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result monoslasherHighSpeedFlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모노슬래셔-고속섬경 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(9, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "고속섬경", precision, out, diceRoll);
    }

    /**
     * 모노슬래셔 - 그림자이탈 (모든 아군에게 1회 무적 부여)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result monoslasherShadowEscape(PrintStream out) {
        out.println("모노슬래셔-그림자이탈 발동");
        out.println("효과: 모든 아군에게 1회 무적 부여");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 모노슬래셔 - 일도난무 (10D2 + D200)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result monoslasherSlashBarrage(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모노슬래셔-일도난무 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int d1 = Main.dice(10, 2, out);
        int d2 = Main.dice(1, 200, out);
        int baseDamage = d1 + d2;
        out.printf("일도난무 기본 데미지: 10D2(%d) + D200(%d) = %d%n", d1, d2, baseDamage);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "일도난무", precision, out, diceRoll);
    }

    /**
     * 모노슬래셔 - 절단돌파 (14D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result monoslasherSlashBreakthrough(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("모노슬래셔-절단돌파 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(14, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "절단돌파", precision, out, diceRoll);
    }

    /**
     * 모노슬래셔 - 고독의 여정 (자신에게 최대체력의 D20% 피해, 영구적으로 최종 데미지 200% 중첩 가능)
     *
     * @param maxHp 소환수의 최대 체력
     * @param out   출력 스트림
     * @return 결과 객체 (damageTaken = 소환수 자신이 받은 피해)
     */
    public static Result monoslasherSolitaryJourney(int maxHp, PrintStream out) {
        out.println("모노슬래셔-고독의 여정 발동");
        int selfDamagePercent = Main.dice(1, 20, out);
        int selfDamage = (int)(maxHp * selfDamagePercent / 100.0);
        out.printf("자신에게 최대체력(%d)의 %d%% = %d 피해%n", maxHp, selfDamagePercent, selfDamage);
        out.println("효과: 영구적으로 최종 데미지 200% 중첩 가능");
        return new Result(selfDamage, 0, true, 0, 0);
    }

    // ─── 카르나이츠 (HP: 2820) ──────────────────────────────────────

    /**
     * 카르나이츠 - 절단폭풍 (25D4)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result karnaitzSlashStorm(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카르나이츠-절단폭풍 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(25, 4, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "절단폭풍", precision, out, diceRoll);
    }

    /**
     * 카르나이츠 - 고속회전참 (12D8)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result karnaitzHighSpeedSpinSlash(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카르나이츠-고속회전참 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(12, 8, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "고속회전참", precision, out, diceRoll);
    }

    /**
     * 카르나이츠 - 칼날진격 (7D8, 다음 턴 2회 행동)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result karnaitzBladeCharge(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카르나이츠-칼날진격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(7, 8, out);
        Result result = applyDamage(baseDamage, damageBonus, finalMult, stat, "칼날진격", precision, out, diceRoll);
        out.println("추가 효과: 다음 턴 2회 행동");
        return result;
    }

    /**
     * 카르나이츠 - 살육연무 (9D12)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result karnaitzMassacreMist(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카르나이츠-살육연무 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(9, 12, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "살육연무", precision, out, diceRoll);
    }

    /**
     * 카르나이츠 - 회전단층파 (D250)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result karnaitzSpinFaultWave(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("카르나이츠-회전단층파 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 250, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "회전단층파", precision, out, diceRoll);
    }

    /**
     * 카르나이츠 - 소멸의 칼날 (다음 턴부터 피해를 입히는 데 실패하는 턴까지 데미지 300% 증가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result karnaitzVanishingBlade(PrintStream out) {
        out.println("카르나이츠-소멸의 칼날 발동");
        out.println("효과: 다음 턴부터 자신이 피해를 입히는 데 실패하는 턴까지 데미지 300% 증가");
        return new Result(0, 0, true, 0, 0);
    }

    // ═══════════════════════════════════════════════════════════════
    // [전능] 소환수
    // ═══════════════════════════════════════════════════════════════

    // ─── 루인 (HP: 4820) ────────────────────────────────────────────

    /**
     * 루인 - 죽음의 저주 (다음 턴 피해를 입힌다면 D370의 피해를 추가로 입힘)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result luinDeathCurse(PrintStream out) {
        out.println("루인-죽음의 저주 발동");
        out.println("효과: 다음 턴 피해를 입힌다면 D370의 피해를 추가로 입힘");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 루인 - 죽음의 저주 추가 피해 발동 (다음 턴 피해를 입혔을 때 추가로 발동)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result luinDeathCurseProc(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루인-죽음의 저주 추가 피해 발동");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 370, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "죽음의 저주 추가 피해", precision, out, diceRoll);
    }

    /**
     * 루인 - 영혼의 군주 (다음 턴까지 피해를 입지 않으면 영구적으로 최종 데미지 200%)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result luinSoulLord(PrintStream out) {
        out.println("루인-영혼의 군주 발동");
        out.println("효과: 다음 턴까지 피해를 입지 않으면 영구적으로 최종 데미지 200%");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 루인 - 침잠하는 영혼 (해당 적 다음 턴까지 받는 최종 데미지 300%, 행동불가)
     *
     * @param out 출력 스트림
     * @return 결과 객체
     */
    public static Result luinSubsidingSoul(PrintStream out) {
        out.println("루인-침잠하는 영혼 발동");
        out.println("효과: 해당 적은 다음 턴까지 받는 최종 데미지 300%, 행동불가 부여");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 루인 - 소멸 (10D12)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result luinAnnihilation(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루인-소멸 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(10, 12, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "소멸", precision, out, diceRoll);
    }

    /**
     * 루인 - 혼돈 (18D6)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result luinChaos(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루인-혼돈 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(18, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "혼돈", precision, out, diceRoll);
    }

    /**
     * 루인 - 파멸 (9D20)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result luinRuin(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루인-파멸 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(9, 20, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "파멸", precision, out, diceRoll);
    }

    /**
     * 루인 - 침식 (D(D20 x 20): 1D20을 굴린 후 그 값 x 20이 면수가 됨)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result luinErosion(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("루인-침식 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int sidesRoll = Main.dice(1, 20, out);
        int sides = sidesRoll * 20;
        out.printf("침식: D20 결과 %d → 면수 = %d x 20 = %d%n", sidesRoll, sidesRoll, sides);
        int baseDamage = Main.dice(1, sides, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "침식", precision, out, diceRoll);
    }

    /**
     * 루인 - 죽음 (사용 후 3턴 내에 소환수 사망시 전투 참여 모든 인원 즉사, 수비 불가.
     * 해당 효과를 가진 채 사망시 사역 불가.
     * 체력이 3000을 초과하는 대상에게는 200D200으로 적용됨)
     *
     * @param stat        판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정; 3000 초과 대상 전용)
     * @param finalMult   최종 데미지 배율 (곱셈 보정; 3000 초과 대상 전용)
     * @param targetHp    공격 대상의 현재 체력 (3000 초과이면 200D200 적용)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result luinDeath(int stat, int damageBonus, double finalMult, int targetHp, int precision, PrintStream out) {
        out.println("루인-죽음 사용");
        out.println("효과: 사용 후 3턴 내에 소환수 사망시 전투에 참여한 모든 인원 즉사, 수비 불가");
        out.println("해당 효과를 가진 채 사망시 사역할 수 없음");
        if (targetHp > 3000) {
            out.printf("대상 체력 %d > 3000: 200D200으로 적용%n", targetHp);
            int verdict = Main.verdict(stat, out);
            if (verdict <= 0) return new Result(0, 0, false, 0, 0);
            int diceRoll = stat - verdict;
            int baseDamage = Main.dice(200, 200, out);
            return applyDamage(baseDamage, damageBonus, finalMult, stat, "죽음(200D200)", precision, out, diceRoll);
        }
        return new Result(0, 0, true, 0, 0);
    }

    // ═══════════════════════════════════════════════════════════════
    // 공용 유틸리티
    // ═══════════════════════════════════════════════════════════════

    /**
     * 소환수 스킬 데미지 계산 공식:
     * [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
     *
     * @param baseDamage  주사위 기본 데미지
     * @param damageBonus 데미지 증가 합계 (덧셈 %, 예: 20이면 +20%)
     * @param finalMult   최종 데미지 배율 (곱셈, 예: 2.0 = 200%)
     * @param stat        판정 스탯 (주사위 보정에 사용)
     * @param skillName   스킬 이름 (로그용)
     * @param precision   정밀 스탯 (치명타 판정)
     * @param out         출력 스트림
     * @param diceRoll    판정 주사위 값
     * @return 결과 객체
     */
    public static Result applyDamage(int baseDamage, int damageBonus, double finalMult,
                                     int stat, String skillName, int precision,
                                     PrintStream out, int diceRoll) {
        // Step 1: [(기본 데미지) x (100 + 데미지)%]
        int afterBonus = (int)(baseDamage * (100 + damageBonus) / 100.0);
        out.printf("[%s] 기본 데미지 %d x (100 + %d)%% = %d%n", skillName, baseDamage, damageBonus, afterBonus);

        // Step 2: x (최종 데미지)%
        int afterFinal = (int)(afterBonus * finalMult);
        out.printf("[%s] 최종 데미지 %.2f배 적용: %d x %.2f = %d%n", skillName, finalMult, afterBonus, finalMult, afterFinal);

        // Step 3: x (주사위 보정)
        int sideDamage = Main.sideDamage(afterFinal, stat, out, diceRoll);
        int afterSide = afterFinal + sideDamage;
        out.printf("[%s] 주사위 보정: %d + %d = %d%n", skillName, afterFinal, sideDamage, afterSide);

        // Step 4: 치명타 판정
        int finalDmg = Main.criticalHit(precision, afterSide, out);
        out.printf("[%s] 최종 데미지: %d%n", skillName, finalDmg);

        return new Result(0, finalDmg, true, 0, 0);
    }
}
