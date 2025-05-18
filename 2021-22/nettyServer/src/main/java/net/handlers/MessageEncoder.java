package net.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.nio.charset.StandardCharsets;

import static net.util.Logger.log;

public class MessageEncoder extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        log(getClass().getSimpleName() + " called");
        var line = (String) msg;
        ByteBuf buf = Unpooled.wrappedBuffer(line.getBytes(StandardCharsets.UTF_8));
        ctx.writeAndFlush(buf);
    }
}
