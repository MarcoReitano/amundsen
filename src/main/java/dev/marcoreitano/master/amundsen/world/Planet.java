package dev.marcoreitano.master.amundsen.world;

import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.world.internal.Coordinates;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.UUID;

@Getter
public class Planet extends AbstractAggregateRoot<Planet> implements AggregateRoot<Planet, PlanetId> {

    private final PlanetId id;
    private final Association<Game, GameId> gameId;

    private final Coordinates coordinates;
    private final int movementDifficulty;

    protected Planet(GameId gameId, Coordinates coordinates, int movementDifficulty) {
        this.id = new PlanetId(UUID.randomUUID());
        this.gameId = Association.forId(gameId);
        this.coordinates = coordinates;
        this.movementDifficulty = movementDifficulty;
    }
}
