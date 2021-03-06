package Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    @Autowired
    private SimpMessagingTemplate template;
    @Autowired
    private RestTemplate restTemplate;

    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @KafkaListener(topics = "flights", containerFactory="kafkaListenerContainerFactory", groupId = "flight_consumers")
    private void listen(Flight flight){
        log.info("Received message in group flight_consumers: " + flight.toString());
        this.template.convertAndSend("/topic/messages", flight);
    }

    @KafkaListener(topics = "fast-logs", containerFactory="kafkaListenerContainerFactoryLogs", groupId = "logs_consumers")
    private void logListen(String msg){
        this.template.convertAndSend("/topic/logs", msg);
    }

    @GetMapping("/flights")
    public String flights(Model model){
        String url = "http://server:8080/api/flights";
        Flight[] flights = restTemplate.getForObject(url, Flight[].class);
        model.addAttribute("flights", flights);
        return "flights";
    }

    @GetMapping("/history")
    public String history(Model model){
        String url = "http://server:8080/api/history";
        Flight[] flights = restTemplate.getForObject(url, Flight[].class);
        model.addAttribute("flights", flights);
        return "history";
    }

    @GetMapping("/kafkaFlights")
    public String kafkaFlights(){
        return "kafkaFlights";
    }

    @GetMapping("/kafkaLogs")
    public String kafkaLogs(){
        return "kafkaLogs";
    }
}
