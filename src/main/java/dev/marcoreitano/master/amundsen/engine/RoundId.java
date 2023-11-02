package dev.marcoreitano.master.amundsen.engine;


import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record RoundId(UUID id) implements Identifier {
}
