package app.client.command;

import app.IO;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.CheckAuthRequest;
import app.transport.message.storage.CheckAuthResponse;

public class CheckAuthCommand extends Command {
    private final TokenHolder tokenHolder;

    public CheckAuthCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    public void performConnected() {
        transport.send(new CheckAuthRequest(tokenHolder.getToken()));
        var response = expectMessage(CheckAuthResponse.class);
        io.println(response.getMessage());
    }
}
