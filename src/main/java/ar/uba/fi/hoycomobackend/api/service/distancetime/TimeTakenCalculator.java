package ar.uba.fi.hoycomobackend.api.service.distancetime;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TimeTakenCalculator {

    private Logger LOGGER = LoggerFactory.getLogger(TimeTakenCalculator.class);
    private ObjectMapper objectMapper;

    @Autowired
    public TimeTakenCalculator(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Integer timeTakenFromOriginToDestination(Double originLatitude, Double originLongitude, Double destinationLatitude, Double destinationLongitude) {
        try {
            LOGGER.info("Trying to get time taken according to distance from google");
            RestTemplate restTemplate = new RestTemplate();
            String resourceUrl
                    = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" +
                    originLatitude.toString() + "," +
                    originLongitude.toString() + "&destinations=" +
                    destinationLatitude.toString() + "," +
                    destinationLongitude.toString() + "&key=AIzaSyC1_Pl3mGUTHBCGPY-HdHV2hg-OZfb3pWg";
            LOGGER.info("Url using: {}", resourceUrl);
            ResponseEntity<String> response
                    = restTemplate.getForEntity(resourceUrl, String.class);
            LOGGER.info("Got the following response: {}", response);

            JsonNode root = objectMapper.readTree(response.getBody());
            String value = root.path("rows").get(0).path("elements").get(0).path("duration").get("text").asText();
            Integer totalTime = Integer.parseInt(value.split(" ")[0]);
            LOGGER.info("Go the following total time {}", totalTime);

            return totalTime;
        } catch (Exception e) {
            LOGGER.error("Got the following error while trying to get info from google: {}", e.getMessage());
            return 10;
        }
    }
}
