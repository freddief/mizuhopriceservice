package io.freddief.mizuho.priceservice.repository;

import io.freddief.mizuho.priceservice.domain.vendor.Vendor;

public interface VendorRepository {

    void add(Vendor vendor);

    Vendor findById(String id);

}
