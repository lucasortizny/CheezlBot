package nyc.pikaboy.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.internalcommands.HttpCheezlClient;
import nyc.pikaboy.internalcommands.RandomQuoteService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SomeCustomListener extends ListenerAdapter {

    RandomQuoteService randomQuoteService;
    @Autowired
    SomeCustomListener(RandomQuoteService randomQuoteService){
        this.randomQuoteService = randomQuoteService;
    }
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        HttpCheezlClient.submitMessageLog(event);
        this.randomQuoteService.sendRandomizedQuote(event);
    }


}