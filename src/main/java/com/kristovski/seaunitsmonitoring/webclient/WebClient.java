package com.kristovski.seaunitsmonitoring.webclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.kristovski.seaunitsmonitoring.model.openweather.WeatherConditions;
import com.kristovski.seaunitsmonitoring.model.seaunit.Datum;
import com.kristovski.seaunitsmonitoring.model.seaunit.SeaUnit;
import com.kristovski.seaunitsmonitoring.model.token.BarentswatchResponse;
import com.kristovski.seaunitsmonitoring.model.token.Token;
import com.kristovski.seaunitsmonitoring.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WebClient {
    private static final String BARENTSWATCH_URL = "https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?";
    private static final String POSITIONSTACK_URL = "http://api.positionstack.com/v1/forward?access_key=";
    private static final String POSITIONSTACK_ACCESS_KEY = "92c1223f14409f05d863a7e6b25ad0f8";

    private static final String CLIENT_ID_VALUE = "kfilak@onet.eu:kris";
    private static final String CLIENT_SECRET_VALUE = "BarentsPassWatch";

    private static final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/";
    private static final String OPENWEATHER_API_KEY = "17aa55fa1293c0f4e5cd99167d1a0d71";

    private final TokenRepository tokenRepository;

    @Autowired
    public WebClient(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<SeaUnit[]> getSeaUnitsForGivenAreaWithDestination(double xmin, double xmax, double ymin, double ymax) {

        HttpHeaders httpHeaders = getHttpHeadersForAutorization();
        return restTemplate.exchange(
                BARENTSWATCH_URL + "Xmin={xmin}&Xmax={xmax}&Ymin={ymin}&Ymax={ymax}",
                HttpMethod.GET,
                new HttpEntity<>(httpHeaders),
                SeaUnit[].class,
                xmin, xmax, ymin, ymax);
    }

    public Datum getDestination(String destinationName) {
        try {
            String url = POSITIONSTACK_URL + POSITIONSTACK_ACCESS_KEY + "&query=" + destinationName;
            JsonNode data = restTemplate.getForObject(url, JsonNode.class).get("data").get(0);
            double latitude = data.get("latitude").asDouble();
            double longitude = data.get("longitude").asDouble();
            return new Datum(latitude, longitude);

        } catch (Exception e) {
            return new Datum(0.0, 0.0);
        }
    }

    public ResponseEntity<WeatherConditions> getWeatherForPosition(double lat, double lon) {

        String url = WEATHER_URL + "weather?lat={lat}&lon={lon}&appid=" + OPENWEATHER_API_KEY + "&units=metric&lang=en";
        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders()),
                WeatherConditions.class,
                lat, lon);
    }

    private HttpHeaders getHttpHeadersForAutorization() {
        HttpHeaders httpHeaders = new HttpHeaders();

        Token token = getLatestToken();

        String barentswatch_api_key = token.getAccessToken();

        if (barentswatch_api_key == null) {
            addToken(new Token());
        } else if (isTokenExpired(token)) {
            addToken(new Token());
        } else {
            callPostForAccessToken();
        }
        httpHeaders.add("Authorization", "Bearer " + barentswatch_api_key);
        return httpHeaders;
    }

    private void addToken(Token token) {
        token.setCreateTime(LocalDateTime.now());
        String barentswatch_api_key = callPostForAccessToken();
        token.setAccessToken(barentswatch_api_key);
        tokenRepository.save(token);
    }

    private String callPostForAccessToken() {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", CLIENT_ID_VALUE);
        map.add("scope", "api");
        map.add("client_secret", CLIENT_SECRET_VALUE);
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, new HttpHeaders());

        BarentswatchResponse barentswatchResponse = restTemplate.postForObject("https://id.barentswatch.no/connect/token", httpEntity, BarentswatchResponse.class);

        assert barentswatchResponse != null;

        return barentswatchResponse.getAccess_token();
    }

    private boolean isTokenExpired(Token token) {
        LocalDateTime tokenCreateTime = token.getCreateTime();
        LocalDateTime currentTime = LocalDateTime.now();

        return tokenCreateTime.isBefore(currentTime.minusHours(1));
    }

    private Token getLatestToken() {
        List<Token> tokens = tokenRepository.findAll();
        log.info("findAll tokens from db - ids: " + tokens.stream().map(Token::getId).collect(Collectors.toList()));
        if (!tokens.isEmpty()) {
            Token token = tokens.stream().max(Comparator.comparingLong(Token::getId)).orElseThrow(NoSuchElementException::new);
            log.info("Latest token: " + token.toString());
            return token;
        }
        return new Token();
    }
}
