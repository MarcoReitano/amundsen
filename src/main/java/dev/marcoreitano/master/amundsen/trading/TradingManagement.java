package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.planing.GamePlanId;
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

    public ShopId createShop(GamePlanId gamePlanId) {
        Shop shop = shops.save(new Shop(gamePlanId));

        events.publishEvent(new ShopCreated(gamePlanId, shop.getId()));

        return shop.getId();
    }
}




