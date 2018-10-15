package io.freddief.mizuho.priceservice.route;

import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.service.PriceService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceRoute extends RouteBuilder {

    public static final String PRICES_TOPIC = "activemq:topic:prices";

    private final PriceService priceService;

    @Autowired
    public PriceRoute(PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    public void configure() throws Exception {
        from(PRICES_TOPIC)
            .unmarshal()
            .json(JsonLibrary.Jackson, Price.class)
            .bean(priceService, "add");
    }

}