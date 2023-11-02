package dev.marcoreitano.master.amundsen.robot.internal;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface SpawnInfos extends Repository<SpawnInfo, SpawnInfosId> {
    Optional<SpawnInfo> findById(SpawnInfosId spawnInfosId);

    Optional<SpawnInfo> findByGameId(Association<Game, GameId> gameId);

    void save(SpawnInfo spawnInfo);
}
