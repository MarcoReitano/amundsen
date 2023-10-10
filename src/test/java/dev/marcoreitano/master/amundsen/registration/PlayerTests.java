package dev.marcoreitano.master.amundsen.registration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class PlayerTests {

    private final RegistrationManagement registrationService;
    private final Players players;

    @Test
    public void registerPlayer() {
        //Given
        Player player;

        //When
        player = registrationService.registerPlayer();

        //Then
        assertNotNull(player);
    }

    @Test
    public void persistPlayer() {
        //Given
        Player player;

        //When
        player = registrationService.registerPlayer();
        var persistedPlayer = players.findById(player.getId());

        //Then
        assertTrue(persistedPlayer.isPresent());
        assertEquals(player.getId(), persistedPlayer.get().getId());
    }

    @Test
    public void publishPlayerRegistered(PublishedEvents events) {
        //Given
        Player player;

        //When
        player = registrationService.registerPlayer();

        //Then
        var matching = events.ofType(PlayerRegistered.class)
                .matching(PlayerRegistered::getPlayerId, player.getId());

        assertThat(matching).hasSize(1);
    }
}
