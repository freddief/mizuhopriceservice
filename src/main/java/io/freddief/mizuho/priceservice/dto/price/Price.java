package io.freddief.mizuho.priceservice.dto.price;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.event.Event;
import io.freddief.mizuho.priceservice.dto.instrument.Instrument;
import io.freddief.mizuho.priceservice.dto.vendor.Vendor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public class Price implements Event {

    private final String id;
    private final Instrument instrument;
    private final Vendor vendor;
    private final Instant timestamp;
    private final BigDecimal price;
    private final CurrencyCode currencyCode;

    @JsonCreator
    public Price(@JsonProperty("id") String id,
                 @JsonProperty("instrument") Instrument instrument,
                 @JsonProperty("vendor") Vendor vendor,
                 @JsonProperty("timestamp") Instant timestamp,
                 @JsonProperty("price") BigDecimal price,
                 @JsonProperty("currencyCode") CurrencyCode currencyCode) {
        this.id = id;
        this.instrument = instrument;
        this.vendor = vendor;
        this.timestamp = timestamp;
        this.price = price;
        this.currencyCode = currencyCode;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Instant getTimestamp() {
        return timestamp;
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

    @Override
    public String toString() {
        return "Price{" +
            "id='" + id + '\'' +
            ", instrument=" + instrument +
            ", vendor=" + vendor +
            ", timestamp=" + timestamp +
            ", price=" + price +
            ", currencyCode=" + currencyCode +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price1 = (Price) o;
        return Objects.equals(getId(), price1.getId()) &&
            Objects.equals(getInstrument(), price1.getInstrument()) &&
            Objects.equals(getVendor(), price1.getVendor()) &&
            Objects.equals(getTimestamp(), price1.getTimestamp()) &&
            Objects.equals(getPrice(), price1.getPrice()) &&
            getCurrencyCode() == price1.getCurrencyCode();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getInstrument(), getVendor(), getTimestamp(), getPrice(), getCurrencyCode());
    }
}
