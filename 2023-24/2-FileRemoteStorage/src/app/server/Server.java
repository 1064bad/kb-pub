package app.server;

import app.IO;
import app.Settings;
import app.server.filestorage.FileSystemService;
import app.server.net.*;
import app.server.session.SessionService;
import app.server.session.Token;
import app.server.user.UserService;
import app.transport.SerializedTransport;
import app.transport.Transport;
import app.transport.message.AuthorizedMessage;
import app.transport.message.ErrorResponse;
import app.transport.message.Message;
import app.transport.message.SuccessResponse;
import app.transport.message.storage.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private final IO io = new IO();
    private final UserService userService = new UserService();
    private final SessionService sessionService = new SessionService();
    private final FileSystemService fileSystemService = new FileSystemService();
    private final ExecutorService pool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        new Server().listenLoop();
    }

    private void listenLoop() throws IOException {
        try (var ss = new ServerSocket(Settings.PORT)) {
            io.println(STR."server listening on port \{Settings.PORT}");
            while (true) {
                try {
                    var clientSocket = ss.accept();
                    io.println(STR."client connected: \{clientSocket}");
                    pool.submit(() -> handle(new SerializedTransport(clientSocket)));
                } catch (Exception e) {
                    io.println(STR."error: \{e.getMessage()}");
                }
            }
        } finally {
            pool.shutdown();
        }
    }

    private void handle(Transport transport) {
        try {
            var request = transport.receive();
            checkAuth(request);
            routeToHandler(transport, request);
        } catch (Exception e) {
            io.println(STR."handle error: \{e.getMessage()}");
            transport.send(new ErrorResponse(e.getMessage()));
        } finally {
            transport.disconnect();
        }
    }

    private void checkAuth(Message request) {
        if (request instanceof AuthorizedMessage auth) {
            if (auth.getAuthToken() == null) {
                throw new ServerException("no authorization token found in request");
            }
            var token = Token.fromText(auth.getAuthToken());
            var session = sessionService.get(token);
            if (session == null) {
                throw new ServerException("authorization failed");
            }
        }
    }

    private void routeToHandler(Transport transport, Message request) {
        if (request instanceof Ping) {
            transport.send(new Pong());
        }

        (switch (request) {
            case RegisterUsernameRequest req -> new RegisterHandler(transport, io, userService, fileSystemService);
            case LoginRequest req -> new LoginHandler(transport, io, userService, sessionService);
            case CheckAuthRequest req -> new CheckAuthHandler(transport, io, sessionService);
            case FileListRequest req -> new FileListHandler(transport, io, fileSystemService, sessionService);
            case FileUploadRequest req -> new FileUploadHandler(transport, io, fileSystemService, sessionService);
            case FileDownloadRequest req -> new FileDownloadHandler(transport, io, fileSystemService, sessionService);
            case FileDeleteRequest req -> new FileDeleteHandler(transport, io, fileSystemService, sessionService);
            default -> new UnimplementedHandler(transport, io);
        }).handle(request);
    }
}
