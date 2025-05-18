package app.client.command;

import app.IO;
import app.client.token.TokenHolder;
import app.transport.Transport;
import app.transport.message.storage.LoginRequest;
import app.transport.message.storage.LoginResponse;

public class LoginCommand extends Command {
    private final TokenHolder tokenHolder;

    public LoginCommand(Transport transport, IO io, TokenHolder tokenHolder) {
        super(transport, io);
        this.tokenHolder = tokenHolder;
    }

    @Override
    public void performConnected() {
        io.print("enter username: ");
        var username = io.readln();
        io.print("enter password: ");
        var password = io.readln();

        transport.send(new LoginRequest(username, password));

        var response = expectMessage(LoginResponse.class);
        tokenHolder.setToken(response.getToken());

        io.println(STR."User successfully login with token \{tokenHolder.getToken()}");
    }
}
