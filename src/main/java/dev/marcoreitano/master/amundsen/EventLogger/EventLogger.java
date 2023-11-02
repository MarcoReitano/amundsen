package dev.marcoreitano.master.amundsen.EventLogger;

import dev.marcoreitano.master.amundsen.engine.events.*;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanned;
import dev.marcoreitano.master.amundsen.planing.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.robot.RobotSpawned;
import dev.marcoreitano.master.amundsen.trading.RobotBought;
import dev.marcoreitano.master.amundsen.trading.ShopCreated;
import dev.marcoreitano.master.amundsen.world.WorldCreated;
import lombok.extern.slf4j.Slf4j;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.modulith.events.ApplicationModuleListener;

@Service
@Slf4j
public class EventLogger {

    @ApplicationModuleListener
    void on(GamePlanned gamePlanned) {
        log.info(gamePlanned.gamePlanId() + " was planned!");
    }

    @ApplicationModuleListener
    void on(PlayerJoined playerJoined) {
        log.info("It seems like a player " + playerJoined.getPlayerId() + " joined a planed game!");
    }

    @ApplicationModuleListener
    void on(GamePlanScheduled gamePlanScheduled) {
        log.info("GamePlan " + gamePlanScheduled.gamePlanId() + " was scheduled!");
    }

    @ApplicationModuleListener
    void on(GameCreated gameCreated) {
        log.info("Game " + gameCreated.gameId() + " was created!");
    }

    @ApplicationModuleListener
    void on(GameStarted gameStarted) {
        log.info("Game " + gameStarted.gameId() + " was started!");
    }

    @ApplicationModuleListener
    void on(WorldCreated worldCreated) {
        log.info("A world for " + worldCreated.gameId() + " was created!");
    }

    @ApplicationModuleListener
    void on(RoundsCreated roundsCreated) {
        log.info("Rounds were created for game " + roundsCreated.gameId());

    }

    @ApplicationModuleListener
    void on(RoundStarted roundStarted) {
        log.info("Round " + roundStarted.number().toString() + " in " + roundStarted.gameId().toString() + " started!");
    }

    @ApplicationModuleListener
    void on(RoundEnded roundEnded) {
        log.info("Round " + roundEnded.number().toString() + " in " + roundEnded.gameId().toString() + " ended!");
    }

    @ApplicationModuleListener
    void on(ShopCreated shopCreated) {
        log.info(shopCreated.shopId() + " was created!");
    }

    @ApplicationModuleListener
    void on(RobotBought robotBought) {
        log.info(robotBought.playerId() + " bought a robot!");
    }

    @ApplicationModuleListener
    void on(RobotSpawned robotSpawned) {
        log.info(robotSpawned.robotId() + "spawned on planet " + robotSpawned.planetId());
    }

}
