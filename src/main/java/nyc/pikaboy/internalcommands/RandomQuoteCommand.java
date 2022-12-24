package nyc.pikaboy.internalcommands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuoteMethods;
import nyc.pikaboy.settings.Settings;

import java.util.Objects;
import java.util.Random;

public class RandomQuoteCommand {

    public static void sendRandomizedQuote (JDA jda, Settings settings, MessageReceivedEvent event){

        if (settings.getAllowedTextChannels().contains(event.getChannel().getId())) {
            Random randnumbergen = new Random();
            int number = randnumbergen.nextInt(0, settings.getRandomizerBounds());
            if (number == 9) {
                Main.logger.info("Message received passed the lottery threshold! Requesting new quote...");
                Objects.requireNonNull(jda.getTextChannelById(event.getChannel().getId())).sendMessage(CheezlQuoteMethods.getRandomQuote().getQuote()).queue();

            } else {
                Main.logger.info("Message received did not pass the lottery threshold!");
            }
        }
    }

}
