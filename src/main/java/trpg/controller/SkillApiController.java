package trpg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import trpg.service.SkillService;

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
        return skillService.executeSkill(request.subclass, request.skill, request.params);
    }

    /**
     * Request body for skill execution.
     */
    public static class ExecuteRequest {
        public String subclass;
        public String skill;
        public Map<String, Object> params;

        public String getSubclass() { return subclass; }
        public void setSubclass(String subclass) { this.subclass = subclass; }
        public String getSkill() { return skill; }
        public void setSkill(String skill) { this.skill = skill; }
        public Map<String, Object> getParams() { return params; }
        public void setParams(Map<String, Object> params) { this.params = params; }
    }
}
