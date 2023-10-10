package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.game.internal.GameStatus;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Games extends Repository<Game, GameId> {
    Game save(Game game);

    Optional<Game> findById(GameId gameId);

    Iterable<Game> findAll();

    boolean existsByStatus(GameStatus status);
}
