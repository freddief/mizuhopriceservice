package io.freddief.mizuho.priceservice.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.dto.vendor.Vendor;
import io.freddief.mizuho.priceservice.service.IgGroupPriceService;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpoints;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
@MockEndpoints
@DirtiesContext
@UseAdviceWith
public class IgGroupPriceRouteTest {

    private static final String CSV_FILE =
        "BARC,1,GBP\n" +
        "HSBA,12.1,GBP";

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ObjectMapper objectMapper;
    @EndpointInject(uri = "mock:" + PriceRoute.PRICES_TOPIC)
    private MockEndpoint priceEndpoint;
    @EndpointInject(uri = "mock:" + IgGroupPriceRoute.DEAD_QUEUE)
    private MockEndpoint deadQueueEndpoint;

    @Autowired
    private ProducerTemplate template;

    @Before
    public void setup() throws Exception {
        if (!camelContext.getStatus().isStarted()) {
            camelContext.getRouteDefinitions().get(1).adviceWith(camelContext, new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                    replaceFromWith("direct:input");
                }
            });

            camelContext.start();
        }
    }

    @Test
    public void igPriceRoute() throws Exception {

        priceEndpoint.expectedMessageCount(2);

        template.sendBody("direct:input", CSV_FILE);

        priceEndpoint.assertIsSatisfied();

        Price price = objectMapper.readValue(priceEndpoint.getExchanges().get(0).getMessage().getBody(String.class), Price.class);

        assertThat(price.getId()).isNotNull();
        assertThat(price.getTimestamp()).isNotNull();
        assertThat(price.getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(price.getPrice()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(price.getVendor()).isEqualTo(new Vendor(IgGroupPriceService.IG_GROUP_VENDOR_ID, "IG Group"));
        assertThat(price.getInstrument()).isEqualTo(new Stock("barclaysStockId", "BARC"));

        Price price2 = objectMapper.readValue(priceEndpoint.getExchanges().get(1).getMessage().getBody(String.class), Price.class);

        assertThat(price2.getId()).isNotNull();
        assertThat(price2.getTimestamp()).isNotNull();
        assertThat(price2.getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(price2.getPrice()).isEqualTo(BigDecimal.valueOf(12.1));
        assertThat(price2.getVendor()).isEqualTo(new Vendor(IgGroupPriceService.IG_GROUP_VENDOR_ID, "IG Group"));
        assertThat(price2.getInstrument()).isEqualTo(new Stock("hsbcStockId", "HSBA"));

    }

    @Test
    public void igPriceRoute_whenInvalidMessage_thenRouteToDeadQueue() throws Exception {

        deadQueueEndpoint.expectedMessageCount(1);

        template.sendBody("direct:input", "an,invalid,csv");

        deadQueueEndpoint.assertIsSatisfied();
    }

}