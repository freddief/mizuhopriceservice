package io.freddief.mizuho.priceservice.route;

import io.freddief.mizuho.priceservice.dto.price.BloombergPrice;
import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.service.BloombergPriceService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BloombergPriceRoute extends RouteBuilder {

    public static final String BLOOMBERG_SOCKET = "websocket://127.0.0.1:1234/bloombergPrices";
    public static final String DEAD_QUEUE = "activemq:queue:bloomberg-prices-dead";

    private final BloombergPriceService bloombergPriceService;

    @Autowired
    public BloombergPriceRoute(BloombergPriceService bloombergPriceService) {
        this.bloombergPriceService = bloombergPriceService;
    }

    @Override
    public void configure() {

        errorHandler(
            deadLetterChannel(DEAD_QUEUE)
                .maximumRedeliveries(0));

        from(BLOOMBERG_SOCKET)
            .log("Beginning processing ${body}")
            .unmarshal()
            .json(JsonLibrary.Jackson, BloombergPrice.class)
            .bean(bloombergPriceService, "transform")
            .marshal()
            .json(JsonLibrary.Jackson, Price.class)
            .to(PriceRoute.PRICES_TOPIC)
            .log("Processed ${body}");
    }

}