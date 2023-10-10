package dev.marcoreitano.master.amundsen.EventLogger;

import dev.marcoreitano.master.amundsen.game.GameCreated;
import dev.marcoreitano.master.amundsen.game.GameStarted;
import dev.marcoreitano.master.amundsen.game.PlayerJoined;
import dev.marcoreitano.master.amundsen.world.WorldCreated;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.modulith.ApplicationModuleListener;

@Service
@Slf4j
public class EventLogger {

    @ApplicationModuleListener
    void on(GameCreated gameCreated) {
        log.info("There seems to be a game that was created!");
    }

    @ApplicationModuleListener
    void on(PlayerJoined playerJoined) {
        log.info("It seems like a player joined a game!");
    }

    @ApplicationModuleListener
    void on(GameStarted gameStarted) {
        log.info("It seems like a game was started!");
    }

    @ApplicationModuleListener
    void on(WorldCreated worldCreated) {
        log.info("A world was created!");
    }

}
