package dev.marcoreitano.master.amundsen.trading;

import dev.marcoreitano.master.amundsen.planing.GamePlan;
import dev.marcoreitano.master.amundsen.planing.GamePlanId;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.trading.internal.Account;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;
import org.jmolecules.ddd.types.Association;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Transactional
public class Shop extends AbstractAggregateRoot<Shop> implements AggregateRoot<Shop, ShopId> {

    private final ShopId id;
    private final Association<GamePlan, GamePlanId> gameId;

    @Getter(AccessLevel.NONE)
    private final List<Account> accounts;

    protected Shop(GamePlanId gamePlanId) {
        this.id = new ShopId(UUID.randomUUID());
        this.gameId = Association.forId(gamePlanId);
        this.accounts = new ArrayList<>();
    }

    public void openAccount(PlayerId playerId, BigDecimal initialBalance) {
        var account = new Account(playerId);
        account.deposit(initialBalance);
        this.accounts.add(account);
    }

    private void chargeAccount(PlayerId playerId, BigDecimal amount) {
        this.accounts.stream()
                .filter(it -> it.getPlayer().getId().equals(playerId))
                .findFirst()
                .ifPresent(account -> account.charge(amount));

    }

    public Optional<Account> getAccount(PlayerId playerId) {
        return accounts.stream()
                .filter(it -> it.getPlayer().getId().equals(playerId))
                .findFirst();
    }

    //Only robots for now
    public void buyRobot(PlayerId playerId) {
        chargeAccount(playerId, BigDecimal.valueOf(-100));

        registerEvent(new RobotBought(gameId.getId(), playerId));
    }
}
