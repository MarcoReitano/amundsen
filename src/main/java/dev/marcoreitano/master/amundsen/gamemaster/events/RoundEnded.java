package dev.marcoreitano.master.amundsen.gamemaster.events;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.gamemaster.Round;
import dev.marcoreitano.master.amundsen.gamemaster.RoundId;
import org.jmolecules.event.types.DomainEvent;

public record RoundEnded(GameId gameId, RoundId roundId, Integer number, boolean isLastRound) implements DomainEvent {

    public RoundEnded(Round round) {
        this(round.getGameId().getId(), round.getId(), round.getNumber(), round.isLastRound());
    }
}
