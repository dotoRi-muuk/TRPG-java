package trpg.service;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class SkillServiceTest {

    @Test
    void assassinSkillChooserHidesFatalMomentSubSkillsAndShowsTurnSelectorSkill() {
        SkillService service = new SkillService();

        List<SkillService.SkillInfo> skills = service.getSkills("암살자");
        Set<String> methodNames = skills.stream()
                .map(SkillService.SkillInfo::getMethodName)
                .collect(Collectors.toSet());

        assertTrue(methodNames.contains("fatalMoment"));
        assertFalse(methodNames.contains("startFatalMoment"));
        assertFalse(methodNames.contains("triggerFatalMomentTurn"));
        assertFalse(methodNames.contains("unus"));
        assertFalse(methodNames.contains("duo"));
        assertFalse(methodNames.contains("tres"));
        assertFalse(methodNames.contains("quattuor"));
        assertFalse(methodNames.contains("quinque"));
        assertFalse(methodNames.contains("six"));
        assertFalse(methodNames.contains("septem"));
        assertFalse(methodNames.contains("octo"));
        assertFalse(methodNames.contains("novem"));
        assertFalse(methodNames.contains("decem"));

        SkillService.SkillInfo fatalMoment = skills.stream()
                .filter(skill -> "fatalMoment".equals(skill.getMethodName()))
                .findFirst()
                .orElseThrow();
        Set<String> params = fatalMoment.getParameters().stream()
                .map(SkillService.ParameterInfo::getName)
                .collect(Collectors.toSet());
        assertTrue(params.contains("turnNumber"));
        assertTrue(params.contains("additionalDamage"));
    }

    @Test
    void skillChooserDoesNotExposeGlobalStatsAsSkillParameter() {
        SkillService service = new SkillService();

        List<String> primaryClasses = service.getPrimaryClasses();
        for (String primaryClass : primaryClasses) {
            List<String> subclasses = service.getSubclasses(primaryClass);
            for (String subclass : subclasses) {
                List<SkillService.SkillInfo> skills = service.getSkills(subclass);
                for (SkillService.SkillInfo skill : skills) {
                    Set<String> params = skill.getParameters().stream()
                            .map(SkillService.ParameterInfo::getName)
                            .collect(Collectors.toSet());
                    assertFalse(params.contains("stat"),
                            () -> "stat should be configured globally, but was exposed in " + subclass + "." + skill.getMethodName());
                    assertFalse(params.contains("precision"),
                            () -> "precision should be configured globally, but was exposed in " + subclass + "." + skill.getMethodName());
                }
            }
        }
    }
}
