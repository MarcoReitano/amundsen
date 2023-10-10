package dev.marcoreitano.master.amundsen.registration;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record PlayerId(UUID id) implements Identifier {
}