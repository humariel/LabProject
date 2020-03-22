package Application;

import java.util.ArrayList;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String, Flight> kafkaTemplate;


    private static final long hour = 3600;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static Flight[] recentFlights;
    private List<Flight> allFlights = new ArrayList<Flight>();
    private final String baseURL = "https://opensky-network.org/api";

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime() {
        //fetch data from OpenSky REST API
        long unixTimestamp = Instant.now().getEpochSecond() - 24*hour;
        String url = baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp;
        recentFlights = restTemplate.getForObject(url, Flight[].class);
        Arrays.sort(recentFlights, Comparator.comparing(h -> h.getIcao24()));
        log.info("Fetched data from OpenSky");
        
        EntityManager em = Application.createEntityManager();
        for(Flight f: recentFlights){
            if(!allFlights.contains(f)){
                log.info("New Flight");
                allFlights.add(f);
                Application.insertFlight(em, f);
                sendKafkaMessage("flights", f);
            }
        }
        Application.closeEM(em);
        //Store data in persistence unit
        //Produce kafka message to "flights" topic
        //sendKafkaMessage("flights", recentFlights[0].toString());
    }

    public Flight[] getRecentFlights(){
        return recentFlights;
    }

    public void setAllFlights(List<Flight> fs) {
        allFlights = fs;
    }

    public void sendKafkaMessage(String topic, Flight flight) {
        ListenableFuture<SendResult<String, Flight>> future = kafkaTemplate.send(topic, flight);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Flight>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + flight.toString() + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Flight> result) {
                log.info("Kafka: Sent message=[" + flight.toString() + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }
}