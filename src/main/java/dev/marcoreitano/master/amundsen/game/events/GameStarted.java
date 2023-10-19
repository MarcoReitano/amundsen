package dev.marcoreitano.master.amundsen.game.events;

import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.event.types.DomainEvent;

import java.time.Duration;

public record GameStarted(GameId gameId, int participantCount, int rounds,
                          Duration roundDuration) implements DomainEvent {
    public GameStarted(Game game) {
        this(game.getId(), game.getParticipants().size(), game.getRounds(), game.getRoundDuration());
    }
}
