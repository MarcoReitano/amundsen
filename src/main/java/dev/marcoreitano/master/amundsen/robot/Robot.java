package dev.marcoreitano.master.amundsen.robot;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.world.Planet;
import dev.marcoreitano.master.amundsen.world.PlanetId;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.util.UUID;

@Getter
public class Robot extends AbstractAggregateRoot<Robot> implements AggregateRoot<Robot, RobotId> {

    private final RobotId id;
    private final Association<Game, GameId> gameId;
    private final Association<Player, PlayerId> playerId;

    @NotNull
    private Association<Planet, PlanetId> planetId;


    protected Robot(GameId gameId, PlayerId playerId, PlanetId planetId) {
        this.id = new RobotId(UUID.randomUUID());
        this.gameId = Association.forId(gameId);
        this.playerId = Association.forId(playerId);

        this.planetId = Association.forId(planetId);

    }
}
