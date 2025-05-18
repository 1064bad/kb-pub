package server.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.controller.Dispatcher;

public class HttpServer implements Runnable {
    private final Logger logger = LoggerFactory.getLogger("Server");

    @Override
    public void run() {
        var parentLoopGroup = new NioEventLoopGroup();
        var childLoopGroup = new NioEventLoopGroup();
        try {
            var boot = new ServerBootstrap()
                    .channel(NioServerSocketChannel.class)
                    .group(parentLoopGroup, childLoopGroup)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new HttpServerCodec())
                                    .addLast(new HttpObjectAggregator(1024 * 1024 * 1024))
                                    .addLast(new ProtocolHandler(new Dispatcher()))
                                    .addLast(new ExceptionHandler());
                        }
                    })
                    .bind(9753).syncUninterruptibly();
            logger.info("server started");
            boot.channel().closeFuture().syncUninterruptibly();
        }
        finally {
            parentLoopGroup.shutdownGracefully();
            childLoopGroup.shutdownGracefully();
        }
    }
}
