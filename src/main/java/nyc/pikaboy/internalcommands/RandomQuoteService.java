package nyc.pikaboy.internalcommands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.config.CheezlbotConfiguration;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.service.CheezlQuotesServiceLegacyImpl;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;
@Service
@Slf4j
@RequiredArgsConstructor
public class RandomQuoteService {
    private final CheezlQuotesServiceLegacyImpl cheezlQuotesService;
    private final CheezlbotConfiguration cheezlbotConfiguration;

    public void sendRandomizedQuote (MessageReceivedEvent event){

        if (validateAllowedTextChannelAndUser(event)) {
            Random randnumbergen = new Random();
            int number = randnumbergen.nextInt(0, cheezlbotConfiguration.getRandomizerBounds());
            if (number == 0) {
                cheezlQuotesService.getRandomQuote()
                        .doOnNext(cheezlQuote -> event.getChannel().sendMessage(cheezlQuote.getQuote()).queue())
                        .doOnError(e -> log.error("Random Quote Service error", e))
                        .subscribe();
            }
        }
    }

    public boolean validateAllowedTextChannelAndUser(MessageReceivedEvent event){
        return cheezlbotConfiguration.getAllowedTextChannels().contains(event.getChannel().getId()) && !event.getAuthor().isBot();
    }

}
