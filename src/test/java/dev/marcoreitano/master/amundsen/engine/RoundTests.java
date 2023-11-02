package dev.marcoreitano.master.amundsen.engine;

import dev.marcoreitano.master.amundsen.engine.events.RoundEnded;
import dev.marcoreitano.master.amundsen.engine.events.RoundStarted;
import dev.marcoreitano.master.amundsen.engine.internal.RoundPhase;
import dev.marcoreitano.master.amundsen.engine.internal.Rounds;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.modulith.test.Scenario;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ApplicationModuleTest
@RequiredArgsConstructor
public class RoundTests {

    private final GameManagement gameManagement;
    private final Rounds rounds;

    @Test
    public void createAndPersistRound() {
        //Given

        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);

        //When
        Round round = new Round(gameId, number, duration);

        //Then
        assertNotNull(round.getId());
        assertEquals(gameId, round.getGameId().getId());
        assertEquals(number, round.getNumber());
        assertEquals(RoundPhase.UPCOMING, round.getPhase());
        assertEquals(duration, round.getDuration());
    }

    @Test
    public void createRoundsWhenGamePlanIsScheduled(Scenario scenario) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        GameId gameId = new GameId(gamePlanId);

        //When/Then
        scenario.publish(new GamePlanScheduled(gamePlanId, 1, 20, Duration.ofSeconds(1)))
                .andWaitForStateChange(() ->
                                rounds.findAllByGameId(Association.forId(gameId)),
                        it -> !it.isEmpty()
                ).andVerify(it -> assertEquals(20, it.size()));

        //Then

    }

    @Test
    public void startRound() {
        //Given

        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);
        Round round = new Round(gameId, number, duration);

        //When
        round.start();

        //Then
        assertEquals(RoundPhase.PRESENT, round.getPhase());
        assertThat(round.getStartTime()).isCloseTo(startTime, within(100, ChronoUnit.MILLIS));
        assertThat(round.getEndTime()).isCloseTo(endTime, within(100, ChronoUnit.MILLIS));


    }

    @Test
    public void ensureStartRoundPublishRoundStartedEvent(PublishedEvents events) {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);
        Round round = new Round(gameId, number, duration);

        //When
        round.start();
        rounds.save(round); //Making sure events are fired

        //Then
        var matchingEvents = events.ofType(RoundStarted.class)
                .matching(RoundStarted::roundId, round.getId());
        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    public void endRound() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Duration duration = Duration.ofSeconds(20);
        Round round = new Round(gameId, number, duration);
        round.start();

        //When
        round.end();

        //Then
        assertEquals(RoundPhase.PAST, round.getPhase());

    }

    @Test
    public void ensureRoundEndPublishRoundEndedEvent(PublishedEvents events) {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);
        Round round = new Round(gameId, number, duration);
        rounds.save(round);
        round.start();

        //When
        round.end();
        rounds.save(round);

        //Then
        var matchingEvents = events.ofType(RoundEnded.class)
                .matching(RoundEnded::roundId, round.getId());
        assertThat(matchingEvents).hasSize(1);
    }
}
