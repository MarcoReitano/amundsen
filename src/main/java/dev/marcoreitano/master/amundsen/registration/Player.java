package dev.marcoreitano.master.amundsen.registration;

import lombok.Getter;
import org.jmolecules.ddd.types.AggregateRoot;

import java.util.UUID;

@Getter
public class Player implements AggregateRoot<Player, PlayerId> {

    public final PlayerId id;

    protected Player() {
        this.id = new PlayerId(UUID.randomUUID());
    }
}