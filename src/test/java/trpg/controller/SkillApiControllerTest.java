package trpg.controller;

import org.junit.jupiter.api.Test;
import trpg.service.SkillService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SkillApiControllerTest {

    @Test
    void executeSkillUsesActivationStatFromExternalSettings() {
        SkillService skillService = mock(SkillService.class);
        SkillApiController controller = new SkillApiController(skillService);
        when(skillService.executeSkill(eq("암살자"), eq("fatalMoment"), eq(Map.of("stat", 15, "turnNumber", 1, "precision", 0)),
                anyInt(), anyInt(), anyInt()))
                .thenReturn(new SkillService.SkillResult(true, "ok", Map.of()));

        SkillApiController.ExecuteRequest request = new SkillApiController.ExecuteRequest();
        request.subclass = "암살자";
        request.skill = "fatalMoment";
        request.params = new LinkedHashMap<>();
        request.params.put("stat", 10);
        request.params.put("turnNumber", 1);
        request.activationStat = 15;

        controller.executeSkill(request);

        verify(skillService).executeSkill("암살자", "fatalMoment", Map.of("stat", 15, "turnNumber", 1, "precision", 0), 0, 0, 100);
    }

    @Test
    void executeSkillUsesExternalPrecisionFromExternalSettings() {
        SkillService skillService = mock(SkillService.class);
        SkillApiController controller = new SkillApiController(skillService);
        when(skillService.executeSkill(eq("암살자"), eq("fatalMoment"), eq(Map.of("turnNumber", 1, "precision", 17)),
                anyInt(), anyInt(), anyInt()))
                .thenReturn(new SkillService.SkillResult(true, "ok", Map.of()));

        SkillApiController.ExecuteRequest request = new SkillApiController.ExecuteRequest();
        request.subclass = "암살자";
        request.skill = "fatalMoment";
        request.params = new LinkedHashMap<>();
        request.params.put("turnNumber", 1);
        request.externalPrecision = 17;

        controller.executeSkill(request);

        verify(skillService).executeSkill("암살자", "fatalMoment", Map.of("turnNumber", 1, "precision", 17), 0, 0, 100);
    }

    @Test
    void executeSkillFallsBackToLegacyStatInParams() {
        SkillService skillService = mock(SkillService.class);
        SkillApiController controller = new SkillApiController(skillService);
        Map<String, Object> legacyParams = new HashMap<>();
        legacyParams.put("stat", 12);
        legacyParams.put("turnNumber", 2);
        legacyParams.put("precision", 11);
        when(skillService.executeSkill(eq("암살자"), eq("fatalMoment"), eq(legacyParams), anyInt(), anyInt(), anyInt()))
                .thenReturn(new SkillService.SkillResult(true, "ok", Map.of()));

        SkillApiController.ExecuteRequest request = new SkillApiController.ExecuteRequest();
        request.subclass = "암살자";
        request.skill = "fatalMoment";
        request.params = legacyParams;

        controller.executeSkill(request);

        verify(skillService).executeSkill("암살자", "fatalMoment", legacyParams, 0, 0, 100);
    }

    @Test
    void executeSkillDefaultsWhenNoParamsProvided() {
        SkillService skillService = mock(SkillService.class);
        SkillApiController controller = new SkillApiController(skillService);
        when(skillService.executeSkill(eq("기사"), eq("strike"), eq(Map.of("stat", 10, "level", 3, "precision", 0)), anyInt(), anyInt(), anyInt()))
                .thenReturn(new SkillService.SkillResult(true, "ok", Map.of()));

        SkillApiController.ExecuteRequest request = new SkillApiController.ExecuteRequest();
        request.subclass = "기사";
        request.skill = "strike";
        request.params = null;
        request.activationStat = 10;
        request.level = 3;
        request.externalDamageBonus = 20;
        request.externalFinalDamageMult = 150;

        controller.executeSkill(request);

        verify(skillService).executeSkill("기사", "strike", Map.of("stat", 10, "level", 3, "precision", 0), 3, 20, 150);
    }
}
