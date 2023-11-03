package dev.marcoreitano.master.amundsen.round;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.round.events.RoundEnded;
import dev.marcoreitano.master.amundsen.round.events.RoundStarted;
import dev.marcoreitano.master.amundsen.round.internal.RoundPhase;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
@Slf4j
public class Round extends AbstractAggregateRoot<Round> implements AggregateRoot<Round, RoundId> {

    private final RoundId id;
    private final Association<Game, GameId> gameId;

    private final Integer number;
    private final Duration duration;

    private RoundPhase phase;

    private Instant startTime;
    private Instant endTime;

    //TODO: If more parameter in constructor arrise, might be a candidate for a factory
    public Round(GameId gameId, Integer number, Duration duration) {
        this.id = new RoundId(UUID.randomUUID());
        this.gameId = Association.forId(gameId);

        this.number = number;
        this.phase = RoundPhase.UPCOMING;

        this.duration = duration;

    }

    public void start() {
        if (!phase.equals(RoundPhase.UPCOMING))
            throw new IllegalStateException("Only upcoming roundCount can be started!");
        this.phase = RoundPhase.PRESENT;

        this.startTime = Instant.now();
        this.endTime = this.startTime.plus(duration);

        registerEvent(new RoundStarted(this));
    }

    public void end() {
        if (!phase.equals(RoundPhase.PRESENT))
            throw new IllegalStateException("Only currently present roundCount can be ended!");
        phase = RoundPhase.PAST;

        registerEvent(new RoundEnded(this));
    }
}
