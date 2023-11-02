package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.robot.internal.Robots;
import dev.marcoreitano.master.amundsen.robot.internal.SpawnInfos;
import dev.marcoreitano.master.amundsen.world.PlanetId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.jmolecules.ddd.types.Association;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
@Service
@RequiredArgsConstructor
public class RobotManagement {

    private final ApplicationEventPublisher events;
    private final Robots robots;
    private final SpawnInfos spawnInfos;

    public Robot spawnRobotAtRandomPlanet(GameId gameId, PlayerId playerId) {
        AtomicReference<PlanetId> planetId = new AtomicReference<>();
        var spawninfo = spawnInfos.findByGameId(Association.forId(gameId));
        spawninfo.ifPresent(s -> {
            Random random = new Random();
            int randomIndex = random.nextInt(s.getSpawnablePlanetIds().size());
            planetId.set(s.getSpawnablePlanetIds().get(randomIndex));
        });
        return spawnRobotAtPlanet(gameId, playerId, planetId.get());
    }

    protected Robot spawnRobotAtPlanet(GameId gameId, PlayerId playerId, PlanetId planetId) {
        var robot = new Robot(gameId, playerId, planetId);

        events.publishEvent(new RobotSpawned(robot));

        return robots.save(robot);
    }
}
