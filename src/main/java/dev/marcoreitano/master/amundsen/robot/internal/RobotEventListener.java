package dev.marcoreitano.master.amundsen.robot.internal;

import dev.marcoreitano.master.amundsen.robot.RobotManagement;
import dev.marcoreitano.master.amundsen.trading.RobotBought;
import dev.marcoreitano.master.amundsen.world.WorldCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RobotEventListener {

    private final RobotManagement robotManagement;
    private final SpawnInfos spawnInfos;

    @ApplicationModuleListener
    void handleRobotBought(RobotBought robotBought) {
        robotManagement.spawnRobotAtRandomPlanet(robotBought.gameId(), robotBought.playerId());
    }

    @ApplicationModuleListener
    void handleWorldCreated(WorldCreated worldCreated) {

        var spawnInfo = new SpawnInfo(worldCreated.gameId(), worldCreated.planetIds());

        spawnInfos.save(spawnInfo);
    }
}
