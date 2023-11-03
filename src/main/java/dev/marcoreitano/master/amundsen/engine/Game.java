package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.persistence.ElementCollection;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;
import java.util.Set;

@Getter
public class Game extends AbstractAggregateRoot<Game> implements AggregateRoot<Game, GameId> {

    private final GameId id;
    private final Integer roundCount;
    private final Duration roundDuration;

    @ElementCollection
    private final Set<PlayerId> participants;

    private GameStatus gameStatus = GameStatus.SCHEDULED;

    public Game(GamePlanId gamePlanId, Integer roundCount, Duration roundDuration, Set<PlayerId> participants) {
        this.id = new GameId(gamePlanId.id());
        this.roundCount = roundCount;
        this.roundDuration = roundDuration;
        this.participants = participants;
    }
}
