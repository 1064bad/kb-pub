package app.transport.echo;

import app.Settings;
import app.transport.Message;
import app.transport.Transport;
import app.transport.TransportException;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SerializedTransport implements Transport {
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;

    public SerializedTransport() {
    }

    public SerializedTransport(Socket socket) {
        try {
            this.socket = socket;
            writer = new ObjectOutputStream(socket.getOutputStream());
            reader = new ObjectInputStream(socket.getInputStream());
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
                writer = new ObjectOutputStream(socket.getOutputStream());
                reader = new ObjectInputStream(socket.getInputStream());
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
    public void send(Message m) throws TransportException {
        checkIsConnected();
        try {
            writer.writeObject(m);
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
            return (Message) reader.readObject();
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    private void checkIsConnected() {
        if (socket == null || socket.isClosed())
            throw new TransportException("transport closed");
    }
}
