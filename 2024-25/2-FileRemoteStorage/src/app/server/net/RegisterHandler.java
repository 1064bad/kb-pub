package app.server.net;

import app.IO;
import app.server.ServerException;
import app.server.filestorage.FileStorageService;
import app.server.user.UserService;
import app.transport.Transport;
import app.transport.message.Message;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.RegisterPasswordRequest;
import app.transport.message.storage.RegisterUsernameRequest;

public class RegisterHandler extends Handler {
    private final UserService userService;
    private final FileStorageService fileSystemService;

    public RegisterHandler(Transport transport, IO io, UserService userService, FileStorageService fileSystemService) {
        super(transport, io);
        this.userService = userService;
        this.fileSystemService = fileSystemService;
    }

    @Override
    public void handle(Message message) {
        var req = (RegisterUsernameRequest) message;

        var username = req.getUsername();
        io.debug(STR."username - \{username}");
        if (!fileSystemService.isUsernameValid(username)) {
            throw new ServerException("username is invalid, pattern: [a-zA-Z0-9_]+");
        }
        if (userService.userExists(username)) {
            throw new ServerException("user registered already");
        }
        transport.send(new SuccessResponse());

        var password = transport.receive(RegisterPasswordRequest.class).getPassword();
        io.debug(STR."password - \{password}");
        if (!userService.isPasswordValid(password)) {
            throw new ServerException("password is invalid");
        }

        userService.register(username, password);
        transport.send(new SuccessResponse());
        io.println(STR."registered \{username}:\{password}");
    }
}
