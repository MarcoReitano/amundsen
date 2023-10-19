package dev.marcoreitano.master.amundsen.EventLogger;

import dev.marcoreitano.master.amundsen.game.events.GameCreated;
import dev.marcoreitano.master.amundsen.game.events.GameEnded;
import dev.marcoreitano.master.amundsen.game.events.GameStarted;
import dev.marcoreitano.master.amundsen.game.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.gamemaster.events.RoundEnded;
import dev.marcoreitano.master.amundsen.gamemaster.events.RoundStarted;
import dev.marcoreitano.master.amundsen.world.WorldCreated;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.modulith.events.ApplicationModuleListener;

@Service
@Slf4j
public class EventLogger {

    @ApplicationModuleListener
    void on(GameCreated gameCreated) {
        log.info("Game " + gameCreated.gameId() + " was created!");
    }

    @ApplicationModuleListener
    void on(PlayerJoined playerJoined) {
        log.info("It seems like a player " + playerJoined.getPlayerId() + " joined a game!");
    }

    @ApplicationModuleListener
    void on(GameStarted gameStarted) {
        log.info("Game " + gameStarted.gameId() + " was started!");
    }

    @ApplicationModuleListener
    void on(WorldCreated worldCreated) {
        log.info("A world for game " + worldCreated.gameId() + " was created!");
    }

    @ApplicationModuleListener
    void on(RoundStarted roundStarted) {
        log.info("Round " + roundStarted.number().toString() + " in Game " + roundStarted.gameId().toString() + " started!");
    }

    @ApplicationModuleListener
    void on(RoundEnded roundEnded) {
        log.info("Round " + roundEnded.number().toString() + " in Game " + roundEnded.gameId().toString() + " ended!");
    }

    @ApplicationModuleListener
    void on(GameEnded gameEnded) {
        log.info("Game " + gameEnded.gameId().toString() + " ended!");
    }

}
