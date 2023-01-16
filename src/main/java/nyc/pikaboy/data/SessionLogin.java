package nyc.pikaboy.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The session login object will be built using this object, then later serialized in JSON.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionLogin {
    String password;
}
