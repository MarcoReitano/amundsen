package dev.marcoreitano.master.amundsen.game;

import org.jmolecules.event.types.DomainEvent;

public record GameCreated(GameId gameId, int maxRounds, int maxPlayers) implements DomainEvent {

    public GameCreated(Game game) {
        this(game.getId(), game.getMaxRounds(), game.getMaxPlayer());
    }
}
