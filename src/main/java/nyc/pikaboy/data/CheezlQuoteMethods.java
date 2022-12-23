package nyc.pikaboy.data;

import nyc.pikaboy.Main;
import nyc.pikaboy.settings.Settings;
import nyc.pikaboy.settings.SettingsGeneration;

import java.util.Random;

public class CheezlQuoteMethods {
    public static CheezlQuote getRandomQuote(){
        return Main.SETTINGS.getCheezlQuotesList().get(new Random().nextInt(Main.SETTINGS.getCheezlQuotesList().size()));
    }

    public static void addCheezlQuote(Settings settings, CheezlQuote quote){
        settings.getCheezlQuotesList().add(quote);
        SettingsGeneration.saveExistingSettings(Main.gson, Main.settingsDestination, settings);
    }
}
