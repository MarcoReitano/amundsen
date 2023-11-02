package dev.marcoreitano.master.amundsen.trading.internal;

import dev.marcoreitano.master.amundsen.planing.events.GamePlanned;
import dev.marcoreitano.master.amundsen.planing.events.PlayerJoined;
import dev.marcoreitano.master.amundsen.trading.Shops;
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
    public void openShopWhenGameIsCreated(GamePlanned gamePlanned) {
        tradingManagement.createShop(gamePlanned.gamePlanId());
    }

    @ApplicationModuleListener
    public void openAccountWhenPlayerJoins(PlayerJoined playerJoined) {
        var shop = shops.findByGameId(Association.forId(playerJoined.getGamePlanId()));
        shop.ifPresent(it -> it.openAccount(playerJoined.getPlayerId(), BigDecimal.valueOf(500)));
    }
}
