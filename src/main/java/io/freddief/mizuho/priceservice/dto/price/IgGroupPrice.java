package io.freddief.mizuho.priceservice.dto.price;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class IgGroupPrice {

    private final String instrumentCode;
    private final MonetaryAmount price;

    @JsonCreator
    public IgGroupPrice(@JsonProperty("instrumentCode") String instrumentCode,
                        @JsonProperty("price") MonetaryAmount price) {
        this.instrumentCode = instrumentCode;
        this.price = price;
    }

    public static class MonetaryAmount {

        private final BigDecimal price;
        private final String currency;

        @JsonCreator
        public MonetaryAmount(@JsonProperty("price") BigDecimal price,
                              @JsonProperty("currency") String currency) {
            this.price = price;
            this.currency = currency;
        }

        public BigDecimal getPrice() {
            return price;
        }

        public String getCurrency() {
            return currency;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MonetaryAmount that = (MonetaryAmount) o;
            return Objects.equals(getPrice(), that.getPrice()) &&
                Objects.equals(getCurrency(), that.getCurrency());
        }

        @Override
        public int hashCode() {

            return Objects.hash(getPrice(), getCurrency());
        }
    }

    public String getInstrumentCode() {
        return instrumentCode;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IgGroupPrice that = (IgGroupPrice) o;
        return Objects.equals(getInstrumentCode(), that.getInstrumentCode()) &&
            Objects.equals(getPrice(), that.getPrice());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getInstrumentCode(), getPrice());
    }
}
