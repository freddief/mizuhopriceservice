package io.freddief.mizuho.priceservice.domain.instrument;

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

}
