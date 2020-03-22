package Application;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@RestController
public class AppController {

    @Autowired
    private ScheduledTasks tasks;

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @GetMapping("/api/flights")
    public Flight[] flights(){
        Flight[] flights = tasks.getRecentFlights();
        return flights;
    }

    @GetMapping("/api/history")
    public List<Flight> history(){
        EntityManager em = Application.createEntityManager();
        Query q = em.createQuery("select t from Flight t");
        return q.getResultList();
    }

/*    @GetMapping("/consume")
    public List<String> consume() {
        return kafka_messages;
    }

    @KafkaListener(topics = "flights", groupId = "flight_consumers")
    private void listen(String msg){
        log.info("Received message in group flight_consumers: " + msg);
        kafka_messages.add(msg);
    }*/

}
