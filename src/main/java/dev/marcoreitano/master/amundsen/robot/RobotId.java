package dev.marcoreitano.master.amundsen.robot;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record RobotId(UUID id) implements Identifier {
}
