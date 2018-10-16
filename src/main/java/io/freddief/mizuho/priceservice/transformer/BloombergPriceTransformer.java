package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.bloomberg.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class BloombergPriceTransformer {

    private final InstrumentTransformer instrumentTransformer;
    private final VendorTransformer vendorTransformer;

    @Autowired
    public BloombergPriceTransformer(InstrumentTransformer instrumentTransformer,
                                     VendorTransformer vendorTransformer) {
        this.instrumentTransformer = instrumentTransformer;
        this.vendorTransformer = vendorTransformer;
    }

    public Price transform(BloombergPrice bloombergPrice,
                           Instrument instrument,
                           Vendor vendor) {

        return new Price(
            UUID.randomUUID().toString(),
            instrumentTransformer.toDto(instrument),
            vendorTransformer.toDto(vendor),
            Instant.now(),
            bloombergPrice.getPrice(),
            CurrencyCode.valueOf(bloombergPrice.getCurrency())
        );
    }
}
