package dev.marcoreitano.master.amundsen.planing.events;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import org.jmolecules.event.types.DomainEvent;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

public record GamePlanScheduled(
        GamePlanId gamePlanId,
        int roundCount,
        Duration roundDuration,
        Set<PlayerId> participants
) implements DomainEvent {
    public GamePlanScheduled(GamePlan gamePlan) {
        this(gamePlan.getId(), gamePlan.getRoundCount(), gamePlan.getRoundDuration(), new HashSet<>(gamePlan.getParticipants()));
    }
}
