package io.freddief.mizuho.priceservice.transformer;

import io.freddief.mizuho.priceservice.dto.instrument.Instrument;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.dto.vendor.Vendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.time.Instant;

import static io.freddief.mizuho.priceservice.domain.price.CurrencyCode.GBP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class PriceTransformerTest {

    @Mock
    private VendorTransformer vendorTransformer;
    @Mock
    private InstrumentTransformer instrumentTransformer;
    @InjectMocks
    private PriceTransformer priceTransformer;

    @Test
    public void toDomain_callsVendorTransformer() {

        Instrument instrument = mock(Instrument.class);
        Vendor vendor = mock(Vendor.class);

        Price dto = new Price(
            "priceId",
            instrument,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(10),
            GBP
        );

        priceTransformer.toDomain(dto);

        verify(vendorTransformer).toDomain(vendor);
    }

    @Test
    public void toDomain_callsInstrumentTransformer() {

        Instrument instrument = mock(Instrument.class);
        Vendor vendor = mock(Vendor.class);

        Price dto = new Price(
            "priceId",
            instrument,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(10),
            GBP
        );

        priceTransformer.toDomain(dto);

        verify(instrumentTransformer).toDomain(instrument);
    }

    @Test
    public void toDomain_transforms() {

        Instrument instrument = mock(Instrument.class);
        Vendor vendor = mock(Vendor.class);

        Instant now = Instant.now();

        Price dto = new Price(
            "priceId",
            instrument,
            vendor,
            now,
            BigDecimal.valueOf(10),
            GBP
        );

        io.freddief.mizuho.priceservice.domain.instrument.Instrument domainInstrument = mock(io.freddief.mizuho.priceservice.domain.instrument.Instrument.class);
        io.freddief.mizuho.priceservice.domain.vendor.Vendor domainVendor = mock(io.freddief.mizuho.priceservice.domain.vendor.Vendor.class);

        when(instrumentTransformer.toDomain(instrument)).thenReturn(domainInstrument);
        when(vendorTransformer.toDomain(vendor)).thenReturn(domainVendor);

        io.freddief.mizuho.priceservice.domain.price.Price domain = priceTransformer.toDomain(dto);

        assertThat(domain.getId()).isEqualTo("priceId");
        assertThat(domain.getInstrument()).isEqualTo(domainInstrument);
        assertThat(domain.getVendor()).isEqualTo(domainVendor);
        assertThat(domain.getTimestamp()).isEqualTo(now);
        assertThat(domain.getPrice()).isEqualTo(BigDecimal.valueOf(10));
        assertThat(domain.getCurrencyCode()).isEqualTo(GBP);

    }

    @Test
    public void toDto_callsVendorTransformer() {

        io.freddief.mizuho.priceservice.domain.instrument.Instrument instrument = mock(io.freddief.mizuho.priceservice.domain.instrument.Instrument.class);
        io.freddief.mizuho.priceservice.domain.vendor.Vendor vendor = mock(io.freddief.mizuho.priceservice.domain.vendor.Vendor.class);

        io.freddief.mizuho.priceservice.domain.price.Price domain = new io.freddief.mizuho.priceservice.domain.price.Price(
            "priceId",
            instrument,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(10),
            GBP
        );

        priceTransformer.toDto(domain);

        verify(vendorTransformer).toDto(vendor);
    }

    @Test
    public void toDto_callsInstrumentTransformer() {

        io.freddief.mizuho.priceservice.domain.instrument.Instrument instrument = mock(io.freddief.mizuho.priceservice.domain.instrument.Instrument.class);
        io.freddief.mizuho.priceservice.domain.vendor.Vendor vendor = mock(io.freddief.mizuho.priceservice.domain.vendor.Vendor.class);

        io.freddief.mizuho.priceservice.domain.price.Price domain = new io.freddief.mizuho.priceservice.domain.price.Price(
            "priceId",
            instrument,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(10),
            GBP
        );

        priceTransformer.toDto(domain);

        verify(instrumentTransformer).toDto(instrument);
    }

    @Test
    public void toDto_transforms() {

        io.freddief.mizuho.priceservice.domain.instrument.Instrument instrument = mock(io.freddief.mizuho.priceservice.domain.instrument.Instrument.class);
        io.freddief.mizuho.priceservice.domain.vendor.Vendor vendor = mock(io.freddief.mizuho.priceservice.domain.vendor.Vendor.class);

        Instant now = Instant.now();

        io.freddief.mizuho.priceservice.domain.price.Price domain = new io.freddief.mizuho.priceservice.domain.price.Price(
            "priceId",
            instrument,
            vendor,
            now,
            BigDecimal.valueOf(10),
            GBP
        );

        Instrument dtoInstrument = mock(Instrument.class);
        Vendor dtoVendor = mock(Vendor.class);

        when(instrumentTransformer.toDto(instrument)).thenReturn(dtoInstrument);
        when(vendorTransformer.toDto(vendor)).thenReturn(dtoVendor);

        Price dto = priceTransformer.toDto(domain);

        assertThat(dto.getId()).isEqualTo("priceId");
        assertThat(dto.getInstrument()).isEqualTo(dtoInstrument);
        assertThat(dto.getVendor()).isEqualTo(dtoVendor);
        assertThat(dto.getTimestamp()).isEqualTo(now);
        assertThat(dto.getPrice()).isEqualTo(BigDecimal.valueOf(10));
        assertThat(dto.getCurrencyCode()).isEqualTo(GBP);

    }

}