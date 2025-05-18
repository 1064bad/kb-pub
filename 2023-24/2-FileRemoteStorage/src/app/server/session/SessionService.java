package app.server.session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO session expiration
public class SessionService {
    private final Map<Token, Session> map = new ConcurrentHashMap<>();

    public TokenSession create() {
        var token = new Token();
        var session = new Session();
        map.put(token, session);
        return new TokenSession(token, session);
    }

    public Session get(Token token) {
        return map.get(token);
    }

    public void remove(Token token) {
        map.remove(token);
    }

    public record TokenSession(Token token, Session session) {
    }
}
