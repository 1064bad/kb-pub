package net.transport.session.socket;

import net.core.Transport;
import net.core.exception.TransportException;
import net.settings.Settings;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SessionSocketTransport implements Transport {
    @Override
    public void connect() {

    }

    @Override
    public String converse(String message) {
        try (var socket = new Socket()) {
            socket.connect(Settings.ADDRESS);
            return tryConverse(socket, message);
        }
        catch (Exception e) {
            throw new TransportException(e);
        }
    }

    private String tryConverse(Socket socket, String message) throws Exception {
        var out = new PrintWriter(socket.getOutputStream(), true,
                StandardCharsets.UTF_8);
        var in = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                StandardCharsets.UTF_8));

        out.println(message);
        return in.readLine();
    }

    @Override
    public void disconnect() {

    }
}
