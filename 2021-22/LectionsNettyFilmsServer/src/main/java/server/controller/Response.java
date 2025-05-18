package server.controller;

import io.netty.buffer.ByteBuf;

public class Response {
    private final ContentType contentType;
    private final ByteBuf content;

    public Response(ContentType contentType, ByteBuf content) {
        this.contentType = contentType;
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public ByteBuf getContent() {
        return content;
    }
}
