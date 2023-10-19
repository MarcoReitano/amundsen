package dev.marcoreitano.master.amundsen.registration;

import dev.marcoreitano.master.amundsen.registration.internal.Players;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

@Transactional
@Service
@RequiredArgsConstructor
public class RegistrationManagement {

    private final ApplicationEventPublisher events;
    private final Players players;

    public Player registerPlayer() {

        var registeredPlayer = players.save(new Player());

        events.publishEvent(new PlayerRegistered(registeredPlayer));

        return registeredPlayer;
    }

}
