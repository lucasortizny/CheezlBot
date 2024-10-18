package nyc.pikaboy.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.service.CheezlQuotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
@Component
@Slf4j
@RequiredArgsConstructor
public class CheezlQuoteMethods {
    private final CheezlQuotesService cheezlQuotesService;

    public boolean addCheezlQuote(CheezlQuote quote){
        try {
            return cheezlQuotesService.createCheezlQuote(quote);
        } catch (RestClientException e){
            log.warn("Unable to add Cheezl Quote to the Database.", e);
            return false;
        }
    }
}
