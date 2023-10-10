package dev.marcoreitano.master.amundsen.trading.internal;

import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import dev.marcoreitano.master.amundsen.trading.Shop;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.jmolecules.ddd.types.Association;
import org.jmolecules.ddd.types.Entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Transactional
public class Account implements Entity<Shop, AccountId> {

    private final AccountId id;
    private final Association<Player, PlayerId> player;

    @ElementCollection(fetch = FetchType.EAGER)
    private final List<Transaction> transactions;

    public Account(PlayerId playerId) {
        this.id = new AccountId(UUID.randomUUID());
        this.player = Association.forId(playerId);

        this.transactions = new ArrayList<>();
    }

    public BigDecimal getBalance() {
        return transactions.stream()
                .map(Transaction::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void deposit(BigDecimal amount) {
        //TODO: Need to validate if amount positive here!?
        this.transactions.add(new Transaction(amount));
    }

    public void charge(BigDecimal amount) {
        //TODO: Need to validate if amount negative here
        this.transactions.add((new Transaction(amount)));
    }
}
