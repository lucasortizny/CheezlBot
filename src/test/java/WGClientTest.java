import nyc.pikaboy.data.WGClient;
import nyc.pikaboy.data.WGClientCollection;
import nyc.pikaboy.wireguard.WGConnect;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

public class WGClientTest {
    static WGConnect client;
    static WGClientCollection referCollection;

    static ArrayList<String> testClientNames;

    @BeforeClass
    public static void setUp(){
        client = new WGConnect("http://10.0.0.23:51821", "PokettoMonster0912");
        referCollection = new WGClientCollection();
        referCollection.getWGClients()
                .add(new WGClient("10.8.0.2", "2022-11-24T04:55:21.487Z", false, "71e472c9-5ec8-4589-95a3-8a5289d2e0c3",
                        "cheezl-ny-pikaboy", "5G3o0y43dWz4FTUju5VnVmHk2hu5qpyRBsYcFwjBcBI=", "2022-12-29T16:58:53.973Z"));
        testClientNames = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            testClientNames.add(UUID.randomUUID().toString());
        }
    }

    /**
     * Test the Login Session functionality.
     */
    @Test
    public void testWGLoginSession(){
        Assert.assertEquals(204, client.loginSession().statusCode());
    }

    /**
     * Test the retrieval of WGClient objects.
     * @throws InterruptedException
     */
    @Test
    public void testWGClientCollectionRetrieval() throws InterruptedException {
        client.loginSession();
        WGClientCollection collectionTest = client.getClientCollection();

        referCollection.getWGClients().forEach((client) -> {
            Assert.assertTrue(collectionTest.getWGClients().contains(client));
        });
    }

    /**
     * Test the functionality of retrieving clients by certain properties.
     */
    @Test
    public void testWGClientRetrievalByProperties() {
        referCollection.getWGClients().forEach((clientRetrieved) -> {
            Assert.assertEquals(clientRetrieved, client.getClientByName(clientRetrieved.getName()));
            Assert.assertEquals(clientRetrieved, client.getClientById(clientRetrieved.getId()));
        });

    }

    @Test
    public void testWGClientAggregation(){
        testClientNames.forEach((clientname) -> {
            Assert.assertEquals(200, client.createClient(clientname));
        });
        // Now retrieve the client list and compare.
        WGClientCollection collectedClients = client.getClientCollection();
        testClientNames.forEach((clientname) -> {
           Assert.assertTrue(collectedClients.getWGClients().parallelStream().anyMatch((client) -> client.getName().equals(clientname)));
        });
    }
    @Test
    public void testWGClientDeletion(){
        testClientNames.forEach((clientname) -> {
            System.out.println("Client name: " + clientname);
           Assert.assertEquals(204, client.deleteClientByName(clientname));
        });
    }




}
