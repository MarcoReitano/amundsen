package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.engine.internal.Games;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class GameManagement {

    private final ApplicationEventPublisher events;
    private final Games games;

    public GameId createGame(GameId gameId, Integer roundCount, Duration roundDuration, Set<PlayerId> participants) {

        Game game = new Game(gameId, roundCount, roundDuration, participants);

        games.save(game);

        events.publishEvent(new GameCreated(game));

        return game.getId();
    }
}
