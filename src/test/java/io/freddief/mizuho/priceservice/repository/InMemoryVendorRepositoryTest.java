package io.freddief.mizuho.priceservice.repository;

import com.google.common.collect.Maps;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.exception.NotFoundException;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InMemoryVendorRepositoryTest {

    @Test
    public void add_callsPut() {
        Map<String, Vendor>  map = mock(Map.class);
        Vendor vendor = mock(Vendor.class);

        when(vendor.getId()).thenReturn("vendorId");

        InMemoryVendorRepository repository = new InMemoryVendorRepository(map);

        repository.add(vendor);

        verify(map).put("vendorId", vendor);
    }

    @Test
    public void findById() {

        Vendor vendor = new Vendor(
            "id",
            "name"
        );

        InMemoryVendorRepository repository = new InMemoryVendorRepository(Maps.newHashMap());

        repository.add(vendor);

        Vendor returned = repository.findById("id");

        assertThat(returned).isEqualTo(
            new Vendor(
                "id",
                "name"
            )
        );
    }

    @Test(expected = NotFoundException.class)
    public void findById_whenVendorNotFound_thenThrowException() {

        InMemoryVendorRepository repository = new InMemoryVendorRepository(Maps.newHashMap());

        repository.findById("id");
    }

    @Test
    public void constructor_initialisesWithDefaultVendors() {

        InMemoryVendorRepository repository = new InMemoryVendorRepository(Maps.newHashMap());

        Vendor bloombergVendor = repository.findById("bloombergVendorId");
        Vendor igGroupVendor= repository.findById("igGroupVendorId");

        assertThat(bloombergVendor).isEqualTo(
            new Vendor("bloombergVendorId", "Bloomberg")
        );

        assertThat(igGroupVendor).isEqualTo(
            new Vendor("igGroupVendorId", "IG Group")
        );

    }

}