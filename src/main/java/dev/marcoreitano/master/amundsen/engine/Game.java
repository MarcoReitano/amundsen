package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.GameEnded;
import dev.marcoreitano.master.amundsen.engine.events.GameStarted;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;

@Getter
public class Game extends AbstractAggregateRoot<Game> implements AggregateRoot<Game, GameId> {

    private final GameId id;
    private final Integer roundCount;
    private final Duration roundDuration;

    private GameStatus gameStatus = GameStatus.SCHEDULED;

    public Game(GamePlanId gamePlanId, Integer roundCount, Duration roundDuration) {
        this.id = new GameId(gamePlanId.id());
        this.roundCount = roundCount;
        this.roundDuration = roundDuration;
    }

    public void start() {

        if (this.gameStatus != GameStatus.SCHEDULED)
            throw new IllegalStateException("Already started/ended games can't be started again");

        this.gameStatus = GameStatus.STARTED;

        registerEvent(new GameStarted(id));
    }

    public void end() {
        if (this.gameStatus != GameStatus.STARTED)
            throw new IllegalStateException("Only started games can be ended");

        this.gameStatus = GameStatus.ENDED;

        registerEvent(new GameEnded(id));
    }
}
