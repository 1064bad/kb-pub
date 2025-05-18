package net;

import static net.util.Logger.log;

public class App {
    public static void main(String[] args) {
        log("Client starting...");
        AppConstructor.construct().run();
        log("Client finished");
    }
}
