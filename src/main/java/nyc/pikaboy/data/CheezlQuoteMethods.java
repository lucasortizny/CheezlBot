package nyc.pikaboy.data;

import lombok.extern.slf4j.Slf4j;
import nyc.pikaboy.service.CheezlQuotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
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
