package net.transport;

import net.core.Transport;

import static net.util.Logger.log;

public class StubTransport implements Transport {
    @Override
    public void connect() {
        log("connect");
    }

    @Override
    public String converse(String message) {
        log("converse " + message);
        return "echo " + message;
    }

    @Override
    public void disconnect() {
        log("disconnect");
    }
}
