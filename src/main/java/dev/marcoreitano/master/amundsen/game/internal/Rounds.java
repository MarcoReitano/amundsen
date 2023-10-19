package dev.marcoreitano.master.amundsen.game.internal;

import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.game.Round;
import dev.marcoreitano.master.amundsen.game.RoundId;
import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import java.util.Collection;
import java.util.Optional;

public interface Rounds extends Repository<Round, RoundId>, AssociationResolver<Player, PlayerId> {
    Optional<Round> findById(RoundId roundId);

    void save(Round round);

    Collection<Round> findAllByGameId(Association<Game, GameId> gameId);

    Optional<Round> findAllByGameIdAndNumber(Association<Game, GameId> gameId, Integer number);

    Optional<Round> findByGameIdAndPhase(Association<Game, GameId> gameId, RoundPhase roundPhase);
}
