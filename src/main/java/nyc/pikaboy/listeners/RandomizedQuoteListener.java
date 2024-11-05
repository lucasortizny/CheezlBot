package nyc.pikaboy.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.UserActivityStartEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivityOrderEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.internalcommands.HttpCheezlClient;
import nyc.pikaboy.internalcommands.RandomQuoteService;
import nyc.pikaboy.service.CheezlActivityService;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RandomizedQuoteListener extends ListenerAdapter {

    private final RandomQuoteService randomQuoteService;
    private final HttpCheezlClient httpCheezlClient;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        this.randomQuoteService.sendRandomizedQuote(event);
        httpCheezlClient.submitMessageLog(event);
    }


    @Override
    public void onUserUpdateActivities(@NotNull UserUpdateActivitiesEvent event) {

    }
}