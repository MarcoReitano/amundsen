package dev.marcoreitano.master.amundsen.robot.internal;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.world.PlanetId;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;

import java.util.List;
import java.util.UUID;

@Getter
public class SpawnInfo implements AggregateRoot<SpawnInfo, SpawnInfosId> {

    private final SpawnInfosId id;

    private final Association<Game, GameId> gameId;
    @ElementCollection(fetch = FetchType.EAGER)
    private final List<PlanetId> spawnablePlanetIds;

    public SpawnInfo(GameId gameId, List<PlanetId> spawnablePlanetIds) {
        this.id = new SpawnInfosId(UUID.randomUUID());
        this.gameId = Association.forId(gameId);

        this.spawnablePlanetIds = spawnablePlanetIds;
    }
}
