package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.engine.internal.Games;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class GameManagement {

    private final ApplicationEventPublisher events;
    private final Games games;

    public GameId createGame(GamePlanId gamePlanId, Integer roundCount, Duration roundDuration) {
        Game game = new Game(gamePlanId, roundCount, roundDuration);

        games.save(game);

        events.publishEvent(new GameCreated(game));

        return game.getId();
    }

    public void checkForGameEnd(GameId gameId, Integer roundNumber) {
        var game = games.findById(gameId);
        game.ifPresent(g -> {
            if (roundNumber == g.getRoundCount()) {
                g.end();
                games.save(g);
            }
        });

    }
}
