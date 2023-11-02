package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.event.types.DomainEvent;

public record RobotBought(GameId gameId, PlayerId playerId) implements DomainEvent {
}
