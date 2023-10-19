package dev.marcoreitano.master.amundsen.game.internal;

import dev.marcoreitano.master.amundsen.game.Games;
import dev.marcoreitano.master.amundsen.gamemaster.events.RoundEnded;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class GameEventListener {

    private final Games games;

    @ApplicationModuleListener
    void endGameWhen(RoundEnded roundEnded) {
        if (roundEnded.isLastRound()) {

            var game = games.findById(roundEnded.gameId());
            game.ifPresent(g -> {
                g.end();
                games.save(g);
            });

        }
    }
}
