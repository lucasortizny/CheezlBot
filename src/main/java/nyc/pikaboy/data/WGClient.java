package nyc.pikaboy.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Each client object will get de/serialized into this object.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WGClient {
    public String address; //VPN IP address
    public String createdAt; //Creation date
    public boolean enabled; //Traffic Enabled
    public String id; // Unique Client ID
    public String name; // Name of Client
    public String publicKey; // Public Key used for encryption
    public String updatedAt; // Last update timestamp


}
