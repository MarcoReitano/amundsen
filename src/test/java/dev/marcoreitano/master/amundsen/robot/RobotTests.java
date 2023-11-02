package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.robot.internal.Robots;
import dev.marcoreitano.master.amundsen.robot.internal.SpawnInfos;
import dev.marcoreitano.master.amundsen.trading.RobotBought;
import dev.marcoreitano.master.amundsen.world.PlanetId;
import dev.marcoreitano.master.amundsen.world.WorldCreated;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.modulith.test.Scenario;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class RobotTests {

    private final RobotManagement robotManagement;
    private final Robots robots;
    private final SpawnInfos spawnInfos;


    @Test
    void spawnRobot() {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        PlanetId planetId = new PlanetId(UUID.randomUUID());
        Robot robot;

        //When
        robot = robotManagement.spawnRobotAtPlanet(gamePlanId, playerId, planetId);

        //Then
        assertNotNull(robot);
        assertEquals(playerId, robot.getPlayerId().getId());
        assertEquals(gamePlanId, robot.getGameId().getId());
    }

    @Test
    void persistRobot() {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        PlanetId planetId = new PlanetId(UUID.randomUUID());
        Robot robot;

        //When
        robot = robotManagement.spawnRobotAtPlanet(gamePlanId, playerId, planetId);
        Optional<Robot> persistedRobot = robots.findById(robot.getId());

        //Then
        assertTrue(persistedRobot.isPresent());
    }

    @Test
    void handleRobotBoughtEvent(Scenario scenario) {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        PlanetId planetId = new PlanetId(UUID.randomUUID());

        //When / Then
        scenario.publish(new RobotBought(gamePlanId, playerId))
                .andWaitForEventOfType(RobotSpawned.class)
                .toArriveAndVerify(event -> {
                    event.gamePlanId().equals(gamePlanId);
                    event.playerId().equals(playerId);
                });
        //Then
    }

    @Test
    void publishRobotSpawned(PublishedEvents events) {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        PlanetId planetId = new PlanetId(UUID.randomUUID());
        Robot robot;

        //When
        robot = robotManagement.spawnRobotAtPlanet(gamePlanId, playerId, planetId);

        //Then
        var matchingEvents = events.ofType(RobotSpawned.class)
                .matching(RobotSpawned::gamePlanId, robot.getGameId().getId())
                .matching(RobotSpawned::playerId, robot.getPlayerId().getId())
                .matching(RobotSpawned::robotId, robot.getId());

        assertThat(matchingEvents).hasSize(1);

    }

    @Test
    void memorizeSpawnpoints(Scenario scenario) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        PlanetId mars = new PlanetId(UUID.randomUUID());
        PlanetId venus = new PlanetId(UUID.randomUUID());

        //When
        scenario.publish(new WorldCreated(gamePlanId, Arrays.asList(mars, venus)))
                .andWaitForStateChange(() -> spawnInfos.findByGameId(Association.forId(gamePlanId)))
                .andVerify(result -> {
                    result.ifPresent(r -> {
                                assertThat(r.getSpawnablePlanetIds().size()).isEqualTo(2);
                            }
                    );
                });
        //Then
    }
}
