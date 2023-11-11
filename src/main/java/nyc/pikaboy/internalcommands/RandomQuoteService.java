package nyc.pikaboy.internalcommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.service.CheezlQuotesService;
import nyc.pikaboy.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
@Service
public class RandomQuoteService {
    private CheezlQuotesService cheezlQuotesService;
    private Settings settings;
    @Autowired
    RandomQuoteService(CheezlQuotesService cheezlQuotesService, Settings settings){
        this.cheezlQuotesService = cheezlQuotesService;
        this.settings = settings;

    }
    public void sendRandomizedQuote (MessageReceivedEvent event){

        if (settings.getAllowedTextChannels().contains(event.getChannel().getId()) && !event.getAuthor().isBot()) {
            Random randnumbergen = new Random();
            int number = randnumbergen.nextInt(0, settings.getRandomizerBounds());
            if (number == 0) {
                Optional<CheezlQuote> retQuote = Optional.of(cheezlQuotesService.getRandomQuote());
                retQuote.ifPresent((quote) -> {
                    Objects.requireNonNull(event.getChannel()).sendMessage(quote.getQuote()).queue();
                });
            } else {
            }
        }
    }

}
