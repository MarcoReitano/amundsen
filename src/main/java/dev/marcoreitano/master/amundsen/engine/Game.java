package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.internal.PlayerAssociationConverter;
import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;
import java.util.Set;

@Getter
@Transactional
public class Game extends AbstractAggregateRoot<Game> implements AggregateRoot<Game, GameId> {

    private final GameId id;
    private final Integer roundCount;
    private final Duration roundDuration;

    @ElementCollection
    @Convert(converter = PlayerAssociationConverter.class)
    private final Set<Association<Player, PlayerId>> participants;

    private GameStatus gameStatus = GameStatus.SCHEDULED;

    public Game(GameId gameId, Integer roundCount, Duration roundDuration, Set<Association<Player, PlayerId>> participants) {
        this.id = gameId;
        this.roundCount = roundCount;
        this.roundDuration = roundDuration;
        this.participants = participants;
    }
}
