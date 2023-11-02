package dev.marcoreitano.master.amundsen.planing;

import dev.marcoreitano.master.amundsen.planing.internal.GamePlanStatus;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface GamePlans extends Repository<GamePlan, GamePlanId>, AssociationResolver<GamePlan, GamePlanId> {
    GamePlan save(GamePlan gamePlan);

    Optional<GamePlan> findById(GamePlanId gamePlanId);

    Iterable<GamePlan> findAll();

    boolean existsByStatus(GamePlanStatus status);

    void deleteAll();
}
