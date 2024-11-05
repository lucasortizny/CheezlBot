package nyc.pikaboy.service;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheezlActivityServiceImpl implements CheezlActivityService {
    private final JDA jda;
    @Override
    public void changeToDefaultActivity() {

    }

    @Override
    public void changeToNewActivity(Activity activity) {
        jda.getPresence().setActivity(activity);
    }
}
