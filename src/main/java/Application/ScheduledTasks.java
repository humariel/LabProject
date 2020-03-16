package Application;

import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
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


    private static final long hour = 3600;
    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);
    private static Flight[] recentFlights;
    private final String baseURL = "https://opensky-network.org/api";

    //private static EntityManager em = Application.createEntityManager();

    @Scheduled(fixedRate = 1000)
    public void reportCurrentTime(){

        long unixTimestamp = Instant.now().getEpochSecond() - 24*hour;
        String url = baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp;
        recentFlights = restTemplate.getForObject(url, Flight[].class);
        Arrays.sort(recentFlights, Comparator.comparing(h -> h.getIcao24()));
        log.info(recentFlights.toString());
        EntityManager em = Application.createEntityManager();
        Query q = em.createQuery("select t from Flight t");
        List<Flight> todoList = q.getResultList();
        int item_number = todoList.size();
        Application.insertFlight(em, recentFlights[0]);

        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer kafkaProducer = new KafkaProducer(properties);
        try{
            kafkaProducer.send(new ProducerRecord("my-flights", Integer.toString(item_number), recentFlights[0].toString()));
        }catch (Exception e){
        }finally {
            kafkaProducer.close();
        }

    }

    public static Flight[] getRecentFlights(){
        return recentFlights;
    }
}