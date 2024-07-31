package nyc.pikaboy.internalcommands;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import nyc.pikaboy.CheezlBot;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;
import nyc.pikaboy.service.CheezlQuotesService;
import nyc.pikaboy.settings.Settings;
import nyc.pikaboy.wireguard.WGConnect;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the Cheezl slash commands functionality needed within the Cheezl Bot!
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class CheezlSlashCommands {

    private final CheezlQuotesService cheezlQuotesService;
    private final CheezlQuoteMethods cheezlQuoteMethods;
    private final Gson gson;
    private final WGConnect wgConnect;
    


    /**
     * Creates a new quote for the Cheezl Bot! This method does validation before submitting request
     * to the API.
     * @param event
     */
    public void newQuote(SlashCommandEvent event){
        event.deferReply(true).queue();
        try {
            //start biz validation of key
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
            boolean exists = cheezlQuotesService.quoteExistsByQuoteKey(keyname);
            if (exists){
                event.getHook().editOriginal("Key already exists. Please try another key-name.").queueAfter(3L, TimeUnit.SECONDS);
            }
            else {
                event.getHook().sendMessage("Currently submitting quote...").queue();
                this.cheezlQuoteMethods.addCheezlQuote(new CheezlQuote(keyname, quote));
                event.getHook().editOriginal("Quote submitted succesfully under key: " + keyname).queueAfter(3L, TimeUnit.SECONDS);
            }

        } catch (Exception e){
            System.out.println("Command invokation failed. Not doing anything :(");
            e.printStackTrace();
        }
    }

    public void removeQuote(SlashCommandEvent event) {
        event.deferReply(true).queue();
        try {
            String keyname = event.getOption("quote-key").getAsString();
            if (keyname.isBlank()){
                event.getHook().editOriginal("Quote key must contain text.").queue();
                return;
            }
            cheezlQuotesService.deleteQuoteByKey(keyname);
            event.getHook().editOriginal("Submitted Deletion Request of Quote with key: " + keyname).queueAfter(2L, TimeUnit.SECONDS);

        } catch (Exception e){
            log.warn("Issue removing quote.", e);
        }
    }
    //Leave this method as it is considering the pain in the way I designed the CheezlAPI.
    public void listQuotes(SlashCommandEvent event){
        event.deferReply(true).queue();
        try{
            event.getHook().editOriginal("Writing to file now...").queue();
            String filename = UUID.randomUUID().toString();
            File fileToSend = new File(filename + ".json");
            FileWriter writer = new FileWriter(fileToSend);
            writer.write(gson.toJson(cheezlQuotesService.getAllQuotes()));
            writer.flush();
            event.getMember().getUser().openPrivateChannel().queue((privateChannel -> {
                privateChannel.sendFile(fileToSend).queue(message -> {
                    try {
                        writer.close();
                        java.nio.file.Files.delete(Path.of(filename + ".json"));
                    } catch (IOException ignored) {
                    }
                });
            }));
//            java.nio.file.Files.delete(Path.of(filename + ".json"));
            event.getHook().editOriginal("File written and sent to DMs").queue();
        }
        catch (IOException ex){
            event.getHook().editOriginal("No quotes in the DB.").queue();
        }
        catch (Exception e){
            event.getHook().editOriginal("File Command has encountered an error. Check the logs.").queue();
            e.printStackTrace();
        }

    }

    public void getVPN(SlashCommandEvent event){
        event.deferReply(true).queue();
        try {
            String name = KeyGenerators.string().generateKey();
            wgConnect.createClient(name);
//            event.getHook().sendFile(Main.client.getClientConfiguration(Main.client.getClientIdByName(name))).queue();
            event.getUser().openPrivateChannel().queue((privateChannel -> {
                File configurationFile = wgConnect.getClientConfiguration(wgConnect.getClientIdByName(name), name);
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
