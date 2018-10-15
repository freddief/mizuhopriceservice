package io.freddief.mizuho.priceservice.controller;

import io.freddief.mizuho.priceservice.dto.price.Price;
import io.freddief.mizuho.priceservice.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/prices")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @RequestMapping(value = "/vendors/{vendorId}", method = RequestMethod.GET)
    public Collection<Price> findPricesByVendorId(@PathVariable String vendorId) {
        return priceService.findPricesByVendorId(vendorId);
    }

    @RequestMapping(value = "/instruments/{instrumentId}", method = RequestMethod.GET)
    public Collection<Price> findPricesByInstrumentId(@PathVariable String instrumentId) {
        return priceService.findPricesByInstrumentId(instrumentId);
    }

}
