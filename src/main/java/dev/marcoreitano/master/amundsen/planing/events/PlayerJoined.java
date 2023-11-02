package dev.marcoreitano.master.amundsen.planing.events;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.event.types.DomainEvent;

@Getter
@RequiredArgsConstructor
public class PlayerJoined implements DomainEvent {

    public PlayerJoined(GamePlan gamePlan, PlayerId playerId) {
        this.gamePlanId = gamePlan.getId();
        this.playerId = playerId;
    }

    private final GamePlanId gamePlanId;
    private final PlayerId playerId;
}
