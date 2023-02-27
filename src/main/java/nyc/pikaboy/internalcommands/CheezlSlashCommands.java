package nyc.pikaboy.internalcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;
import nyc.pikaboy.data.OutgoingKeyCheck;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            if (!(keyname.matches("[A-z]*"))){
                event.getHook().editOriginal("You must supply a quote-key without invalid characters.").queue();
                return;
            }
            ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
            OutgoingKeyCheck keyCheck = new OutgoingKeyCheck(keyname);
            String bodyToSend = writer.writeValueAsString(keyCheck);
            int statusCode = Request.Get(Main.SETTINGS.getCheezlapiuri()+"/quote/key-exists/"+keyname)
                    .execute()
                    .returnResponse()
                    .getStatusLine()
                    .getStatusCode();
            if (statusCode == 200){
                event.getHook().editOriginal("Key already exists. Please try another key-name.").queueAfter(3L, TimeUnit.SECONDS);
                return;
            }
            if (statusCode == 404){
                event.getHook().sendMessage("Currently submitting quote...").queue();
                CheezlQuoteMethods.addCheezlQuote(Main.SETTINGS, new CheezlQuote(keyname, quote));
                event.getHook().editOriginal("Quote submitted succesfully under key: " + keyname).queueAfter(3L, TimeUnit.SECONDS);

            }
            else{
                event.getHook().editOriginal("Key is in invalid format.").queueAfter(3L, TimeUnit.SECONDS);
            }

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
            int statusCode = Request.Delete(Main.SETTINGS.getCheezlapiuri()+"/quote/delete-key/"+keyname)
                    .execute()
                    .returnResponse()
                    .getStatusLine()
                    .getStatusCode();
            if (statusCode == 200){
                event.getHook().editOriginal("Deleted quote-key(s) with value of " + keyname).queueAfter(2L, TimeUnit.SECONDS);
            }
            else if (statusCode == 404){
                event.getHook().editOriginal("Quote key not found in Database.").queue();
            }
            else {
                event.getHook().editOriginal("Issue with deletion. Contact developer.").queue();
            }

        } catch (Exception e){
            System.out.println("Command invokation failed. Not doing anything :(");
            e.printStackTrace();
        }
    }

    public static void listQuotes(SlashCommandEvent event){
        event.deferReply(true).queue();
        try{
            event.getHook().editOriginal("Writing to file now...").queue();
            String filename = UUID.randomUUID().toString();
            File fileToSend = new File(filename + ".json");
            FileWriter writer = new FileWriter(fileToSend);
            Content content = Request.Get(Main.SETTINGS.getCheezlapiuri() + "/quote/all").execute().returnContent();
            writer.write(content.asString());
            writer.flush();
            event.getMember().getUser().openPrivateChannel().queue((privateChannel -> {
                privateChannel.sendFile(fileToSend).queue(message -> {
                    try {
                        writer.close();
                        java.nio.file.Files.delete(Path.of(filename + ".json"));
                    } catch (IOException e) {
                        Main.logger.error("Unable to delete file.");
                    }
                });
            }));
//            java.nio.file.Files.delete(Path.of(filename + ".json"));
            event.getHook().editOriginal("File written and sent to DMs").queue();
        }
        catch (IOException ex){
            Main.logger.error("Unable to remove the file.");
            event.getHook().editOriginal("No quotes in the DB.").queue();
        }
        catch (Exception e){
            Main.logger.error("Error with sending list of quotes in file. Check log.");
            event.getHook().editOriginal("File Command has encountered an error. Check the logs.").queue();
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
