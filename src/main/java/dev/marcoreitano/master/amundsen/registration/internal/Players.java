package dev.marcoreitano.master.amundsen.registration.internal;

import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Repository;

import java.util.Optional;

public interface Players extends Repository<Player, PlayerId>, AssociationResolver<Player, PlayerId> {
    Player save(Player player);

    Optional<Player> findById(PlayerId playerId);
}
