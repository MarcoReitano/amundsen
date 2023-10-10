package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.robot.internal.Robots;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class RobotTests {

    private final RobotManagement robotManagement;
    private final Robots robots;

    @Test
    void spawnRobot() {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GameId gameId = new GameId(UUID.randomUUID());
        Robot robot;

        //When
        robot = robotManagement.spawnRobot(gameId, playerId);

        //Then
        assertNotNull(robot);
        assertEquals(playerId, robot.getPlayerId().getId());
        assertEquals(gameId, robot.getGameId().getId());
    }

    @Test
    void persistRobot() {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GameId gameId = new GameId(UUID.randomUUID());
        Robot robot;

        //When
        robot = robotManagement.spawnRobot(gameId, playerId);
        Optional<Robot> persistedRobot = robots.findById(robot.getId());

        //Then
        assertTrue(persistedRobot.isPresent());
    }

    @Test
    void publishRobotSpawned(PublishedEvents events) {
        //Given
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        GameId gameId = new GameId(UUID.randomUUID());
        Robot robot;

        //When
        robot = robotManagement.spawnRobot(gameId, playerId);

        //Then
        var matchingEvents = events.ofType(RobotSpawned.class)
                .matching(RobotSpawned::getGameId, robot.getGameId().getId())
                .matching(RobotSpawned::getPlayerId, robot.getPlayerId().getId())
                .matching(RobotSpawned::getRobotId, robot.getId());

        assertThat(matchingEvents).hasSize(1);

    }
}
