package nyc.pikaboy.listeners;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.Main;
import nyc.pikaboy.internalcommands.CheezlSlashCommands;

public class CheezlSlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        Main.logger.debug("Slash Command has been reached.");
        switch (event.getName()) {
            case "newquote" -> {
//                System.out.println("Reached the newquote");
                CheezlSlashCommands.newQuote(event);
            }
            case "removequote" -> CheezlSlashCommands.removeQuote(event);
            case "listquotes" -> CheezlSlashCommands.listQuotes(event);
            case "vpn-add" -> CheezlSlashCommands.getVPN(event);
        }
    }
}
