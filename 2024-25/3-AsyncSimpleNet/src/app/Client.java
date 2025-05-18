package app;

import app.codec.SerializableMessageCodec;
import app.message.EchoResponse;
import app.transport.BlockedTransport;
import app.transport.Transport;
import app.message.EchoRequest;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class Client {
    private final IO io = new IO();
    private final Transport transport = new BlockedTransport(new SerializableMessageCodec());

    public static void main(String[] args) {
        new Client().commandLoop();
    }

    private void commandLoop() {
        String userInput;
        do {
            io.print(">>> ");
            userInput = io.readln().strip().toLowerCase();
            try {
                switch (userInput) {
                    case "echo" -> echo();
                    case "file" -> file();
                    case "exit" -> io.println("bye!");
                    default -> io.println("unrecognized command");
                }
            } catch (Exception e) {
                io.println("error: " + e.getMessage());
            }
        } while (!"exit".equals(userInput));
    }

    private void echo() {
        io.print("enter a text: ");
        var text = io.readln();
        try {
            transport.connect();
            transport.send(new EchoRequest(text));
            var response = transport.receive();
            io.println("server response: " + response);
        } finally {
            transport.disconnect();
        }
    }

    private void file() throws IOException {
        var defaultFilename = "/home/kb/test/lenin_trunc.txt";
        io.print("enter a file path (empty for default '%s'): ".formatted(defaultFilename));
        var filename = io.readln();
        if (filename.isBlank()) {
            filename = defaultFilename;
        }

        var text = Files.readString(Path.of(filename));

        try {
            transport.connect();
            transport.send(new EchoRequest(text));
            var response = (EchoResponse) transport.receive();
//            io.println("server response: " + response);

            Files.writeString(Path.of("/home/kb/test/lenin_received.txt"),
                    response.getMessage());
        } finally {
            transport.disconnect();
        }
    }
}
