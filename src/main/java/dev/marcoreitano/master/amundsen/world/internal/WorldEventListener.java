package dev.marcoreitano.master.amundsen.world.internal;

import dev.marcoreitano.master.amundsen.engine.events.GameCreated;
import dev.marcoreitano.master.amundsen.world.WorldManagement;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorldEventListener {

    private final WorldManagement worldManagement;

    @ApplicationModuleListener
    void handleGameCreated(GameCreated gameCreated) {
        worldManagement.generateMapByParticipants(gameCreated.gameId(), gameCreated.participants().size());
    }
}
