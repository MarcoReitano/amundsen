package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.engine.internal.Games;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.time.Duration;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@RequiredArgsConstructor
public class GameTests {

    private final Games games;

    @Test
    public void createGame() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        //When
        Game game = new Game(gameId, 20, Duration.ofSeconds(1), Set.of(Association.forId(playerId)));

        //Then
        assertThat(game).isNotNull();
        assertThat(game.getRoundCount()).isEqualTo(20);
        assertThat(game.getRoundDuration()).isEqualTo(Duration.ofSeconds(1));
        assertThat(game.getParticipants().size()).isEqualTo(1);
        assertThat(game.getParticipants()).contains(Association.forId(playerId));
    }

    @Test
    public void createGameWhenGamePlanScheduled(Scenario scenario) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        //When
        scenario.publish(new GamePlanScheduled(gamePlanId, 2, Duration.ofSeconds(1), Set.of()))
                .andWaitForEventOfType(GameCreated.class)
                .toArriveAndVerify(gameCreated -> {
                    assertThat(gameCreated.gameId().id()).isEqualTo(gamePlanId.id());
                });
        //Then
    }
}
