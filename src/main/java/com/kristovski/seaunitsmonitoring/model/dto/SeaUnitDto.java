package com.kristovski.seaunitsmonitoring.model.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seaunit")
public class SeaUnitDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    private double y;
    private double x;
    private int mmsi;
    private String name;
    private int shipType;
    @Column(name = "destination_y")
    private double destinationY;
    @Column(name = "destination_x")
    private double destinationX;

    public SeaUnitDto(LocalDateTime created, double y, double x, int mmsi, String name, int shipType, double destinationY, double destinationX) {
        this.created = created;
        this.y = y;
        this.x = x;
        this.mmsi = mmsi;
        this.name = name;
        this.shipType = shipType;
        this.destinationY = destinationY;
        this.destinationX = destinationX;
    }
}
