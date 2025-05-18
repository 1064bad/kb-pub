package net.core;

public interface Transport {
    void connect();
    String converse(String message);
    void disconnect();
}
