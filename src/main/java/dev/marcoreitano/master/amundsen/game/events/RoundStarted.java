package dev.marcoreitano.master.amundsen.game.events;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.game.Round;
import dev.marcoreitano.master.amundsen.game.RoundId;
import org.jmolecules.event.types.DomainEvent;

public record RoundStarted(GameId gameId, RoundId roundId, Integer number) implements DomainEvent {

    public RoundStarted(Round round) {
        this(round.getGameId().getId(), round.getId(), round.getNumber());
    }
}
