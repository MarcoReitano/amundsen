package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.event.types.DomainEvent;

public record ShopCreated(GameId gameId, ShopId shopId) implements DomainEvent {
}
