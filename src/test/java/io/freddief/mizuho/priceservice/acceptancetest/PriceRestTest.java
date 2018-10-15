package io.freddief.mizuho.priceservice.acceptancetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.freddief.mizuho.priceservice.domain.instrument.Stock;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.domain.price.Price;
import io.freddief.mizuho.priceservice.domain.vendor.Vendor;
import io.freddief.mizuho.priceservice.repository.PriceRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

public class PriceRestTest extends BaseAcceptanceTest {

    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findPricesByVendorId() {

        priceRepository.add(new Price(
            "id1",
            new Stock("barclaysStockId", "BARC"),
            new Vendor("bloombergVendorId", "Bloomberg"),
            Instant.now(),
            BigDecimal.valueOf(12),
            CurrencyCode.GBP
        ));

        priceRepository.add(new Price(
            "id2",
            new Stock("hsbcStockId", "HSBA"),
            new Vendor("bloombergVendorId", "Bloomberg"),
            Instant.now(),
            BigDecimal.valueOf(15),
            CurrencyCode.GBP
        ));

        priceRepository.add(new Price(
            "id3",
            new Stock("bunzlStockId", "BNZL"),
            new Vendor("igGroupVendorId", "IG Group"),
            Instant.now(),
            BigDecimal.valueOf(12),
            CurrencyCode.GBP
        ));


        List<io.freddief.mizuho.priceservice.dto.price.Price> prices = Lists.newArrayList(
            given()
                .contentType("application/json")
            .when()
                .get(formatUrl("/prices/vendors/bloombergVendorId"))
            .then()
                .statusCode(200)
                .extract()
                .as(io.freddief.mizuho.priceservice.dto.price.Price[].class));

        assertThat(prices).hasSize(2);

        assertThat(prices.get(0).getId()).isEqualTo("id1");
        assertThat(prices.get(0).getInstrument()).isEqualTo(new io.freddief.mizuho.priceservice.dto.instrument.Stock("barclaysStockId", "BARC"));
        assertThat(prices.get(0).getVendor()).isEqualTo(new io.freddief.mizuho.priceservice.dto.vendor.Vendor("bloombergVendorId", "Bloomberg"));
        assertThat(prices.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(prices.get(0).getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(prices.get(0).getTimestamp()).isNotNull();

        assertThat(prices.get(1).getId()).isEqualTo("id2");
        assertThat(prices.get(1).getInstrument()).isEqualTo(new io.freddief.mizuho.priceservice.dto.instrument.Stock("hsbcStockId", "HSBA"));
        assertThat(prices.get(1).getVendor()).isEqualTo(new io.freddief.mizuho.priceservice.dto.vendor.Vendor("bloombergVendorId", "Bloomberg"));
        assertThat(prices.get(1).getPrice()).isEqualTo(BigDecimal.valueOf(15));
        assertThat(prices.get(1).getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(prices.get(1).getTimestamp()).isNotNull();

    }

    @Test
    public void findPricesByVendorId_whenVendorNotFound_then404() {

        given()
            .contentType("application/json")
        .when()
            .get(formatUrl("/prices/vendors/notFoundVendorId"))
        .then()
            .statusCode(404)
            .body("[0].message", is("Vendor with id='notFoundVendorId' not found"));

    }

    @Test
    public void findPricesByInstrumentId() {

        priceRepository.add(new Price(
            "id1",
            new Stock("barclaysStockId", "BARC"),
            new Vendor("bloombergVendorId", "Bloomberg"),
            Instant.now(),
            BigDecimal.valueOf(12),
            CurrencyCode.GBP
        ));

        priceRepository.add(new Price(
            "id2",
            new Stock("barclaysStockId", "BARC"),
            new Vendor("igGroupVendorId", "IG Group"),
            Instant.now(),
            BigDecimal.valueOf(15),
            CurrencyCode.GBP
        ));

        priceRepository.add(new Price(
            "id3",
            new Stock("bunzlStockId", "BNZL"),
            new Vendor("igGroupVendorId", "IG Group"),
            Instant.now(),
            BigDecimal.valueOf(12),
            CurrencyCode.GBP
        ));


        List<io.freddief.mizuho.priceservice.dto.price.Price> prices = Lists.newArrayList(
            given()
                .contentType("application/json")
            .when()
                .get(formatUrl("/prices/instruments/barclaysStockId"))
            .then()
                .statusCode(200)
                .extract()
                .as(io.freddief.mizuho.priceservice.dto.price.Price[].class));

        assertThat(prices).hasSize(2);

        assertThat(prices.get(0).getId()).isEqualTo("id1");
        assertThat(prices.get(0).getInstrument()).isEqualTo(new io.freddief.mizuho.priceservice.dto.instrument.Stock("barclaysStockId", "BARC"));
        assertThat(prices.get(0).getVendor()).isEqualTo(new io.freddief.mizuho.priceservice.dto.vendor.Vendor("bloombergVendorId", "Bloomberg"));
        assertThat(prices.get(0).getPrice()).isEqualTo(BigDecimal.valueOf(12));
        assertThat(prices.get(0).getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(prices.get(0).getTimestamp()).isNotNull();

        assertThat(prices.get(1).getId()).isEqualTo("id2");
        assertThat(prices.get(1).getInstrument()).isEqualTo(new io.freddief.mizuho.priceservice.dto.instrument.Stock("barclaysStockId", "BARC"));
        assertThat(prices.get(1).getVendor()).isEqualTo(new io.freddief.mizuho.priceservice.dto.vendor.Vendor("igGroupVendorId", "IG Group"));
        assertThat(prices.get(1).getPrice()).isEqualTo(BigDecimal.valueOf(15));
        assertThat(prices.get(1).getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(prices.get(1).getTimestamp()).isNotNull();

    }

    @Test
    public void findPricesByInstrumentId_whenInstrumentNotFound_then404() {


        given()
            .contentType("application/json")
        .when()
            .get(formatUrl("/prices/instruments/notFoundInstrument"))
        .then()
            .statusCode(404)
            .body("[0].message", is("Instrument with id='notFoundInstrument' not found"));

    }



}
