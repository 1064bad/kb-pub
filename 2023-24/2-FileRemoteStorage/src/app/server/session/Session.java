package app.server.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private final Map<String, Object> map = new ConcurrentHashMap<>();
    public static final String
            USERNAME = "username",
            LAST_USAGE = "last_usage";

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public String getString(String key) {
        return (String) get(key);
    }

}
