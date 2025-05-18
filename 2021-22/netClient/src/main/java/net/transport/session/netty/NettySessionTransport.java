package net.transport.session.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import net.core.Transport;
import net.settings.Settings;

public class NettySessionTransport implements Transport {
    private String echoMessage;

    @Override
    public void connect() {

    }

    @Override
    public String converse(String message) {
        var loopGroup = new NioEventLoopGroup();
        try {
            new Bootstrap()
                    .channel(NioSocketChannel.class)
                    .group(loopGroup)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    .addLast(new MessageEncoder())

                                    .addLast(new LineBasedFrameDecoder(1024))
                                    .addLast(new StringDecoder())
                                    .addLast(new EchoProtocol(message, echo -> echoMessage = echo));
                        }
                    })
                    .connect(Settings.ADDRESS)
                    .syncUninterruptibly()
                    .channel().closeFuture()
                    .syncUninterruptibly();
            return echoMessage;
        } finally {
            loopGroup.shutdownGracefully();
        }
    }

    @Override
    public void disconnect() {

    }
}
