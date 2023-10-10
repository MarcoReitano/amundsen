package dev.marcoreitano.master.amundsen.trading.internal;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record AccountId(UUID id) implements Identifier {
}
