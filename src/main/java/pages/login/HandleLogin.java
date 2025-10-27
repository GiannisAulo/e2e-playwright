package pages.login;

import config.EnvDataConfig;
import utils.enums.Actors;

import java.util.HashMap;
import java.util.Map;

public class HandleLogin {
    private final EnvDataConfig envDataConfig;
    public HandleLogin(){
        envDataConfig = new EnvDataConfig();
    }

    private Map<String, String> userCredentials(Actors actor) {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", envDataConfig.getUsername(actor.toString()));
        credentials.put("password", envDataConfig.getPassword(actor.toString()));
        credentials.put("country", envDataConfig.getCountry(actor.toString()));
        credentials.put("subdomain", envDataConfig.getSubdomain(actor.toString()));

        return credentials;
    }

}
