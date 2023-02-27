package nyc.pikaboy.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nyc.pikaboy.Main;
import nyc.pikaboy.settings.Settings;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.util.Optional;

public class CheezlQuoteMethods {
    public static Optional<CheezlQuote> getRandomQuote(){
        try {
            Response response = Request.Get(Main.SETTINGS.getCheezlapiuri()+"/quote/random")
                    .execute();
            Content content = response.returnContent();
            if (content.asString() == null || content.asString().isEmpty()){
                return Optional.empty();
            }


            String receivedQuote = content.asString();
            return Optional.of(new ObjectMapper().readValue(receivedQuote, CheezlQuote.class));
        } catch (HttpResponseException ex){
            Main.logger.error("Error, most likely because no quotes in DB. Ignoring.");
            return Optional.empty();
        }
        catch (IOException e) {
            Main.logger.error("IO Exception. Printing stack trace.");
            e.printStackTrace();
            return Optional.empty();
        }

    }

    public static void addCheezlQuote(Settings settings, CheezlQuote quote){
//        settings.getCheezlQuotesList().add(quote);
        try {
            String bodyToSend = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(quote);
            Request.Post(Main.SETTINGS.getCheezlapiuri()+"/quote/create")
                    .bodyString(bodyToSend, ContentType.APPLICATION_JSON).execute();
        } catch (JsonProcessingException e) {
            Main.logger.error("Problem adding quote. Let's just proceed...");
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            Main.logger.error("Client protocol for HTTP is incorrect. Double check protocol.");
            e.printStackTrace();
        } catch (IOException e) {
            Main.logger.error("Problem with IO. Exiting...");
            e.printStackTrace();
        }

//        SettingsGeneration.saveExistingSettings(Main.gson, Main.settingsDestination, settings);
    }
}
