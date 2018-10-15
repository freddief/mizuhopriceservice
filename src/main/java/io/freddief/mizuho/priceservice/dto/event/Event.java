package io.freddief.mizuho.priceservice.dto.event;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Instant;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public interface Event {

    String getId();

    Instant getTimestamp();

}
