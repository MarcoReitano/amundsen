package dev.marcoreitano.master.amundsen.engine;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record GameId(UUID id) implements Identifier {
}
