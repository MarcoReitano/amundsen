package dev.marcoreitano.master.amundsen.world;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.world.internal.Coordinates;
import dev.marcoreitano.master.amundsen.world.internal.Planets;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.jmolecules.ddd.types.Association;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;


@Transactional
@Service
@RequiredArgsConstructor
public class WorldManagement {
    private static final Logger LOG = LoggerFactory.getLogger(WorldManagement.class);
    private final ApplicationEventPublisher events;
    private final Planets planets;

    Planet createPlanet(GameId gameId, Coordinates coordinates, int movementDifficulty) {
        Planet planet = new Planet(gameId, coordinates, movementDifficulty);

        return planets.save(planet);
    }

    public void generateMapByParticipants(GameId gameId, int participantCount) {
        var mapSize = calculateMapSize(participantCount);

        generateMap(gameId, mapSize);

        var spawnablePlanetsOfGame = planets.findAllByGameId(Association.forId(gameId));
        var spawnablePlanetIdsOfGame = spawnablePlanetsOfGame.stream().map(Planet::getId).toList();

        events.publishEvent(new WorldCreated(gameId, spawnablePlanetIdsOfGame));
    }

    int calculateMapSize(int playerCount) {
        if (playerCount < 10)
            return 15;
        if (playerCount <= 20)
            return 20;
        return 35;
    }

    void generateMap(GameId gameId, int mapSize) {
        for (int x = 0; x < mapSize; x++) {
            for (int y = 0; y < mapSize; y++) {
                createPlanet(gameId, new Coordinates(x, y), 1);
            }
        }
    }

//    public void discoverPlanet(GameId gameId, RobotId robotId, PlayerId playerId, PlanetId planetId) {
//        var planet = planets.findById(planetId);
//
//        planet.ifPresent(planet1 -> planet1.discover());
//    }
}
