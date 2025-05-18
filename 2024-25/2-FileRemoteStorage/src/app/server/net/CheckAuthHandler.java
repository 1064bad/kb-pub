package app.server.net;

import app.IO;
import app.server.session.Session;
import app.server.session.SessionService;
import app.server.session.Token;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.storage.CheckAuthRequest;
import app.transport.message.storage.CheckAuthResponse;

public class CheckAuthHandler extends Handler {
    private final SessionService sessionService;

    public CheckAuthHandler(Transport transport, IO io, SessionService sessionService) {
        super(transport, io);
        this.sessionService = sessionService;
    }

    @Override
    public void handle(Message message) {
        var req = (CheckAuthRequest) message;
        var session = sessionService.get(Token.fromText(req.getAuthToken()));
        var username = session.getString(Session.USERNAME);
        transport.send(new CheckAuthResponse(STR."you logged in as '\{username}'"));
    }
}
