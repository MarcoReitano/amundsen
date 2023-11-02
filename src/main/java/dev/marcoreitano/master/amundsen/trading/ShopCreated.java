package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import org.jmolecules.event.types.DomainEvent;

public record ShopCreated(GamePlanId gamePlanId, ShopId shopId) implements DomainEvent {
}
