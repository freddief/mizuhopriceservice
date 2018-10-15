package io.freddief.mizuho.priceservice.domain.instrument;

import java.util.Objects;

import static io.freddief.mizuho.priceservice.domain.instrument.InstrumentType.BOND;

public class Bond implements Instrument {

    private static final InstrumentType INSTRUMENT_TYPE = BOND;

    private final String id;
    private final String code;

    public Bond(String id, String code) {
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
    public InstrumentType getType() {
        return INSTRUMENT_TYPE;
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
