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

public class LoginHandler extends Handler {
    private final UserService userService;
    private final SessionService sessionService;

    public LoginHandler(Transport transport, IO io, UserService userService, SessionService sessionService) {
        super(transport, io);
        this.userService = userService;
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (LoginRequest) message;

        if (!userService.checkPassword(req.getUsername(), req.getPassword())) {
            throw new ServerException("username or password are incorrect");
        }

        var pair = sessionService.create();
        var token = pair.token();
        var session = pair.session();
        session.put(Session.USERNAME, req.getUsername());

        transport.send(new LoginResponse(token.getText()));
        io.println(STR."user \{req.getUsername()} logged in with token \{token}");
    }
}
