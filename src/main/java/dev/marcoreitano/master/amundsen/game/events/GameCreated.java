package dev.marcoreitano.master.amundsen.game.events;

import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.event.types.DomainEvent;

public record GameCreated(GameId gameId, int maxRounds, int maxPlayers) implements DomainEvent {

    public GameCreated(Game game) {
        this(game.getId(), game.getRounds(), game.getMaxPlayer());
    }
}
