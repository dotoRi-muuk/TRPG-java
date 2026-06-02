package trpg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trpg.service.SkillService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API Controller for TRPG skill management.
 */
@RestController
@RequestMapping("/api")
public class SkillApiController {

    private final SkillService skillService;

    @Autowired
    public SkillApiController(SkillService skillService) {
        this.skillService = skillService;
    }

    /**
     * Get all primary job classes.
     * GET /api/classes
     */
    @GetMapping("/classes")
    public List<String> getPrimaryClasses() {
        return skillService.getPrimaryClasses();
    }

    /**
     * Get subclasses for a primary class.
     * GET /api/classes/{className}/subclasses
     */
    @GetMapping("/classes/{className}/subclasses")
    public List<String> getSubclasses(@PathVariable String className) {
        return skillService.getSubclasses(className);
    }

    /**
     * Get skills for a subclass.
     * GET /api/subclasses/{subclassName}/skills
     */
    @GetMapping("/subclasses/{subclassName}/skills")
    public List<SkillService.SkillInfo> getSkills(@PathVariable String subclassName) {
        return skillService.getSkills(subclassName);
    }

    /**
     * Execute a skill.
     * POST /api/execute
     */
    @PostMapping("/execute")
    public SkillService.SkillResult executeSkill(@RequestBody ExecuteRequest request) {
        int level = request.level != null ? request.level : 0;
        int externalDamageBonus = request.externalDamageBonus != null ? request.externalDamageBonus : 0;
        int externalFinalDamageMult = request.externalFinalDamageMult != null ? request.externalFinalDamageMult : 100;
        Map<String, Object> params = request.params != null ? new HashMap<>(request.params) : new HashMap<>();
        int externalPrecision = request.externalPrecision != null
                ? request.externalPrecision
                : parseIntOrDefault(params.get("precision"), 0);
        if (request.activationStat != null) {
            params.put("stat", request.activationStat);
        }
        params.put("precision", externalPrecision);
        return skillService.executeSkill(request.subclass, request.skill, params,
                level, externalDamageBonus, externalFinalDamageMult);
    }

    private int parseIntOrDefault(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        try {
            return Integer.parseInt(value.toString());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Request body for skill execution.
     */
    public static class ExecuteRequest {
        public String subclass;
        public String skill;
        public Map<String, Object> params;
        /** 레벨 (최종 데미지 (100+레벨²)% 적용). 기본값 0 (미적용). */
        public Integer level;
        /** 직업 외 데미지 증가값 (합연산 %). 기본값 0 (미적용). */
        public Integer externalDamageBonus;
        /** 직업 외 최종 데미지 배율 (%, 100 = 기본). 기본값 100. */
        public Integer externalFinalDamageMult;
        /** 직업 외 치명타 정밀 스탯. 기본값 0. */
        public Integer externalPrecision;
        /** 스킬 발동 스탯 (기존 params.stat 대체/호환). */
        public Integer activationStat;

        public String getSubclass() { return subclass; }
        public void setSubclass(String subclass) { this.subclass = subclass; }
        public String getSkill() { return skill; }
        public void setSkill(String skill) { this.skill = skill; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
        public Integer getLevel() { return level; }
        public void setLevel(Integer level) { this.level = level; }
        public Integer getExternalDamageBonus() { return externalDamageBonus; }
        public void setExternalDamageBonus(Integer externalDamageBonus) { this.externalDamageBonus = externalDamageBonus; }
        public Integer getExternalFinalDamageMult() { return externalFinalDamageMult; }
        public void setExternalFinalDamageMult(Integer externalFinalDamageMult) { this.externalFinalDamageMult = externalFinalDamageMult; }
        public Integer getExternalPrecision() { return externalPrecision; }
        public void setExternalPrecision(Integer externalPrecision) { this.externalPrecision = externalPrecision; }
        public Integer getActivationStat() { return activationStat; }
        public void setActivationStat(Integer activationStat) { this.activationStat = activationStat; }
    }
}
