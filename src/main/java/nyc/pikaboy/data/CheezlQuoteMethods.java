package nyc.pikaboy.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.service.CheezlQuotesService;
import nyc.pikaboy.settings.Settings;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Optional;
@Component
@Slf4j
public class CheezlQuoteMethods {
    CheezlQuotesService cheezlQuotesService;
    @Autowired
    CheezlQuoteMethods(CheezlQuotesService cheezlQuotesService){
        this.cheezlQuotesService = cheezlQuotesService;
    }

    public boolean addCheezlQuote(CheezlQuote quote){
        try {
            return cheezlQuotesService.createCheezlQuote(quote);
        } catch (RestClientException e){
            log.warn("Unable to add Cheezl Quote to the Database.", e);
            return false;
        }
    }
}
