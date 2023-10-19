package dev.marcoreitano.master.amundsen.world;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.game.events.GameStarted;
import dev.marcoreitano.master.amundsen.world.internal.Coordinates;
import dev.marcoreitano.master.amundsen.world.internal.Planets;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.ApplicationModuleListener;

@Transactional
@Service
@RequiredArgsConstructor
public class WorldManagement {
    private static final Logger LOG = LoggerFactory.getLogger(WorldManagement.class);
    private final ApplicationEventPublisher events;
    private final Planets planets;

    @ApplicationModuleListener
    void handleGameStarted(GameStarted gameStarted) {

        var mapSize = calculateMapSize(gameStarted.participantCount());

        generateMap(gameStarted.gameId(), mapSize);

        events.publishEvent(new WorldCreated(gameStarted.gameId()));
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

    public Planet createPlanet(GameId gameId, Coordinates coordinates, int movementDifficulty) {
        Planet planet = new Planet(gameId, coordinates, movementDifficulty);

        return planets.save(planet);
    }
}
