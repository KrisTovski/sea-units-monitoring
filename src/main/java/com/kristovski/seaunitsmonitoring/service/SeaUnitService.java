package com.kristovski.seaunitsmonitoring.service;

import com.kristovski.seaunitsmonitoring.model.dto.SeaUnitDto;
import com.kristovski.seaunitsmonitoring.model.openweather.Weather;
import com.kristovski.seaunitsmonitoring.model.openweather.WeatherConditions;
import com.kristovski.seaunitsmonitoring.model.seaunit.SeaUnit;
import com.kristovski.seaunitsmonitoring.model.seaunit.SeaUnitPoint;
import com.kristovski.seaunitsmonitoring.repository.SeaUnitRepository;
import com.kristovski.seaunitsmonitoring.webclient.WebClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.kristovski.seaunitsmonitoring.model.mapper.SeaUnitDtoMapper.mapPointToDto;

@Service
@Slf4j
@AllArgsConstructor
public class SeaUnitService {

    private final WebClient webClient;
    private final SeaUnitRepository repository;

    private static final double X_MIN = 7.50;
    private static final double X_MAX = 11.50;
    private static final double Y_MIN = 63.10;
    private static final double Y_MAX = 64.10;

    public List<SeaUnitPoint> getSeaUnits() {
        ResponseEntity<SeaUnit[]> response = webClient.getSeaUnitsForGivenAreaWithDestination(X_MIN, X_MAX, Y_MIN, Y_MAX);

        List<SeaUnitPoint> collect = Stream.of(Objects.requireNonNull(response.getBody()))
                .map(this::apply).collect(Collectors.toList());

        log.info("Get Sea Units: " + collect);
        return collect;
    }

    public List<SeaUnitPoint> getSeaUnitsByType(int unitType) {
        ResponseEntity<SeaUnit[]> response = webClient.getSeaUnitsForGivenAreaWithDestination(X_MIN, X_MAX, Y_MIN, Y_MAX);

        List<SeaUnitPoint> collect = Stream.of(Objects.requireNonNull(response.getBody()))
                .filter(seaUnit -> seaUnit.getShipType().equals(unitType))
                .map(this::apply).collect(Collectors.toList());

        log.info("Get Sea Units By Type: " + collect);
        return collect;
    }

    public void saveDto(List<SeaUnitPoint> points) {
        repository.saveAll(mapPointToDto(points));
    }


    private SeaUnitPoint apply(SeaUnit seaUnit) {
        Double lat = seaUnit.getGeometry().getCoordinates().get(0);
        Double lon = seaUnit.getGeometry().getCoordinates().get(1);
        ResponseEntity<WeatherConditions> weatherForPositionResponse = webClient.getWeatherForPosition(lat, lon);
        Weather weather = Objects.requireNonNull(weatherForPositionResponse.getBody()).getWeather().get(0);

        return new SeaUnitPoint(
                lat,
                lon,
                seaUnit.getMmsi(),
                seaUnit.getName(),
                seaUnit.getShipType(),
                webClient.getDestination(seaUnit.getDestination()).getLongitude(),
                webClient.getDestination(seaUnit.getDestination()).getLatitude(),
                weatherForPositionResponse.getBody().getMain().getTemp(),
                weatherForPositionResponse.getBody().getWind().getSpeed(),
                weather.getDescription(),
                weather.getIcon()
        );
    }
}
