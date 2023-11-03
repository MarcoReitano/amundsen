package dev.marcoreitano.master.amundsen.round.events;


import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.event.types.DomainEvent;

public record RoundsCreated(GameId gameId) implements DomainEvent {
}
