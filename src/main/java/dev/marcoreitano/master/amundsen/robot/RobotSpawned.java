package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.world.PlanetId;
import org.jmolecules.event.types.DomainEvent;

public record RobotSpawned(GamePlanId gamePlanId,
                           RobotId robotId,
                           PlayerId playerId,
                           PlanetId planetId)
        implements DomainEvent {

    public RobotSpawned(Robot robot) {
        this(robot.getGameId().getId(), robot.getId(), robot.getPlayerId().getId(), robot.getPlanetId().getId());
    }
}
