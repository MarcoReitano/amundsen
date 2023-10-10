package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.event.types.DomainEvent;

public record RobotBought(PlayerId playerId) implements DomainEvent {
}
