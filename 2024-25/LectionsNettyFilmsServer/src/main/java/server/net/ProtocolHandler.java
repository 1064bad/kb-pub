package server.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import lombok.extern.slf4j.Slf4j;
import server.controller.ContentType;
import server.controller.Dispatcher;
import server.controller.Request;
import server.controller.Response;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProtocolHandler extends SimpleChannelInboundHandler<HttpObject> {
    private final Dispatcher dispatcher;

    public ProtocolHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            try {
                var httpRequest = (FullHttpRequest) msg;
                var request = extractRequest(httpRequest);

                var response = getResponse(request);

                var httpResponse = new DefaultFullHttpResponse(
                        httpRequest.protocolVersion(),
                        HttpResponseStatus.OK,
                        response.getContent()
                );

                httpResponse.headers()
                        .addInt(HttpHeaderNames.CONTENT_LENGTH, response.getContent().readableBytes())
                        .add(HttpHeaderNames.CONTENT_TYPE, response.getContentType());

                ctx.writeAndFlush(httpResponse);
            }
            catch (Throwable t) {
                ctx.fireExceptionCaught(t);
            }
        } else {
            ctx.fireExceptionCaught(new Exception("unexpected " + msg));
        }
    }

    private Request extractRequest(FullHttpRequest httpRequest) {
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());

        var parameters = extractParameters(httpRequest);
        log.info(parameters.toString());

        var accept = httpRequest.headers().get(HttpHeaderNames.ACCEPT,
                HttpHeaderValues.TEXT_HTML.toString());
        var type = ContentType.get(accept);

        return new Request(type, decoder.path(), parameters);
    }

    private Map<String, List<String>> extractParameters(FullHttpRequest httpRequest) {
        QueryStringDecoder decoder = new QueryStringDecoder(httpRequest.uri());
        var postDecoder = new QueryStringDecoder(
                httpRequest.content().toString(StandardCharsets.UTF_8), false );

        var parameters = new HashMap<String, List<String>>();
        decoder.parameters().forEach( (k, v) ->
                parameters.computeIfAbsent(k, x -> new ArrayList<>()).addAll(v));
        postDecoder.parameters().forEach( (k, v) ->
                parameters.computeIfAbsent(k, x -> new ArrayList<>()).addAll(v));

        return parameters;
    }

    private Response getResponse(Request request) {
        return dispatcher.handle(request);
    }
}
