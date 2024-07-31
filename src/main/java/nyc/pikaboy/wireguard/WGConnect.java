package nyc.pikaboy.wireguard;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nyc.pikaboy.data.SessionLogin;
import nyc.pikaboy.data.WGClient;
import nyc.pikaboy.data.WGClientCollection;
import nyc.pikaboy.data.WGClientCreation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * WGConnect Object used to connect to the Wireguard API using
 * weejewel's implementation of the WGEasy.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class WGConnect {
    @Value("${wireguard.uri}")
    private String uri;

    @Value("${wireguard.password}")
    private String password;



    /**
     * Login to the Session. Only invoke the command after verifying there is no current active session.
     * Alternatively, you can invoke the command all the time as a precursor to invoking other WGConnect commands.
     */
    public HttpResponse loginSession(){
        // Create a SessionLogin Object in order to generate JSON
        SessionLogin login = new SessionLogin(password);
        Gson newGson = new GsonBuilder().setPrettyPrinting().create();
        String ApiLoginStr = newGson.toJson(login);
        // Now create the request
        HttpClient httpClient = HttpClient.newHttpClient();
        System.out.println("Connecting to " + uri + "/api/session");
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .header("Content-Type", "application/json")
                .uri(URI.create(uri + "/api/session"))
                .POST(HttpRequest.BodyPublishers.ofString(ApiLoginStr)).build();
        HttpResponse response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Problem sending HTTP Request for Session Login");
        }
        if (response == null){
            System.out.println("Response is nothing...");
            return null;
        }
        switch (response.statusCode()){
            case 204 -> {
                System.out.println("HTTP Status is 200 OK, login succeeded. 204 OK on the POST Request.");
            }
            case 401 -> {
                System.out.println("HTTP Status is 401 Unauthorized, login unsuccessful.");
            }
            default -> {
                System.out.println("Status code is " + response.statusCode());
            }
        }
        return response;


    }

    /**
     * After logging into the session, grab a collection of Clients.
     * @return A collection of clients.
     */

    public WGClientCollection getClientCollection(){
        // Create HTTP Request to get Collection Objects
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = createRequest("GET", "/api/wireguard/client", "", "", "");
        HttpResponse response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.out.println("Unable to retrieve clients...");
            e.printStackTrace();
            return null;
        }
        switch (response.statusCode()){
            case 200 -> {
                System.out.println("Clients successfully obtained. Now converting to object form...");
                WGClient[] collection = new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .fromJson(response.body().toString(), WGClient[].class);
                WGClientCollection collectionAsObj = new WGClientCollection();
                collectionAsObj.setWGClients(List.of(collection));
                return collectionAsObj;
            }
            default -> {
                System.out.println("Did not get 200. Omitting and returning null...");
                return null;
            }
        }



    }

    public WGClient getClientById(String id){
        ArrayList<WGClient> matchingClients = new ArrayList<>();
        WGClientCollection collectionList = this.getClientCollection();
        if (collectionList.getWGClients().parallelStream().anyMatch((client) -> client.getId().equals(id))){
            collectionList.getWGClients().forEach((client) -> {
                if (client.getId().equals(id)){
                    matchingClients.add(client);
                }
            });
        }
        if (matchingClients.size() > 0){
            // ASSUMPTION: There will only be one UUID per Client.
            return matchingClients.get(0);
        }
        return null;


    }

    public WGClient getClientByName(String name){
        WGClientCollection clientCollection = this.getClientCollection();
        ArrayList<WGClient> retclient = new ArrayList<>();
         if (clientCollection.getWGClients().parallelStream().anyMatch((client) -> client.getName().equals(name))){
             clientCollection.getWGClients().forEach((client) -> {
                 if (client.getName().equals(name)){
                     retclient.add(client);
                 }
             });
         }
         if (retclient.isEmpty()){
             return null;
         }
         return retclient.get(0);
    }

    public int createClient(String name){
        // Create HTTP Request to get Collection Objects
        String body = new GsonBuilder()
                .setPrettyPrinting()
                .create()
                .toJson(new WGClientCreation(name));
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = createRequest("POST", "/api/wireguard/client", body, "", "");
        return executeRequest(httpClient, httpRequest);

    }

    public String obtainCookie(){
        String loginCookie = "";
        try {
            loginCookie = this.loginSession().headers().firstValue("set-cookie").get();
            Thread.sleep(500L);

        } catch (InterruptedException e){
            e.printStackTrace();
            System.out.println("Unable to obtain cookie...");
        }
        return loginCookie;
    }

    public HttpRequest createRequest(String requestType, String pathuri, String body, String forwarduri, String getparam){
        String cookie = obtainCookie();
        switch(requestType){
            case "GET" -> {
                return HttpRequest.newBuilder()
                        .uri(URI.create(uri + pathuri + getparam + "/" + forwarduri))
                        .setHeader("Cookie", cookie)
                        .GET()
                        .build();
            }
            case "POST" -> {
                return HttpRequest.newBuilder()
                        .uri(URI.create(uri + pathuri))
                        .setHeader("Cookie", cookie)
                        .setHeader("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();
            }
            case "DELETE" -> {
                return HttpRequest.newBuilder()
                        .uri(URI.create(uri + pathuri + getparam + "/" + forwarduri))
                        .setHeader("Cookie", cookie)
                        .DELETE()
                        .build();
            }
            default -> {
                return HttpRequest.newBuilder().build();
            }
        }
    }

    public int executeRequest(HttpClient httpClient, HttpRequest httpRequest){
        HttpResponse response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Problem sending HTTP Request for Session Login");
        }
        if (response == null){
            System.out.println("Response is nothing...");
            return 0;
        }
        switch (response.statusCode()){
            case 200 -> {
                System.out.println("HTTP Status is 200 OK.");
                return 200;
            }
            case 204 -> {
                System.out.println("HTTP Status is 200 OK, login succeeded. 204 OK on the POST Request.");
                return 204;
            }
            case 401 -> {
                System.out.println("HTTP Status is 401 Unauthorized, login unsuccessful.");
                return 401;
            }
            case 404 -> {
                System.out.println("HTTP Status is 404 Not Found. Check URI building.");
                return 404;
            }
            default -> {
                System.out.println("Status code is " + response.statusCode());
                return response.statusCode();
            }
        }
    }

    public HttpResponse executeRequestAndGetResponse(HttpClient httpClient, HttpRequest httpRequest){
        HttpResponse response = null;
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Problem sending HTTP Request for Session Login");
        }
        return response;
    }

    public int deleteClientById(String id){
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = createRequest("DELETE", "/api/wireguard/client/", "", "", id);
        return executeRequest(httpClient, httpRequest);
    }

    public int deleteClientByName(String name){
        String clientID = getClientIdByName(name);
        return deleteClientById(clientID);

    }
    public String getClientIdByName(String name){
        WGClientCollection clientCollection = getClientCollection();
        ArrayList<String> returnedClientIDs = new ArrayList<>();
        //Assumption: one uuid EVER
        clientCollection.getWGClients().forEach((client) -> {
//            System.out.println("Name is " + name + " and current client name retrieved is " + client.getName());
            if (client.getName().equals(name)){
                returnedClientIDs.add(client.getId());
            }
        });
        if (returnedClientIDs.isEmpty()){
            return "null";
        }
        return returnedClientIDs.get(0);
    }

    public File getClientConfiguration(String id){
        File writeTo = new File(id + ".conf");
        if (writeTo.exists()){
            return writeTo;
        }
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = createRequest("GET", "/api/wireguard/client/", "", "configuration", id);
        HttpResponse response = executeRequestAndGetResponse(httpClient, httpRequest);
        switch (response.statusCode()){
            case 200 -> {
                //Case where it is retrieved successfully and file does not exist.
                try {
                    writeTo.createNewFile();
                    FileWriter writer = new FileWriter(writeTo);
                    writer.write(response.body().toString());
                    writer.flush();
                    writer.close();
                    return writeTo;

                } catch (Exception e){
                    System.out.println("Please run the bot in a folder you have access to.");
                    e.printStackTrace();
                    return null;
                }

            }
            default -> {
                return null;
            }
        }
    }


}
