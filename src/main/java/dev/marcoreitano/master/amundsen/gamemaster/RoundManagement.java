package dev.marcoreitano.master.amundsen.gamemaster;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.gamemaster.internal.Rounds;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class RoundManagement {

    private final Rounds rounds;

    public RoundId createRound(GameId gameId, Integer number, boolean isLastRound, Duration duration) {
        var round = new Round(gameId, number, isLastRound, duration);

        rounds.save(round);

        return round.getId();
    }

}
