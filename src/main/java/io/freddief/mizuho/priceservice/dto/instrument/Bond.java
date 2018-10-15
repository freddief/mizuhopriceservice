package io.freddief.mizuho.priceservice.dto.instrument;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Bond implements Instrument {

    private final String id;
    private final String code;

    @JsonCreator
    public Bond(@JsonProperty("id") String id,
                @JsonProperty("code") String code) {
        this.id = id;
        this.code = code;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Bond{" +
            "id='" + id + '\'' +
            ", code='" + code + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bond bond = (Bond) o;
        return Objects.equals(getId(), bond.getId()) &&
            Objects.equals(getCode(), bond.getCode());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getCode());
    }
}
