package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.game.internal.GameStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;


@Transactional
@Service
@RequiredArgsConstructor
@Slf4j
public class GameManagement {

    private final ApplicationEventPublisher events;
    private final Games games;

    public Game createGame() {

        var createdGame = new Game();

        events.publishEvent(new GameCreated(createdGame));

        return games.save(createdGame);
    }

    public void startGame(Game game) {
        games.findAll().forEach(g -> log.info(g.getId().toString()));
        if (games.existsByStatus(GameStatus.STARTED)) {
            throw new IllegalStateException("Only one game can be started simultaneously");
        }
        game.start();
    }
}
