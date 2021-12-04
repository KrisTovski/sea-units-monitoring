package com.kristovski.seaunitsmonitoring.model.seaunit;

public enum UnitType {
    FISHING(30, "Fishing boat"),
    MILITARY(35, "Military ship"),
    PASSENGER(60, "Passenger ship"),
    CARGO(70, "Cargo"),
    TANKER(80, "Tanker"),
    OTHER(90, "Other");

    private final int value;
    private final String valueName;

    UnitType(int value, String valueName) {
        this.value = value;
        this.valueName = valueName;
    }

    public int getValue() {
        return value;
    }

    public String getValueName() {
        return valueName;
    }
}
