package io.freddief.mizuho.priceservice.dto.bloomberg;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

public class BloombergPrice {

    private final String code;
    private final BigDecimal price;
    private final String currency;

    @JsonCreator
    public BloombergPrice(@JsonProperty("code") String code,
                          @JsonProperty("price") BigDecimal price,
                          @JsonProperty("currency") String currency) {
        this.code = code;
        this.price = price;
        this.currency = currency;
    }

    public String getCode() {
        return code;
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
        BloombergPrice that = (BloombergPrice) o;
        return Objects.equals(getCode(), that.getCode()) &&
            Objects.equals(getPrice(), that.getPrice()) &&
            Objects.equals(getCurrency(), that.getCurrency());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getCode(), getPrice(), getCurrency());
    }
}
