package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.InstrumentRepository;
import io.freddief.mizuho.priceservice.repository.PriceRepository;
import io.freddief.mizuho.priceservice.repository.VendorRepository;
import io.freddief.mizuho.priceservice.transformer.PriceTransformer;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;
    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private PriceTransformer priceTransformer;
    @InjectMocks
    private PriceService priceService;

    @Test
    public void add_transformsDto() {
        Price dto = mock(Price.class);

        priceService.add(dto);

        verify(priceTransformer).toDomain(dto);
    }

    @Test
    public void add_callsRepository() {
        Price dto = mock(Price.class);
        io.freddief.mizuho.priceservice.domain.price.Price domain = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);

        when(priceTransformer.toDomain(dto)).thenReturn(domain);

        priceService.add(dto);

        verify(priceRepository).add(domain);
    }

    @Test
    public void add_returnsPrice() {
        Price dto = mock(Price.class);

        Price returned = priceService.add(dto);

        assertThat(returned).isEqualTo(dto);
    }

    @Test
    public void findPricesByVendorId_findsVendor() {
        priceService.findPricesByVendorId("vendorId");

        verify(vendorRepository).findById("vendorId");
    }

    @Test
    public void findPricesByVendorId_findsPrices() {

        Vendor vendor = mock(Vendor.class);

        when(vendorRepository.findById("vendorId")).thenReturn(vendor);

        priceService.findPricesByVendorId("vendorId");

        verify(priceRepository).findPricesByVendor(vendor);
    }

    @Test
    public void findPricesByVendorId_transformsPrices() {

        io.freddief.mizuho.priceservice.domain.price.Price price1 = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);
        io.freddief.mizuho.priceservice.domain.price.Price price2 = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);

        when(priceRepository.findPricesByVendor(any())).thenReturn(Lists.newArrayList(price1, price2));

        priceService.findPricesByVendorId("vendorId");

        verify(priceTransformer, times(2)).toDto(any());
        verify(priceTransformer).toDto(price1);
        verify(priceTransformer).toDto(price2);
    }

    @Test
    public void findPricesByVendorId_returnsPrices() {

        io.freddief.mizuho.priceservice.domain.price.Price price1 = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);
        Price dtoPrice = mock(Price.class);

        when(priceRepository.findPricesByVendor(any())).thenReturn(Lists.newArrayList(price1));
        when(priceTransformer.toDto(any())).thenReturn(dtoPrice);

        Collection<Price> returned = priceService.findPricesByVendorId("vendorId");

        assertThat(returned).hasSize(1);
        assertThat(returned.iterator().next()).isEqualTo(dtoPrice);
    }

    @Test
    public void findPricesByInstrumentId_findsInstrument() {
        priceService.findPricesByInstrumentId("instrumentId");

        verify(instrumentRepository).findById("instrumentId");
    }

    @Test
    public void findPricesByInstrumentId_findsPrices() {

        Instrument instrument = mock(Instrument.class);

        when(instrumentRepository.findById("instrumentId")).thenReturn(instrument);

        priceService.findPricesByInstrumentId("instrumentId");

        verify(priceRepository).findPricesByInstrument(instrument);
    }

    @Test
    public void findPricesByInstrumentId_transformsPrices() {

        io.freddief.mizuho.priceservice.domain.price.Price price1 = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);
        io.freddief.mizuho.priceservice.domain.price.Price price2 = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);

        when(priceRepository.findPricesByInstrument(any())).thenReturn(Lists.newArrayList(price1, price2));

        priceService.findPricesByInstrumentId("instrumentId");

        verify(priceTransformer, times(2)).toDto(any());
        verify(priceTransformer).toDto(price1);
        verify(priceTransformer).toDto(price2);
    }

    @Test
    public void findPricesByInstrumentId_returnsPrices() {

        io.freddief.mizuho.priceservice.domain.price.Price price1 = mock(io.freddief.mizuho.priceservice.domain.price.Price.class);
        Price dtoPrice = mock(Price.class);

        when(priceRepository.findPricesByInstrument(any())).thenReturn(Lists.newArrayList(price1));
        when(priceTransformer.toDto(any())).thenReturn(dtoPrice);

        Collection<Price> returned = priceService.findPricesByInstrumentId("instrumentId");

        assertThat(returned).hasSize(1);
        assertThat(returned.iterator().next()).isEqualTo(dtoPrice);
    }

}