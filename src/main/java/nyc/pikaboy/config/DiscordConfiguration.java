package nyc.pikaboy.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import nyc.pikaboy.listeners.CheezlSlashCommandListener;
import nyc.pikaboy.listeners.RandomizedQuoteListener;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DiscordConfiguration {
    private final CheezlbotConfiguration cheezlbotConfiguration;
    private final RandomizedQuoteListener randomizedQuoteListener;
    private final CheezlSlashCommandListener cheezlSlashCommandListener;

    @Bean("jda")
    public JDA discordBot(){
        try {
            log.debug("Creating the JDA Builder for the Bot.");
            JDA jda;
            jda = JDABuilder.createDefault(cheezlbotConfiguration.getDiscordToken())
                    .addEventListeners(randomizedQuoteListener, cheezlSlashCommandListener)
                    .build().awaitReady();
            log.debug("Registering different commands.");
            jda.getGuildById(cheezlbotConfiguration.getGuildId()).updateCommands().addCommands(
                            new CommandDataImpl("newquote", "Add a new quote to the Cheezl bot!")
                                    .addOption(OptionType.STRING, "quote-key", "Name to be referenced internally within the Bot", true)
                                    .addOption(OptionType.STRING, "quote", "Quote from good ol' cheezl~", true),
                            new CommandDataImpl("removequote", "Remove an existing quote by key-name.")
                                    .addOption(OptionType.STRING, "quote-key", "The key-name of the associated quote.", true),
                            new CommandDataImpl("listquotes", "List all existing quotes and their key-names."),
                            new CommandDataImpl("vpn-add", "Create a VPN configuration."))
                    .queue();
            return jda;
        } catch (Exception e){
            log.error("Error registering JDA.", e);
            return null;
        }
    }

}
