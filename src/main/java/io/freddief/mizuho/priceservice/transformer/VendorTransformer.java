package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VendorTransformer {

    @Autowired
    public VendorTransformer() {
    }

    public Vendor toDomain(io.freddief.mizuho.priceservice.dto.vendor.Vendor dto) {
        return new Vendor(
            dto.getId(),
            dto.getName()
        );
    }

    public io.freddief.mizuho.priceservice.dto.vendor.Vendor toDto(Vendor domain) {
        return new io.freddief.mizuho.priceservice.dto.vendor.Vendor(
            domain.getId(),
            domain.getName()
        );
    }

}
