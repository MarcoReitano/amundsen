package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jmolecules.event.types.DomainEvent;

@Getter
@RequiredArgsConstructor
public class PlayerJoined implements DomainEvent {

    public PlayerJoined(Game game, PlayerId playerId) {
        this.gameId = game.getId();
        this.playerId = playerId;
    }

    private final GameId gameId;
    private final PlayerId playerId;
}
