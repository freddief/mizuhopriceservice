package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.InstrumentRepository;
import io.freddief.mizuho.priceservice.repository.PriceRepository;
import io.freddief.mizuho.priceservice.repository.VendorRepository;
import io.freddief.mizuho.priceservice.transformer.PriceTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class PriceService {

    private final PriceRepository priceRepository;
    private final VendorRepository vendorRepository;
    private final InstrumentRepository instrumentRepository;
    private final PriceTransformer priceTransformer;

    @Autowired
    public PriceService(PriceRepository priceRepository,
                        VendorRepository vendorRepository,
                        InstrumentRepository instrumentRepository,
                        PriceTransformer priceTransformer) {
        this.priceRepository = priceRepository;
        this.vendorRepository = vendorRepository;
        this.instrumentRepository = instrumentRepository;
        this.priceTransformer = priceTransformer;
    }

    public Price add(Price price) {
        priceRepository.add(priceTransformer.toDomain(price));
        return price;
    }

    public Collection<Price> findPricesByVendorId(String vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId);
        return priceRepository.findPricesByVendor(vendor)
            .stream()
            .map(priceTransformer::toDto)
            .collect(Collectors.toList());
    }

    public Collection<Price> findPricesByInstrumentId(String instrumentId) {
        Instrument instrument = instrumentRepository.findById(instrumentId);
        return priceRepository.findPricesByInstrument(instrument)
            .stream()
            .map(priceTransformer::toDto)
            .collect(Collectors.toList());
    }

}
