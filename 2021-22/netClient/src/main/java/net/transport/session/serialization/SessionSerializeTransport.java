package net.transport.session.serialization;

import net.core.Transport;
import net.core.exception.TransportException;
import net.settings.Settings;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static net.util.Logger.log;

public class SessionSerializeTransport implements Transport {

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
        var out = new ObjectOutputStream(socket.getOutputStream());
        var in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(new Message(message));
        Message obj = (Message) in.readObject();
        log(obj);

        return obj.getMessage();
    }

    @Override
    public void disconnect() {

    }
}
