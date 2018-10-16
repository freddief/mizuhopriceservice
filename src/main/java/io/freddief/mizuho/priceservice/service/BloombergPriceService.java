package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.bloomberg.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.InstrumentRepository;
import io.freddief.mizuho.priceservice.repository.VendorRepository;
import io.freddief.mizuho.priceservice.transformer.BloombergPriceTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloombergPriceService {

    public static final String BLOOMBERG_VENDOR_ID = "bloombergVendorId";

    private final VendorRepository vendorRepository;
    private final InstrumentRepository instrumentRepository;
    private final BloombergPriceTransformer bloombergPriceTransformer;

    @Autowired
    public BloombergPriceService(VendorRepository vendorRepository,
                                 InstrumentRepository instrumentRepository,
                                 BloombergPriceTransformer bloombergPriceTransformer) {
        this.vendorRepository = vendorRepository;
        this.instrumentRepository = instrumentRepository;
        this.bloombergPriceTransformer = bloombergPriceTransformer;
    }

    public Price transform(BloombergPrice bloombergPrice) {
        Vendor vendor = vendorRepository.findById(BLOOMBERG_VENDOR_ID);
        Instrument instrument = instrumentRepository.findByCode(bloombergPrice.getCode());
        return bloombergPriceTransformer.transform(
            bloombergPrice,
            instrument,
            vendor
        );
    }

}
