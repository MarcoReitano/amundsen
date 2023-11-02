package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.GameEnded;
import dev.marcoreitano.master.amundsen.engine.events.RoundEnded;
import dev.marcoreitano.master.amundsen.engine.internal.Games;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.time.Duration;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ApplicationModuleTest
@RequiredArgsConstructor
public class GameTests {

    private final Games games;

    @Test
    public void createGame() {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());

        //When
        Game game = new Game(gamePlanId, 20, Duration.ofSeconds(1));

        //Then
        assertThat(game).isNotNull();
        assertThat(game.getRoundCount()).isEqualTo(20);
        assertThat(game.getRoundDuration()).isEqualTo(Duration.ofSeconds(1));
    }


    @Test
    public void ensureGameEndsWhenLastRoundEnded(Scenario scenario) {
        //Given
        Game game = new Game(new GamePlanId(UUID.randomUUID()), 20, Duration.ofSeconds(1));
        game.start();
        games.save(game);

        //When/Then
        scenario.publish(new RoundEnded(game.getId(), new RoundId(UUID.randomUUID()), 20))
                .andWaitForEventOfType(GameEnded.class)
                .toArriveAndVerify(gameEnded -> {
                    assertThat(gameEnded.gameId()).isEqualTo(game.getId());
                });
    }
}
