package server;

import server.net.HttpServer;

public class App {
    public static void main(String[] args) {
        new HttpServer().run();
    }
}
