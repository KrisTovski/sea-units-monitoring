package com.kristovski.seaunitsmonitoring.seaunits;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class SeaUnitService {

    private final SeaUnitsClient seaUnitsClient;

    private static final double X_MIN = 7.50;
    private static final double X_MAX = 11.50;
    private static final double Y_MIN = 63.10;
    private static final double Y_MAX = 64.10;

    public List<SeaUnitDto> getSeaUnits(int unitType) {
        ResponseEntity<SeaUnit[]> response = seaUnitsClient.getSeaUnitsForGivenAreaWithDestination(X_MIN, X_MAX, Y_MIN, Y_MAX);

        List<SeaUnitDto> collect = Stream.of(response.getBody())
                .filter(seaUnit -> seaUnit.getShipType().equals(unitType))
                .map(seaUnit -> new SeaUnitDto(
                        seaUnit.getGeometry().getCoordinates().get(0),
                        seaUnit.getGeometry().getCoordinates().get(1),
                        seaUnit.getName(),
                        seaUnit.getShipType(),
                        seaUnitsClient.getDestination(seaUnit.getDestination()).getLongitude(),
                        seaUnitsClient.getDestination(seaUnit.getDestination()).getLatitude()
                )).collect(Collectors.toList());

        log.info(collect.toString());
        return collect;
    }


}
