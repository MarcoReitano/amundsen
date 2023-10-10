package dev.marcoreitano.master.amundsen.registration;

import lombok.Getter;
import org.jmolecules.event.types.DomainEvent;

@Getter
public class PlayerRegistered implements DomainEvent {
    public PlayerRegistered(Player player) {
        this.playerId = player.getId();
    }

    private final PlayerId playerId;
}
