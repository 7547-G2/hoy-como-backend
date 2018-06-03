package ar.uba.fi.hoycomobackend.api.service.distancetime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TimeTakenCalculator {

    private ObjectMapper objectMapper;

    @Autowired
    public TimeTakenCalculator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Integer timeTakenFromOriginToDestination(Double originLatitude, Double originLongitude, Double destinationLatitude, Double destinationLongitude) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String fooResourceUrl
                    = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                    originLatitude.toString() + "," +
                    originLongitude.toString() + "&destinations=" +
                    destinationLatitude.toString() + "," +
                    destinationLongitude.toString() + "&key=AIzaSyC1_Pl3mGUTHBCGPY-HdHV2hg-OZfb3pWg";
            ResponseEntity<String> response
                    = restTemplate.getForEntity(fooResourceUrl, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            String value = root.path("rows").get(0).path("elements").get(0).path("duration").get("text").asText();
            Integer totalTime = Integer.parseInt(value.split(" ")[0]);

            return totalTime;
        } catch (Exception e) {
            return 10;
        }
    }
}
