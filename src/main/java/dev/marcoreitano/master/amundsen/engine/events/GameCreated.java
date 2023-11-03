package dev.marcoreitano.master.amundsen.engine.events;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.event.types.DomainEvent;

import java.time.Duration;
import java.util.Set;

public record GameCreated(GameId gameId,
                          Integer roundCount,
                          Duration roundDuration,
                          Set<PlayerId> participants)
        implements DomainEvent {

    public GameCreated(Game game) {
        this(game.getId(), game.getRoundCount(), game.getRoundDuration(), game.getParticipants());
    }
}
