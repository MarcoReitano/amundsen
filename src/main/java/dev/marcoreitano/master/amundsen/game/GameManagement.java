package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.game.events.GameCreated;
import dev.marcoreitano.master.amundsen.game.internal.GameStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Duration;


@Transactional
@Service
@RequiredArgsConstructor
public class GameManagement {

    private final ApplicationEventPublisher events;
    private final Games games;

    public Game createGame() {

        var game = new Game();

        events.publishEvent(new GameCreated(game));

        return games.save(game);
    }

    public Game createGame(Integer maxPlayer, Integer rounds, Duration roundDuration) {
        var game = new Game(maxPlayer, rounds, roundDuration);

        events.publishEvent(new GameCreated(game));

        return games.save(game);
    }

    public void startGame(Game game) {
        if (games.existsByStatus(GameStatus.STARTED)) {
            throw new IllegalStateException("Only one game can be started simultaneously");
        }
        game.start();
    }
}
