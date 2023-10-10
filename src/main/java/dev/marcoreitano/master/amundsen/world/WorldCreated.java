package dev.marcoreitano.master.amundsen.world;

import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.event.types.DomainEvent;

public record WorldCreated(GameId gameId) implements DomainEvent {
}
