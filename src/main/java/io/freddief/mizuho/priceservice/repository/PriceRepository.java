package io.freddief.mizuho.priceservice.repository;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.price.Price;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;

import java.util.Collection;

public interface PriceRepository {

    void add(Price price);

    Collection<Price> findPricesByVendor(Vendor vendor);

    Collection<Price> findPricesByInstrument(Instrument instrument);

}
