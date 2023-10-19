package dev.marcoreitano.master.amundsen.gamemaster;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record RoundId(UUID id) implements Identifier {
}
