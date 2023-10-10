package dev.marcoreitano.master.amundsen.EventLogger;

import dev.marcoreitano.master.amundsen.AmundsenApplication;
import dev.marcoreitano.master.amundsen.game.Game;
import dev.marcoreitano.master.amundsen.game.GameManagement;
import dev.marcoreitano.master.amundsen.game.Games;
import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.RegistrationManagement;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AmundsenApplication.class);

    private final RegistrationManagement registrationManagement;
    private final GameManagement gameManagement;
    private final Games games;

    @Override
    public void run(String... args) throws Exception {
        defaultGameflow();
    }

    public void defaultGameflow() {
        LOG.info("Running a default Gameflow...");
        Player player = registrationManagement.registerPlayer();
        Game game = gameManagement.createGame();
        game.join(player.getId());
        gameManagement.startGame(game);
        games.save(game);

    }


}
