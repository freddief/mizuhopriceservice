package io.freddief.mizuho.priceservice.repository;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.instrument.Stock;
import io.freddief.mizuho.priceservice.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryInstrumentRepository implements InstrumentRepository {

    private final Map<String, Instrument> instrumentsByIdMap;

    @Autowired
    public InMemoryInstrumentRepository(Map<String, Instrument> instrumentsByIdMap) {
        this.instrumentsByIdMap = instrumentsByIdMap;
        init();
    }

    @Override
    public void add(Instrument vendor) {
        instrumentsByIdMap.put(vendor.getId(), vendor);
    }

    @Override
    public Instrument findById(String id) {
        return instrumentsByIdMap.computeIfAbsent(id, key -> {
            throw new NotFoundException("Instrument with id='%s' not found", key);
        });
    }

    private void init() {
        add(new Stock("barclaysStockId", "BARC"));
        add(new Stock("hsbcStockId", "HSBA"));
        add(new Stock("bunzlStockId", "BNZL"));
    }

}
