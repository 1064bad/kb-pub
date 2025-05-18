package net.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

import static net.util.Logger.log;

public class MessageDecoder extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log(getClass().getSimpleName() + " called");
        var buf = (ByteBuf) msg;
        var array = new byte[buf.readableBytes()];
        buf.getBytes(0, array);
        var line = new String(array, StandardCharsets.UTF_8);
        ctx.fireChannelRead(line);
    }
}
