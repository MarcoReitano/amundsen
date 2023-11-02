package dev.marcoreitano.master.amundsen.planing;

import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanned;
import dev.marcoreitano.master.amundsen.planing.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.planing.internal.GamePlanStatus;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class GamePlanTests {

    private final GamePlanManagement gamePlanManagement;

    private final GamePlans gamePlans;

    @Test
    public void planGame() {
        //Given
        GamePlan gamePlan;

        //When
        gamePlan = gamePlanManagement.planGame();

        //Then
        assertNotNull(gamePlan);
        assertEquals(GamePlanStatus.PLANNED, gamePlan.getStatus());
        assertEquals(58, gamePlan.getRoundCount());
        assertEquals(Duration.ofSeconds(20), gamePlan.getRoundDuration());
        assertEquals(6, gamePlan.getMaxPlayer());
    }

    @Test
    public void persistGamePlan() {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        GamePlanId gamePlanId = gamePlan.getId();

        //When
        Optional<GamePlan> persistedGamePlan = gamePlans.findById(gamePlanId);

        //Then
        assertTrue(persistedGamePlan.isPresent());
        assertSame(gamePlanId, persistedGamePlan.get().getId());
    }

    @Test
    public void publishesGamePlanned(PublishedEvents events) {
        //Given
        GamePlan gamePlan;

        //When
        gamePlan = gamePlanManagement.planGame();

        //Then
        var matchingEvents = events.ofType(GamePlanned.class)
                .matching(GamePlanned::gamePlanId, gamePlan.getId());

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void publishGameScheduled(PublishedEvents events) {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        gamePlan.join(playerId);
        gamePlans.save(gamePlan);

        //When
        gamePlan.schedule();

        gamePlans.save(gamePlan); //Needs to be saved manually, so the transaction commits?!

        //Then
        var matchingEvents = events.ofType(GamePlanScheduled.class)
                .matching(GamePlanScheduled::gamePlanId, gamePlan.getId());

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    public void publishPlayerJoined(PublishedEvents events) {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        //When
        gamePlan.join(playerId);

        gamePlans.save(gamePlan); //Needs to be saved manually, so the transaction commits?!

        //Then
        var matchingEvents = events.ofType(PlayerJoined.class)
                .matching(PlayerJoined::getGamePlanId, gamePlan.getId())
                .matching(PlayerJoined::getPlayerId, playerId);

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    public void playerJoinsGamePlan() {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        //When
        gamePlan.join(playerId);

        //Then
        assertTrue(gamePlan.getParticipants().contains(playerId));
    }

    @Test
    public void preventPlayerJoinsGamePlanAgain(PublishedEvents events) {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        gamePlan.join(playerId);

        //When/Then
        assertThrows(IllegalStateException.class, () -> {
            gamePlan.join(playerId);
        });

        gamePlans.save(gamePlan); //Needs to be saved manually, so the transaction commits?!

        //Then
        var matchingEvents = events.ofType(PlayerJoined.class)
                .matching(PlayerJoined::getGamePlanId, gamePlan.getId())
                .matching(PlayerJoined::getPlayerId, playerId);

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void preventPlayerToJoinAlreadyScheduledGames() {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        PlayerId secondPlayerId = new PlayerId(UUID.randomUUID());

        gamePlan.join(playerId);
        gamePlan.schedule();
        gamePlans.save(gamePlan);

        //When/Then
        assertThrows(IllegalStateException.class,
                () -> gamePlan.join(secondPlayerId),
                "Players shouldn't be able to join already started games");

        //Then
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void scheduleGame() {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        gamePlan.join(playerId);

        //When
        gamePlan.schedule();

        //Then
        assertEquals(GamePlanStatus.SCHEDULED, gamePlan.getStatus());
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void ensureEmptyGamePlansCantBeStarted() {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();

        //When/Then
        Exception exception = assertThrows(
                IllegalStateException.class,
                () -> gamePlan.schedule(),
                "IllegalStateException wasn't thrown");

        //Then
//        assertTrue(exception.getMessage().contains("Empty"));
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void ensureAlreadyScheduledGamePlansCantBeStarted() {
        //Given
        GamePlan gamePlan = gamePlanManagement.planGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        gamePlan.join(playerId);
        gamePlan.schedule();

        //When/Then
        Exception exception = assertThrows(
                IllegalStateException.class,
                () -> gamePlan.schedule(),
                "Games started although its status wasnt created");

        //Then
    }
}
