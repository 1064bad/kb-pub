package server.controller;

import java.util.List;
import java.util.Map;

public class Request {
    private final ContentType responseType;
    private final String path;
    private final Map<String, List<String>> options;

    public Request(ContentType responseType, String path, Map<String, List<String>> options) {
        this.responseType = responseType;
        this.path = path;
        this.options = options;
    }

    public ContentType getResponseType() {
        return responseType;
    }

    public String getPath() {
        return path;
    }

    public Map<String, List<String>> getOptions() {
        return options;
    }

    public long getSingleLongOption(String name, Long defaultValue) {
        var list = options.get(name);
        return list == null ? defaultValue : Long.parseLong(list.get(0));
    }
}
