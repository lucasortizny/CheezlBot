package nyc.pikaboy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
@Slf4j
@RequiredArgsConstructor
public class GsonConfiguration {
    private final WebClient cheezlApiWebClient;
    @Bean
    public Gson getGson(){
        Flux<?> thing = cheezlApiWebClient
                .get()
                .uri("/test/woah")
                .exchangeToFlux(clientResponse -> {
                    log.info("Logging Request Info: {}", String.valueOf(clientResponse.request().getHeaders()));
                    return clientResponse.bodyToFlux(String.class);
                });
        thing.subscribe();
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
