package nyc.pikaboy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteWrapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
@ConditionalOnProperty(prefix = "cheezlbot", name = "legacy-support", havingValue = "false", matchIfMissing = true)
@Slf4j
@RequiredArgsConstructor
public class CheezlQuotesServiceFluxImpl implements CheezlQuotesService{
    private final WebClient cheezlApiWebClient;

    @Override
    public Mono<CheezlQuote> getQuoteByQuoteKey(String key) {
        return cheezlApiWebClient
                .get()
                .uri("/quote/get-key/")
                .retrieve()
                .bodyToMono(CheezlQuote.class);
    }

    @Override
    public Mono<Boolean> quoteExistsByQuoteKey(String key) {
        return cheezlApiWebClient
                .get()
                .uri("/quote/key-exists/{key}", key)
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(Boolean.FALSE);
    }

    @Override
    public Mono<CheezlQuote> getRandomQuote() {
        return cheezlApiWebClient
                .get()
                .uri("/quote/random")
                .retrieve()
                .bodyToMono(CheezlQuote.class);
    }

    @Override
    public Mono<CheezlQuoteWrapper> getAllQuotes() {
        return cheezlApiWebClient
                .get()
                .uri("/quote/all")
                .retrieve()
                .bodyToMono(CheezlQuoteWrapper.class);
    }

    @Override
    public void deleteQuoteByKey(String keyName) {
        cheezlApiWebClient
                .delete()
                .uri("/quote/delete-key/{key}", keyName)
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }

    @Override
    public Mono<Boolean> createCheezlQuote(CheezlQuote quote) {
        return cheezlApiWebClient
                .post()
                .uri("/quote/create")
                .bodyValue(quote)
                .retrieve()
                .toBodilessEntity()
                .map(voidResponseEntity -> voidResponseEntity.getStatusCode().is2xxSuccessful());
    }
}
