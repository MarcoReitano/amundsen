package dev.marcoreitano.master.amundsen.world.internal;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import dev.marcoreitano.master.amundsen.world.WorldManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorldEventListener {

    private final WorldManagement worldManagement;

    @ApplicationModuleListener
    void handleGamePlanSchedule(GamePlanScheduled gamePlanScheduled) {

        GameId gameId = new GameId(gamePlanScheduled.gamePlanId().id());

        worldManagement.generateMapByParticipants(gameId, gamePlanScheduled.participantCount());
    }
}
