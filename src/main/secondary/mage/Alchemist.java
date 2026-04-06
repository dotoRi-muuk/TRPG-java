package main.secondary.mage;


import main.Main;
import main.Result;

import java.io.PrintStream;
import java.util.*;

/**
 * 연금술사 (Alchemist)
 * <p>
 * 판정 사용 스탯 : 지능(지혜)
 * <p>
 * 데미지 공식: [(기본 데미지) x (100 + 데미지 증가)%] x (최종 데미지)% x (주사위 보정)
 * + 레벨 보너스 (100 + 레벨^2)
 */
public class Alchemist {

    /**
     * 속성 융합에 사용되는 원소 유형
     */
    public enum Element {
        CORROSION, POISON, FIRE, ICE, AMPLIFY
    }

    /**
     * 문자열 목록을 Element 집합으로 변환합니다.
     *
     * @param elementNames 원소 이름 목록 (영문 또는 한글)
     * @return Element 집합
     */
    public static Set<Element> parseElements(List<String> elementNames) {
        Set<Element> elements = new HashSet<>();
        if (elementNames == null) return elements;
        for (String name : elementNames) {
            switch (name.toLowerCase()) {
                case "corrosion": case "부식": elements.add(Element.CORROSION); break;
                case "poison":    case "맹독": elements.add(Element.POISON);    break;
                case "fire":      case "화염": elements.add(Element.FIRE);      break;
                case "ice":       case "빙결": elements.add(Element.ICE);       break;
                case "amplify":   case "증폭": elements.add(Element.AMPLIFY);   break;
            }
        }
        return elements;
    }

    /**
     * 공통 최종 데미지 계산 헬퍼.
     * [(기본 데미지) x (100 + damageBonus)%] x (최종 데미지%)% x (주사위 보정) + 레벨 보너스
     *
     * @param baseDamage        주사위로 굴린 기본 데미지
     * @param damageBonus       데미지 증가 (%) - 증폭 스택 + 외부 효과 합산
     * @param flasks            현재 플라스크 개수 (연성 패시브: 개당 25% 최종 데미지 증가)
     * @param fusionCount       속성 융합 성공 횟수 (변이 신체 패시브: 회당 20% 최종 데미지 증가)
     * @param externalFinalDmg  외부 최종 데미지 증가 (%)
     * @param level             캐릭터 레벨 (레벨 보너스: 100 + 레벨^2)
     * @param verdict           판정 결과 (스탯 - D20 주사위값, 주사위 보정 계수에 사용)
     * @param out               출력 스트림
     * @return 레벨 보너스 포함 최종 데미지
     */
    private static int computeFinalDamage(int baseDamage, int damageBonus,
                                           int flasks, int fusionCount, int externalFinalDmg,
                                           int level, int verdict, PrintStream out) {
        // 최종 데미지 배율: 기본 100% + 연성 패시브 + 변이 신체 패시브 + 외부 효과
        double finalMultiplier = (100.0 + (flasks * 25.0) + (fusionCount * 20.0) + externalFinalDmg) / 100.0;
        // 주사위 보정: 1 + 판정 결과 * 0.1
        double diceModifier = 1.0 + verdict * 0.1;

        if (flasks > 0) {
            out.printf("연성 패시브: 플라스크 %d개 → 최종 데미지 %d%% 증가%n", flasks, flasks * 25);
        }
        if (fusionCount > 0) {
            out.printf("변이 신체 패시브: 융합 %d회 → 최종 데미지 %d%% 증가%n", fusionCount, fusionCount * 20);
        }
        out.printf("최종 데미지 배율: %.2f (주사위 보정: %.2f)%n", finalMultiplier, diceModifier);

        int finalDamage = Main.calculateDamage(baseDamage, damageBonus, finalMultiplier, diceModifier, out);

        // 레벨 보너스: 100 + 레벨^2
        int levelBonus = 100 + level * level;
        finalDamage += levelBonus;
        out.printf("레벨 보너스 (+%d): 합계 %d%n", levelBonus, finalDamage);

        return finalDamage;
    }

