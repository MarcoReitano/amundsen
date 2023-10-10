package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.event.types.DomainEvent;

public record ShopCreated(GameId gameId, ShopId shopId) implements DomainEvent {
}
