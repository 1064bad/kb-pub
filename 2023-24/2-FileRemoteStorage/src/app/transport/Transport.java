package app.transport;

import app.transport.message.Message;

import java.io.InputStream;
import java.io.OutputStream;

public interface Transport {
    void connect() throws TransportException;
    void disconnect() throws TransportException;
    void send(Message o) throws TransportException;
    Message receive() throws TransportException;
    <T extends Message> T receive(Class<T> type) throws TransportException;
    InputStream getInputStream();
    OutputStream getOutputStream();
}
