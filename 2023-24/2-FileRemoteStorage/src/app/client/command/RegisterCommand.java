package app.client.command;

import app.IO;
import app.transport.Transport;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.RegisterPasswordRequest;
import app.transport.message.storage.RegisterUsernameRequest;

public class RegisterCommand extends Command {
    public RegisterCommand(Transport transport, IO io) {
        super(transport, io);
    }

    @Override
    public void performConnected() {
        io.print("enter username: ");
        var username = io.readln();
        transport.send(new RegisterUsernameRequest(username));
        expectMessage(SuccessResponse.class);

        io.print("enter password: ");
        var password = io.readln();
        transport.send(new RegisterPasswordRequest(password));
        expectMessage(SuccessResponse.class);

        io.println("User successfully registered");
    }
}
