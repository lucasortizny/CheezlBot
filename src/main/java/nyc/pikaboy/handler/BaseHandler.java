package nyc.pikaboy.handler;

import net.dv8tion.jda.api.events.Event;

public interface BaseHandler<T extends Event> {
    void handle(T event);
}
