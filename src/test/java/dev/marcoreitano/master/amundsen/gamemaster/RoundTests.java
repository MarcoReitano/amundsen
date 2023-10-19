package dev.marcoreitano.master.amundsen.gamemaster;

import dev.marcoreitano.master.amundsen.game.GameId;
import dev.marcoreitano.master.amundsen.game.events.GameStarted;
import dev.marcoreitano.master.amundsen.gamemaster.events.RoundEnded;
import dev.marcoreitano.master.amundsen.gamemaster.events.RoundStarted;
import dev.marcoreitano.master.amundsen.gamemaster.internal.RoundPhase;
import dev.marcoreitano.master.amundsen.gamemaster.internal.Rounds;
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
import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class RoundTests {

    private final RoundManagement roundManagement;
    private final Rounds rounds;

    @Test
    public void createAndPersistRound() {
        //Given

        GameId gameId = new GameId(UUID.randomUUID());
        RoundId roundId;
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);

        //When
        roundId = roundManagement.createRound(gameId, number, false, duration);

        //Then
        var round = rounds.findById(roundId);
        assertThat(round).isPresent();
        assertNotNull(round.get().getId());
        assertEquals(gameId, round.get().getGameId().getId());
        assertEquals(number, round.get().getNumber());
        assertFalse(round.get().isLastRound());
        assertEquals(RoundPhase.UPCOMING, round.get().getPhase());
        assertEquals(duration, round.get().getDuration());
    }

    @Test
    public void createRoundsAtGameStart(Scenario scenario) {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());

        //When/Then
        scenario.publish(new GameStarted(gameId, 1, 20, Duration.ofSeconds(20)))
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
        RoundId roundId = roundManagement.createRound(gameId, number, false, duration);

        //When
        var round = rounds.findById(roundId);
        round.ifPresent(r -> {
            r.start();

            //Then
            assertEquals(RoundPhase.PRESENT, r.getPhase());
            assertThat(r.getStartTime()).isCloseTo(startTime, within(10, ChronoUnit.MILLIS));
            assertThat(r.getEndTime()).isCloseTo(endTime, within(10, ChronoUnit.MILLIS));
        });


    }

    @Test
    public void ensureStartRoundPublishRoundStartedEvent(PublishedEvents events) {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);
        RoundId roundId = roundManagement.createRound(gameId, number, false, duration);

        //When
        var round = rounds.findById(roundId);
        assertThat(round).isPresent();
        round.get().start();
        rounds.save(round.get()); //Making sure events are fired

        //Then
        var matchingEvents = events.ofType(RoundStarted.class)
                .matching(RoundStarted::roundId, round.get().getId());
        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    public void endRound() {
        //Given

        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);
        RoundId roundId = roundManagement.createRound(gameId, number, false, duration);
        var round = rounds.findById(roundId);
        assertThat(round).isPresent();
        round.get().start();

        //When

        round.get().end();

        //Then
        assertEquals(RoundPhase.PAST, round.get().getPhase());

    }

    @Test
    public void ensureRoundEndPublishRoundEndedEvent(PublishedEvents events) {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        Integer number = 1;
        Instant startTime = Instant.now();
        Duration duration = Duration.ofSeconds(20);
        Instant endTime = startTime.plus(duration);
        RoundId roundId = roundManagement.createRound(gameId, number, false, duration);
        var round = rounds.findById(roundId);
        assertThat(round).isPresent();
        round.get().start();

        //When
        round.get().end();
        rounds.save(round.get()); //Making sure events are fired

        //Then
        var matchingEvents = events.ofType(RoundEnded.class)
                .matching(RoundEnded::roundId, round.get().getId());
        assertThat(matchingEvents).hasSize(1);
    }
}
