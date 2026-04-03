package main.secondary.mage;

import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.Random;

/**
 * 소환술사
 * <p>
 * 판정 사용 스탯 : 지능
 * <p>
 * 데미지 계산 공식: [(기본 데미지) x (100 + 데미지)%] x (최종 데미지)% x (주사위 보정)
 * <p>
 * 패시브:
 * <ul>
 *   <li>사역: 소환수와 전투하여 승리할 시 사역할 수 있습니다. 소환한 소환수는 매 턴 랜덤한 스킬을 사용합니다.
 *       자신의 소환수와 전투 중인 상태를 제외한 다른 때에 사망한 소환수는 다시 소환할 수 없습니다.</li>
 *   <li>유대감: 길들인 소환수의 수와 등급에 따라 데미지가 증가합니다.
 *       (보유한 소환수 1마리의 등급당 데미지 증가량: 초급 5% / 중등 10% / 고위 15% / 특급 20% / 초월 25% / 전능 50%)</li>
 *   <li>소환패: 길들인 소환수를 소환할 수 있는 [소환패]를 (레벨/2)개 가지고 전투를 시작합니다.</li>
 *   <li>계약: 인간형이 아닌 적을 자신 또는 자신의 소환수가 처치 시 해당 적을 소환수로 사용할 수 있음.
 *       (해당 소환수는 전투에 소모된 체력을 회복할 수 없습니다)</li>
 * </ul>
 * <p>
 * 스킬:
 * <ul>
 *   <li>소환: 소환수를 소환합니다. (마나 3 소모, 쿨타임 8턴)</li>
 *   <li>소환수를 이기는 주먹: 대상에게 D10의 피해를 입힙니다. (스태미나 1 소모)</li>
 *   <li>천혈: 소환중인 소환수의 현재 체력 D110%의 피해를 주고 해당 피해만큼 적에게 피해를 입힙니다. (마나 10 소모, 쿨타임 10턴)</li>
 *   <li>소환해제: 소환한 소환수를 소환 해제시킵니다. 지혜 판정 성공 시 [소환패]를 (소환 해제시킨 소환수의 체력)%개 얻습니다.</li>
 *   <li>잠깐: 소환수가 사용할 스킬을 다시 랜덤하게 선택합니다. (마나 3 소모, 쿨타임 3턴)</li>
 *   <li>지휘자: 소환수가 사용할 스킬을 선택할 수 있습니다. (마나 5 소모, 쿨타임 5턴)</li>
 *   <li>소환 증폭: 전투 내 영구적으로 해당 소환수의 최종 데미지가 (200/(현재 소환된 소환수의 수))%로 증가합니다. (마나 5 소모, 쿨타임 5턴)</li>
 *   <li>결속의 끈: 소환 후 5턴이 지난 소환수에게 사용 가능합니다. 영구적으로 소환수가 가하는 최종 데미지가 2배로 증가합니다. (마나 7 소모, 쿨타임 7턴)</li>
 *   <li>생명력 공유: 소환 가능한 소환수의 체력을 일정량 감소시키고 현재 소환된 소환수의 체력을 회복시킵니다. (소환패 1개 소모, 쿨타임 5턴)</li>
 *   <li>영혼 귀환: 3턴 동안 이전에 사망한 소환수를 다시 소환할 수 있습니다. (마나 10 소모, 쿨타임 15턴)</li>
 *   <li>원호 방어 (전용 신속): 지능 또는 지혜 판정을 진행합니다. 성공 시, 자신이나 아군이 받을 공격을 소환수가 대신 맞게 할 수 있습니다.</li>
 *   <li>소환수: 현재 소환된 소환수가 스킬을 사용합니다. 기본적으로 소환수가 가진 스킬 중 하나를 랜덤하게 선택합니다.
 *       지휘자 스킬 사용 후에는 플레이어가 직접 소환수가 사용할 스킬을 선택할 수 있습니다.</li>
 * </ul>
 */
public class Summoner {

    private static final Random RANDOM = new Random();

