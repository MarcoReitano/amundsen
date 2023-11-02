package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Shops extends Repository<Shop, ShopId>, AssociationResolver<Shop, ShopId> {
    Shop save(Shop shop);

    Optional<Shop> findById(ShopId id);

    Optional<Shop> findByGameId(Association<GamePlan, GamePlanId> id);
}
