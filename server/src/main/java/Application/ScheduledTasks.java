package Application;

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
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    private static final long hour = 3600;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static Flight[] recentFlights;
    private final String baseURL = "https://opensky-network.org/api";

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime(){

        //fetch data from OpenSky REST API
        long unixTimestamp = Instant.now().getEpochSecond() - 24*hour;
        String url = baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp;
        recentFlights = restTemplate.getForObject(url, Flight[].class);
        Arrays.sort(recentFlights, Comparator.comparing(h -> h.getIcao24()));
        //Store data in persistence unit
        EntityManager em = Application.createEntityManager();
        Query q = em.createQuery("select t from Flight t");
        List<Flight> todoList = q.getResultList();
        int item_number = todoList.size();
        Application.insertFlight(em, recentFlights[0]);
        //Produce kafka message to "flights" topic
        sendKafkaMessage("flights", recentFlights[0].toString());
    }

    public static Flight[] getRecentFlights(){
        return recentFlights;
    }

    public void sendKafkaMessage(String topic, String msg) {
        ListenableFuture<SendResult<String,String>> future = kafkaTemplate.send(topic, msg);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[" + msg + "] due to : " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[" + msg + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }
}