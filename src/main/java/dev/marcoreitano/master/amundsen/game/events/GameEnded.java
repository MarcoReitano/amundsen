package dev.marcoreitano.master.amundsen.game.events;

import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.event.types.DomainEvent;

public record GameEnded(GameId gameId) implements DomainEvent {
    public GameEnded(Game game) {
        this(game.getId());
    }
}
