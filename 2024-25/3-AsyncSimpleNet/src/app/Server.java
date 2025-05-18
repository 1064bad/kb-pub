package app;

import app.codec.SerializableMessageCodec;
import app.message.EchoRequest;
import app.message.EchoResponse;
import app.message.Message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class Server {
    private final IO io = new IO();
    private Selector selector;

    public static void main(String[] args) throws Exception {
        new Server().listenLoop();
    }

    private void listenLoop() throws IOException {
        try (var ssc = ServerSocketChannel.open(); var selector = Selector.open()) {
            this.selector = selector;
            ssc.bind(new InetSocketAddress("localhost", Settings.PORT));
            ssc.configureBlocking(false);
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();

                var keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    var key = keyIterator.next();
                    try {
                        if (key.isValid() && key.isAcceptable()) {
                            accept(key);
                        }

                        if (key.isValid() && key.isReadable()) {
                            read(key);
                        }

                        if (key.isValid() && key.isWritable()) {
                            write(key);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            key.cancel();
                            key.channel().close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }

                    keyIterator.remove();
                }
            }
        }
    }

    private void accept(SelectionKey key) throws IOException {
        var ssc = (ServerSocketChannel) key.channel();
        SocketChannel clientChannel;
        do {
            clientChannel = ssc.accept();
            if (clientChannel != null) {
                clientChannel.configureBlocking(false);
                var clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
                clientKey.attach(new KeyAttachment());
            }
        } while (clientChannel != null);
    }


    static class KeyAttachment {
        boolean firstRead = true;
        private ByteArrayOutputStream baos;
        private int inputSize;
    }

    private void read(SelectionKey key) throws IOException {
        var attachment = (KeyAttachment) key.attachment();

        if (attachment.firstRead) {
            attachment.inputSize = -1; // ...
            attachment.baos = new ByteArrayOutputStream();
            attachment.firstRead = false;
        }

        var channel = (SocketChannel) key.channel();
        var buffer = ByteBuffer.allocate(200_480_000);
        channel.read(buffer);
        buffer.flip();

        var msg = (EchoRequest) new SerializableMessageCodec().decode(buffer);
        io.println("read: " + msg);
        key.attach(new EchoResponse("async echo: " + msg));
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private void write(SelectionKey key) throws IOException {
        var channel = (SocketChannel) key.channel();
        var msg = (Message) key.attachment();
        var byteStream = new ByteArrayOutputStream();
        new SerializableMessageCodec().encode(msg, byteStream);
        var bytes = byteStream.toByteArray();
        var buffer = ByteBuffer.wrap(bytes);
        channel.write(buffer);

        if (!buffer.hasRemaining()) {
            key.cancel();
            channel.close();
        }
    }
}
