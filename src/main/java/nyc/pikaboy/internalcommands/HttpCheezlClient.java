package nyc.pikaboy.internalcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.OutgoingMessage;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
@Slf4j
public class HttpCheezlClient {

    public static void submitMessageLog(MessageReceivedEvent event){

        OutgoingMessage message = new OutgoingMessage(
                event.getAuthor().getAsTag(),
                event.getAuthor().getId(),
                LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()).toString(),
                event.getChannel().getName(),
                event.getChannel().getId(),
                event.getMessage().getContentDisplay(),
                botAsMessage(event)
        );

        try{
            ObjectMapper mapper = new ObjectMapper();
            String objectAsJson = mapper.writeValueAsString(message);
            Request.Post(Main.SETTINGS.getCheezlapiuri()+"/message/create")
                    .bodyString(objectAsJson, ContentType.APPLICATION_JSON).execute();
        } catch (Exception e){
            log.error("Unable to submit messages.");
        }

    }
    public static String botAsMessage(MessageReceivedEvent event){
        if (event.getAuthor().isBot()){
            return "Y";
        }
        return "N";
    }

}
