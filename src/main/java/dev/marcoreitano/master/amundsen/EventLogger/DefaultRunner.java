package dev.marcoreitano.master.amundsen.EventLogger;

import dev.marcoreitano.master.amundsen.AmundsenApplication;
import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanManagement;
import dev.marcoreitano.master.amundsen.planing.GamePlans;
import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.RegistrationManagement;
import dev.marcoreitano.master.amundsen.trading.Shop;
import dev.marcoreitano.master.amundsen.trading.ShopCreated;
import dev.marcoreitano.master.amundsen.trading.Shops;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class DefaultRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AmundsenApplication.class);

    private final RegistrationManagement registrationManagement;
    private final GamePlanManagement gamePlanManagement;
    private final GamePlans gamePlans;
    private final Shops shops;

    private Shop shop;
    private GamePlan gamePlan;
    private Player player;

    @Override
    public void run(String... args) throws Exception {
        defaultGameflow();
    }

    public void defaultGameflow() {
        LOG.info("Running a default Gameflow...");
        player = registrationManagement.registerPlayer();
        gamePlan = gamePlanManagement.planGame(1, 2, Duration.ofMillis(1000));
        gamePlan.join(player.getId());
        gamePlan.schedule();
        gamePlans.save(gamePlan);
    }

//    @ApplicationModuleListener
//    void on(WorldCreated worldCreated) {
//        shop.buyRobot(player.getId());
//        shops.save(shop);
//    }

    @ApplicationModuleListener
    void onShopCreated(ShopCreated shopCreated) {
        shops.findById(shopCreated.shopId()).ifPresent(s -> {
            shop = s;
        });
    }
}
