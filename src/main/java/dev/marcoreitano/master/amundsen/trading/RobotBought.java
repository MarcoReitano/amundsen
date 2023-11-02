package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.event.types.DomainEvent;

public record RobotBought(GamePlanId gamePlanId, PlayerId playerId) implements DomainEvent {
}
