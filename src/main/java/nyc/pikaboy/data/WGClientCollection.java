package nyc.pikaboy.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Bind the client responses to this class utilizing JSON.
 */
@Data
public class WGClientCollection {
//    public WGClient[] WGClients = new WGClient[256];
    public List<WGClient> WGClients = new ArrayList<>();
}
