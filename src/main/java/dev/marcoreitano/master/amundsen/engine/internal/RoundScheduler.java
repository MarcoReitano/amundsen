package dev.marcoreitano.master.amundsen.engine.internal;

import dev.marcoreitano.master.amundsen.engine.GameId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Instant;

@RequiredArgsConstructor
@Component
@Transactional
public class RoundScheduler {

    private final Rounds rounds;
    private final TaskScheduler scheduler;

    public void attachToGame(GameId gameId) {
        scheduler.schedule(new AdvanceRoundTask(gameId), Instant.now());
    }

    @RequiredArgsConstructor
    class AdvanceRoundTask implements Runnable {

        private final GameId gameId;

        @Override
        @Transactional
        public void run() {
            var currentRound = rounds.findByGameIdAndPhase(Association.forId(gameId), RoundPhase.PRESENT);
            currentRound.ifPresentOrElse(cR -> {

                cR.end();
                rounds.save(cR);

                var nextRoundNumber = cR.getNumber() + 1;
                var nextRound = rounds.findAllByGameIdAndNumber(Association.forId(gameId), nextRoundNumber);

                nextRound.ifPresent(nR -> {
                    nR.start();
                    rounds.save(nR);
                    scheduler.schedule(new AdvanceRoundTask(gameId), nR.getEndTime());
                });

            }, () -> {
                var firstRound = rounds.findAllByGameIdAndNumber(Association.forId(gameId), 1);
                firstRound.ifPresent(fR -> {
                    fR.start();
                    rounds.save(fR);
                    scheduler.schedule(new AdvanceRoundTask(gameId), fR.getEndTime());
                });
            });
        }
    }
}
