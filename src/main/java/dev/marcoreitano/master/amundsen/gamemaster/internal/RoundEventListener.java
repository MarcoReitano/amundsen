package dev.marcoreitano.master.amundsen.gamemaster.internal;

import dev.marcoreitano.master.amundsen.game.events.GameStarted;
import dev.marcoreitano.master.amundsen.gamemaster.Round;
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
    public void scheduleRoundAdvanceWhen(RoundsCreated roundsCreated) {
        roundScheduler.attachToGame(roundsCreated.gameId());
    }

    @ApplicationModuleListener
    public void createRoundsAt(GameStarted gameStarted) {

        for (int roundNumber = 1; roundNumber < gameStarted.rounds(); roundNumber++) {
            var round = new Round(gameStarted.gameId(), roundNumber, false, gameStarted.roundDuration());
            rounds.save(round);
        }

        var lastRound = new Round(gameStarted.gameId(), gameStarted.rounds(), true, gameStarted.roundDuration());
        rounds.save(lastRound);

        events.publishEvent(new RoundsCreated(gameStarted.gameId()));
    }
}
