package app.transport;

import app.IO;
import app.Settings;
import app.transport.echo.EchoRequest;
import app.transport.echo.EchoResponse;

import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class StringLineTransport implements Transport {
    private Socket socket;
    private PrintWriter writer;
    private Scanner scanner;
    private final IO io = new IO();

    public StringLineTransport() {
    }

    public StringLineTransport(Socket socket) {
        try {
            this.socket = socket;
            writer = new PrintWriter(socket.getOutputStream(), false, StandardCharsets.UTF_8);
            scanner = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void connect() throws TransportException {
        try {
            if (socket == null || socket.isClosed()) {
                socket = new Socket(Settings.HOST, Settings.PORT);
                writer = new PrintWriter(socket.getOutputStream(), false, StandardCharsets.UTF_8);
                scanner = new Scanner(socket.getInputStream(), StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public void disconnect() throws TransportException {
        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (Exception e) {
            throw new TransportException(e);
        }
    }

    @Override
    public void send(Message o) throws TransportException {
        checkIsConnected();
        try {
            var text = o.getClass().getSimpleName() + "|" + o;
            io.log("sending '%s'", text);
            writer.println(text);
            writer.flush();
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public Message receive() throws TransportException {
        checkIsConnected();
        try {
            var parts = scanner.nextLine().split("\\|");
            return responseFactory(parts[0], parts[1]);
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    private Message responseFactory(String id, String text) {
        if (id.equals(EchoRequest.class.getSimpleName())) {
            return new EchoRequest(text);
        } else if (id.equals(EchoResponse.class.getSimpleName())) {
            return new EchoResponse(text);
        } else {
            throw new TransportException("unknown message id");
        }
    }

    private void checkIsConnected() {
        if (socket == null || socket.isClosed())
            throw new TransportException("transport closed");
    }
}
