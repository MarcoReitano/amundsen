package dev.marcoreitano.master.amundsen.trading.internal;

import org.jmolecules.ddd.types.ValueObject;

import java.math.BigDecimal;

public record Transaction(BigDecimal amount) implements ValueObject {
}
