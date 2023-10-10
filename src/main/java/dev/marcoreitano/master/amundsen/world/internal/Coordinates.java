package dev.marcoreitano.master.amundsen.world.internal;


import org.jmolecules.ddd.types.ValueObject;

public record Coordinates(Integer x, Integer y) implements ValueObject {
}
