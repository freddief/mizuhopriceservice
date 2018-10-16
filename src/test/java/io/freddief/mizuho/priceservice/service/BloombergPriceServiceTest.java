package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.bloomberg.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.InstrumentRepository;
import io.freddief.mizuho.priceservice.repository.VendorRepository;
import io.freddief.mizuho.priceservice.transformer.BloombergPriceTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BloombergPriceServiceTest {

    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private BloombergPriceTransformer bloombergPriceTransformer;
    @InjectMocks
    private BloombergPriceService bloombergPriceService;

    @Test
    public void transform_findsVendor() {
        BloombergPrice bloombergPrice = mock(BloombergPrice.class);

        bloombergPriceService.transform(bloombergPrice);

        verify(vendorRepository).findById(BloombergPriceService.BLOOMBERG_VENDOR_ID);
    }


    @Test
    public void transform_findsInstrument() {
        BloombergPrice bloombergPrice = mock(BloombergPrice.class);

        when(bloombergPrice.getCode()).thenReturn("code");

        bloombergPriceService.transform(bloombergPrice);

        verify(instrumentRepository).findByCode("code");
    }

    @Test
    public void transform_callsTransformer() {
        BloombergPrice bloombergPrice = mock(BloombergPrice.class);
        Vendor vendor = mock(Vendor.class);
        Instrument instrument = mock(Instrument.class);

        when(vendorRepository.findById(any())).thenReturn(vendor);
        when(instrumentRepository.findByCode(any())).thenReturn(instrument);

        bloombergPriceService.transform(bloombergPrice);

        verify(bloombergPriceTransformer).transform(
            bloombergPrice,
            instrument,
            vendor
        );
    }

    @Test
    public void transform_returnsFromTransformer() {
        BloombergPrice bloombergPrice = mock(BloombergPrice.class);
        Price price = mock(Price.class);

        when(bloombergPriceTransformer.transform(any(), any(), any())).thenReturn(price);

        Price returned = bloombergPriceService.transform(bloombergPrice);

        assertThat(returned).isEqualTo(price);
    }

}