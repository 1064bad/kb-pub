package app;

import app.transport.StringLineTransport;
import app.transport.Transport;
import app.transport.echo.EchoRequest;
import app.transport.echo.SerializedTransport;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
    private final IO io = new IO();
    public static void main(String[] args) throws Exception {
        new Server().listenLoop();
    }

    private void listenLoop() throws IOException {
        try (var ss = new ServerSocket(Settings.PORT)) {
            while (true) {
                var clientSocket = ss.accept();
                io.println("client connected: " + clientSocket);
                clientSocket.setSoTimeout(1000);
                var transport = new StringLineTransport(clientSocket);
//                var transport = new SerializedTransport(clientSocket);
                echo(transport);
            }
        }
    }

    private void echo(Transport transport) {
        try {
            transport.send(new EchoRequest("echo " + transport.receive()));
        } finally {
            transport.disconnect();
        }
    }
}
