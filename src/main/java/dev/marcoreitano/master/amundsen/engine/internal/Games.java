package dev.marcoreitano.master.amundsen.engine.internal;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Games extends Repository<Game, GameId> {
    void save(Game game);

    Optional<Game> findById(GameId gameId);
}
