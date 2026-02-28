package trpg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Service for managing TRPG skills and their execution.
 */
@Service
public class SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillService.class);

    // Primary classes (main job classes)
    private static final Map<String, String> PRIMARY_CLASSES = new LinkedHashMap<>();
    
    // Secondary classes (subclasses) mapped to their primary class
    private static final Map<String, List<SubclassInfo>> SECONDARY_CLASSES = new LinkedHashMap<>();

    static {
        // Initialize primary classes with Korean names
        PRIMARY_CLASSES.put("궁수", "archer");
        PRIMARY_CLASSES.put("도적", "rogue");
        PRIMARY_CLASSES.put("마법사", "mage");
        PRIMARY_CLASSES.put("사제", "priest");
        PRIMARY_CLASSES.put("전사", "warrior");

        // Initialize secondary classes for each primary
        // 궁수 (Archer)
        SECONDARY_CLASSES.put("archer", Arrays.asList(
            new SubclassInfo("궁수", "main.primary.Archer", false),
            new SubclassInfo("건슬링거", "main.secondary.archer.Gunslinger", false),
            new SubclassInfo("명사수", "main.secondary.archer.MasterArcher", false),
            new SubclassInfo("밀렵꾼", "main.secondary.archer.Poacher", false),
            new SubclassInfo("석궁사수", "main.secondary.archer.Crossbowman", false),
            new SubclassInfo("저격수", "main.secondary.archer.Sniper", false)
        ));

        // 도적 (Rogue)
        SECONDARY_CLASSES.put("rogue", Arrays.asList(
            new SubclassInfo("도적", "main.primary.Rogue", false),
            new SubclassInfo("암살자", "main.secondary.rogue.Assassin", false),
            new SubclassInfo("겜블러", "main.secondary.rogue.Gambler", false),
            new SubclassInfo("닌자", "main.secondary.rogue.Ninja", false),
            new SubclassInfo("트릭스터", "main.secondary.rogue.Trickster", false)
        ));

        // 마법사 (Mage)
        SECONDARY_CLASSES.put("mage", Arrays.asList(
            new SubclassInfo("마법사", "main.primary.Mage", false),
            new SubclassInfo("연금술사", "main.secondary.mage.Alchemist", false),
            new SubclassInfo("마도사", "main.secondary.mage.Arcanist", false),
            new SubclassInfo("결계술사", "main.secondary.mage.BarrierMaster", false),
            new SubclassInfo("마검사", "main.secondary.mage.MagicSwordsman", false),
            new SubclassInfo("소환술사", "main.secondary.mage.Summoner", false)
        ));

        // 사제 (Priest)
        SECONDARY_CLASSES.put("priest", Arrays.asList(
            new SubclassInfo("사제", "main.primary.Priest", false),
            new SubclassInfo("어둠의 사제", "main.secondary.priest.DarkPriest", true),
            new SubclassInfo("빛의 사제", "main.secondary.priest.LightPriest", false),
            new SubclassInfo("번개의 사제", "main.secondary.priest.LightningPriest", false),
            new SubclassInfo("영혼의 사제", "main.secondary.priest.SoulPriest", true),
            new SubclassInfo("시간의 사제", "main.secondary.priest.TimePriest", false)
        ));

        // 전사 (Warrior)
        SECONDARY_CLASSES.put("warrior", Arrays.asList(
            new SubclassInfo("전사", "main.primary.Warrior", false),
            new SubclassInfo("버서커", "main.secondary.warrior.Berserker", true),
            new SubclassInfo("무사", "main.secondary.warrior.BladeMaster", true),
            new SubclassInfo("기사", "main.secondary.warrior.Knight", true),
            new SubclassInfo("창술사", "main.secondary.warrior.SpearMaster", true)
        ));
    }

    /**
     * Get all primary class names.
     */
    public List<String> getPrimaryClasses() {
        return new ArrayList<>(PRIMARY_CLASSES.keySet());
    }

    /**
     * Get subclasses for a primary class.
     */
    public List<String> getSubclasses(String primaryClass) {
        String key = PRIMARY_CLASSES.get(primaryClass);
        if (key == null) return Collections.emptyList();
        
        List<SubclassInfo> subclasses = SECONDARY_CLASSES.get(key);
        if (subclasses == null) return Collections.emptyList();
        
        List<String> names = new ArrayList<>();
        for (SubclassInfo info : subclasses) {
            names.add(info.name);
        }
        return names;
    }

    /**
     * Get skills (methods) for a subclass.
     */
    public List<SkillInfo> getSkills(String subclassName) {
        SubclassInfo classInfo = findSubclassInfo(subclassName);
        if (classInfo == null) return Collections.emptyList();

        try {
            Class<?> clazz = Class.forName(classInfo.className);
            List<SkillInfo> skills = new ArrayList<>();

            for (Method method : clazz.getDeclaredMethods()) {
                // Only include public methods
                if (!Modifier.isPublic(method.getModifiers())) continue;
                
                // Skip methods without PrintStream parameter (not skill methods)
                boolean hasPrintStream = false;
                for (Parameter param : method.getParameters()) {
                    if (param.getType() == PrintStream.class) {
                        hasPrintStream = true;
                        break;
                    }
                }
                if (!hasPrintStream) continue;

                // Build skill info
                SkillInfo skill = new SkillInfo();
                skill.methodName = method.getName();
                skill.displayName = getDisplayName(method.getName());
                skill.parameters = new ArrayList<>();

                for (Parameter param : method.getParameters()) {
                    if (param.getType() == PrintStream.class) continue;
                    
                    ParameterInfo paramInfo = new ParameterInfo();
                    paramInfo.name = param.getName();
                    paramInfo.type = getSimpleTypeName(param.getType());
                    paramInfo.displayName = getParameterDisplayName(param.getName());
                    skill.parameters.add(paramInfo);
                }

                skills.add(skill);
            }

            // Sort skills by name
            skills.sort(Comparator.comparing(s -> s.displayName));
            
            return skills;
        } catch (ClassNotFoundException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Execute a skill with the given parameters.
     */
    public SkillResult executeSkill(String subclassName, String methodName, Map<String, Object> params) {
        SubclassInfo classInfo = findSubclassInfo(subclassName);
        if (classInfo == null) {
            return new SkillResult(false, "클래스를 찾을 수 없습니다: " + subclassName, null);
        }

        try {
            Class<?> clazz = Class.forName(classInfo.className);
            
            // Find the method
            Method targetMethod = null;
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(methodName) && Modifier.isPublic(method.getModifiers())) {
                    targetMethod = method;
                    break;
                }
            }
            
            if (targetMethod == null) {
                return new SkillResult(false, "메소드를 찾을 수 없습니다: " + methodName, null);
            }

            // Build arguments
            Object[] args = new Object[targetMethod.getParameterCount()];
            Parameter[] parameters = targetMethod.getParameters();
            
            // Create output stream to capture output
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8);

            for (int i = 0; i < parameters.length; i++) {
                Parameter param = parameters[i];
                if (param.getType() == PrintStream.class) {
                    args[i] = ps;
                } else {
                    Object value = params.get(param.getName());
                    args[i] = convertValue(value, param.getType());
                }
            }

            // Invoke the method
            Object instance = null;
            if (!Modifier.isStatic(targetMethod.getModifiers())) {
                // For Kotlin classes or instance methods, we need an instance
                instance = clazz.getDeclaredConstructor().newInstance();
            }
            
            Object result = targetMethod.invoke(instance, args);
            ps.flush();
            
            String output = baos.toString(StandardCharsets.UTF_8);
            
            // Build result
            Map<String, Object> resultData = new LinkedHashMap<>();
            resultData.put("output", output);
            
            if (result != null) {
                // Handle Result record
                if (result.getClass().getName().equals("main.Result")) {
                    try {
                        resultData.put("damageTaken", result.getClass().getMethod("damageTaken").invoke(result));
                        resultData.put("damageDealt", result.getClass().getMethod("damageDealt").invoke(result));
                        resultData.put("succeeded", result.getClass().getMethod("succeeded").invoke(result));
                        resultData.put("manaUsed", result.getClass().getMethod("manaUsed").invoke(result));
                        resultData.put("staminaUsed", result.getClass().getMethod("staminaUsed").invoke(result));
                    } catch (Exception e) {
                        // Ignore if Result methods don't exist
                    }
                } else if (result instanceof Integer) {
                    resultData.put("damageDealt", result);
                }
            }
            
            return new SkillResult(true, "스킬 실행 성공", resultData);
            
        } catch (Exception e) {
            logger.error("Failed to execute skill '{}' for subclass '{}': {}", methodName, subclassName, e.getMessage(), e);
            return new SkillResult(false, "스킬 실행 오류: " + e.getMessage(), null);
        }
    }

    private SubclassInfo findSubclassInfo(String subclassName) {
        for (List<SubclassInfo> subclasses : SECONDARY_CLASSES.values()) {
            for (SubclassInfo info : subclasses) {
                if (info.name.equals(subclassName)) {
                    return info;
                }
            }
        }
        return null;
    }

    private String getDisplayName(String methodName) {
        return switch (methodName) {
            case "plain" -> "기본공격";
            case "quickShot" -> "퀵샷";
            case "dash" -> "대쉬";
            case "hunt" -> "사냥감";
            case "strike" -> "강타";
            case "side" -> "가로베기";
            case "shield" -> "방패";
            case "magicBullet" -> "마탄";
            case "manaBlast" -> "마나 블래스트";
            case "magicGuard" -> "매직가드";
            case "revenge" -> "복수";
            case "sacrifice" -> "희생";
            case "stab" -> "쑤시기";
            case "throwAttack" -> "투척/속공";
            case "setTrap" -> "덫 깔기";
            case "headSmash" -> "머리찍기";
            case "snareShot" -> "올가미 탄";
            case "headShot", "HeadShot" -> "헤드샷";
            case "backStab" -> "백스탭";
            case "opportunity" -> "활약 기회";
            case "focusedFire" -> "일점사";
            case "doubleShot" -> "더블샷";
            case "quickDraw" -> "퀵드로우";
            case "toss" -> "던지기";
            case "singleShot" -> "단일사격";
            case "luminousArrow" -> "발광 화살";
            case "paralysisArrow" -> "마비 화살";
            case "arrowBreak" -> "화살 꺾기";
            case "crisisReload" -> "이럴 때 일수록!";
            case "fire" -> "발사";
            case "camouflage" -> "위장";
            case "assassinate" -> "암살";
            case "vitalPointStab" -> "급소 찌르기";
            case "throatSlit" -> "목 긋기";
            case "wristSlit" -> "손목 긋기";
            case "luckyMoment" -> "럭키 모먼트";
            case "flow" -> "흐름";
            case "coinToss" -> "코인 토스";
            case "jokerCard" -> "조커 카드";
            case "blackJack" -> "블랙잭";
            case "yachtDice" -> "야추 다이스";
            case "royalFlush" -> "로얄 플러쉬";
            case "throwShuriken" -> "투척 표창";
            case "phantomDance" -> "환영난무";
            case "allOutThrow" -> "일점투척";
            case "slash" -> "난도";
            case "fakeDagger" -> "페이크 단검";
            case "beanBomb" -> "콩알탄";
            case "oilBarrel" -> "기름통 투척";
            case "lighterThrow" -> "라이터 투척";
            case "xlDagger" -> "특대형 단검";
            case "waitingTime" -> "대기 시간";
            case "perfectPreparation" -> "완벽한 준비";
            case "hastyPreparation" -> "성급한 준비";
            case "explosivePotion" -> "폭발 물약";
            case "toxicPotion" -> "독성 물약";
            case "rampageAura" -> "폭주오라";
            case "lumenConversion" -> "루멘 컨버전";
            case "etherCatastrophe" -> "에테르 카타스트로피";
            case "manaBullet" -> "마력탄";
            case "forceFieldBarrier" -> "역장 결계";
            case "manaSlash" -> "마나 슬래쉬";
            case "manaStrike" -> "마나 스트라이크";
            case "manaSphere" -> "마나 스피어";
            case "spinChrist" -> "스핀 크라이스트";
            case "tripleSlain" -> "트리플 슬레인";
            case "etherealImperio" -> "에테리얼 임페리오";
            case "fistOfObedience" -> "말을 잘 듣게 하는 주먹";
            case "fistBeatingSummon" -> "소환수를 이기는 주먹";
            case "heavensDoor" -> "헤븐즈 도어";
            case "prayer" -> "기도";
            case "invocation" -> "기원";
            case "holyGrailOfLight" -> "빛의 성배";
            case "heal" -> "힐";
            case "healingWind" -> "치유의 바람";
            case "spark" -> "스파크";
            case "chainLightning" -> "체인 라이트닝";
            case "electricField" -> "일렉트릭 필드";
            case "divineThunderStrike" -> "신뇌격";
            case "corrosion" -> "부식";
            case "grudge" -> "원한";
            case "chestPain" -> "흉통";
            case "curse" -> "저주";
            case "enthiaStickelia" -> "엔시아스티켈리아";
            case "anaisPhilane" -> "어나이스필레인";
            case "exilister" -> "엑실리스터";
            case "uzmania" -> "우즈마니아";
            case "grasp" -> "손아귀";
            case "darkEnergy" -> "어둠의 기운";
            case "mutualDestruction" -> "동귀어진";
            case "smash" -> "찍어내리기";
            case "crush" -> "부수기";
            case "mindlessThrashing" -> "무지성 난타";
            case "ferociousAssault" -> "흉폭한 맹공";
            case "devastatingBlow" -> "파멸의 일격";
            case "finalStrike" -> "최후의 일격";
            case "drawBlade" -> "발검";
            case "drawSword" -> "발도";
            case "cut" -> "자법";
            case "flash" -> "일섬";
            case "rampage" -> "난격";
            case "flashStrike" -> "섬격";
            case "terminus" -> "종점";
            case "blooming" -> "개화";
            case "resolve" -> "각오";
            case "lifeOrDeath" -> "생사결단";
            case "despair" -> "절명";
            case "limitBreak" -> "극한돌파";
            case "moonHide" -> "월은";
            case "deflect" -> "빗겨내기";
            case "downwardStrike" -> "내려치기";
            case "bash" -> "후려치기";
            case "headStrike" -> "머리치기";
            case "defenseBreak" -> "수비파괴";
            case "stun" -> "기절시키기";
            case "spinEvasion" -> "회전 회피";
            case "spinStab" -> "돌려 찌르기";
            case "spinStrike" -> "회전 타격";
            case "lowSlash" -> "하단 베기";
            case "frontalStab" -> "[연계] 정면 찌르기";
            case "flashSpear" -> "[연계] 일섬창";
            case "heavenlyThunderStrike" -> "[연계] 천뢰격";
            default -> methodName;
        };
    }

    private String getParameterDisplayName(String paramName) {
        return switch (paramName) {
            case "stat" -> "스탯";
            case "out" -> "출력";
            case "intelligence" -> "지능";
            case "useMana" -> "마나 사용 여부";
            case "additionalMana" -> "추가 마나";
            case "damageTaken" -> "받은 데미지";
            case "strength" -> "힘";
            case "dexterity" -> "민첩";
            case "consecutiveHits" -> "연속 공격 횟수";
            case "power" -> "힘";
            case "maxHealth", "maxHp" -> "최대 체력";
            case "curHealth", "currentHp" -> "현재 체력";
            case "useTwoDice" -> "주사위 2개 사용 여부";
            case "swiftness" -> "신속";
            case "hunting" -> "사냥 패시브 여부";
            case "survivalOfTheFittest" -> "약육강식 패시브 여부";
            case "contemptForTheWeak" -> "약자멸시 스킬 여부";
            case "reload" -> "장전 여부";
            case "loadedArrows" -> "장전된 화살 수";
            case "calculateRange" -> "비거리 계산 여부";
            case "eliminateError" -> "오차 제거 여부";
            case "executionArrows" -> "처형 화살 수";
            case "indiscriminateFire" -> "무차별 난사 여부";
            case "consumedArrows" -> "소모할 화살 수";
            case "vitalAim" -> "급소조준 여부";
            case "deathBullet" -> "죽음의 탄환 여부";
            case "assemble" -> "조립 여부";
            case "aim" -> "조준 여부";
            case "sureHit" -> "필즉 여부";
            case "stabilize" -> "안정화 여부";
            case "focus" -> "몰입 여부";
            case "conviction" -> "확신 여부";
            case "heightenedSenses" -> "신경 극대화 여부";
            case "prudence" -> "신중함 여부";
            case "calculatedMove" -> "노림수 여부";
            case "judge" -> "심판자 여부";
            case "judgementTarget" -> "심판 대상 여부";
            case "warning" -> "경고 여부";
            case "isHeavyString" -> "무거운 시위 여부";
            case "isFirstTarget" -> "첫 대상 여부";
            case "isEmergency" -> "긴급 사격 여부";
            case "ability" -> "적용 기술";
            case "preyEnabled" -> "사냥감 스킬 여부";
            case "isArrowReinforced" -> "화살 강화 여부";
            case "calm" -> "차분함 여부";
            case "cracking" -> "흐름 깨기 여부";
            case "stage" -> "무대 여부";
            case "assassinationTarget" -> "암살 대상 여부";
            case "powerOverLifeAndDeath" -> "생사여탈 여부";
            case "confirmKill" -> "확인 사살 여부";
            case "agiDex" -> "민첩";
            case "speed" -> "신속";
            case "luckStat" -> "운 스탯";
            case "decreasedLuck" -> "감소한 운";
            case "illusion" -> "환영 여부";
            case "quickReflexes" -> "순발력 여부";
            case "ideologySeal" -> "이념 봉인 여부";
            case "dex" -> "민첩";
            case "numShurikens" -> "표창 수";
            case "focusedFire" -> "일점사 여부";
            case "regularCustomer" -> "단골 손님 여부";
            case "fakeDagger" -> "페이크 단검 여부";
            case "partyTime" -> "파티 타임 여부";
            case "greatScar" -> "거대한 상흔 여부";
            case "oilHit" -> "기름통 적중 여부";
            case "suddenEvent" -> "기술 사용 횟수";
            case "eventPreparation" -> "이벤트 준비 여부";
            case "mainEvent" -> "메인 이벤트 여부";
            case "alchemyPreparation" -> "미지의 물약 수";
            case "frailty" -> "허약 패시브 여부";
            case "frailtyPortion" -> "적 체력 비율";
            case "condensation" -> "영창 턴 수";
            case "annihilator" -> "어나일레이터 여부";
            case "global" -> "광역 여부";
            case "castSum" -> "결계 영창 시간";
            case "enableBarrierExpansion" -> "결계 확장 여부";
            case "enhancementBarrier" -> "강화 결계 여부";
            case "enhancementBarrierCast" -> "강화 결계 영창 시간";
            case "sealingBarrier" -> "봉인 결계 여부";
            case "cloneBarrier" -> "분신 결계 여부";
            case "lastMana" -> "이전 마나";
            case "currentMana" -> "현재 마나";
            case "overload" -> "오버로드 여부";
            case "ethailSolar" -> "에테일 솔라 여부";
            case "shiftLifter" -> "쉬프트리스터 여부";
            case "level" -> "레벨";
            case "mercy" -> "자비 여부";
            case "favoritism" -> "편애 여부";
            case "transfer" -> "양도 여부";
            case "piety" -> "신앙심 여부";
            case "chantTurns" -> "영창 턴 수";
            case "monopoly" -> "독점 여부";
            case "monopolyAmount" -> "독점 비율";
            case "impatience" -> "성급함 여부";
            case "turns" -> "행동 횟수";
            case "soul" -> "영혼 수";
            case "ruins" -> "폐허 여부";
            case "soulUse" -> "사용 영혼 수";
            case "domination" -> "지배 여부";
            case "scapegoat" -> "희생양 여부";
            case "erosion" -> "침식 아군 수";
            case "resistance" -> "저항 여부";
            case "euphoria" -> "감소 체력";
            case "currentStamina" -> "현재 스태미나";
            case "recentStaminaConsumed" -> "최근 5턴 소모 스태미나";
            case "objectI" -> "물아 모드 여부";
            case "oneStrikeKill" -> "일격필살 여부";
            case "lifeOrDeath" -> "생사결단 여부";
            case "notMe" -> "무아 모드 여부";
            case "resolve" -> "각오 스킬 사용 여부";
            case "despair" -> "절명 여부";
            case "limitBreak" -> "극한돌파 여부";
            case "moonHide" -> "월은 여부";
            case "precision" -> "정밀 스탯";
            case "blessing" -> "축복 여부";
            case "agi" -> "민첩";
            case "adaptation" -> "적응 여부";
            case "totalCast" -> "총 영창 시간";
            case "remainingCast" -> "남은 영창 시간";
            default -> paramName;
        };
    }

    private String getSimpleTypeName(Class<?> type) {
        if (type == int.class || type == Integer.class) return "int";
        if (type == boolean.class || type == Boolean.class) return "boolean";
        if (type == double.class || type == Double.class) return "double";
        if (type.isEnum()) return "enum:" + type.getSimpleName();
        return type.getSimpleName();
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) {
            if (targetType == int.class) return 0;
            if (targetType == boolean.class) return false;
            if (targetType == double.class) return 0.0;
            return null;
        }

        if (targetType == int.class || targetType == Integer.class) {
            if (value instanceof Number) return ((Number) value).intValue();
            return Integer.parseInt(value.toString());
        }
        if (targetType == boolean.class || targetType == Boolean.class) {
            if (value instanceof Boolean) return value;
            return Boolean.parseBoolean(value.toString());
        }
        if (targetType == double.class || targetType == Double.class) {
            if (value instanceof Number) return ((Number) value).doubleValue();
            return Double.parseDouble(value.toString());
        }
        if (targetType.isEnum()) {
            try {
                @SuppressWarnings({"unchecked", "rawtypes"})
                Object enumValue = Enum.valueOf((Class<Enum>) targetType, value.toString());
                return enumValue;
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid enum value '{}' for type '{}'. Valid values: {}", 
                    value, targetType.getSimpleName(), Arrays.toString(targetType.getEnumConstants()));
                // Return first enum value as default
                Object[] constants = targetType.getEnumConstants();
                return constants.length > 0 ? constants[0] : null;
            }
        }

        return value;
    }

    // Helper classes
    public static class SubclassInfo {
        public String name;
        public String className;
        public boolean isKotlin;

        public SubclassInfo(String name, String className, boolean isKotlin) {
            this.name = name;
            this.className = className;
            this.isKotlin = isKotlin;
        }
    }

    public static class SkillInfo {
        public String methodName;
        public String displayName;
        public List<ParameterInfo> parameters;

        public String getMethodName() { return methodName; }
        public String getDisplayName() { return displayName; }
        public List<ParameterInfo> getParameters() { return parameters; }
    }

    public static class ParameterInfo {
        public String name;
        public String type;
        public String displayName;

        public String getName() { return name; }
        public String getType() { return type; }
        public String getDisplayName() { return displayName; }
    }

    public static class SkillResult {
        public boolean success;
        public String message;
        public Map<String, Object> data;

        public SkillResult(boolean success, String message, Map<String, Object> data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Map<String, Object> getData() { return data; }
    }
}
