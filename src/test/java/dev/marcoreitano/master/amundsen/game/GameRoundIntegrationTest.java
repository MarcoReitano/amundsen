package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.gamemaster.events.RoundStarted;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

import java.time.Duration;
import java.util.UUID;

@ApplicationModuleTest(extraIncludes = "gamemaster")
@RequiredArgsConstructor
public class GameRoundIntegrationTest {

    private final GameManagement gameManagement;
    private final Games games;

    @BeforeEach
    public void before() {
        games.deleteAll();
    }

    @Test
    public void ensureGameEnds(Scenario scenario) {
        //Given
        Game game = gameManagement.createGame(1, 2, Duration.ofSeconds(1));
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        game.join(playerId);
        games.save(game);

        //When/Then
        scenario.stimulate(() -> {
                    gameManagement.startGame(game);
                    games.save(game); //Needs to be saved manually, so the transaction commits?!
                })
                .andWaitForEventOfType(RoundStarted.class)
                .matching(event -> event.gameId().equals(game.getId()))
                .toArriveAndVerify(
                        event -> event.number().equals(1)
                );
        //Then
    }

    //TODO: Make tests independent
//    @Test
//    public void ensureFirstRoundStartsWhenGameStarts(Scenario scenario) {
//        //Given
//        Game game = gameManagement.createGame(1, 2, Duration.ofSeconds(1));
//        PlayerId playerId = new PlayerId(UUID.randomUUID());
//        game.join(playerId);
//        games.save(game);
//
//        //When/Then
//        scenario.stimulate(() -> {
//                    gameManagement.startGame(game);
//                    games.save(game); //Needs to be saved manually, so the transaction commits?!
//                })
//                .andWaitForEventOfType(RoundStarted.class)
//                .matching(event -> event.gameId().equals(game.getId()))
//                .toArriveAndVerify(
//                        event -> event.number().equals(1)
//                );
//
//
//        //Then
//
//    }


}
