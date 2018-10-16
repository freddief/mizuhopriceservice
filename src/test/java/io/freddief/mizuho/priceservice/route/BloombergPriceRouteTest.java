package io.freddief.mizuho.priceservice.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import io.freddief.mizuho.priceservice.dto.price.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.dto.vendor.Vendor;
import io.freddief.mizuho.priceservice.service.BloombergPriceService;
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
public class BloombergPriceRouteTest {

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ObjectMapper objectMapper;
    @EndpointInject(uri = "mock:activemq:topic:prices")
    private MockEndpoint priceEndpoint;
    @EndpointInject(uri = "mock:" + BloombergPriceRoute.DEAD_QUEUE)
    private MockEndpoint deadQueueEndpoint;

    @Autowired
    private ProducerTemplate template;

    @Before
    public void setup() throws Exception {
        if (!camelContext.getStatus().isStarted()) {
            camelContext.getRouteDefinitions().get(0).adviceWith(camelContext, new AdviceWithRouteBuilder() {
                @Override
                public void configure() throws Exception {
                    replaceFromWith("direct:input");
                }
            });

            camelContext.start();
        }
    }

    @Test
    public void bloombergPriceRoute() throws Exception {

        BloombergPrice bloombergPrice = new BloombergPrice(
            "BARC",
            BigDecimal.valueOf(1),
            "GBP"
        );

        priceEndpoint.expectedMessageCount(1);

        template.sendBody("direct:input", objectMapper.writeValueAsString(bloombergPrice));

        priceEndpoint.assertIsSatisfied();

        Price price = objectMapper.readValue(priceEndpoint.getExchanges().get(0).getMessage().getBody(String.class), Price.class);

        assertThat(price.getId()).isNotNull();
        assertThat(price.getTimestamp()).isNotNull();
        assertThat(price.getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(price.getPrice()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(price.getVendor()).isEqualTo(new Vendor(BloombergPriceService.BLOOMBERG_VENDOR_ID, "Bloomberg"));
        assertThat(price.getInstrument()).isEqualTo(new Stock("barclaysStockId", "BARC"));

    }

    @Test
    public void bloombergRoute_whenInvalidMessage_thenRouteToDeadQueue() throws Exception {

        deadQueueEndpoint.expectedMessageCount(1);

        template.sendBody("direct:input", "an invalid message");

        deadQueueEndpoint.assertIsSatisfied();
    }

}