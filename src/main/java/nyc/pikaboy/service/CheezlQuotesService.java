package nyc.pikaboy.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@Slf4j
public class CheezlQuotesService {
    private RestTemplate cheezlApiRestTemplate;

    @Autowired
    CheezlQuotesService(RestTemplate cheezlApiRestTemplate){
        this.cheezlApiRestTemplate = cheezlApiRestTemplate;
    }

    public CheezlQuote getQuoteByQuoteKey(String key){
        try {
            log.debug("Making a request to get quote by the quote key {}", key);
            return cheezlApiRestTemplate.getForObject(URI.create("/quote/get-key/" + key), CheezlQuote.class);
        } catch (RestClientException e){
            log.warn("The API get-key request was not completed successfully.", e);
            return null;
        }
    }

    public boolean quoteExistsByQuoteKey(String key){
        try {
            log.debug("Making a request to check if quote exists by key {}", key);
            ResponseEntity<Boolean> response = cheezlApiRestTemplate.getForEntity("/quote/key-exists/" + key, Boolean.class);
            if (response.getStatusCode().is4xxClientError())
                return false;
            else if (response.getStatusCode().is2xxSuccessful())
                return true;
        } catch (RestClientException e){
            log.warn("The API key-exists request was not completed successfully. Either found w/ problems or not found at all.", e);
            return false;
        }
        return true;
    }
    public CheezlQuote getRandomQuote(){
        try {
            log.debug("Making a request for a random quote.");
            return cheezlApiRestTemplate.getForObject("/quote/random", CheezlQuote.class);

        } catch (RestClientException e){
            log.warn("The API random quote request was not completed successfully.", e);
            return null;
        }
    }

    public void deleteQuoteByKey(String keyName){
        cheezlApiRestTemplate.delete("/quote/delete-key/" + keyName);
    }

    public boolean createCheezlQuote(CheezlQuote quote){
        ResponseEntity<CheezlQuote> response = cheezlApiRestTemplate.postForEntity("/quote/create", quote, CheezlQuote.class);
        return response.getStatusCode().is2xxSuccessful();
    }


}
