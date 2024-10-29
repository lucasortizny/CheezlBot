package nyc.pikaboy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
@Slf4j
@ConditionalOnProperty(prefix = "cheezlbot", name = "legacy-support", havingValue = "true")
@Deprecated
@RequiredArgsConstructor
public class CheezlQuotesServiceLegacyImpl implements CheezlQuotesService{
    private final RestTemplate cheezlApiRestTemplate;
    public Mono<CheezlQuote> getQuoteByQuoteKey(String key){
        try {
            log.debug("Making a request to get quote by the quote key {}", key);
            return Mono.just(cheezlApiRestTemplate.getForObject(URI.create("/quote/get-key/" + key), CheezlQuote.class));
        } catch (RestClientException e){
            log.warn("The API get-key request was not completed successfully.", e);
            return Mono.empty();
        }
    }

    public Mono<Boolean> quoteExistsByQuoteKey(String key){
        try {
            log.debug("Making a request to check if quote exists by key {}", key);
            ResponseEntity<Boolean> response = cheezlApiRestTemplate.getForEntity("/quote/key-exists/" + key, Boolean.class);
            if (response.getStatusCode().is4xxClientError())
                return Mono.just(Boolean.FALSE);
            else if (response.getStatusCode().is2xxSuccessful())
                return Mono.just(Boolean.TRUE);
        } catch (RestClientException e){
            log.warn("The API key-exists request was not completed successfully. Either found w/ problems or not found at all.", e);
            return Mono.just(Boolean.FALSE);
        }
        return Mono.just(true);
    }
    public Mono<CheezlQuote> getRandomQuote(){
        try {
            log.debug("Making a request for a random quote.");
            return Mono.just(cheezlApiRestTemplate.getForObject("/quote/random", CheezlQuote.class));

        } catch (RestClientException e){
            log.warn("The API random quote request was not completed successfully.", e);
            return Mono.empty();
        }
    }

    public Mono<CheezlQuoteWrapper> getAllQuotes(){
        try {
            return Mono.just(cheezlApiRestTemplate.getForObject("/quote/all", CheezlQuoteWrapper.class));
        } catch (RestClientException e){
            log.warn("The API random quote request was not completed successfully.", e);
            return Mono.empty();
        }

    }

    public void deleteQuoteByKey(String keyName){
        cheezlApiRestTemplate.delete("/quote/delete-key/" + keyName);
    }

    public Mono<Boolean> createCheezlQuote(CheezlQuote quote){
        ResponseEntity<CheezlQuote> response = cheezlApiRestTemplate.postForEntity("/quote/create", quote, CheezlQuote.class);
        return Mono.just(response.getStatusCode().is2xxSuccessful());
    }


}
