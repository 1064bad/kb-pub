package net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import net.handlers.EchoProtocol;
import net.handlers.MessageDecoder;
import net.handlers.MessageEncoder;
import net.settings.Settings;

import static net.util.Logger.log;

public class App {
    public static void main(String[] args) {
        log("server starting...");

        var parentGroup = new NioEventLoopGroup();
        var childGroup = new NioEventLoopGroup();
        try {
            var boot = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(parentGroup, childGroup)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast("1", new MessageEncoder())
                                    //.addLast("2", new MessageDecoder())
                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast("3", new EchoProtocol());
                        }
                    })
                    .bind(Settings.ADDRESS)
                    .syncUninterruptibly();
            log("server started!");
            boot.channel().closeFuture().syncUninterruptibly();
        }
        finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
