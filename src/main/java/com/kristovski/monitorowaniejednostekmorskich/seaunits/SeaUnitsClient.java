package com.kristovski.monitorowaniejednostekmorskich.seaunits;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SeaUnitsClient {
    private static final String BARENTSWATCH_API_KEY = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjBCM0I1NEUyRkQ5OUZCQkY5NzVERDMxNDBDREQ4OEI1QzA5RkFDRjMiLCJ0eXAiOiJhdCtqd3QiLCJ4NXQiOiJDenRVNHYyWi03LVhYZE1VRE4ySXRjQ2ZyUE0ifQ.eyJuYmYiOjE2MzgzNTc4NDAsImV4cCI6MTYzODM2MTQ0MCwiaXNzIjoiaHR0cHM6Ly9pZC5iYXJlbnRzd2F0Y2gubm8iLCJhdWQiOiJhcGkiLCJjbGllbnRfaWQiOiJrZmlsYWtAb25ldC5ldTprcmlzIiwic2NvcGUiOlsiYXBpIl19.ndNuEOtnJv6QCZoSvRTc3Tj2GP73eVWHfgXztZhpSSGe5nLnQXnq4s2z3ah6oHdqVCQv7kIGZFhvuPnpzxILWKD0ylMOg6Uek__4T4ye_FzWPllwEagdjaeZdm50uIMYbCfLpp1y5L9B0c7VxLn5ddRuDG8TdhDPnmq8yOMAevP_g8fgQ453D2D10aCli9y0FutJ8sGMAlMRJ2tPB5ESgCDS8BHS-BbeSrpXvzHgJc_Q7vvdUJ_wpCANwQqOBPEHOa6DtreShN-NnJaP2nU8pKWdHKQq3BMZ2PDA52Jn1Wgm-EWa6oslsQ8ND2xq3V8TrbwOQJeruXNl8XBQfzXtUXjm-28OyMQjG4-g0TjMYB52K8JXYzXcdqE95Zb9fZDcDi4rRnplfplISAYC1vFGN9EAo8tV4vp0On7S2sSrZWYcobrCiwuJORyyET6vKBu8D1AVseRJvPNaZyIswCP6fYpz0q3LeOGROAcIqVWT-NDMqtwx0hEGeTQTpjTHXYgntLf_TIqPMYLADBIYySvGyF7yOIelmoUKjOSZypsZdmY013eZG3EA9EbpQwOWylvv4WU5VTskBOPgfpSTadWD4yA1Vpp37yjOyuFKcDY8BcpMOd2H5_HJYFaAgtSEdhwnh5UPcqL-XaeLNqGdFWpw2INpa89UL3uM-d_umc6HtLA";
    private static final String BARENTSWATCH_URL = "https://www.barentswatch.no/bwapi/v2/geodata/ais/openpositions?";
    private static final String POSITIONSTACK_URL = "http://api.positionstack.com/v1/forward?access_key=";
    private static final String POSITIONSTACK_ACCESS_KEY = "92c1223f14409f05d863a7e6b25ad0f8";


    RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<SeaUnit[]> getSeaUnitsForGivenAreaWithDestination(double xmin, double xmax, double ymin, double ymax){
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity httpEntity = new HttpEntity(httpHeaders);

        httpHeaders.add("Authorization", "Bearer " + BARENTSWATCH_API_KEY);

        ResponseEntity<SeaUnit[]> exchange = null;
        try {
            exchange = restTemplate.exchange(
                    BARENTSWATCH_URL + "Xmin={xmin}&Xmax={xmax}&Ymin={ymin}&Ymax={ymax}",
                    HttpMethod.GET,
                    httpEntity,
                    SeaUnit[].class,
                    xmin, xmax, ymin, ymax);
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = exchange.getStatusCode();
            if (statusCode !=HttpStatus.NOT_FOUND){
                throw e;
            }
        }
        return exchange;

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




}
