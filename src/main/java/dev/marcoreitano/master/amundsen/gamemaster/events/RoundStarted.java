package dev.marcoreitano.master.amundsen.gamemaster.events;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.gamemaster.Round;
import dev.marcoreitano.master.amundsen.gamemaster.RoundId;
import org.jmolecules.event.types.DomainEvent;

public record RoundStarted(GameId gameId, RoundId roundId, Integer number) implements DomainEvent {

    public RoundStarted(Round round) {
        this(round.getGameId().getId(), round.getId(), round.getNumber());
    }
}
