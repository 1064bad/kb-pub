package app.codec;

import app.message.Message;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public interface MessageCodec {
    void encode(Message msg, OutputStream out);

    Message decode(InputStream in);
    Message decode(ByteBuffer buffer);
}
