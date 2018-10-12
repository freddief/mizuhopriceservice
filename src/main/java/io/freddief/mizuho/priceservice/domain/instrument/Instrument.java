package io.freddief.mizuho.priceservice.domain.instrument;

public interface Instrument {

    String getId();

    String getCode();

    InstrumentType getType();

}
