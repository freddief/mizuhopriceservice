package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.domain.price.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceTransformer {

    private final VendorTransformer vendorTransformer;
    private final InstrumentTransformer instrumentTransformer;

    @Autowired
    public PriceTransformer(VendorTransformer vendorTransformer,
                            InstrumentTransformer instrumentTransformer) {
        this.vendorTransformer = vendorTransformer;
        this.instrumentTransformer = instrumentTransformer;
    }

    public Price toDomain(io.freddief.mizuho.priceservice.dto.price.Price dto) {
        return new Price(
            dto.getId(),
            dto.getInstrument() == null ? null : instrumentTransformer.toDomain(dto.getInstrument()),
            dto.getVendor() == null ? null : vendorTransformer.toDomain(dto.getVendor()),
            dto.getTimestamp(),
            dto.getPrice(),
            dto.getCurrencyCode()
        );
    }

    public io.freddief.mizuho.priceservice.dto.price.Price toDto(Price domain) {
        return new io.freddief.mizuho.priceservice.dto.price.Price(
            domain.getId(),
            domain.getInstrument() == null ? null : instrumentTransformer.toDto(domain.getInstrument()),
            domain.getVendor() == null ? null : vendorTransformer.toDto(domain.getVendor()),
            domain.getTimestamp(),
            domain.getPrice(),
            domain.getCurrencyCode()
        );
    }

}
