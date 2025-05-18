package app.transport;

import app.message.Message;

public interface Transport {
    void connect() throws TransportException;
    void disconnect() throws TransportException;
    void send(Message o) throws TransportException;
    Message receive() throws TransportException;
}
