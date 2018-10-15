package io.freddief.mizuho.priceservice.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.dto.vendor.Vendor;
import io.freddief.mizuho.priceservice.repository.PriceRepository;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static io.freddief.mizuho.priceservice.domain.price.CurrencyCode.GBP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class PriceRouteTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private PriceRepository priceRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void priceRoute_writesToCache() throws Exception {

        ProducerTemplate template = camelContext.createProducerTemplate();

        Price price = new Price(
            "priceId",
            new Stock("id1",
                      "BARC"),
            new Vendor(
                "vendor",
                "Bloomberg"
            ),
            Instant.now(),
            BigDecimal.valueOf(1),
            GBP
        );

        template.sendBody(PriceRoute.PRICES_TOPIC, objectMapper.writeValueAsString(price));

        await()
            .atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() -> {

                Collection<io.freddief.mizuho.priceservice.domain.price.Price> returned = priceRepository.findPricesByVendor(new io.freddief.mizuho.priceservice.domain.vendor.Vendor(
                    "vendor",
                    "Bloomberg"
                ));

                assertThat(returned).hasSize(1);
                io.freddief.mizuho.priceservice.domain.price.Price cachePrice = returned.iterator().next();
                assertThat(cachePrice.getId()).isEqualTo("priceId");
                assertThat(cachePrice.getPrice()).isEqualTo(BigDecimal.valueOf(1));
                assertThat(cachePrice.getInstrument()).isEqualTo(
                    new io.freddief.mizuho.priceservice.domain.instrument.Stock(
                        "id1",
                        "BARC"
                    ));
                assertThat(cachePrice.getVendor()).isEqualTo(
                    new io.freddief.mizuho.priceservice.domain.vendor.Vendor(
                        "vendor",
                        "Bloomberg"));
                assertThat(cachePrice.getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
                assertThat(cachePrice.getTimestamp()).isEqualTo(price.getTimestamp());

            });

    }

}