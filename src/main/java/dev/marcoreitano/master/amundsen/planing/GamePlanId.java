package dev.marcoreitano.master.amundsen.planing;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record GamePlanId(UUID id) implements Identifier {
}

