package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.dto.vendor.Vendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VendorTransformerTest {

    @InjectMocks
    private VendorTransformer vendorTransformer;

    @Test
    public void toDomain() {

        Vendor dto = new Vendor(
            "id",
            "name"
        );

        io.freddief.mizuho.priceservice.domain.vendor.Vendor domain = vendorTransformer.toDomain(dto);

        assertThat(domain.getId()).isEqualTo("id");
        assertThat(domain.getName()).isEqualTo("name");
    }

    @Test
    public void toDto() {

        io.freddief.mizuho.priceservice.domain.vendor.Vendor domain = new io.freddief.mizuho.priceservice.domain.vendor.Vendor(
            "id",
            "name"
        );

        Vendor dto = vendorTransformer.toDto(domain);

        assertThat(dto.getId()).isEqualTo("id");
        assertThat(dto.getName()).isEqualTo("name");
    }

}