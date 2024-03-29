package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.engine.GameId;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanScheduled;
import dev.marcoreitano.master.amundsen.planing.events.GamePlanned;
import dev.marcoreitano.master.amundsen.planing.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.junit.jupiter.api.Test;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.PublishedEvents;
import org.springframework.modulith.test.Scenario;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ApplicationModuleTest
@RequiredArgsConstructor
public class TradingTests {

    private final TradingManagement tradingManagement;
    private final Shops shops;


    @Test
    public void createShop() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());

        //When
        ShopId shopId = tradingManagement.createShop(gameId);

        //Then
        assertNotNull(shopId);
    }

    @Test
    public void persistShop() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());

        //When
        ShopId shopId = tradingManagement.createShop(gameId);
        var shop = shops.findById(shopId);

        //Then
        assertTrue(shop.isPresent());
        assertEquals(shopId, shop.get().getId());
    }

    @Test
    public void createAccount() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        ShopId shopId = tradingManagement.createShop(gameId);

        //When
        var shop = shops.findById(shopId);
        assertTrue(shop.isPresent());
        shop.get().openAccount(playerId, BigDecimal.valueOf(500));

        //Then
        var playerAccount = shop.get().getAccount(playerId);

        assertTrue(playerAccount.isPresent());
        assertEquals(playerId, playerAccount.get().getPlayer().getId());
    }

    @Test
    public void persistAccount() {
        //Given
        GameId gameId = new GameId(UUID.randomUUID());
        PlayerId playerId = new PlayerId(UUID.randomUUID());
        ShopId shopId = tradingManagement.createShop(gameId);

        //When
        var shop = shops.findById(shopId);
        assertTrue(shop.isPresent());
        shop.get().openAccount(playerId, BigDecimal.valueOf(500));
        shops.save(shop.get());

        //Then
        var persistedShop = shops.findById(shopId);
        assertTrue(persistedShop.isPresent());

        var playerAccount = persistedShop.get().getAccount(playerId);
        assertTrue(playerAccount.isPresent());

        assertEquals(playerId, playerAccount.get().getPlayer().getId());
    }

    @Test
    public void createShopWhenGameIsCreated(Scenario scenario) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        GameId gameId = new GameId(gamePlanId.id());

        //When/Then
        scenario.publish(new GamePlanned(gamePlanId, 1, 1))
                .andWaitForEventOfType(ShopCreated.class)
                .matching(event -> event.gameId().equals(gameId)).toArrive();

        //Then
    }

    @Test
    public void openAccountWhenPlayerJoins(Scenario scenario) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        GameId gameId = new GameId(gamePlanId.id());

        PlayerId playerId = new PlayerId(UUID.randomUUID());
        ShopId shopId = tradingManagement.createShop(gameId);

        //When/Then
        scenario.publish(new PlayerJoined(gamePlanId, playerId))
                .andWaitForStateChange(() -> {
                    var shop = shops.findById(shopId);
                    assertTrue(shop.isPresent());
                    return shop.get().getAccount(playerId);
                }, Optional::isPresent)
                .andVerify(account -> {
                    assertTrue(account.isPresent());
                    account.get().getPlayer().equals(playerId);
                });
        //Then
    }

    @Test
    public void depositInitialBalanceWhenGameStarts(Scenario scenario) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        GameId gameId = new GameId(gamePlanId.id());

        PlayerId playerId = new PlayerId(UUID.randomUUID());
        ShopId shopId = tradingManagement.createShop(gameId);
        var shop = shops.findById(shopId);
        shop.ifPresent(it -> it.openAccount(playerId, BigDecimal.valueOf(500)));

        shops.save(shop.get());

        //When/Then
        scenario.publish(new GamePlanScheduled(gamePlanId, 20, Duration.ofSeconds(20), Set.of(Association.forId(playerId))))
                .andWaitForStateChange(() -> {
                    var persistedShop = shops.findById(shopId);
                    assertTrue(persistedShop.isPresent());
                    var playerAccount = persistedShop.get().getAccount(playerId);
                    return playerAccount.get();
                }, account -> account.getTransactions().size() == 1)
                .andVerify(account -> {
                    account.getBalance().equals(new BigDecimal(500));
                });
    }

    @Test
    public void buyNewRobotForPlayerShouldPublishPlayerBought(PublishedEvents events) {
        //Given
        GamePlanId gamePlanId = new GamePlanId(UUID.randomUUID());
        GameId gameId = new GameId(gamePlanId.id());

        PlayerId playerId = new PlayerId(UUID.randomUUID());
        ShopId shopId = tradingManagement.createShop(gameId);
        var shop = shops.findById(shopId);
        shop.ifPresent(it -> it.openAccount(playerId, BigDecimal.valueOf(500)));
        shops.save(shop.get());

        //When
        shop.get().buyRobot(playerId);
        shops.save(shop.get());

        //Then
        var matchingEvents = events.ofType(RobotBought.class)
                .matching(RobotBought::playerId, playerId);
        assertThat(matchingEvents).hasSize(1);
        var account = shop.get().getAccount(playerId);
        assertEquals(BigDecimal.valueOf(400), account.get().getBalance());
    }
}
