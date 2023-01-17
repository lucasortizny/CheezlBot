package nyc.pikaboy.listeners;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.Main;
import nyc.pikaboy.internalcommands.RandomQuoteCommand;
import org.jetbrains.annotations.NotNull;

public class SomeCustomListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        RandomQuoteCommand.sendRandomizedQuote(Main.jda, Main.SETTINGS, event);
    }


}