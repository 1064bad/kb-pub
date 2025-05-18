package net.transport.session.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.function.Consumer;

public class EchoProtocol extends ChannelInboundHandlerAdapter {
    private final String message;
    private final Consumer<String> callback;

    public EchoProtocol(String message, Consumer<String> callback) {
        this.message = message;
        this.callback = callback;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(message + "\n");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        var line = (String) msg;
        callback.accept(line);
        ctx.channel().close();
    }
}
