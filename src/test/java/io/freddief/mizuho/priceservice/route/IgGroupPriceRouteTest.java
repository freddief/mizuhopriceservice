package io.freddief.mizuho.priceservice.route;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.freddief.mizuho.priceservice.domain.price.CurrencyCode;
import io.freddief.mizuho.priceservice.dto.instrument.Stock;
import io.freddief.mizuho.priceservice.dto.price.IgGroupPrice;
import io.freddief.mizuho.priceservice.dto.price.IgGroupPrice.MonetaryAmount;
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

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private ObjectMapper objectMapper;
    @EndpointInject(uri = "mock:activemq:topic:prices")
    private MockEndpoint priceEndpoint;

    @Autowired
    private ProducerTemplate template;

    @Test
    public void igPriceRoute() throws Exception {
        camelContext.getRouteDefinitions().get(1).adviceWith(camelContext, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:input");
            }
        });


        IgGroupPrice igGroup = new IgGroupPrice(
            "BARC",
            new MonetaryAmount(
                BigDecimal.valueOf(1),
                "GBP")
        );

        camelContext.start();

        priceEndpoint.expectedMessageCount(1);

        template.sendBody("direct:input", objectMapper.writeValueAsString(igGroup));

        priceEndpoint.assertIsSatisfied();

        Price price = objectMapper.readValue(priceEndpoint.getExchanges().get(0).getMessage().getBody(String.class), Price.class);

        assertThat(price.getId()).isNotNull();
        assertThat(price.getTimestamp()).isNotNull();
        assertThat(price.getCurrencyCode()).isEqualTo(CurrencyCode.GBP);
        assertThat(price.getPrice()).isEqualTo(BigDecimal.valueOf(1));
        assertThat(price.getVendor()).isEqualTo(new Vendor(IgGroupPriceService.IG_GROUP_VENDOR_ID, "IG Group"));
        assertThat(price.getInstrument()).isEqualTo(new Stock("barclaysStockId", "BARC"));

    }

}