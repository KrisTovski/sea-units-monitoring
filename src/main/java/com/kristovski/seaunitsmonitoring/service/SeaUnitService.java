package com.kristovski.seaunitsmonitoring.service;

import com.kristovski.seaunitsmonitoring.utils.email.EmailSender;
import com.kristovski.seaunitsmonitoring.utils.email.EmailTemplate;
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

    private static final double TRONDHEIM_Y = 63.446827;
    private static final double TRONDHEIM_X = 10.421906;
    private static final int MILITARY_UNITS_TYPE = 35;
    private static final int DISTANCE_FROM_TRONDHEIM = 100;
    private final WebClient webClient;
    private final SeaUnitRepository repository;
    private final EmailSender emailSender;

    private static final double X_MIN = 7.50;
    private static final double X_MAX = 11.50;
    private static final double Y_MIN = 63.10;
    private static final double Y_MAX = 64.10;

    public List<SeaUnitPoint> getSeaUnitsByType(int unitType) {
        ResponseEntity<SeaUnit[]> response = webClient.getSeaUnitsForGivenAreaWithDestination(X_MIN, X_MAX, Y_MIN, Y_MAX);

        List<SeaUnitPoint> collect = Stream.of(Objects.requireNonNull(response.getBody()))
                .filter(seaUnit -> seaUnit.getShipType().equals(unitType))
                .map(this::apply).collect(Collectors.toList());

        // Save only military units to db
        if (isMilitary(unitType)) {
            saveDto(collect);
            sendEmailIfMilitaryUnitCloseToTrondheim(collect);
        }

        log.info("Get Sea Units By Type: " + collect);
        return collect;
    }

    // Don't delete this method
    // Method is inactive when "show all units" in getMap() is commented out in controller
    public List<SeaUnitPoint> getSeaUnits() {
        ResponseEntity<SeaUnit[]> response = webClient.getSeaUnitsForGivenAreaWithDestination(X_MIN, X_MAX, Y_MIN, Y_MAX);

        List<SeaUnitPoint> collect = Stream.of(Objects.requireNonNull(response.getBody()))
                .map(this::apply).collect(Collectors.toList());

        log.info("Get Sea Units: " + collect);
        return collect;
    }

    private void sendEmailIfMilitaryUnitCloseToTrondheim(List<SeaUnitPoint> collect) {
        for (SeaUnitPoint militaryUnit : collect) {
            double y = militaryUnit.getY();
            double x = militaryUnit.getX();

            double powX = Math.pow((x - TRONDHEIM_X), 2);
            double powY = Math.pow((y - TRONDHEIM_Y), 2);
            double distance = Math.sqrt(powX + powY);
            log.info(militaryUnit.getName() + " is " + distance + "km from Trondheim");

            if (distance <= DISTANCE_FROM_TRONDHEIM) {
                emailSender.send("kristovski.dev@gmail.com", buildEmail());
            }
        }
    }

    private boolean isMilitary(int value) {
        return value == MILITARY_UNITS_TYPE;
    }

    private void saveDto(List<SeaUnitPoint> points) {
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
                farenheitToCelcius(weatherForPositionResponse.getBody().getMain().getTemp()),
                weatherForPositionResponse.getBody().getWind().getSpeed(),
                weather.getDescription(),
                weather.getIcon()
        );
    }

    private Double farenheitToCelcius(Double temp) {
        double result = (temp - 32) * 5 / 9;
        return Math.round(result * 100.0) / 100.0;
    }

    private String buildEmail() {
        return new EmailTemplate().getEmailTemplate();
    }
}
