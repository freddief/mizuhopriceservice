package io.freddief.mizuho.priceservice.dto.price;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class IgGroupPrice {

    private final String instrumentCode;
    private final String currencyCode;
    private final BigDecimal price;

    @JsonCreator
    public IgGroupPrice(@JsonProperty("instrumentCode") String instrumentCode,
                        @JsonProperty("currencyCode") String currencyCode,
                        @JsonProperty("price") BigDecimal price) {
        this.instrumentCode = instrumentCode;
        this.currencyCode = currencyCode;
        this.price = price;
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IgGroupPrice that = (IgGroupPrice) o;
        return Objects.equals(getInstrumentCode(), that.getInstrumentCode()) &&
            Objects.equals(getCurrencyCode(), that.getCurrencyCode()) &&
            Objects.equals(getPrice(), that.getPrice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getInstrumentCode(), getCurrencyCode(), getPrice());
    }
}
