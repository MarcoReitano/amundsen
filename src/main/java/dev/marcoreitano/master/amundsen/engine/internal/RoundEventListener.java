package dev.marcoreitano.master.amundsen.engine.internal;

import dev.marcoreitano.master.amundsen.engine.Round;
import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.engine.events.GameStarted;
import dev.marcoreitano.master.amundsen.engine.events.RoundsCreated;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoundEventListener {

    private final ApplicationEventPublisher events;
    private final RoundScheduler roundScheduler;
    private final Rounds rounds;


    @ApplicationModuleListener
    public void createRoundsWhen(GameCreated gameCreated) {

        for (int roundNumber = 1; roundNumber <= gameCreated.roundCount(); roundNumber++) {
            var round = new Round(gameCreated.gameId(), roundNumber, gameCreated.roundDuration());
            rounds.save(round);
        }

        events.publishEvent(new RoundsCreated(gameCreated.gameId()));
    }

    @ApplicationModuleListener
    public void scheduleRoundAdvanceWhen(GameStarted gameStarted) {
        roundScheduler.attachToGame(gameStarted.gameId());
    }
}
