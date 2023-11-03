package dev.marcoreitano.master.amundsen.round.internal;

import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.round.Round;
import dev.marcoreitano.master.amundsen.round.events.RoundsCreated;
import dev.marcoreitano.master.amundsen.world.WorldCreated;
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
    public void scheduleRoundAdvanceWhen(WorldCreated worldCreated) {
        roundScheduler.attachToGame(worldCreated.gameId());
    }
}
