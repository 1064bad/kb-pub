package app.transport;

import app.Settings;
import app.codec.MessageCodec;
import app.message.Message;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class BlockedTransport implements Transport {
    private Socket socket;
    private final MessageCodec codec;

    public BlockedTransport(MessageCodec codec) {
        assert codec != null;
        this.codec = codec;
    }

    public BlockedTransport(Socket socket, MessageCodec codec) {
        assert socket != null && codec != null;
        try {
            this.codec = codec;
            this.socket = socket;
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
            codec.encode(m, socket.getOutputStream());
            socket.getOutputStream().flush();
        } catch (Exception e) {
            disconnect();
            throw new TransportException(e);
        }
    }

    @Override
    public Message receive() throws TransportException {
        checkIsConnected();
        try {
            return codec.decode(socket.getInputStream());
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
