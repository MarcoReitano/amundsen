package dev.marcoreitano.master.amundsen.engine.events;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.event.types.DomainEvent;

import java.time.Duration;

public record GameCreated(GameId gameId,
                          Integer roundCount,
                          Duration roundDuration)
        implements DomainEvent {

    public GameCreated(Game game) {
        this(game.getId(), game.getRoundCount(), game.getRoundDuration());
    }
}
