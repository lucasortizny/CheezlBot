package nyc.pikaboy.listeners;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.internalcommands.CheezlSlashCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheezlSlashCommandListener extends ListenerAdapter {

    private final CheezlSlashCommands cheezlSlashCommands;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "newquote" -> {
                this.cheezlSlashCommands.newQuote(event);
            }
            case "removequote" -> this.cheezlSlashCommands.removeQuote(event);
            case "listquotes" -> this.cheezlSlashCommands.listQuotes(event);
            case "vpn-add" -> this.cheezlSlashCommands.getVPN(event);
        }
    }
}
