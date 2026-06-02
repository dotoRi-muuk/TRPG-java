package main.secondary.warrior;

import main.Result;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class SpearMasterTest {

    @Test
    void moonSeveringSkyCollapseSlashRequiresMoonLink() {
        SpearMaster spearMaster = new SpearMaster();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = spearMaster.moonSeveringSkyCollapseSlash(100, 100, false, false,
                0, false, 0, false, 0, 1, out);

        assertFalse(result.succeeded());
        assertEquals(0, result.damageDealt());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("[연계 : 월]이 필요"));
    }

    @Test
    void descentOfAnnihilationRequiresAllPrerequisiteSkills() {
        SpearMaster spearMaster = new SpearMaster();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = spearMaster.descentOfAnnihilation(true, true, false, out);

        assertFalse(result.succeeded());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("소멸 강림 사용 불가"));
    }

    @Test
    void descentOfAnnihilationGrantsDestructionLinkWhenPrerequisitesMet() {
        SpearMaster spearMaster = new SpearMaster();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = spearMaster.descentOfAnnihilation(true, true, true, out);

        assertTrue(result.succeeded());
        assertEquals(0, result.damageDealt());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("[연계 : 멸] 획득"));
    }

    @Test
    void sunMoonDivineAnnihilationAbsoluteIronSlashRequiresNextTurnAfterDestructionLink() {
        SpearMaster spearMaster = new SpearMaster();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

        Result result = spearMaster.sunMoonDivineAnnihilationAbsoluteIronSlash(100, 100, false, true, false,
                0, false, 0, false, 0, 1, out);

        assertFalse(result.succeeded());
        assertTrue(baos.toString(StandardCharsets.UTF_8).contains("획득 다음 턴"));
    }
}
