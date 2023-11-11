package nyc.pikaboy.internalcommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import nyc.pikaboy.data.OutgoingMessage;
import nyc.pikaboy.service.CheezlMessageLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
@Slf4j
@Component
public class HttpCheezlClient {
    CheezlMessageLoggingService cheezlMessageLoggingService;
    @Autowired
    HttpCheezlClient(CheezlMessageLoggingService cheezlMessageLoggingService){
        this.cheezlMessageLoggingService = cheezlMessageLoggingService;
    }

    public void submitMessageLog(MessageReceivedEvent event){

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
            cheezlMessageLoggingService.submitMessage(message);
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
