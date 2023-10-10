package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.robot.internal.Robots;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

@Transactional
@Service
@RequiredArgsConstructor
public class RobotManagement {

    private final ApplicationEventPublisher events;
    private final Robots robots;

    public Robot spawnRobot(GameId gameId, PlayerId playerId) {
        var robot = new Robot(gameId, playerId);

        events.publishEvent(new RobotSpawned(robot));

        return robots.save(robot);
    }
}
