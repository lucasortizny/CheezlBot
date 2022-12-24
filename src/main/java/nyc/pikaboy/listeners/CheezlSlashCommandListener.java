package nyc.pikaboy.listeners;

import net.dv8tion.jda.api.events.application.GenericApplicationCommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;
import nyc.pikaboy.internalcommands.CheezlSlashCommands;
import org.jetbrains.annotations.NotNull;

public class CheezlSlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        System.out.println("Reached Slash Command");
        switch (event.getName()){
            case "newquote":
                System.out.println("Reached the newquote");
                CheezlSlashCommands.newQuote(event);
                break;
            case "removequote":
                CheezlSlashCommands.removeQuote(event);
                break;
            case "listquotes":
                CheezlSlashCommands.listQuotes(event);
                break;

        }
    }
}
