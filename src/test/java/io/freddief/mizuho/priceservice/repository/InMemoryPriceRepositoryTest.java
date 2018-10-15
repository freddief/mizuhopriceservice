package io.freddief.mizuho.priceservice.repository;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import io.freddief.mizuho.priceservice.domain.instrument.Instrument;
import io.freddief.mizuho.priceservice.domain.instrument.Stock;
import io.freddief.mizuho.priceservice.domain.price.Price;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;

import static io.freddief.mizuho.priceservice.domain.price.CurrencyCode.GBP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class InMemoryPriceRepositoryTest {

    @Test
    public void add_callsPut() {
        Table<Vendor, Instrument, Price> table = mock(Table.class);
        Price price = mock(Price.class);
        Vendor vendor = mock(Vendor.class);
        Instrument instrument = mock(Instrument.class);

        when(price.getVendor()).thenReturn(vendor);
        when(price.getInstrument()).thenReturn(instrument);

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(table);
        inMemoryPriceRepository.add(price);

        verify(table).put(vendor, instrument, price);
    }

    @Test
    public void findPricesByVendor_whenNoPricesAdded_thenReturnsEmptyList() {

        Vendor vendor = new Vendor(
            "vendor",
            "Bloomberg"
        );

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(HashBasedTable.create());

        Collection<Price> returned = inMemoryPriceRepository.findPricesByVendor(vendor);

        assertThat(returned).isEmpty();

    }

    @Test
    public void findPricesByVendor_filtersVendorPricesOnly() {

        Vendor vendor1 = new Vendor(
            "vendor1",
            "Bloomberg"
        );

        Vendor vendor2 = new Vendor(
            "vendor2",
            "IG Group"
        );

        Price vendor1Price = new Price(
            "price1",
            new Stock("id1",
                      "BARC"),
            vendor1,
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        Price vendor1Price2 = new Price(
            "price2",
            new Stock("id2",
                      "HSBA"),
            vendor1,
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        Price vendor2Price = new Price(
            "price3",
            new Stock("id3",
                      "BNZL"),
            vendor2,
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(HashBasedTable.create());

        inMemoryPriceRepository.add(vendor1Price);
        inMemoryPriceRepository.add(vendor1Price2);
        inMemoryPriceRepository.add(vendor2Price);

        Collection<Price> returned = inMemoryPriceRepository.findPricesByVendor(vendor1);

        assertThat(returned).containsExactly(
            vendor1Price,
            vendor1Price2
        );

    }

    @Test
    public void findPricesByVendor_whenTwoPricesAdded_returnsMostRecent() {

        Vendor vendor = new Vendor(
            "vendor",
            "Bloomberg"
        );

        Stock stock = new Stock("id1",
                                "BARC");

        Price price1 = new Price(
            "price1",
            stock,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        Price price2 = new Price(
            "price2",
            stock,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(2),
            GBP
        );

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(HashBasedTable.create());

        inMemoryPriceRepository.add(price1);
        inMemoryPriceRepository.add(price2);

        Collection<Price> returned = inMemoryPriceRepository.findPricesByVendor(vendor);

        assertThat(returned).containsExactly(
            price2
        );

    }

    @Test
    public void findPricesByInstrument_whenNoPricesAdded_thenReturnsEmptyList() {

        Stock stock = new Stock("id1",
                                "BARC");

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(HashBasedTable.create());

        Collection<Price> returned = inMemoryPriceRepository.findPricesByInstrument(stock);

        assertThat(returned).isEmpty();

    }

    @Test
    public void findPricesByInstrument_filtersInstrumentPricesOnly() {

        Stock stock1 = new Stock("id1",
                                 "BARC");

        Stock stock2 = new Stock("id2",
                                 "BNZL");

        Price stock1Price = new Price(
            "price1",
            stock1,
            new Vendor(
                "vendor",
                "Bloomberg"
            ),
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        Price stock1Price2 = new Price(
            "price2",
            stock1,
            new Vendor(
                "vendor2",
                "IG group"
            ),
            Instant.now(),
            BigDecimal.valueOf(2),
            GBP
        );

        Price stock2Price = new Price(
            "price3",
            stock2,
            new Vendor(
                "vendor3",
                "Hargreaves Lansdown"
            ),
            Instant.now(),
            BigDecimal.valueOf(3),
            GBP
        );

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(HashBasedTable.create());

        inMemoryPriceRepository.add(stock1Price);
        inMemoryPriceRepository.add(stock1Price2);
        inMemoryPriceRepository.add(stock2Price);

        Collection<Price> returned = inMemoryPriceRepository.findPricesByInstrument(stock1);

        assertThat(returned).containsExactly(
            stock1Price,
            stock1Price2
        );

    }


    @Test
    public void findPricesByInstrument_whenTwoPricesAdded_returnsMostRecent() {

        Vendor vendor = new Vendor(
            "vendor",
            "Bloomberg"
        );

        Stock stock = new Stock("id1",
                                "BARC");

        Price price1 = new Price(
            "price1",
            stock,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        Price price2 = new Price(
            "price2",
            stock,
            vendor,
            Instant.now(),
            BigDecimal.valueOf(2),
            GBP
        );

        InMemoryPriceRepository inMemoryPriceRepository = new InMemoryPriceRepository(HashBasedTable.create());

        inMemoryPriceRepository.add(price1);
        inMemoryPriceRepository.add(price2);

        Collection<Price> returned = inMemoryPriceRepository.findPricesByInstrument(stock);

        assertThat(returned).containsExactly(
            price2
        );

    }

}