package nyc.pikaboy.internalcommands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the Cheezl slash commands functionality needed within the Cheezl Bot!
 */
public class CheezlSlashCommands {

    /**
     * Creates a new quote for the Cheezl Bot!
     * @param event
     */
    public static void newQuote(SlashCommandEvent event){
        event.deferReply(true).queue();
        try {
            String keyname = event.getOption("quote-key").getAsString();
            String quote = event.getOption("quote").getAsString();
            if (keyname.isBlank() || quote.isBlank()){
                event.getHook().editOriginal("You must supply a quote-key and quote.").queue();
                return;
            }
            boolean checkCopies = Main.SETTINGS.getCheezlQuotesList().parallelStream().anyMatch(cheezlQuote -> {
                return cheezlQuote.getQuoteKeyName().equals(keyname);
            });
            if (checkCopies){
                event.getHook().editOriginal("Key already exists. Please try another key-name.").queueAfter(3L, TimeUnit.SECONDS);
                return;
            }
            event.getHook().sendMessage("Currently submitting quote...").queue();
            CheezlQuoteMethods.addCheezlQuote(Main.SETTINGS, new CheezlQuote(keyname, quote));
            event.getHook().editOriginal("Quote submitted successfully. ").queueAfter(3L, TimeUnit.SECONDS);

        } catch (Exception e){
            System.out.println("Command invokation failed. Not doing anything :(");
            e.printStackTrace();
        }
    }

    public static void removeQuote(SlashCommandEvent event) {
        event.deferReply(true).queue();
        try {
            String keyname = event.getOption("quote-key").getAsString();
            if (keyname.isBlank()){
                event.getHook().editOriginal("Quote key must contain text.").queue();
                return;
            }
            boolean checkCopies = Main.SETTINGS.getCheezlQuotesList().parallelStream().anyMatch(cheezlQuote -> {
                return cheezlQuote.getQuoteKeyName().equals(keyname);
            });
            if (!checkCopies){
                event.getHook().editOriginal("No key with such name found.").queueAfter(3L, TimeUnit.SECONDS);
                return;
            }
            List<CheezlQuote> deleteQuotes = new ArrayList<>();
            Main.SETTINGS.getCheezlQuotesList().forEach(cheezlQuote -> {
                if (cheezlQuote.getQuoteKeyName().equals(keyname)){
                    deleteQuotes.add(cheezlQuote);
                }
            });
            //Now perform delete operation
            Main.SETTINGS.getCheezlQuotesList().removeAll(deleteQuotes);
            event.getHook().editOriginal("Deleted quote-key(s) with value of " + keyname).queueAfter(2L, TimeUnit.SECONDS);

        } catch (Exception e){
            System.out.println("Command invokation failed. Not doing anything :(");
            e.printStackTrace();
        }
    }

    public static void listQuotes(SlashCommandEvent event){
        try {

            event.deferReply(true).queue();
            event.getHook().editOriginal("Building Message to send via DMs").queue();
            StringBuilder strbuilder = new StringBuilder();
            Main.SETTINGS.getCheezlQuotesList().forEach(quote ->{
                strbuilder.append(String.format("**Quote Key**: %s \nQuote: %s\n", quote.getQuoteKeyName(), quote.getQuote()));
                strbuilder.append("\n");
            });
            event.getUser().openPrivateChannel().flatMap(message -> {
                message.sendMessage(strbuilder.toString()).queue();
                return null;
            }).queue();
            event.getHook().editOriginal("Message sent via DMs.").queue();
        } catch (Exception e){
            event.getHook().editOriginal("Quotes aggregation failed. Contact bot owner.").queueAfter(2L, TimeUnit.SECONDS);
            e.printStackTrace();
        }

    }
}
