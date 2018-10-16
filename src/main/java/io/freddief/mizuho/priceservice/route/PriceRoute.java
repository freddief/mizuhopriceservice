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
    public static final String DEAD_QUEUE = "activemq:queue:prices-dead";

    private final PriceService priceService;

    @Autowired
    public PriceRoute(PriceService priceService) {
        this.priceService = priceService;
    }

    @Override
    public void configure() {

        errorHandler(
            deadLetterChannel(DEAD_QUEUE)
                .maximumRedeliveries(0));

        from(PRICES_TOPIC)
            .log("Beginning processing ${body}")
            .unmarshal()
            .json(JsonLibrary.Jackson, Price.class)
            .bean(priceService, "add")
            .log("Processed ${body}");
    }

}