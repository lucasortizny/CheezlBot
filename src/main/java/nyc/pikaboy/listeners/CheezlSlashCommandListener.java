package nyc.pikaboy.listeners;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.internalcommands.CheezlSlashCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheezlSlashCommandListener extends ListenerAdapter {

    CheezlSlashCommands cheezlSlashCommands;
    @Autowired
    CheezlSlashCommandListener(CheezlSlashCommands cheezlSlashCommands){
        this.cheezlSlashCommands = cheezlSlashCommands;
    }
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        switch (event.getName()) {
            case "newquote" -> {
//                System.out.println("Reached the newquote");
                this.cheezlSlashCommands.newQuote(event);
            }
            case "removequote" -> this.cheezlSlashCommands.removeQuote(event);
            case "listquotes" -> this.cheezlSlashCommands.listQuotes(event);
            case "vpn-add" -> this.cheezlSlashCommands.getVPN(event);
        }
    }
}
