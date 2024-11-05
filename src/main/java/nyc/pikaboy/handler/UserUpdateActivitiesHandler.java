package nyc.pikaboy.handler;

import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.user.update.UserUpdateActivitiesEvent;
import nyc.pikaboy.service.CheezlActivityService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUpdateActivitiesHandler implements BaseHandler<UserUpdateActivitiesEvent> {
    private final CheezlActivityService cheezlActivityService;
    @Override
    public void handle(UserUpdateActivitiesEvent event) {
        //TODO: Implement User Update Activity Event handling for CheezlBot Auto-Stream change
    }
}
