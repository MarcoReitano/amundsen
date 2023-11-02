package dev.marcoreitano.master.amundsen.engine.events;

import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.event.types.DomainEvent;

public record GameStarted(GameId gameId) implements DomainEvent {
}
