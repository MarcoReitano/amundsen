package dev.marcoreitano.master.amundsen.game;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record GameId(UUID id) implements Identifier {
}

