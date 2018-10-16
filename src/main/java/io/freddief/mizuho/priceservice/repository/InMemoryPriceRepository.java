package io.freddief.mizuho.priceservice.repository;

import com.google.common.collect.Table;
import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.price.Price;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;

@Component
public class InMemoryPriceRepository implements PriceRepository {

    private static final Duration EXPIRY_DURATION = Duration.ofDays(30);

    private final Table<Vendor, Instrument, Price> pricesTable;

    @Autowired
    public InMemoryPriceRepository(Table<Vendor, Instrument, Price> pricesTable) {
        this.pricesTable = pricesTable;
    }

    @Override
    public void add(Price price) {
        pricesTable.put(price.getVendor(),
                        price.getInstrument(),
                        price);
    }

    @Override
    public Collection<Price> findPricesByVendor(Vendor vendor) {
        return pricesTable
            .row(vendor)
            .values();
    }

    @Override
    public Collection<Price> findPricesByInstrument(Instrument instrument) {
        return pricesTable
            .column(instrument)
            .values();
    }

    @Override
    @Scheduled(fixedDelay = 1000)
    public void cleanUp() {
        pricesTable.cellSet()
            .removeIf(cell ->
                          cell.getValue().getTimestamp()
                              .isBefore(Instant.now().minus(EXPIRY_DURATION)));

    }
}
