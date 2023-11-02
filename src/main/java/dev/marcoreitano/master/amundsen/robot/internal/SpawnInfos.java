package dev.marcoreitano.master.amundsen.robot.internal;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface SpawnInfos extends Repository<SpawnInfo, SpawnInfosId> {
    Optional<SpawnInfo> findById(SpawnInfosId spawnInfosId);

    Optional<SpawnInfo> findByGameId(Association<GamePlan, GamePlanId> gameId);

    void save(SpawnInfo spawnInfo);
}
