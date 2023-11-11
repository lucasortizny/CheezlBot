package nyc.pikaboy.service;

import nyc.pikaboy.data.OutgoingMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CheezlMessageLoggingService {
    RestTemplate cheezlApiRestTemplate;
    @Autowired
    CheezlMessageLoggingService(RestTemplate cheezlApiRestTemplate){
        this.cheezlApiRestTemplate = cheezlApiRestTemplate;
    }

    public void submitMessage(OutgoingMessage message){
        cheezlApiRestTemplate.postForEntity("/message/create", message, String.class);
    }
}
