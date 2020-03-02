package Application;

import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private RestTemplate restTemplate;

    private static final long hour = 3600;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static Flight[] recentFlights;
    private final String baseURL = "https://opensky-network.org/api";

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime(){

        long unixTimestamp = Instant.now().getEpochSecond() - 24*hour;
        String url = baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp;
        recentFlights = restTemplate.getForObject(url, Flight[].class);
        Arrays.sort(recentFlights, (h1, h2) -> h1.getIcao24().compareTo(h2.getIcao24()));
        log.info(recentFlights.toString());
        //for(Flight f: recentFlights) log.info(f.toString());
    }

    public static Flight[] getRecentFlights(){
        return recentFlights;
    }
}