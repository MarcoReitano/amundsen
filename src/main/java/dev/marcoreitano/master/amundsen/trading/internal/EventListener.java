package dev.marcoreitano.master.amundsen.trading.internal;

import dev.marcoreitano.master.amundsen.game.events.GameCreated;
import dev.marcoreitano.master.amundsen.game.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.trading.TradingManagement;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class EventListener {

    private final TradingManagement tradingManagement;
    private final Shops shops;

    @ApplicationModuleListener
    public void openShopWhenGameIsCreated(GameCreated gameCreated) {
        tradingManagement.createShop(gameCreated.gameId());
    }

    @ApplicationModuleListener
    public void openAccountWhenPlayerJoins(PlayerJoined playerJoined) {
        var shop = shops.findByGameId(Association.forId(playerJoined.getGameId()));
        shop.ifPresent(it -> it.openAccount(playerJoined.getPlayerId(), BigDecimal.valueOf(500)));
    }
}