    /**
     * 플라스크 스킬 공통 로직.
     *
     * @param skillName         스킬 이름
     * @param dices             주사위 개수
     * @param sides             주사위 면 수
     * @param mana              마나 소모량
     * @param stat              지능 스탯
     * @param level             캐릭터 레벨
     * @param flasks            현재 플라스크 개수
     * @param fusionCount       속성 융합 성공 횟수
     * @param elementAmpBonus   원소별 데미지 증가 (%)
     * @param allAmpBonus       전체 스킬 데미지 증가 (%)
     * @param externalDmgIncrease 외부 데미지 증가 (%)
     * @param externalFinalDmg  외부 최종 데미지 증가 (%)
     * @param precision         정밀 스탯
     * @param out               출력 스트림
     * @return 결과 객체
     */
    private static Result flaskSkill(String skillName, int dices, int sides, int mana,
                                      int stat, int level, int flasks, int fusionCount,
                                      int elementAmpBonus, int allAmpBonus,
                                      int externalDmgIncrease, int externalFinalDmg,
                                      int precision, PrintStream out) {
        out.printf("연금술사-%s 사용 ([플라스크] 1개 소모)%n", skillName);

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, mana, 0);

        int baseDamage = Main.dice(dices, sides, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        int damageBonus = elementAmpBonus + allAmpBonus + externalDmgIncrease;
        if (damageBonus > 0) {
            out.printf("데미지 증가 보너스: 원소 증폭 %d%% + 전체 증폭 %d%% + 외부 %d%% = %d%%%n",
                    elementAmpBonus, allAmpBonus, externalDmgIncrease, damageBonus);
        }

        int finalDamage = computeFinalDamage(baseDamage, damageBonus, flasks, fusionCount,
                externalFinalDmg, level, verdict, out);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);

