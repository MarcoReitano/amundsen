package dev.marcoreitano.master.amundsen.trading;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record ShopId(UUID id) implements Identifier {
}
