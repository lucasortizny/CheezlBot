package nyc.pikaboy.listeners;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.internalcommands.HttpCheezlClient;
import nyc.pikaboy.internalcommands.RandomQuoteService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RandomizedQuoteListener extends ListenerAdapter {

    private final RandomQuoteService randomQuoteService;
    private final HttpCheezlClient httpCheezlClient;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        this.randomQuoteService.sendRandomizedQuote(event);
        this.httpCheezlClient.submitMessageLog(event);
    }


}