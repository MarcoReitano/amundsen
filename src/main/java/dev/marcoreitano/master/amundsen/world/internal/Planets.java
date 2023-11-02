package dev.marcoreitano.master.amundsen.world.internal;

import dev.marcoreitano.master.amundsen.engine.Game;
import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.world.Planet;
import dev.marcoreitano.master.amundsen.world.PlanetId;
import org.jmolecules.ddd.integration.AssociationResolver;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Repository;

import java.util.List;
import java.util.Optional;

public interface Planets extends Repository<Planet, PlanetId>, AssociationResolver<Planet, PlanetId> {
    Planet save(Planet planet);

    Optional<Planet> findById(PlanetId planetId);

    List<Planet> findAllByGameId(Association<Game, GameId> gameId);
}
