package com.kristovski.seaunitsmonitoring.model.mapper;

import com.kristovski.seaunitsmonitoring.model.dto.SeaUnitDto;
import com.kristovski.seaunitsmonitoring.model.seaunit.SeaUnitPoint;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SeaUnitDtoMapper {

    public static List<SeaUnitDto> mapPointToDto(List<SeaUnitPoint> points) {
        return points.stream()
                .map(point -> new SeaUnitDto(LocalDateTime.now(),
                        point.getY(),
                        point.getX(),
                        point.getMmsi(),
                        point.getName(),
                        point.getShipType(),
                        point.getDestinationY(),
                        point.getDestinationX()))
                .collect(Collectors.toList());
    }

}
