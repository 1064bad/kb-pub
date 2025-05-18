package server.controller;

import io.netty.handler.codec.http.HttpHeaderValues;
import server.net.exception.MalformedRequestException;

public enum ContentType {
    JSON, XML, HTML;

    public static ContentType get(String name) {
        name = name.strip().toLowerCase();
        if (name.contains(HttpHeaderValues.TEXT_HTML.toString())) {
            return HTML;
        }
        else if (name.contains(HttpHeaderValues.APPLICATION_JSON.toString())) {
            return JSON;
        }
        else if (name.contains(HttpHeaderValues.APPLICATION_XML.toString())) {
            return XML;
        }
        else {
            throw new MalformedRequestException("unsupported accept type: " + name);
        }
    }
}
