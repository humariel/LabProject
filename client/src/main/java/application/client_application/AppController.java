package application.client_application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AppController {

    private static final Logger log = LoggerFactory.getLogger(AppController.class);
    private List<String> kafka_messages = new ArrayList<>();

    @KafkaListener(topics = "flights", groupId = "flight_consumers")
    private void listen(String msg){
        log.info("Received message in group flight_consumers: " + msg);
        kafka_messages.add(msg);
    }

    @MessageMapping("/flights")
    @SendTo("/topic/messages")
    public List<String> send() {
        return kafka_messages;
    }
}
