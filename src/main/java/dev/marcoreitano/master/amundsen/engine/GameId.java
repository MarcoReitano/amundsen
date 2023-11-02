package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

public record GameId(UUID id) implements Identifier {
    GameId(GamePlanId gamePlanId) {
        this(gamePlanId.id());
    }
}
