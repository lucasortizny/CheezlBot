package nyc.pikaboy.settings;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nyc.pikaboy.data.CheezlQuote;

import java.util.ArrayList;
import java.util.List;

/**
 * Settings files for the Cheezl Bot
 */
@Data
@NoArgsConstructor
public class Settings {
    private String discordToken = "";
    private String guildId = "";
    public List<String> allowedTextChannels = new ArrayList<>();
    public List<CheezlQuote> cheezlQuotesList = new ArrayList<>();

    public int randomizerBounds = 2;
    public String wireguarduri;
    public String wireguardPassword;
    public String cheezlapiuri;

}
