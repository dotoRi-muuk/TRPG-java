package main.secondary.mage;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SummonerTest {

    @ParameterizedTest
    @EnumSource(SummonerMinion.MinionType.class)
    void minionActionFixesStatAndPrecisionForAllMinions(SummonerMinion.MinionType minionType) {
        int selectedSkillIndex = minionType == SummonerMinion.MinionType.LUIN ? 2 : 1;
        boolean checkedPrecision = false;

        for (int attempt = 0; attempt < 20; attempt++) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(baos, true, StandardCharsets.UTF_8);

            Summoner.minionAction(minionType, selectedSkillIndex, 0, 1.0, out);
            String log = baos.toString(StandardCharsets.UTF_8);

            assertTrue(log.contains("스탯 20"), () -> minionType + " should use fixed stat 20");
            if (log.contains("정밀 판정")) {
                checkedPrecision = true;
                assertTrue(log.contains("정밀 15"), () -> minionType + " should use fixed precision 15");
            }
        }

        assertTrue(checkedPrecision, () -> minionType + " should reach precision check at least once");
    }
}
