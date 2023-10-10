package dev.marcoreitano.master.amundsen.registration;

import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Players extends Repository<Player, PlayerId>, AssociationResolver<Player, PlayerId> {
    Player save(Player player);

    Optional<Player> findById(PlayerId playerId);
}
