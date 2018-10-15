package io.freddief.mizuho.priceservice.dto.instrument;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.CLASS)
public interface Instrument {

    String getId();

    String getCode();

}
