package dev.marcoreitano.master.amundsen.game;

import org.jmolecules.event.types.DomainEvent;

public record GameStarted(GameId gameId, int participantCount) implements DomainEvent {
    public GameStarted(Game game) {
        this(game.getId(), game.getParticipants().size());
    }
}
