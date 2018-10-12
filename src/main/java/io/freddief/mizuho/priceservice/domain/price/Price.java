package io.freddief.mizuho.priceservice.domain.price;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;

import java.math.BigDecimal;

public class Price {

    private final String id;
    private final Instrument instrument;
    private final Vendor vendor;
    private final BigDecimal price;
    private final CurrencyCode currencyCode;

    public Price(String id, Instrument instrument, Vendor vendor, BigDecimal price, CurrencyCode currencyCode) {
        this.id = id;
        this.instrument = instrument;
        this.vendor = vendor;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    public String getId() {
        return id;
    }

    public Instrument getInstrument() {
        return instrument;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public CurrencyCode getCurrencyCode() {
        return currencyCode;
    }
}