        return new Result(0, finalDamage, true, mana, 0);
    }

    // ────────────────────────────── 기본 스킬 ──────────────────────────────

    /**
     * 기본 공격 : 대상에게 1D6의 데미지를 입힙니다.
     *
     * @param stat                사용할 스탯 (지능)
     * @param level               캐릭터 레벨
     * @param flasks              현재 플라스크 개수
     * @param fusionCount         속성 융합 성공 횟수
     * @param allAmpBonus         전체 스킬 데미지 증가 (%)
     * @param externalDmgIncrease 외부 데미지 증가 (%)
     * @param externalFinalDmg    외부 최종 데미지 증가 (%)
     * @param precision           정밀 스탯
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result plain(int stat, int level, int flasks, int fusionCount,
                                int allAmpBonus, int externalDmgIncrease, int externalFinalDmg,
                                int precision, PrintStream out) {
        out.println("연금술사-기본 공격 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 0, 0);

        int baseDamage = Main.dice(1, 6, out);
        out.printf("기본 데미지 : %d%n", baseDamage);

        int damageBonus = allAmpBonus + externalDmgIncrease;
        int finalDamage = computeFinalDamage(baseDamage, damageBonus, flasks, fusionCount,
                externalFinalDmg, level, verdict, out);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);

        return new Result(0, finalDamage, true, 0, 0);
    }

    // ────────────────────────────── 플라스크 스킬 ──────────────────────────────

    /**
     * 비트리올 플라스크 : [플라스크] 소모. 3D8 피해. [부식] 부여. (마나 3, 쿨타임 3턴)
     *
     * @param stat                사용할 스탯 (지능)
     * @param level               캐릭터 레벨
     * @param flasks              현재 플라스크 개수
     * @param fusionCount         속성 융합 성공 횟수
     * @param corrosionAmpBonus   부식 계열 데미지 증가 누적량 (%)
     * @param allAmpBonus         전체 스킬 데미지 증가 (%)
     * @param externalDmgIncrease 외부 데미지 증가 (%)
     * @param externalFinalDmg    외부 최종 데미지 증가 (%)
     * @param precision           정밀 스탯
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result vitriolicFlask(int stat, int level, int flasks, int fusionCount,
                                         int corrosionAmpBonus, int allAmpBonus,
                                         int externalDmgIncrease, int externalFinalDmg,
                                         int precision, PrintStream out) {
        return flaskSkill("비트리올 플라스크", 3, 8, 3,
                stat, level, flasks, fusionCount,
                corrosionAmpBonus, allAmpBonus, externalDmgIncrease, externalFinalDmg, precision, out);
    }

    /**
     * 블라이트 플라스크 : [플라스크] 소모. 6D4 피해. [맹독] 부여. (마나 3, 쿨타임 3턴)
     *
     * @param poisonAmpBonus 맹독 계열 데미지 증가 누적량 (%)
     */
    public static Result blightFlask(int stat, int level, int flasks, int fusionCount,
                                      int poisonAmpBonus, int allAmpBonus,
                                      int externalDmgIncrease, int externalFinalDmg,
                                      int precision, PrintStream out) {
        return flaskSkill("블라이트 플라스크", 6, 4, 3,
                stat, level, flasks, fusionCount,
                poisonAmpBonus, allAmpBonus, externalDmgIncrease, externalFinalDmg, precision, out);
    }

    /**
     * 이그니스 플라스크 : [플라스크] 소모. 3D12 피해. [화염] 부여. (마나 3, 쿨타임 3턴)
     *
     * @param fireAmpBonus 화염 계열 데미지 증가 누적량 (%)
     */
    public static Result ignisFlask(int stat, int level, int flasks, int fusionCount,
                                     int fireAmpBonus, int allAmpBonus,
                                     int externalDmgIncrease, int externalFinalDmg,
                                     int precision, PrintStream out) {
        return flaskSkill("이그니스 플라스크", 3, 12, 3,
                stat, level, flasks, fusionCount,
                fireAmpBonus, allAmpBonus, externalDmgIncrease, externalFinalDmg, precision, out);
    }

    /**
     * 앱솔루트 플라스크 : [플라스크] 소모. 2D8 피해. [빙결] 부여. (마나 4, 쿨타임 5턴)
     * ※ 지능 판정 성공 시 적에게 공격불가 1회 부여 (UI에서 별도 처리)
     *
     * @param iceAmpBonus 빙결 계열 데미지 증가 누적량 (%)
     */
    public static Result absoluteFlask(int stat, int level, int flasks, int fusionCount,
                                        int iceAmpBonus, int allAmpBonus,
                                        int externalDmgIncrease, int externalFinalDmg,
                                        int precision, PrintStream out) {
        return flaskSkill("앱솔루트 플라스크", 2, 8, 4,
                stat, level, flasks, fusionCount,
                iceAmpBonus, allAmpBonus, externalDmgIncrease, externalFinalDmg, precision, out);
    }

    /**
     * 아케인 플라스크 : [플라스크] 소모. 2D20 피해. [증폭] 부여. (마나 5, 쿨타임 6턴)
     */
    public static Result arcaneFlask(int stat, int level, int flasks, int fusionCount,
                                      int allAmpBonus, int externalDmgIncrease, int externalFinalDmg,
                                      int precision, PrintStream out) {
        return flaskSkill("아케인 플라스크", 2, 20, 5,
                stat, level, flasks, fusionCount,
                0, allAmpBonus, externalDmgIncrease, externalFinalDmg, precision, out);
    }

    // ────────────────────────────── 융합 스킬 ──────────────────────────────

    /**
     * 리액션 : 마지막으로 적용한 속성 2가지를 융합합니다. (마나 3)
     *
     * @param stat                사용할 스탯 (지능)
     * @param level               캐릭터 레벨
     * @param flasks              현재 플라스크 개수
     * @param fusionCount         속성 융합 성공 횟수 (변이 신체 패시브)
     * @param corrosionAmpBonus   부식 계열 데미지 증가 누적량 (%)
     * @param poisonAmpBonus      맹독 계열 데미지 증가 누적량 (%)
     * @param fireAmpBonus        화염 계열 데미지 증가 누적량 (%)
     * @param iceAmpBonus         빙결 계열 데미지 증가 누적량 (%)
     * @param allAmpBonus         전체 스킬 데미지 증가 (%)
     * @param externalDmgIncrease 외부 데미지 증가 (%)
     * @param externalFinalDmg    외부 최종 데미지 증가 (%)
     * @param precision           정밀 스탯
     * @param elementNames        융합할 원소 이름 목록 (2개 필요)
     * @param out                 출력 스트림
     * @return 결과 객체
     */
    public static Result reaction(int stat, int level, int flasks, int fusionCount,
                                   int corrosionAmpBonus, int poisonAmpBonus, int fireAmpBonus,
                                   int iceAmpBonus, int allAmpBonus,
                                   int externalDmgIncrease, int externalFinalDmg,
                                   int precision, List<String> elementNames, PrintStream out) {
        out.println("연금술사-리액션 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 3, 0);

        Set<Element> elements = parseElements(elementNames);

        if (elements.size() != 2) {
            out.println("오류: 리액션은 정확히 2가지 속성을 선택해야 합니다.");
            return new Result(0, 0, false, 3, 0);
        }

        boolean hasCorrosion = elements.contains(Element.CORROSION);
        boolean hasPoison    = elements.contains(Element.POISON);
        boolean hasFire      = elements.contains(Element.FIRE);
        boolean hasIce       = elements.contains(Element.ICE);
        boolean hasAmplify   = elements.contains(Element.AMPLIFY);

        // 관련 원소 증폭 보너스 합산
        int elementAmpBonus = 0;
        if (hasCorrosion) elementAmpBonus += corrosionAmpBonus;
        if (hasPoison)    elementAmpBonus += poisonAmpBonus;
        if (hasFire)      elementAmpBonus += fireAmpBonus;
        if (hasIce)       elementAmpBonus += iceAmpBonus;
        int damageBonus = elementAmpBonus + allAmpBonus + externalDmgIncrease;

        String fusionName;
        String fusionEffect;
        int baseDamage;

        if (hasCorrosion && hasPoison) {
            fusionName  = "부패독";
            baseDamage  = Main.dice(3, 8, out);
            fusionEffect = "패시브 부패의 최대 데미지 증가량 50% 상승";
        } else if (hasCorrosion && hasFire) {
            fusionName  = "산화 폭발";
            baseDamage  = Main.dice(3, 20, out);
            fusionEffect = "";
        } else if (hasCorrosion && hasIce) {
            fusionName  = "결정 부식";
            baseDamage  = Main.dice(3, 12, out);
            fusionEffect = "수비 불가 1회";
        } else if (hasCorrosion && hasAmplify) {
            fusionName  = "극속 부식";
            baseDamage  = 0;
            fusionEffect = "이후 [부식] 관련 스킬 데미지 100% 증가 (중첩 가능) ← 부식 데미지 증가 누적에 +100 입력";
        } else if (hasPoison && hasFire) {
            fusionName  = "맹열";
            baseDamage  = Main.dice(7, 4, out);
            fusionEffect = "대상 공격 데미지 1회 70% 감소";
        } else if (hasPoison && hasIce) {
            fusionName  = "동결독";
            baseDamage  = Main.dice(7, 8, out);
            fusionEffect = "공격 불가 1회";
        } else if (hasPoison && hasAmplify) {
            fusionName  = "극독";
            baseDamage  = 0;
            fusionEffect = "이후 [맹독] 관련 스킬 데미지 100% 증가 (중첩 가능) ← 맹독 데미지 증가 누적에 +100 입력";
        } else if (hasFire && hasIce) {
            fusionName  = "강제 용해";
            baseDamage  = Main.dice(8, 4, out);
            fusionEffect = "저지불가 3회 감소";
        } else if (hasFire && hasAmplify) {
            fusionName  = "작열";
            baseDamage  = 0;
            fusionEffect = "이후 [화염] 관련 스킬 데미지 100% 증가 (중첩 가능) ← 화염 데미지 증가 누적에 +100 입력";
        } else if (hasIce && hasAmplify) {
            fusionName  = "절대영도";
            baseDamage  = 0;
            fusionEffect = "행동불가 1회, 이후 [빙결] 관련 스킬 데미지 100% 증가 (중첩 가능) ← 빙결 데미지 증가 누적에 +100 입력";
        } else {
            out.println("알 수 없는 속성 조합입니다.");
            return new Result(0, 0, false, 3, 0);
        }

        out.printf("[리액션] %s 발동!%n", fusionName);
        if (!fusionEffect.isEmpty()) out.printf("효과: %s%n", fusionEffect);

        int finalDamage = 0;
        if (baseDamage > 0) {
            out.printf("기본 데미지 : %d%n", baseDamage);
            finalDamage = computeFinalDamage(baseDamage, damageBonus, flasks, fusionCount,
                    externalFinalDmg, level, verdict, out);
            finalDamage = Main.criticalHit(precision, finalDamage, out);
            out.printf("최종 데미지 : %d%n", finalDamage);
        } else {
            out.println("(데미지 없음 - 버프/디버프 효과만 발동)");
        }

        return new Result(0, finalDamage, true, 3, 0);
    }

    /**
     * 체인 디스토션 : 마지막으로 적용한 속성 3가지를 융합합니다. (마나 4, 쿨타임 5턴)
     *
     * @param elementNames 융합할 원소 이름 목록 (3개 필요)
     */
    public static Result chainDistortion(int stat, int level, int flasks, int fusionCount,
                                          int corrosionAmpBonus, int poisonAmpBonus, int fireAmpBonus,
                                          int iceAmpBonus, int allAmpBonus,
                                          int externalDmgIncrease, int externalFinalDmg,
                                          int precision, List<String> elementNames, PrintStream out) {
        out.println("연금술사-체인 디스토션 사용");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 4, 0);

        Set<Element> elements = parseElements(elementNames);

        if (elements.size() != 3) {
            out.println("오류: 체인 디스토션은 정확히 3가지 속성을 선택해야 합니다.");
            return new Result(0, 0, false, 4, 0);
        }

        boolean hasCorrosion = elements.contains(Element.CORROSION);
        boolean hasPoison    = elements.contains(Element.POISON);
        boolean hasFire      = elements.contains(Element.FIRE);
        boolean hasIce       = elements.contains(Element.ICE);
        boolean hasAmplify   = elements.contains(Element.AMPLIFY);

        int elementAmpBonus = 0;
        if (hasCorrosion) elementAmpBonus += corrosionAmpBonus;
        if (hasPoison)    elementAmpBonus += poisonAmpBonus;
        if (hasFire)      elementAmpBonus += fireAmpBonus;
        if (hasIce)       elementAmpBonus += iceAmpBonus;
        int damageBonus = elementAmpBonus + allAmpBonus + externalDmgIncrease;

        String fusionName;
        String fusionEffect;
        int baseDamage;

        if (hasCorrosion && hasPoison && hasFire) {
            fusionName  = "부패폭발";
            baseDamage  = Main.dice(3, 8, out) + Main.dice(7, 4, out);
            fusionEffect = "패시브 부패의 최대 데미지 증가량 60% 상승";
        } else if (hasCorrosion && hasPoison && hasIce) {
            fusionName  = "결정부패";
            baseDamage  = Main.dice(3, 6, out) + Main.dice(3, 8, out);
            fusionEffect = "저지불가 무시 행동불가 1회, 저지불가 1회 감소";
        } else if (hasCorrosion && hasPoison && hasAmplify) {
            fusionName  = "식극화독";
            baseDamage  = Main.dice(12, 8, out);
            fusionEffect = "이후 [부식], [맹독] 관련 스킬 데미지 75% 증가 (중첩 가능)";
        } else if (hasCorrosion && hasFire && hasIce) {
            fusionName  = "산화용해";
            baseDamage  = Main.dice(9, 6, out);
            fusionEffect = "대상 저지불가 제거, 수비불가 1회";
        } else if (hasCorrosion && hasFire && hasAmplify) {
            fusionName  = "극산폭발";
            baseDamage  = Main.dice(6, 20, out);
            fusionEffect = "이후 [화염], [부식] 관련 스킬 데미지 75% 증가 (중첩 가능)";
        } else if (hasCorrosion && hasIce && hasAmplify) {
            fusionName  = "식화빙멸";
            baseDamage  = Main.dice(6, 12, out);
            fusionEffect = "수비불가 3회, 이후 [부식], [빙결] 관련 스킬 데미지 75% 증가";
        } else if (hasPoison && hasFire && hasIce) {
            fusionName  = "암독결정용해";
            baseDamage  = Main.dice(8, 6, out);
            fusionEffect = "대상 저지불가 제거, 공격불가 1회";
        } else if (hasPoison && hasFire && hasAmplify) {
            fusionName  = "극독폭렬";
            baseDamage  = Main.dice(9, 12, out);
            fusionEffect = "이후 [맹독], [화염] 관련 스킬 데미지 75% 증가";
        } else if (hasPoison && hasIce && hasAmplify) {
            fusionName  = "절대극독";
            baseDamage  = Main.dice(8, 10, out);
            fusionEffect = "공격불가 2회, 이후 [맹독], [빙결] 관련 스킬 데미지 75% 증가";
        } else if (hasFire && hasIce && hasAmplify) {
            fusionName  = "절영작열";
            baseDamage  = Main.dice(9, 6, out) + Main.dice(3, 20, out);
            fusionEffect = "대상 저지불가 제거, 행동불가 1회, 이후 [화염], [빙결] 관련 스킬 데미지 75% 증가";
        } else {
            out.println("알 수 없는 속성 조합입니다.");
            return new Result(0, 0, false, 4, 0);
        }

        out.printf("[체인 디스토션] %s 발동!%n", fusionName);
        out.printf("효과: %s%n", fusionEffect);
        out.printf("기본 데미지 : %d%n", baseDamage);

        int finalDamage = computeFinalDamage(baseDamage, damageBonus, flasks, fusionCount,
                externalFinalDmg, level, verdict, out);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);

        return new Result(0, finalDamage, true, 4, 0);
    }

    /**
     * 매터 디재스티아 : 5가지 속성 모두 융합. (마나 6, 쿨타임 12턴)
     * → 오블리테라 맬리티아 : 10D6 + D200 피해.
     * 대상 저지불가 제거, 행동불가 2회, 이후 모든 스킬 데미지 100% 증가.
     */
    public static Result matterDisaster(int stat, int level, int flasks, int fusionCount,
                                         int corrosionAmpBonus, int poisonAmpBonus, int fireAmpBonus,
                                         int iceAmpBonus, int allAmpBonus,
                                         int externalDmgIncrease, int externalFinalDmg,
                                         int precision, PrintStream out) {
        out.println("연금술사-매터 디재스티아 사용");
        out.println("[부식] + [맹독] + [화염] + [빙결] + [증폭] → 오블리테라 맬리티아 발동!");

        int verdict = Main.verdict(stat, out);
        if (verdict <= 0) return new Result(0, 0, false, 6, 0);

        // 모든 원소 증폭 보너스 합산
        int elementAmpBonus = corrosionAmpBonus + poisonAmpBonus + fireAmpBonus + iceAmpBonus;
        int damageBonus = elementAmpBonus + allAmpBonus + externalDmgIncrease;

        int part1 = Main.dice(10, 6, out);
        int part2 = Main.dice(1, 200, out);
        int baseDamage = part1 + part2;

        out.printf("기본 데미지 (10D6: %d + D200: %d) = %d%n", part1, part2, baseDamage);
        out.println("효과: 대상 저지불가 제거, 행동불가 2회");
        out.println("이후 효과: 모든 스킬 데미지 100% 증가 ← 전체 데미지 증가 누적에 +100 입력");

        int finalDamage = computeFinalDamage(baseDamage, damageBonus, flasks, fusionCount,
                externalFinalDmg, level, verdict, out);
        finalDamage = Main.criticalHit(precision, finalDamage, out);
        out.printf("최종 데미지 : %d%n", finalDamage);

        return new Result(0, finalDamage, true, 6, 0);
    }
}
