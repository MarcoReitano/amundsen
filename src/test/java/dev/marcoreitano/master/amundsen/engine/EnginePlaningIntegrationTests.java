package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanManagement;
import dev.marcoreitano.master.amundsen.planing.GamePlans;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest(extraIncludes = "planing")
@RequiredArgsConstructor
public class EnginePlaningIntegrationTests {

    private final GamePlanManagement gamePlanManagement;
    private final GamePlans gamePlans;

    @Test
    public void scheduleGamePlanCreatesGame(Scenario scenario) {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        GamePlan gamePlan = gamePlanManagement.planGame(1, 2, Duration.ofMillis(1000));

        gamePlan.join(playerId);

        //When / Then
        scenario.stimulate(() -> {
            gamePlan.schedule();
            gamePlans.save(gamePlan);
        }).andWaitForEventOfType(GameCreated.class).toArriveAndVerify(
                gameCreated -> {
                    assertThat(gameCreated.gameId().id()).isEqualTo(gamePlan.getId().id());
                }
        );
    }
}
