package nyc.pikaboy.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cheezlbot")
@Data
public class CheezlbotConfiguration {
    /** Discord Login Token **/
    private String discordToken;
    /** Primary Discord Server ID **/
    private String guildId;
    private String allowedTextChannels;
    private String apiUrl;
    private Integer randomizerBounds;
    private WireguardConfiguration wireguard;

    @Data
    @ConfigurationProperties(prefix = "cheezlbot.wireguard")
    public static class WireguardConfiguration {
        private String uri;
        private String password;
    }
}
