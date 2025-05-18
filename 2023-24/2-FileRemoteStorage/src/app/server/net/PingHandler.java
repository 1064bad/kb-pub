package app.server.net;

import app.IO;
import app.server.ServerException;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.user.UserService;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.LoginRequest;
import app.transport.message.storage.LoginResponse;

public class PingHandler extends Handler {
    private final UserService userService;
    private final SessionService sessionService;

    public PingHandler(Transport transport, IO io, UserService userService, SessionService sessionService) {
        super(transport, io);
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {

        transport.send(new Pong());
    }
}
