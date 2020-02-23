package Application;

import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    private static Quote recentQuote = null;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {

        Quote quote = restTemplate.getForObject(
                "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);

        recentQuote = quote;

        log.info(quote.toString());

    }

    public static Quote getRecentQuote(){
        return recentQuote;
    }
}