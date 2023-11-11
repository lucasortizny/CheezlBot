package nyc.pikaboy.config;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import nyc.pikaboy.listeners.CheezlSlashCommandListener;
import nyc.pikaboy.listeners.SomeCustomListener;
import nyc.pikaboy.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
@Slf4j
public class DiscordConfiguration {

    @Bean("settings")
    public Settings getSettings(@Value("${cheezlbot.discord-token}") String discordToken,
                                @Value("${cheezlbot.guild-id}") String guildId,
                                @Value("${cheezlbot.allowed-text-channels}") List<String> allowedTextChannels,
                                @Value("${cheezlbot.api-uri}") String cheezlApiUri,
                                @Value("${wireguard.uri}") String wireguardUri,
                                @Value("${wireguard.password}") String wireguardPassword,
                                @Value("${cheezlbot.randomizer-bounds}") Integer randomizerBounds){
        return Settings.builder()
                .discordToken(discordToken)
                .guildId(guildId)
                .allowedTextChannels(allowedTextChannels)
                .cheezlapiuri(cheezlApiUri)
                .wireguarduri(wireguardUri)
                .wireguardPassword(wireguardPassword)
                .randomizerBounds(randomizerBounds)
                .build();
    }
    @Bean
    @Autowired
    public RestTemplate cheezlApiRestTemplate(Settings settings){
        return new RestTemplateBuilder()
                .rootUri(settings.getCheezlapiuri())
                .build();
    }
    @Bean("jda")
    @Autowired
    public JDA discordBot(Settings setting, SomeCustomListener someCustomListener,
                          CheezlSlashCommandListener cheezlSlashCommandListener){
        try {
            log.debug("Creating the JDA Builder for the Bot.");
            JDA jda;
            jda = JDABuilder.createDefault(setting.getDiscordToken())
                    .addEventListeners(someCustomListener, cheezlSlashCommandListener)
                    .build().awaitReady();
            log.debug("Registering different commands.");
            jda.getGuildById(setting.getGuildId()).updateCommands().addCommands(
                            new CommandData("newquote", "Add a new quote to the Cheezl bot!")
                                    .addOption(OptionType.STRING, "quote-key", "Name to be referenced internally within the Bot", true)
                                    .addOption(OptionType.STRING, "quote", "Quote from good ol' cheezl~", true),
                            new CommandData("removequote", "Remove an existing quote by key-name.")
                                    .addOption(OptionType.STRING, "quote-key", "The key-name of the associated quote.", true),
                            new CommandData("listquotes", "List all existing quotes and their key-names."),
                            new CommandData("vpn-add", "Create a VPN configuration."))
                    .queue();
            return jda;
        } catch (Exception e){
            log.error("Error registering JDA.", e);
            return null;
        }
    }

}
