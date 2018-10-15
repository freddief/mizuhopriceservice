package io.freddief.mizuho.priceservice.service;

import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.repository.PriceRepository;
import io.freddief.mizuho.priceservice.transformer.PriceTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceTransformer priceTransformer;

    @Autowired
    public PriceService(PriceRepository priceRepository,
                        PriceTransformer priceTransformer) {
        this.priceRepository = priceRepository;
        this.priceTransformer = priceTransformer;
    }

    public Price add(Price price) {
        priceRepository.add(priceTransformer.toDomain(price));
        return price;
    }

}
