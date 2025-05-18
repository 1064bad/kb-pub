package app.server.user;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class UserService {
    private final Map<String, String> userPasswords = new ConcurrentHashMap<>() {{
        put("user", "secret");
    }};

    public boolean userExists(String username) {
        return userPasswords.containsKey(username);
    }

    public void register(String username, String password) {
        if (userPasswords.putIfAbsent(username, password) != null) {
            throw new UserException("register new user error - user exists");
        }
    }

    public boolean isPasswordValid(String password) {
        return password != null && !password.isBlank();
    }

    public boolean checkPassword(String username, String password) {
        return Objects.equals(password, userPasswords.get(username));
    }
}
