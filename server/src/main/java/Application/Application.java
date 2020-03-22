package Application;

import java.time.Instant;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private static final String PERSISTENCE_UNIT_NAME = "LabProjectDB";
    private static EntityManagerFactory factory;
    private static EntityManager em;

    @Autowired
    private ScheduledTasks tasks;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
        //createEntityManager();
        int hour = 3600;
        long unixTimestamp = Instant.now().getEpochSecond() - 24*hour;
        String baseURL = "https://opensky-network.org/api";
        String url = baseURL + "/flights/all?begin=" + (unixTimestamp-7200) + "&end=" +  unixTimestamp;
		return args -> {
            EntityManager em =  createEntityManager();
            Query q = em.createQuery("select t from Flight t");
            List<Flight> fs = q.getResultList();
            tasks.setAllFlights(fs);
		};
    }

    public static EntityManager createEntityManager() { 
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        em = factory.createEntityManager();
        return em;
    }

    public static void insertFlight(EntityManager em, Flight f) {
        em.getTransaction().begin();
        em.persist(f);
        em.getTransaction().commit();
        log.info("Inserted Flight to DB");
    }

    public static void closeEM(EntityManager em){
        em.close();
    }
}
