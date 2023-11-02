package dev.marcoreitano.master.amundsen.world;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.world.internal.Coordinates;
import dev.marcoreitano.master.amundsen.world.internal.Planets;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ApplicationModuleTest
@RequiredArgsConstructor
public class PlanetTests {

    private final WorldManagement worldManagement;
    private final Planets planets;

    @Test
    void createPlanet() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());

        //When
        Planet planet = worldManagement.createPlanet(gameId, new Coordinates(0, 0), 1);

        //Then
        assertNotNull(planet);
    }

    @Test
    void persistPlanet() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());

        //When
        Planet planet = worldManagement.createPlanet(gameId, new Coordinates(0, 0), 1);
        Optional<Planet> persistedPlanet = planets.findById(planet.getId());

        //Then
        assertTrue(persistedPlanet.isPresent());
    }
}
