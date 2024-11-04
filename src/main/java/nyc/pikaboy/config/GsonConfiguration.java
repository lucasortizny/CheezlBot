package nyc.pikaboy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
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
        return new GsonBuilder().setPrettyPrinting().create();
    }
}
