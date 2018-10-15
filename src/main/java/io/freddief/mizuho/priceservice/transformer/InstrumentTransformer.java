package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.dto.instrument.Bond;
import io.freddief.mizuho.priceservice.dto.instrument.Derivative;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InstrumentTransformer {

    @Autowired
    public InstrumentTransformer() {
    }

    public Instrument toDomain(io.freddief.mizuho.priceservice.dto.instrument.Instrument dto) {
        if (dto instanceof Stock) {
            return new io.freddief.mizuho.priceservice.domain.instrument.Stock(
                dto.getId(),
                dto.getCode()
            );
        }
        if (dto instanceof Bond) {
            return new io.freddief.mizuho.priceservice.domain.instrument.Bond(
                dto.getId(),
                dto.getCode()
            );
        }
        if (dto instanceof Derivative) {
            return new io.freddief.mizuho.priceservice.domain.instrument.Derivative(
                dto.getId(),
                dto.getCode()
            );
        }
        throw new IllegalArgumentException(String.format("Cannot transform Instrument of type=%s", dto.getClass()));
    }

    public io.freddief.mizuho.priceservice.dto.instrument.Instrument toDto(Instrument domain) {
        if (domain instanceof io.freddief.mizuho.priceservice.domain.instrument.Stock) {
            return new Stock(
                domain.getId(),
                domain.getCode()
            );
        }
        if (domain instanceof io.freddief.mizuho.priceservice.domain.instrument.Bond) {
            return new Bond(
                domain.getId(),
                domain.getCode()
            );
        }
        if (domain instanceof io.freddief.mizuho.priceservice.domain.instrument.Derivative) {
            return new Derivative(
                domain.getId(),
                domain.getCode()
            );
        }
        throw new IllegalArgumentException(String.format("Cannot transform Instrument of type=%s", domain.getClass()));
    }

}
