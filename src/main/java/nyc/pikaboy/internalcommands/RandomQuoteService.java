package nyc.pikaboy.internalcommands;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.config.CheezlbotConfiguration;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.service.CheezlQuotesService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class RandomQuoteService {
    private final CheezlQuotesService cheezlQuotesService;
    private final CheezlbotConfiguration cheezlbotConfiguration;

    public void sendRandomizedQuote (MessageReceivedEvent event){

        if (validateAllowedTextChannelAndUser(event)) {
            Random randnumbergen = new Random();
            int number = randnumbergen.nextInt(0, cheezlbotConfiguration.getRandomizerBounds());
            if (number == 0) {
                Optional<CheezlQuote> retQuote = Optional.of(cheezlQuotesService.getRandomQuote());
                retQuote.ifPresent((quote) -> {
                    Objects.requireNonNull(event.getChannel()).sendMessage(quote.getQuote()).queue();
                });
            }
        }
    }

    public boolean validateAllowedTextChannelAndUser(MessageReceivedEvent event){
        return cheezlbotConfiguration.getAllowedTextChannels().contains(event.getChannel().getId()) && !event.getAuthor().isBot();
    }

}
