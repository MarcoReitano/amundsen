package dev.marcoreitano.master.amundsen.gamemaster.internal;

import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.event.types.DomainEvent;

public record RoundsCreated(GameId gameId) implements DomainEvent {
}
