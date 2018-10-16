package io.freddief.mizuho.priceservice.repository;

import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.exception.NotFoundException;
import io.freddief.mizuho.priceservice.service.BloombergPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryVendorRepository implements VendorRepository {

    private final Map<String, Vendor> vendorsByIdMap;

    @Autowired
    public InMemoryVendorRepository(Map<String, Vendor> vendorsByIdMap) {
        this.vendorsByIdMap = vendorsByIdMap;
        init();
    }

    @Override
    public void add(Vendor vendor) {
        vendorsByIdMap.put(vendor.getId(), vendor);
    }

    @Override
    public Vendor findById(String id) {
        return vendorsByIdMap.computeIfAbsent(id, key -> {
            throw new NotFoundException("Vendor with id='%s' not found", key);
        });
    }

    private void init() {
        add(new Vendor(BloombergPriceService.BLOOMBERG_VENDOR_ID, "Bloomberg"));
        add(new Vendor("igGroupVendorId", "IG Group"));
    }

}
