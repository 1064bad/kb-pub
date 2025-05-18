package net.handlers;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import static net.util.Logger.log;

public class EchoProtocol extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log(getClass().getSimpleName() + " called");
        var line = (String) msg;
        log("received from " + ctx.channel() + ": " + line);
        ctx.channel().closeFuture().addListener((ChannelFuture f) -> {
            log(f.channel() + " closed");
        });
        ctx.writeAndFlush("netty server echo: " + line + "\n")
            .addListener((ChannelFuture f) -> {
                log(f.channel() + " close call");
                f.channel().close();
            });

    }
}
