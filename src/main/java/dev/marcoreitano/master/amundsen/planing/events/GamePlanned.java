package dev.marcoreitano.master.amundsen.planing.events;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import org.jmolecules.event.types.DomainEvent;

public record GamePlanned(GamePlanId gamePlanId, int maxRounds, int maxPlayers) implements DomainEvent {

    public GamePlanned(GamePlan gamePlan) {
        this(gamePlan.getId(), gamePlan.getRoundCount(), gamePlan.getMaxPlayer());
    }
}
