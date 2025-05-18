package app.codec;

import app.message.Message;

import java.io.*;
import java.nio.ByteBuffer;

public class SerializableMessageCodec implements MessageCodec {
    @Override
    public void encode(Message msg, OutputStream out) {
        try {
            var byteStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteStream).writeObject(msg);
            var bytes = byteStream.toByteArray();
            new DataOutputStream(out).writeInt(bytes.length);
            out.write(bytes);
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

    @Override
    public Message decode(InputStream in) {
        try {
            var length = new DataInputStream(in).readInt();
            var bytes = new byte[length];
            var readCount = in.read(bytes);
            assert readCount == length;
            return (Message) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }

    @Override
    public Message decode(ByteBuffer buffer) {
        try {
            var length = buffer.getInt();
            var bytes = new byte[length];
            buffer.get(bytes);
            return (Message) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();
        } catch (Exception e) {
            throw new CodecException(e);
        }
    }
}