    // ─────────────────────────────────────────────────────────────────────────
    // 기본 공격 / 기술
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat        사용할 스탯 (지능)
     * @param damageBonus 데미지 증가 % (유대감 패시브 등, 덧셈 보정; 예: 15이면 +15%)
     * @param finalMult   최종 데미지 배율 (곱셈 보정; 예: 2.0 = 200%)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("소환술사-기본공격 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 6, out);
        return applyDamage(baseDamage, damageBonus, finalMult, stat, "기본공격", precision, out, diceRoll);
    }

    /**
     * 소환수를 이기는 주먹 : 대상에게 D10의 피해를 입힙니다. (스태미나 1 소모)
     *
     * @param stat        사용할 스탯 (지능)
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체 (스태미나 1 소모)
     */
    public static Result fistBeatingSummon(int stat, int damageBonus, double finalMult, int precision, PrintStream out) {
        out.println("소환술사-소환수를 이기는 주먹 사용");
        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 1);
        int diceRoll = stat - verdict;
        int baseDamage = Main.dice(1, 10, out);
        Result r = applyDamage(baseDamage, damageBonus, finalMult, stat, "소환수를 이기는 주먹", precision, out, diceRoll);
        return new Result(r.damageTaken(), r.damageDealt(), r.succeeded(), r.manaUsed(), 1);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 액티브 스킬
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 소환 : 소환수를 소환합니다.
     * <ul>
     *   <li>길들인 소환수를 소환할 시 [소환패] 1개를 소모합니다.</li>
     *   <li>전장의 소환수는 매 턴 소환술사의 마나를 소모합니다.
     *       (초급 0, 중등 1, 고위 2, 특급 3, 초월 3, 전능 5)</li>
     * </ul>
     * (마나 3 소모, 쿨타임 8턴)
     *
     * @param isTamed      소환할 소환수가 길들인 소환수인지 여부 (true이면 소환패 1개 소모)
     * @param minionRank   소환수 등급 ({@link SummonerMinion.Rank})
     * @param out          출력 스트림
     * @return 결과 객체 (마나 3 소모)
     */
    public static Result summon(boolean isTamed, SummonerMinion.Rank minionRank, PrintStream out) {
        out.println("소환술사-소환 사용");
        out.printf("소환수 등급: %s%n", minionRank.getKorName());
        if (isTamed) {
            out.println("[소환패] 1개 소모");
        }
        out.printf("소환수 마나 유지 비용: 매 턴 %d 마나 소모%n", minionRank.getManaUpkeep());
        out.println("마나 3 소모, 쿨타임 8턴");
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 천혈 : 소환중인 소환수의 현재 체력 D110%의 피해를 소환수에게 입히고,
     * 해당 피해만큼 적에게 피해를 입힙니다.
     * (마나 10 소모, 쿨타임 10턴)
     *
     * @param minionCurrentHp 소환중인 소환수의 현재 체력
     * @param out             출력 스트림
     * @return 결과 객체 (damageDealt = 적에게 입힌 피해, damageTaken = 소환수가 받은 피해; 마나 10 소모)
     */
    public static Result bloodSacrifice(int minionCurrentHp, PrintStream out) {
        out.println("소환술사-천혈 사용");
        int percent = Main.dice(1, 110, out);
        int damage = (int)(minionCurrentHp * percent / 100.0);
        out.printf("천혈: 소환수 현재 체력 %d x D110%%(%d%%) = %d 피해%n", minionCurrentHp, percent, damage);
        out.printf("소환수 피해: %d / 적에게도 동일한 피해: %d%n", damage, damage);
        out.println("마나 10 소모, 쿨타임 10턴");
        return new Result(damage, damage, true, 10, 0);
    }

    /**
     * 소환해제 : 소환한 소환수를 소환 해제시킵니다.
     * 이후 지혜(wisdomStat) 판정 성공 시 [소환패]를 (소환 해제시킨 소환수의 현재 체력 / 최대 체력 × 100)% 만큼 얻습니다.
     * (소환패 획득 수는 소수점 이하 버림)
     *
     * @param wisdomStat      지혜 스탯 (소환패 획득 판정)
     * @param minionCurrentHp 소환 해제할 소환수의 현재 체력
     * @param minionMaxHp     소환 해제할 소환수의 최대 체력
     * @param out             출력 스트림
     * @return 결과 객체 (succeeded = 지혜 판정 성공 여부)
     */
    public static Result desummon(int wisdomStat, int minionCurrentHp, int minionMaxHp, PrintStream out) {
        out.println("소환술사-소환해제 사용");
        out.println("소환수를 소환 해제합니다.");
        out.println("지혜 판정 시도...");
        int wisdomVerdict = Main.verdict(wisdomStat, out);
        if (wisdomVerdict > 0) {
            int cardGain = minionCurrentHp * 100 / minionMaxHp;
            out.printf("지혜 판정 성공! [소환패] %d개 획득 (소환수 잔여 체력 %d/%d = %d%%)%n",
                    cardGain, minionCurrentHp, minionMaxHp, cardGain);
            return new Result(0, 0, true, 0, 0);
        } else {
            out.println("지혜 판정 실패. [소환패]를 획득하지 못했습니다.");
            return new Result(0, 0, false, 0, 0);
        }
    }

    /**
     * 잠깐 : 소환수가 사용할 스킬을 다시 랜덤하게 선택합니다.
     * (마나 3 소모, 쿨타임 3턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 3 소모)
     */
    public static Result waitSkill(PrintStream out) {
        out.println("소환술사-잠깐 사용");
        out.println("소환수가 사용할 스킬을 다시 랜덤하게 선택합니다.");
        out.println("마나 3 소모, 쿨타임 3턴");
        return new Result(0, 0, true, 3, 0);
    }

    /**
     * 지휘자 : 소환수가 사용할 스킬을 선택할 수 있습니다.
     * (마나 5 소모, 쿨타임 5턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 5 소모)
     */
    public static Result commander(PrintStream out) {
        out.println("소환술사-지휘자 사용");
        out.println("소환수가 사용할 스킬을 선택합니다.");
        out.println("마나 5 소모, 쿨타임 5턴");
        return new Result(0, 0, true, 5, 0);
    }

    /**
     * 지휘자 : 소환수가 사용할 스킬을 선택할 수 있습니다.
     * 소환수의 사용 가능한 스킬 목록을 출력합니다.
     * (마나 5 소모, 쿨타임 5턴)
     *
     * @param minionType 현재 소환된 소환수 종류
     * @param out        출력 스트림
     * @return 결과 객체 (마나 5 소모)
     */
    public static Result commander(SummonerMinion.MinionType minionType, PrintStream out) {
        out.println("소환술사-지휘자 사용");
        out.printf("[%s] 사용 가능한 스킬 목록:%n", minionType.getKorName());
        String[] skills = SummonerMinion.getSkillNames(minionType);
        for (int i = 0; i < skills.length; i++) {
            out.printf("  [%d] %s%n", i + 1, skills[i]);
        }
        out.println("소환수가 사용할 스킬 번호를 선택하세요.");
        out.println("마나 5 소모, 쿨타임 5턴");
        return new Result(0, 0, true, 5, 0);
    }

    /**
     * 소환수 : 현재 소환된 소환수가 스킬을 랜덤하게 선택하여 사용합니다.
     * (기본 동작 - 지휘자 스킬 미사용 시)
     *
     * @param minionType  현재 소환된 소환수 종류
     * @param stat        소환수 판정 스탯
     * @param damageBonus 데미지 증가 % (덧셈 보정)
     * @param finalMult   최종 데미지 배율 (곱셈 보정)
     * @param precision   정밀 스탯
     * @param out         출력 스트림
     * @return 결과 객체
     */
    public static Result minionAction(SummonerMinion.MinionType minionType, int stat, int damageBonus,
                                      double finalMult, int precision, PrintStream out) {
        out.printf("[소환수 행동] %s가 스킬을 랜덤하게 선택합니다.%n", minionType.getKorName());
        String[] skillNames = SummonerMinion.getSkillNames(minionType);
        int randomIndex = RANDOM.nextInt(skillNames.length);
        out.printf("선택된 스킬: %s%n", skillNames[randomIndex]);
        return SummonerMinion.useSkill(minionType, randomIndex, stat, damageBonus, finalMult, precision, out);
    }

    /**
     * 소환수 (지휘자 활성화 시) : 현재 소환된 소환수가 플레이어가 선택한 스킬을 사용합니다.
     * 지휘자 스킬 사용 후 호출하며, 플레이어가 지정한 스킬 인덱스로 소환수가 행동합니다.
     *
     * @param minionType         현재 소환된 소환수 종류
     * @param selectedSkillIndex 플레이어가 선택한 스킬 인덱스 (1부터 시작,
     *                           {@link SummonerMinion#getSkillNames(SummonerMinion.MinionType)} 의 순서 기준)
     * @param stat               소환수 판정 스탯
     * @param damageBonus        데미지 증가 % (덧셈 보정)
     * @param finalMult          최종 데미지 배율 (곱셈 보정)
     * @param precision          정밀 스탯
     * @param out                출력 스트림
     * @return 결과 객체
     */
    public static Result minionAction(SummonerMinion.MinionType minionType, int selectedSkillIndex,
                                      int stat, int damageBonus, double finalMult, int precision,
                                      PrintStream out) {
        out.printf("[소환수 행동] %s가 지휘자 지시에 따라 스킬을 사용합니다.%n", minionType.getKorName());
        String[] skillNames = SummonerMinion.getSkillNames(minionType);
        int index = selectedSkillIndex - 1;
        if (index < 0 || index >= skillNames.length) {
            out.printf("유효하지 않은 스킬 번호: %d (1~%d 사이여야 합니다)%n",
                    selectedSkillIndex, skillNames.length);
            return new Result(0, 0, false, 0, 0);
        }
        out.printf("선택된 스킬: %s%n", skillNames[index]);
        return SummonerMinion.useSkill(minionType, index, stat, damageBonus, finalMult, precision, out);
    }

    /**
     * 소환 증폭 : 전투 내 영구적으로 해당 소환수의 최종 데미지가 (200 / 현재 소환된 소환수의 수)%로 증가합니다.
     * 중첩 불가. (마나 5 소모, 쿨타임 5턴)
     *
     * @param activeSummons 현재 소환된 소환수의 수 (1 이상)
     * @param out           출력 스트림
     * @return 결과 객체 (마나 5 소모)
     */
    public static Result summonAmplify(int activeSummons, PrintStream out) {
        out.println("소환술사-소환 증폭 사용");
        if (activeSummons <= 0) {
            out.println("실패: 소환된 소환수가 없습니다.");
            return new Result(0, 0, false, 0, 0);
        }
        int amplifyPercent = 200 / activeSummons;
        out.printf("소환 증폭: 현재 소환된 소환수 %d마리 → 최종 데미지 %d%% 증가 (영구, 중첩 불가)%n",
                activeSummons, amplifyPercent);
        out.println("마나 5 소모, 쿨타임 5턴");
        return new Result(0, 0, true, 5, 0);
    }

    /**
     * 결속의 끈 : 소환 후 5턴이 지난 소환수에게 사용 가능합니다.
     * 전투 내에서 영구적으로 소환수가 가하는 최종 데미지가 2배로 증가합니다.
     * (마나 7 소모, 쿨타임 7턴)
     *
     * @param minionSummonedTurnsAgo 해당 소환수가 소환된 지 몇 턴이 지났는지
     * @param out                    출력 스트림
     * @return 결과 객체 (마나 7 소모)
     */
    public static Result bondTie(int minionSummonedTurnsAgo, PrintStream out) {
        out.println("소환술사-결속의 끈 사용");
        if (minionSummonedTurnsAgo < 5) {
            out.printf("실패: 소환 후 %d턴 경과 (5턴 이상 필요)%n", minionSummonedTurnsAgo);
            return new Result(0, 0, false, 0, 0);
        }
        out.printf("소환 후 %d턴 경과 → 결속의 끈 발동: 소환수 최종 데미지 영구 2배 증가%n", minionSummonedTurnsAgo);
        out.println("마나 7 소모, 쿨타임 7턴");
        return new Result(0, 0, true, 7, 0);
    }

    /**
     * 생명력 공유 : 소환 가능한 소환수의 체력을 amount만큼 감소시키고,
     * 해당 값만큼 현재 소환된 소환수의 체력을 회복시킵니다.
     * ([소환패] 1개 소모, 쿨타임 5턴)
     *
     * @param amount 이전할 체력 양
     * @param out    출력 스트림
     * @return 결과 객체 ([소환패] 1개 소모)
     */
    public static Result lifeShare(int amount, PrintStream out) {
        out.println("소환술사-생명력 공유 사용");
        out.printf("소환 가능한 소환수의 체력 %d 감소 → 현재 소환된 소환수의 체력 %d 회복%n", amount, amount);
        out.println("[소환패] 1개 소모, 쿨타임 5턴");
        return new Result(0, 0, true, 0, 0);
    }

    /**
     * 영혼 귀환 : 3턴 동안 이전에 사망한 소환수를 다시 소환할 수 있습니다.
     * 소환된 소환수는 기존의 10%의 체력을 가집니다.
     * (마나 10 소모, 쿨타임 15턴)
     *
     * @param out 출력 스트림
     * @return 결과 객체 (마나 10 소모)
     */
    public static Result soulReturn(PrintStream out) {
        out.println("소환술사-영혼 귀환 사용");
        out.println("효과: 3턴 동안 이전에 사망한 소환수를 다시 소환할 수 있습니다.");
        out.println("소환된 소환수는 기존의 10%의 체력을 가집니다.");
        out.println("마나 10 소모, 쿨타임 15턴");
        return new Result(0, 0, true, 10, 0);
    }

    /**
     * 원호 방어 (전용 신속) : 지능 또는 지혜 판정을 진행합니다.
     * 성공 시, 자신이나 아군이 받을 공격을 소환수가 대신 맞게 할 수 있습니다.
     *
     * @param intStat     지능 스탯
     * @param wisdomStat  지혜 스탯
     * @param useWisdom   지혜로 판정할 경우 true, 지능으로 판정할 경우 false
     * @param out         출력 스트림
     * @return 결과 객체 (succeeded = 판정 성공 여부)
     */
    public static Result supportDefense(int intStat, int wisdomStat, boolean useWisdom, PrintStream out) {
        out.println("소환술사-원호 방어 사용 (전용 신속)");
        int stat = useWisdom ? wisdomStat : intStat;
        out.printf("%s 판정 시도...%n", useWisdom ? "지혜" : "지능");
        int verdict = Main.verdict(stat, out);
        if (verdict > 0) {
            out.println("판정 성공! 소환수가 대신 공격을 받습니다.");
            return new Result(0, 0, true, 0, 0);
        } else {
            out.println("판정 실패. 원호 방어 발동 불가.");
            return new Result(0, 0, false, 0, 0);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 패시브 헬퍼
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 유대감 패시브 : 보유한 소환수 목록의 등급에 따른 데미지 증가량(%)을 계산합니다.
     * (초급 5% / 중등 10% / 고위 15% / 특급 20% / 초월 25% / 전능 50%)
     *
     * @param ranks 보유한 소환수 등급 배열
     * @param out   출력 스트림
     * @return 데미지 증가 퍼센트 합계
     */
    public static int calculateBondBonus(SummonerMinion.Rank[] ranks, PrintStream out) {
        int total = 0;
        for (SummonerMinion.Rank rank : ranks) {
            total += rank.getBondBonus();
        }
        out.printf("유대감 패시브: 보유 소환수 %d마리 → 데미지 +%d%%%n", ranks.length, total);
        return total;
    }

    /**
     * 소환패 패시브 : 레벨에 따른 시작 소환패 수를 계산합니다. (레벨 / 2)
     *
     * @param level 소환술사의 레벨
     * @param out   출력 스트림
     * @return 시작 소환패 수
     */
    public static int calculateSummonCards(int level, PrintStream out) {
        int cards = level / 2;
        out.printf("소환패 패시브: 레벨 %d → 시작 소환패 %d개%n", level, cards);
        return cards;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // 내부 데미지 계산 공식
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * 소환술사 데미지 계산 공식:
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
    private static Result applyDamage(int baseDamage, int damageBonus, double finalMult,
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
