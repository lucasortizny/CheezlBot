package nyc.pikaboy.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OutgoingMessage {

    String username;
    String userid;
    String timestamp;
    String channelname;
    String channelid;
    String messagecontent;
    String isbot;
}
