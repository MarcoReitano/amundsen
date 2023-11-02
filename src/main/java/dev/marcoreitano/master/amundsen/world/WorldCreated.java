package dev.marcoreitano.master.amundsen.world;

import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.event.types.DomainEvent;

import java.util.List;

public record WorldCreated(GameId gameId, List<PlanetId> planetIds) implements DomainEvent {
}
