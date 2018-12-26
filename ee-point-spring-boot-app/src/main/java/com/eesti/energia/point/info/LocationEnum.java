package com.eesti.energia.point.info;

public enum LocationEnum {
    EE("EE"),
    FI("FI"),
    LV("LV");

    private final String location;

    LocationEnum(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
}
