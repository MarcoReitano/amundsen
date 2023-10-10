package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.Getter;
import org.jmolecules.event.types.DomainEvent;

@Getter
public class RobotSpawned implements DomainEvent {

    public RobotSpawned(Robot robot) {
        this.robotId = robot.getId();
        this.gameId = robot.getGameId().getId();
        this.playerId = robot.getPlayerId().getId();
    }

    private final RobotId robotId;
    private final GameId gameId;
    private final PlayerId playerId;

}
