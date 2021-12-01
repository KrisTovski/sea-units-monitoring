package com.kristovski.monitorowaniejednostekmorskich.model;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class TrackService {
    private static final String BARENTSWATCH_API_KEY = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjBCM0I1NEUyRkQ5OUZCQkY5NzVERDMxNDBDREQ4OEI1QzA5RkFDRjMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJDenRVNHYyWi03LVhYZE1VRE4ySXRjQ2ZyUE0ifQ.eyJuYmYiOjE2MzgzMDM0NjMsImV4cCI6MTYzODMwNzA2MywiaXNzIjoiaHR0cHM6Ly9pZC5iYXJlbnRzd2F0Y2gubm8iLCJhdWQiOiJhcGkiLCJjbGllbnRfaWQiOiJrZmlsYWtAb25ldC5ldTprcmlzIiwic2NvcGUiOlsiYXBpIl19.P6FbQa60_EKnhDpQpqNwE0QcFqSWunV8fTQvuOWuvjMpFr80QG41-ZeuZn5FWY-3Rtnntgal3L53TOpkcLHQ363Rrs9FCByyQRXL65oiuQ8coZZA2_xy0Mw-ZUWuxIcDRvNpg_tekqWk8J8Gc1_Jwbc1WIfTOoAh5B0HdcqOyPbt2a4Qll6xq1nW0kJHxYzpctMkONXwOADVp3vAZI4k28oiP9aYWxq5qj44iFHUCaTwYlSX5qGyAhmxGqITITk1aU6uJ9yTQdsohUOZ0v-c89lAVn98MJ4IjN--8mY9Dw-nB2TJJpWAI-MNmaNlehWbZ0OwREmlmaFirXYarvJi7lKXXKcH5U7bsTUF0TRxxLWjXMLL6PlDZ2avgqs2mpGEIInKandVDLayC9BhAr_lMUrLmkYCMZo7Fo2murHH0oO_7y8AFj_B0IxcuwQg5SAzjOFnSQ7yfMwMPgJvSuKD88C9awODnDNjAeMp_VANNnltWXmSLA3DCt8lgmgYO6k9H4IYFsLskKQP1_a9KeLvpQ4QLlyAHE1D85PW8fRefattjNGImrZIJfB_4UAAo3WVN1ZqxO6n_5wbjVj5QSpeQbgmd6vEkugmhEP48KVvtC_tIVwGZlf6i5Yh1z4zJ-nQNiAKgI4VCTEVPTXUjkvZ3qyQgpf0NPtW97ECUIrDV8Q";
    private static final String BARENTSWATCH_URL = "https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?";
    private static final String Xmin = "10.09094";
    private static final String Xmax = "10.67047";
    private static final String Ymin = "63.3989";
    private static final String Ymax = "63.58645";

    RestTemplate restTemplate = new RestTemplate();


    public List<Point> getTracks() {
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        httpHeaders.add("Authorization", "Bearer " + BARENTSWATCH_API_KEY);

        ResponseEntity<Track[]> exchange = restTemplate.exchange(BARENTSWATCH_URL + "Xmin=" + Xmin + "&Xmax=" + Xmax + "&Ymin=" + Ymin + "&Ymax=" + Ymax,
                HttpMethod.GET,
                httpEntity,
                Track[].class);

//   TODO     exchange.getStatusCode() == 200;
        List<Point> collect = Stream.of(exchange.getBody())
                .map(track -> new Point(
                        track.getGeometry().getCoordinates().get(0),
                        track.getGeometry().getCoordinates().get(1),
                        track.getName(),
                        getDestination(track.getDestination()).getLongitude(),
                        getDestination(track.getDestination()).getLatitude()
                )).collect(Collectors.toList());
        return collect;
    }

    public Datum getDestination(String destinationName) {
        try {

            String url = "http://api.positionstack.com/v1/forward?access_key=92c1223f14409f05d863a7e6b25ad0f8&query=" + destinationName;
            JsonNode data = restTemplate.getForObject(url, JsonNode.class).get("data").get(0);
            double latitude = data.get("latitude").asDouble();
            double longitude = data.get("longitude").asDouble();
            return new Datum(latitude, longitude);

        } catch (Exception e) {
            return new Datum(0.0, 0.0);
        }

    }

}
