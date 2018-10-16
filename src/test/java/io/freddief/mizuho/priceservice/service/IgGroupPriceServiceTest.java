package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.dto.price.IgGroupPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.InstrumentRepository;
import io.freddief.mizuho.priceservice.repository.VendorRepository;
import io.freddief.mizuho.priceservice.transformer.IgGroupPriceTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IgGroupPriceServiceTest {

    @Mock
    private VendorRepository vendorRepository;
    @Mock
    private InstrumentRepository instrumentRepository;
    @Mock
    private IgGroupPriceTransformer igGroupPriceTransformer;
    @InjectMocks
    private IgGroupPriceService igGroupPriceService;

    @Test
    public void transform_findsVendor() {
        IgGroupPrice igGroupPrice = mock(IgGroupPrice.class);

        igGroupPriceService.transform(igGroupPrice);

        verify(vendorRepository).findById(IgGroupPriceService.IG_GROUP_VENDOR_ID);
    }


    @Test
    public void transform_findsInstrument() {
        IgGroupPrice igGroupPrice = mock(IgGroupPrice.class);

        when(igGroupPrice.getInstrumentCode()).thenReturn("code");

        igGroupPriceService.transform(igGroupPrice);

        verify(instrumentRepository).findByCode("code");
    }

    @Test
    public void transform_callsTransformer() {
        IgGroupPrice igGroupPrice = mock(IgGroupPrice.class);
        Vendor vendor = mock(Vendor.class);
        Instrument instrument = mock(Instrument.class);

        when(vendorRepository.findById(any())).thenReturn(vendor);
        when(instrumentRepository.findByCode(any())).thenReturn(instrument);

        igGroupPriceService.transform(igGroupPrice);

        verify(igGroupPriceTransformer).transform(
            igGroupPrice,
            instrument,
            vendor
        );
    }

    @Test
    public void transform_returnsFromTransformer() {
        IgGroupPrice igGroupPrice = mock(IgGroupPrice.class);
        Price price = mock(Price.class);

        when(igGroupPriceTransformer.transform(any(), any(), any())).thenReturn(price);

        Price returned = igGroupPriceService.transform(igGroupPrice);

        assertThat(returned).isEqualTo(price);
    }

}