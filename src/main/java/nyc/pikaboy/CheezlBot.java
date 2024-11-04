package nyc.pikaboy;

import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.wireguard.WGConnect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
@Slf4j
public class CheezlBot {

    public static WGConnect client;

    public static void main(String[] args){
        log.info("Starting Spring...");
        SpringApplication.run(CheezlBot.class, args);
        log.info("Finished Spring.");
    }
}