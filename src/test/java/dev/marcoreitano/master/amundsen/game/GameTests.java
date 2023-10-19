package dev.marcoreitano.master.amundsen.game;

import dev.marcoreitano.master.amundsen.game.events.GameCreated;
import dev.marcoreitano.master.amundsen.game.events.GameStarted;
import dev.marcoreitano.master.amundsen.game.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.game.internal.GameStatus;
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
public class GameTests {

    private final GameManagement gameManagement;

    private final Games games;

    @Test
    public void createGame() {
        //Given
        Game game;

        //When
        game = gameManagement.createGame();

        //Then
        assertNotNull(game);
        assertEquals(GameStatus.CREATED, game.getStatus());
        assertEquals(58, game.getRounds());
        assertEquals(Duration.ofSeconds(20), game.getRoundDuration());
        assertEquals(6, game.getMaxPlayer());
    }

    @Test
    public void persistGame() {
        //Given
        Game game = gameManagement.createGame();
        GameId gameId = game.getId();
//        games.save(game); //No need anymore since the service handles saving to the repository

        //When
        Optional<Game> persistedGame = games.findById(gameId);

        //Then
        assertTrue(persistedGame.isPresent());
        assertSame(gameId, persistedGame.get().getId());
    }

    @Test
    public void publishesGameCreation(PublishedEvents events) {
        //Given
        Game game;

        //When
        game = gameManagement.createGame();

        //Then
        var matchingEvents = events.ofType(GameCreated.class)
                .matching(GameCreated::gameId, game.getId());

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void publishGameStarted(PublishedEvents events) {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        game.join(playerId);
        games.save(game);

        //When
        gameManagement.startGame(game);

        games.save(game); //Needs to be saved manually, so the transaction commits?!

        //Then
        var matchingEvents = events.ofType(GameStarted.class)
                .matching(GameStarted::gameId, game.getId());

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    public void publishPlayerJoined(PublishedEvents events) {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        //When
        game.join(playerId);

        games.save(game); //Needs to be saved manually, so the transaction commits?!

        //Then
        var matchingEvents = events.ofType(PlayerJoined.class)
                .matching(PlayerJoined::getGameId, game.getId())
                .matching(PlayerJoined::getPlayerId, playerId);

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    public void playerJoinsGame() {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());

        //When
        game.join(playerId);

        //Then
        assertTrue(game.getParticipants().contains(playerId));
    }

    @Test
    public void preventPlayerJoinsGameAgain(PublishedEvents events) {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        game.join(playerId);

        //When/Then
        assertThrows(IllegalStateException.class, () -> {
            game.join(playerId);
        });

        games.save(game); //Needs to be saved manually, so the transaction commits?!

        //Then
        var matchingEvents = events.ofType(PlayerJoined.class)
                .matching(PlayerJoined::getGameId, game.getId())
                .matching(PlayerJoined::getPlayerId, playerId);

        assertThat(matchingEvents).hasSize(1);
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void preventPlayerToJoinAlreadyStartedGames() {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        PlayerId secondPlayerId = new PlayerId(UUID.randomUUID());

        game.join(playerId);
        games.save(game);
        gameManagement.startGame(game);
        games.save(game);

        //When/Then
        assertThrows(IllegalStateException.class,
                () -> game.join(secondPlayerId),
                "Players shouldn't be able to join already started games");

        //Then
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void startGame() {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        game.join(playerId);

        //When
        gameManagement.startGame(game);

        //Then
        assertEquals(GameStatus.STARTED, game.getStatus());
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void ensureEmptyGamesCantBeStarted() {
        //Given
        Game game = gameManagement.createGame();

        //When/Then
        Exception exception = assertThrows(
                IllegalStateException.class,
                () -> gameManagement.startGame(game),
                "IllegalStateException wasn't thrown");

        //Then
//        assertTrue(exception.getMessage().contains("Empty"));
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void ensureAlreadyStartedGamesCantBeStarted() {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        game.join(playerId);
        gameManagement.startGame(game);

        //When/Then
        Exception exception = assertThrows(
                IllegalStateException.class,
                () -> gameManagement.startGame(game),
                "Games started although its status wasnt created");

        //Then
    }

    @Test
    @Transactional //Making sure started games are rolled back, so other tests don't fail
    public void ensureOnlyOneGameCanBeStartedSimultaneously() {
        //Given
        Game game = gameManagement.createGame();
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        game.join(playerId);
        games.save(game);

        gameManagement.startGame(game);
        games.save(game);

        Game secondGame = gameManagement.createGame();
        secondGame.join(playerId);

        //When/Then
        Exception exception = assertThrows(
                IllegalStateException.class,
                () -> gameManagement.startGame(secondGame),
                "Game started although a second game is already started");

        //Then
    }
}
