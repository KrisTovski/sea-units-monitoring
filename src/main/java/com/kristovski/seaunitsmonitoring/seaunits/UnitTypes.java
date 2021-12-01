package com.kristovski.seaunitsmonitoring.seaunits;

public enum UnitTypes {
    FISHING(30),
    MILITARY(35),
    PASSENGER(60),
    CARGO(70),
    TANKER(80);

    private final int value;

    UnitTypes(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
