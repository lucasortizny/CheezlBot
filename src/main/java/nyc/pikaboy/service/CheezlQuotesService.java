package nyc.pikaboy.service;

import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteWrapper;
import reactor.core.publisher.Mono;

public interface CheezlQuotesService {
    Mono<CheezlQuote> getQuoteByQuoteKey(String key);
    Mono<Boolean> quoteExistsByQuoteKey(String key);
    Mono<CheezlQuote> getRandomQuote();
    Mono<CheezlQuoteWrapper> getAllQuotes();
    void deleteQuoteByKey(String keyName);
    Mono<Boolean> createCheezlQuote(CheezlQuote quote);
}
