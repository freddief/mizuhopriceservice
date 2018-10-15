package io.freddief.mizuho.priceservice.repository;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;

public interface InstrumentRepository {

    void add(Instrument vendor);

    Instrument findById(String id);

}
