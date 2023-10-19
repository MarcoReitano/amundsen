package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.game.events.GameEnded;
import dev.marcoreitano.master.amundsen.game.events.GameStarted;
import dev.marcoreitano.master.amundsen.game.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.game.internal.GameStatus;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.persistence.ElementCollection;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Transactional
public class Game extends AbstractAggregateRoot<Game> implements AggregateRoot<Game, GameId> {

    private final GameId id;

    private GameStatus status;
    private Integer maxPlayer;
    private Integer rounds;
    private Duration roundDuration;

    @ElementCollection
    private Set<PlayerId> participants;

    protected Game() {
        this(6, 58, Duration.of(20, ChronoUnit.SECONDS));
    }

    protected Game(Integer maxPlayer, Integer rounds, Duration roundDuration) {
        this.id = new GameId(UUID.randomUUID());
        this.status = GameStatus.CREATED;
        this.participants = new HashSet<>();

        this.maxPlayer = maxPlayer;
        this.rounds = rounds;
        this.roundDuration = roundDuration;

    }

    public void join(PlayerId playerId) {
        if (status != GameStatus.CREATED)
            throw new IllegalStateException("Player can only join game in CREATED status.");
        if (!participants.add(playerId))
            throw new IllegalStateException("Player already joined game");

        registerEvent(new PlayerJoined(this, playerId));
    }

    protected void start() {

        if (status != GameStatus.CREATED)
            throw new IllegalStateException("Games can only be started, when their current status is 'created'");

        if (participants.isEmpty())
            throw new IllegalStateException("Empty games can't be started");

        this.status = GameStatus.STARTED;

        registerEvent(new GameStarted(this));
    }

    public void end() {
        if (status != GameStatus.STARTED)
            throw new IllegalStateException("Only games in status 'started' can be ended!");

        registerEvent(new GameEnded(this));
    }
}
