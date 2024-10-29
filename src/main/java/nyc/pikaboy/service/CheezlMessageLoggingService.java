package nyc.pikaboy.service;

import lombok.RequiredArgsConstructor;
import nyc.pikaboy.data.OutgoingMessage;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class CheezlMessageLoggingService {
    private final WebClient cheezlApiRestTemplate;

    public void submitMessage(OutgoingMessage message){
        cheezlApiRestTemplate.post().uri("/message/create").bodyValue(message).retrieve().toBodilessEntity().subscribe();
    }
}
