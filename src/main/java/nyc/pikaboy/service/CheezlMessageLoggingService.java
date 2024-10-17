package nyc.pikaboy.service;

import lombok.RequiredArgsConstructor;
import nyc.pikaboy.data.OutgoingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CheezlMessageLoggingService {
    private final RestTemplate cheezlApiRestTemplate;

    public void submitMessage(OutgoingMessage message){
        cheezlApiRestTemplate.postForEntity("/message/create", message, String.class);
    }
}
