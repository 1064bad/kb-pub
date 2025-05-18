package server.net;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ExceptionHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger("NetException");

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error(cause.getMessage());

        var out = new ByteArrayOutputStream();
        cause.printStackTrace(new PrintStream(out, true));
        var httpResponse = new DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                HttpResponseStatus.PAYMENT_REQUIRED,
                Unpooled.wrappedBuffer(out.toByteArray())
        );

        httpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN);

        ctx.writeAndFlush(httpResponse)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
