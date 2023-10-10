package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.game.internal.GameStatus;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.persistence.ElementCollection;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Transactional
public class Game extends AbstractAggregateRoot<Game> implements AggregateRoot<Game, GameId> {

    private final GameId id;

    private GameStatus status;
    private Integer maxPlayer;
    private Integer maxRounds;

    @ElementCollection
    private Set<PlayerId> participants;

    protected Game() {
        this(58, 6);
    }

    protected Game(Integer maxRounds, Integer maxPlayer) {
        this.id = new GameId(UUID.randomUUID());
        this.status = GameStatus.CREATED;
        this.participants = new HashSet<>();

        this.maxRounds = maxRounds;
        this.maxPlayer = maxPlayer;
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

}
