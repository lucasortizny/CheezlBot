package nyc.pikaboy.settings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nyc.pikaboy.data.CheezlQuote;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings files for the Cheezl Bot
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Settings {
    private String discordToken = "";
    private String guildId = "";
    private List<String> allowedTextChannels = new ArrayList<>();
    private List<CheezlQuote> cheezlQuotesList = new ArrayList<>();

    private int randomizerBounds = 2;
    private String wireguarduri;
    private String wireguardPassword;
    private String cheezlapiuri;

}
