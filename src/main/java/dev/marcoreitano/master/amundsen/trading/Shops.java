package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameId;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Shops extends Repository<Shop, ShopId>, AssociationResolver<Shop, ShopId> {
    Shop save(Shop shop);

    Optional<Shop> findById(ShopId id);

    Optional<Shop> findByGameId(Association<Game, GameId> id);
}
