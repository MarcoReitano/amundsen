package dev.marcoreitano.master.amundsen.world;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record PlanetId(UUID id) implements Identifier {
}
