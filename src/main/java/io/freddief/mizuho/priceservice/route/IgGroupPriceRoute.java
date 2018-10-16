package io.freddief.mizuho.priceservice.route;

import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.service.IgGroupPriceService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IgGroupPriceRoute extends RouteBuilder {

    public static final String IG_GROUP_FTP_URI = "ftp://admin@127.0.0.1:12345/file.csv";

    private final IgGroupPriceService igGroupPriceService;

    @Autowired
    public IgGroupPriceRoute(IgGroupPriceService igGroupPriceService) {
        this.igGroupPriceService = igGroupPriceService;
    }

    @Override
    public void configure() throws Exception {

        from(IG_GROUP_FTP_URI)
            .split()
            .tokenize("\n")
            .bean(igGroupPriceService, "transform")
            .marshal()
            .json(JsonLibrary.Jackson, Price.class)
            .to(PriceRoute.PRICES_TOPIC);
    }

}