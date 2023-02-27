package nyc.pikaboy.internalcommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;
import nyc.pikaboy.settings.Settings;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class RandomQuoteCommand {

    public static void sendRandomizedQuote (JDA jda, Settings settings, MessageReceivedEvent event){

        if (settings.getAllowedTextChannels().contains(event.getChannel().getId()) && !event.getAuthor().isBot()) {
            Random randnumbergen = new Random();
            int number = randnumbergen.nextInt(0, settings.getRandomizerBounds());
            if (number == 0) {
                Main.logger.debug("Message received passed the lottery threshold! Requesting new quote...");
                Optional<CheezlQuote> retQuote = CheezlQuoteMethods.getRandomQuote();
                retQuote.ifPresent((quote) -> {
                    Objects.requireNonNull(jda.getTextChannelById(event.getChannel().getId())).sendMessage(quote.getQuote()).queue();
                });
            } else {
                Main.logger.debug("Message received did not pass the lottery threshold!");
            }
        }
    }

}
