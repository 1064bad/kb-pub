package net;

import static net.util.Logger.log;

public class App {
    public static void main(String[] args) {
        log("Server starting...");
//        new PersistSocketServer().run();
//        new SessionSocketServer().run();
//        new SessionChannelServer().run();
//        new SessionSerializeServer().run();
        new SessionSelectorServer().run();
        log("Server finished");
    }
}
