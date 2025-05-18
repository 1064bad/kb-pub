package app.server;

import app.message.EchoRequest;
import app.message.EchoResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EchoProtocolHandler extends ChannelInboundHandlerAdapter {
    private ByteArrayOutputStream receiveStream = null;
    private int bytesRemaining = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("invoked channelRead");
        var buf = (ByteBuf) msg;
        try {
            if (bytesRemaining == 0 && receiveStream == null) {
                bytesRemaining = buf.readInt();
                receiveStream = new ByteArrayOutputStream();
            }

            buf.forEachByte(b -> {
                receiveStream.write(b);
                bytesRemaining--;
                return true;
            });
        } finally {
            buf.release();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("invoked channelReadComplete");
        if (bytesRemaining == 0 && receiveStream != null) {
            var bytes = receiveStream.toByteArray();
            receiveStream = null;
            var request = (EchoRequest) new ObjectInputStream(new ByteArrayInputStream(bytes)).readObject();

            var outStream = new ByteArrayOutputStream();
            new ObjectOutputStream(outStream).writeObject(new EchoResponse(request.getMessage()));
            var outBytes = outStream.toByteArray();

            ctx.write(Unpooled.copyInt(outBytes.length));
            ctx.writeAndFlush(Unpooled.wrappedBuffer(outBytes))
                    .addListener(ChannelFutureListener.CLOSE);
        }
    }
}
