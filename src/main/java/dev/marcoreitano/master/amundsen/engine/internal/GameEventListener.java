package dev.marcoreitano.master.amundsen.engine.internal;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.engine.GameManagement;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GameEventListener {

    private final GameManagement gameManagement;

    @ApplicationModuleListener
    public void handleGameScheduled(GamePlanScheduled gamePlanScheduled) {
        GameId gameId = new GameId(gamePlanScheduled.gamePlanId().id());
        gameManagement.createGame(gameId, gamePlanScheduled.roundCount(), gamePlanScheduled.roundDuration(), gamePlanScheduled.participants());
    }

}
