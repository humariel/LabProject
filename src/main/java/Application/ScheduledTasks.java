package Application;

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
    private static Object[] recentFlights;
    private final String baseURL = "https://opensky-network.org/api";

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        /*Quote quote = restTemplate.getForObject(
                "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
        recentQuote = quote;
        log.info(quote.toString());*/

        //get current hour from previous day
        long unixTimestamp = Instant.now().getEpochSecond() - 24*hour;
        String url = baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp;
        /*System.out.println(baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp);
        FlightList fs = restTemplate.getForObject(baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp, FlightList.class);
        log.info(fs.toString());*/
        ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(url, Object[].class);
        recentFlights = responseEntity.getBody();
    }

    public static Object[] getRecentFlights(){
        return recentFlights;
    }
}