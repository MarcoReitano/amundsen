package dev.marcoreitano.master.amundsen.trading.internal;

import dev.marcoreitano.master.amundsen.game.GameCreated;
import dev.marcoreitano.master.amundsen.game.PlayerJoined;
import dev.marcoreitano.master.amundsen.trading.Shops;
import dev.marcoreitano.master.amundsen.trading.TradingManagement;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.types.Association;
import org.springframework.modulith.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class Eventhandler {

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
