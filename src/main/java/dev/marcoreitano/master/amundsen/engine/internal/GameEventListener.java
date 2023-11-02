package dev.marcoreitano.master.amundsen.engine.internal;

import dev.marcoreitano.master.amundsen.engine.GameManagement;
import dev.marcoreitano.master.amundsen.engine.events.RoundEnded;
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
        gameManagement.createGame(gamePlanScheduled.gamePlanId(), gamePlanScheduled.roundCount(), gamePlanScheduled.roundDuration());
    }

    @ApplicationModuleListener
    public void handleRoundEnd(RoundEnded roundEnded) {
        gameManagement.checkForGameEnd(roundEnded.gameId(), roundEnded.number());
    }
}
