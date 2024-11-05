package nyc.pikaboy.config;

import lombok.Data;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cheezlbot")
@Data
public class CheezlbotConfiguration {
    /** Primary Discord Server ID **/
    private String guildId;
    private String allowedTextChannels;
    private String apiUrl;
    private Integer randomizerBounds;
    private boolean legacySupport = false;
    private DiscordConfiguration discordConfiguration;
    private WireguardConfiguration wireguard;

    @Data
//    @ConfigurationProperties(prefix = "cheezlbot.wireguard")
    public static class WireguardConfiguration {
        private String uri;
        private String password;
    }
    @Data
//    @ConfigurationProperties(prefix = "cheezlbot.discord")
    public static class DiscordConfiguration {
        private String token;
        private Activity.ActivityType activityType;
        private String activityMessage;
        private String activityDescription;
    }
}
