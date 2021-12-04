package com.kristovski.seaunitsmonitoring.model.seaunit;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SeaUnitPoint {

    private double y;
    private double x;
    private int mmsi;
    private String name;
    private int shipType;
    private double destinationY;
    private double destinationX;
    private double temp;
    private double windSpeed;
    private String description;
    private String icon;

}
