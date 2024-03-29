package dev.marcoreitano.master.amundsen.round.events;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.round.RoundId;
import dev.marcoreitano.master.amundsen.round.internal.Round;
import org.jmolecules.event.types.DomainEvent;

public record RoundEnded(GameId gameId, RoundId roundId, Integer number) implements DomainEvent {

    public RoundEnded(Round round) {
        this(round.getGameId().getId(), round.getId(), round.getNumber());
    }
}
