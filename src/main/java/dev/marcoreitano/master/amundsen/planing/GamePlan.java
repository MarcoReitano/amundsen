package dev.marcoreitano.master.amundsen.planing;

import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import dev.marcoreitano.master.amundsen.planing.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.planing.internal.GamePlanStatus;
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
public class GamePlan extends AbstractAggregateRoot<GamePlan> implements AggregateRoot<GamePlan, GamePlanId> {

    private final GamePlanId id;

    private GamePlanStatus status;
    private Integer maxPlayer;
    private Integer roundCount;
    private Duration roundDuration;

    @ElementCollection
    private Set<PlayerId> participants;

    protected GamePlan() {
        this(6, 58, Duration.of(20, ChronoUnit.SECONDS));
    }

    protected GamePlan(Integer maxPlayer, Integer roundCount, Duration roundDuration) {
        this.id = new GamePlanId(UUID.randomUUID());
        this.status = GamePlanStatus.PLANNED;
        this.participants = new HashSet<>();

        this.maxPlayer = maxPlayer;
        this.roundCount = roundCount;
        this.roundDuration = roundDuration;

    }

    public void join(PlayerId playerId) {
        if (status != GamePlanStatus.PLANNED)
            throw new IllegalStateException("Player can only join planing in CREATED status.");
        if (!participants.add(playerId))
            throw new IllegalStateException("Player already joined planing");

        registerEvent(new PlayerJoined(this, playerId));
    }

    public void schedule() {

        if (status != GamePlanStatus.PLANNED)
            throw new IllegalStateException("Games can only be scheduled once");

        if (participants.isEmpty())
            throw new IllegalStateException("Empty games can't be scheduled");

        this.status = GamePlanStatus.SCHEDULED;

        registerEvent(new GamePlanScheduled(this));
    }

}
