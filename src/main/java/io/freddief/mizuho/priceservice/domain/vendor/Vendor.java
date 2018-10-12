package io.freddief.mizuho.priceservice.domain.vendor;

public class Vendor {

    private final String id;
    private final String name;

    public Vendor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
