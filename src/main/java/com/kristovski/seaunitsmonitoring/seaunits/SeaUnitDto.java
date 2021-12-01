package com.kristovski.seaunitsmonitoring.seaunits;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SeaUnitDto {

    private double y;
    private double x;
    private String name;
    private int shipType;
    private double destinationY;
    private double destinationX;

}
