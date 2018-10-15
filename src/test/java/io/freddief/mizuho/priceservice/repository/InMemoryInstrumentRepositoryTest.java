package io.freddief.mizuho.priceservice.repository;

import com.google.common.collect.Maps;
import io.freddief.mizuho.priceservice.domain.instrument.Bond;
import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.instrument.Stock;
import io.freddief.mizuho.priceservice.exception.NotFoundException;
import org.junit.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InMemoryInstrumentRepositoryTest {

    @Test
    public void add_callsPut() {
        Map<String, Instrument> map = mock(Map.class);
        Instrument instrument = mock(Instrument.class);

        when(instrument.getId()).thenReturn("instrumentId");

        InMemoryInstrumentRepository repository = new InMemoryInstrumentRepository(map);

        repository.add(instrument);

        verify(map).put("instrumentId", instrument);
    }

    @Test
    public void findById() {

        Bond instrument = new Bond(
            "id",
            "name"
        );

        InMemoryInstrumentRepository repository = new InMemoryInstrumentRepository(Maps.newHashMap());

        repository.add(instrument);

        Instrument returned = repository.findById("id");

        assertThat(returned).isEqualTo(
            new Bond(
                "id",
                "name"
            )
        );
    }

    @Test(expected = NotFoundException.class)
    public void findById_whenInstrumentNotFound_thenThrowException() {

        InMemoryInstrumentRepository repository = new InMemoryInstrumentRepository(Maps.newHashMap());

        repository.findById("id");
    }

    @Test
    public void constructor_initialisesWithDefaultInstruments() {

        InMemoryInstrumentRepository repository = new InMemoryInstrumentRepository(Maps.newHashMap());

        Instrument barclaysStock = repository.findById("barclaysStockId");
        Instrument hsbcStock = repository.findById("hsbcStockId");
        Instrument bunzlStock = repository.findById("bunzlStockId");

        assertThat(barclaysStock).isEqualTo(
            new Stock("barclaysStockId", "BARC")
        );

        assertThat(hsbcStock).isEqualTo(
            new Stock("hsbcStockId", "HSBA")
        );

        assertThat(bunzlStock).isEqualTo(
            new Stock("bunzlStockId", "BNZL")
        );

    }

}