package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.price.IgGroupPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.InstrumentRepository;
import io.freddief.mizuho.priceservice.repository.VendorRepository;
import io.freddief.mizuho.priceservice.transformer.IgGroupPriceTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IgGroupPriceService {

    public static final String IG_GROUP_VENDOR_ID = "igGroupVendorId";

    private final VendorRepository vendorRepository;
    private final InstrumentRepository instrumentRepository;
    private final IgGroupPriceTransformer bloombergPriceTransformer;

    @Autowired
    public IgGroupPriceService(VendorRepository vendorRepository,
                               InstrumentRepository instrumentRepository,
                               IgGroupPriceTransformer bloombergPriceTransformer) {
        this.vendorRepository = vendorRepository;
        this.instrumentRepository = instrumentRepository;
        this.bloombergPriceTransformer = bloombergPriceTransformer;
    }

    public Price transform(IgGroupPrice igGroupPrice) {
        Vendor vendor = vendorRepository.findById(IG_GROUP_VENDOR_ID);
        Instrument instrument = instrumentRepository.findByCode(igGroupPrice.getInstrumentCode());
        return bloombergPriceTransformer.transform(
            igGroupPrice,
            instrument,
            vendor
        );
    }

}
