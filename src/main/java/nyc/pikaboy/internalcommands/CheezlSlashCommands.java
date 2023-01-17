package nyc.pikaboy.internalcommands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
                String append = String.format("**Quote Key**: %s \nQuote: %s\n\n", quote.getQuoteKeyName(), quote.getQuote());
                if ((strbuilder.length() + append.length()) > 2000){
                    try {
                        event.getUser().openPrivateChannel().queue((message) -> {
                            message.sendMessage(strbuilder.toString()).queue();
                        });
                        Thread.sleep(600L);
                        strbuilder.delete(0, strbuilder.length());

                    } catch (Exception e){
                        System.out.println("Normal exception reached. Continue.");
                    }
                    // Send message in order to clear the String builder.

                }
                // At this point we assume the String Builder is OK!

                strbuilder.append(append);
            });
            // If we reach here and our String Builder still has strings, let's flush it and get it over with.
            if (!strbuilder.isEmpty()){
                event.getUser().openPrivateChannel().queue((message) -> {
                    message.sendMessage(strbuilder.toString()).queue();
                });
            }

            event.getHook().editOriginal("Message sent via DMs.").queue();
        } catch (Exception e){
            event.getHook().editOriginal("Quotes aggregation failed. Contact bot owner.").queueAfter(2L, TimeUnit.SECONDS);
            e.printStackTrace();
        }

    }

    public static void getVPN(SlashCommandEvent event){
        event.deferReply(true).queue();
        try {
            String name = UUID.randomUUID().toString();
            Main.client.createClient(name);
//            event.getHook().sendFile(Main.client.getClientConfiguration(Main.client.getClientIdByName(name))).queue();
            event.getUser().openPrivateChannel().queue((privateChannel -> {
                File configurationFile = Main.client.getClientConfiguration(Main.client.getClientIdByName(name));
                try {
                    Thread.sleep(200L);
                    System.out.println("Does file exist: " + configurationFile.exists());
                    privateChannel.sendFile(configurationFile).queue();
                    Thread.sleep(1000L);
                    configurationFile.delete();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }));
            event.getHook().editOriginal("Client configuration sent.").queue();
        } catch (Exception e){
            event.getHook().setEphemeral(true).sendMessage("VPN provisioning failed.").queue();
        }

    }
}
