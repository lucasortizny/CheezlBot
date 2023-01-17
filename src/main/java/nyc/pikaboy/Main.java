package nyc.pikaboy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Lombok;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import nyc.pikaboy.listeners.CheezlSlashCommandListener;
import nyc.pikaboy.listeners.SomeCustomListener;
import nyc.pikaboy.settings.Settings;
import nyc.pikaboy.settings.SettingsGeneration;
import nyc.pikaboy.wireguard.WGConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ComponentScan("nyc.pikaboy")
//@PropertySource("classpath:application.yml")
public class Main {
    public static Logger logger = LoggerFactory.getLogger(Lombok.class);
    public static Settings SETTINGS;

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static String settingsDestination = "settings.json";
    public static JDA jda;
    public static WGConnect client;
    public static void main(String[] args) {
        try{
            logger.info("Obtaining settings file path from command line arguments...");
            if (args.length > 0){
                //Assume there is a custom settings value...
                settingsDestination = args[0];
            }
            logger.info("Converting to local Settings file...");
            SETTINGS = SettingsGeneration.getExistingSettings(gson, settingsDestination);
            client = new WGConnect(SETTINGS.getWireguarduri(), SETTINGS.getWireguardPassword());
            logger.info(String.format("Registering JDA Bot with token %s", SETTINGS.getDiscordToken()));
            jda = JDABuilder.createDefault(SETTINGS.getDiscordToken())
                    .addEventListeners(new SomeCustomListener(), new CheezlSlashCommandListener())
                    .build().awaitReady();

            jda.getGuildById(SETTINGS.getGuildId()).updateCommands().addCommands(
                    new CommandData("newquote", "Add a new quote to the Cheezl bot!")
                            .addOption(OptionType.STRING, "quote-key", "Name to be referenced internally within the Bot", true)
                            .addOption(OptionType.STRING, "quote", "Quote from good ol' cheezl~", true),
                    new CommandData("removequote", "Remove an existing quote by key-name.")
                            .addOption(OptionType.STRING, "quote-key", "The key-name of the associated quote.", true),
                    new CommandData("listquotes", "List all existing quotes and their key-names."),
                    new CommandData("vpn-add", "Create a VPN configuration."))
                    .queue();

        } catch (Exception e){

            logger.error("Something has failed... check logs. Exiting with code -1.");
            e.printStackTrace();
            System.exit(-1);
        }
    }
}