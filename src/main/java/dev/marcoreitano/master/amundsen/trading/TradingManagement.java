package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.game.GameId;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jmolecules.ddd.annotation.Service;
import org.springframework.context.ApplicationEventPublisher;

@Service
@Transactional
@RequiredArgsConstructor
public class TradingManagement {

    private final ApplicationEventPublisher events;

    private final Shops shops;

    public ShopId createShop(GameId gameId) {
        Shop shop = shops.save(new Shop(gameId));

        events.publishEvent(new ShopCreated(gameId, shop.getId()));

        return shop.getId();
    }
}




