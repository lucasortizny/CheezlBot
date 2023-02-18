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


//    public String getGuildId() {
//        return guildId;
//    }
//
//    public void setGuildId(String guildId) {
//        this.guildId = guildId;
//    }
//
//    public int getRandomizerBounds() {
//        return randomizerBounds;
//    }
//
//    public void setRandomizerBounds(int randomizerBounds) {
//        this.randomizerBounds = randomizerBounds;
//    }
//
//
//    public List<String> getAllowedTextChannels() {
//        return allowedTextChannels;
//    }
//
//    public void setAllowedTextChannels(List<String> allowedTextChannels) {
//        this.allowedTextChannels = allowedTextChannels;
//    }
//
//    public List<CheezlQuote> getCheezlQuotesList() {
//        return cheezlQuotesList;
//    }
//
//    public void setCheezlQuotesList(List<CheezlQuote> cheezlQuotesList) {
//        this.cheezlQuotesList = cheezlQuotesList;
//    }
//
//    public String getDiscordToken() {
//        return discordToken;
//    }
//
//    public void setDiscordToken(String discordToken) {
//        this.discordToken = discordToken;
//    }

//    public Settings (){
//        // Instantiate Default Values
//
//        cheezlQuotesList.add(new CheezlQuote("cat cum", "I just came in a cat"));
//        cheezlQuotesList.add(new CheezlQuote("gang rape", "Time for gaaang rape"));
//        cheezlQuotesList.add(new CheezlQuote("children orgasm", "Am I hearing children in pain having an orgasm?"));
//        cheezlQuotesList.add(new CheezlQuote("children orgasm 2", "I want children to orgasm"));
//        cheezlQuotesList.add(new CheezlQuote("mums MDMA", "I got in trouble for touching my mum's MDMA"));
//        cheezlQuotesList.add(new CheezlQuote("sixteen", "I'm sixteen"));
//        cheezlQuotesList.add(new CheezlQuote("fuck parents", "Fuck your parents"));
//        cheezlQuotesList.add(new CheezlQuote("fuck mother", "I am going tooo~ Fuck my mother~"));
//        cheezlQuotesList.add(new CheezlQuote("brpbrpbrp", "Oh ho ho brpbrpbrpbrpbrp nya nya nya"));
//        cheezlQuotesList.add(new CheezlQuote("pro retard", "I don't think he's a pro, I think he's fuckin retarded"));
//        cheezlQuotesList.add(new CheezlQuote("orgasm", "*orgasm noises*"));
//        cheezlQuotesList.add(new CheezlQuote("sister HALF", "I will fuck your sister, in HALF, cunt"));
//        cheezlQuotesList.add(new CheezlQuote("rape everyone's sisters", "I'm gonna actually gonna rape everyone's sisters and mothers, I swear to god"));
//        cheezlQuotesList.add(new CheezlQuote("cocaine", "I had like STL um LSD, all at the same time. And cocaine."));
//
//
//
//    }

//    public Settings (List<String> allowedTextChannels, List<CheezlQuote> cheezlQuotesList){
//        this.allowedTextChannels = allowedTextChannels;
//        this.cheezlQuotesList = cheezlQuotesList;
//    }

}
