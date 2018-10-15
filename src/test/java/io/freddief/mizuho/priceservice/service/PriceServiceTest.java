package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.PriceRepository;
import io.freddief.mizuho.priceservice.transformer.PriceTransformer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PriceServiceTest {

    @Mock
    private PriceRepository priceRepository;
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

}