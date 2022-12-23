package nyc.pikaboy.listeners;

import net.dv8tion.jda.api.events.application.GenericApplicationCommandEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nyc.pikaboy.Main;
import nyc.pikaboy.data.CheezlQuote;
import nyc.pikaboy.data.CheezlQuoteMethods;
import org.jetbrains.annotations.NotNull;

public class CheezlSlashCommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        switch (event.getName()){
            case "newquote":
                event.deferReply(true).queue();
                try {
                    String keyname = event.getOption("quote-key").getAsString();
                    String quote = event.getOption("quote").getAsString();
                    CheezlQuoteMethods.addCheezlQuote(Main.SETTINGS, new CheezlQuote(keyname, quote));
                    event.getHook().sendMessage("Successfully stored.").queue();

                } catch (Exception e){
                    System.out.println("Command invokation failed. Not doing anything :(");
                }
        }
    }
}
