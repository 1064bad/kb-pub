package app.transport;

import app.IO;
import app.transport.echo.EchoRequest;

public class StubTransport implements Transport {
    private final IO io = new IO();

    @Override
    public void connect() throws TransportException {
        io.log("transport connecting");
    }

    @Override
    public void disconnect() throws TransportException {
        io.log("transport disconnecting");
    }

    @Override
    public void send(Message o) throws TransportException {
        io.log("transport sending " + o);
    }

    @Override
    public Message receive() throws TransportException {
        io.log("transport receiving an object");
        return new EchoRequest("stub transport object");
    }
}
