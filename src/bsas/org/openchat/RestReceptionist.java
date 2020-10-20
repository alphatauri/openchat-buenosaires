package bsas.org.openchat;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.eclipse.jetty.http.HttpStatus.BAD_REQUEST_400;
import static org.eclipse.jetty.http.HttpStatus.CREATED_201;

public class RestReceptionist {
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String ABOUT_KEY = "about";
    public static final String ID_KEY = "id";
    private final OpenChatSystem system;
    private final Map<String,User> usersById = new HashMap<>();

    public RestReceptionist(OpenChatSystem system) {
        this.system = system;
    }

    public ReceptionistResponse registerUser(String registrationBody) {
        JsonObject registrationAsJson = Json.parse(registrationBody).asObject();

        try {
            User registeredUser = system.register(
                    registrationAsJson.getString(USERNAME_KEY, ""),
                    registrationAsJson.getString(PASSWORD_KEY, ""),
                    registrationAsJson.getString(ABOUT_KEY, ""));

            final String registeredUserId = UUID.randomUUID().toString();
            usersById.put(registeredUserId,registeredUser);

            JsonObject responseAsJson = new JsonObject()
                    .add(ID_KEY,registeredUserId)
                    .add(USERNAME_KEY,registeredUser.name())
                    .add(ABOUT_KEY,registeredUser.about());

            return new ReceptionistResponse(CREATED_201, responseAsJson.toString());
        } catch (RuntimeException error){
            return new ReceptionistResponse(BAD_REQUEST_400,error.getMessage());
        }

    }
}
