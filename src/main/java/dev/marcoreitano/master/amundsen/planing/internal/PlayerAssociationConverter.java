package dev.marcoreitano.master.amundsen.planing.internal;

import dev.marcoreitano.master.amundsen.registration.Player;
import dev.marcoreitano.master.amundsen.registration.PlayerId;
import jakarta.persistence.Converter;
import org.jmolecules.spring.jpa.JakartaPersistenceAssociationAttributeConverter;

import java.util.UUID;

@Converter
public class PlayerAssociationConverter extends JakartaPersistenceAssociationAttributeConverter<Player, PlayerId, UUID> {
    public PlayerAssociationConverter() {
        super(PlayerId.class);
    }
}
