package nyc.pikaboy.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;

public interface CheezlActivityService {
    void changeToNewActivity(Activity activity);
    void changeToDefaultActivity();
}
