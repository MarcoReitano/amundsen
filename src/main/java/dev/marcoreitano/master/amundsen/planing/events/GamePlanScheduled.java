package dev.marcoreitano.master.amundsen.planing.events;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import org.jmolecules.event.types.DomainEvent;

import java.time.Duration;

public record GamePlanScheduled(GamePlanId gamePlanId, int participantCount, int roundCount,
                                Duration roundDuration) implements DomainEvent {
    public GamePlanScheduled(GamePlan gamePlan) {
        this(gamePlan.getId(), gamePlan.getParticipants().size(), gamePlan.getRoundCount(), gamePlan.getRoundDuration());
    }
}
