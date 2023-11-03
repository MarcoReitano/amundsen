package dev.marcoreitano.master.amundsen.round.events;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.round.Round;
import dev.marcoreitano.master.amundsen.round.RoundId;
import org.jmolecules.event.types.DomainEvent;

public record RoundStarted(GameId gameId, RoundId roundId, Integer number) implements DomainEvent {

    public RoundStarted(Round round) {
        this(round.getGameId().getId(), round.getId(), round.getNumber());
    }
}
