package kb.tmp2.orm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.armeria.common.*;
import com.linecorp.armeria.server.AbstractHttpService;
import com.linecorp.armeria.server.ServiceRequestContext;
import kb.tmp2.orm.AppException;
import kb.tmp2.orm.controller.message.ErrorResponse;
import lombok.SneakyThrows;

import java.util.concurrent.Callable;

public abstract class BaseHttpService extends AbstractHttpService {
    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public final HttpResponse doGet(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return exceptionHandler(() -> response(200, get(req)));
    }

    protected Object get(HttpRequest req) throws Exception {
        throw new HttpServiceException("method not supported", 405);
    }

    @Override
    public final HttpResponse doPost(ServiceRequestContext ctx, HttpRequest req) throws Exception {
        return exceptionHandler(() -> response(200, post(req)));
    }

    protected Object post(HttpRequest req) throws Exception {
        throw new HttpServiceException("method not supported", 405);
    }

    private HttpResponse exceptionHandler(Callable<HttpResponse> handler) {
        try {
            return handler.call();
        } catch (HttpServiceException e) {
            return response(e.getCode(), new ErrorResponse(e.getMessage()));
        } catch (AppException e) {
            return response(400, new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return response(500, new ErrorResponse(e.getMessage()));
        }
    }

    @SneakyThrows
    private HttpResponse response(int code, Object body) {
        return HttpResponse.of(
                HttpStatus.valueOf(code),
                MediaType.JSON_UTF_8,
                jsonMapper.writeValueAsString(body)
        );
    }

    protected int getIntQueryParam(HttpRequest req, String name) {
        var param = getQueryParam(req, name);
        if (!param.matches("^\\d+$")) {
            throw new HttpServiceException("query param must be non-negative int: " + name, 400);
        }
        return Integer.parseInt(param);
    }


    protected String getQueryParam(HttpRequest req, String name) {
        var params = QueryParams.fromQueryString(req.uri().getQuery());
        if (!params.contains(name)) {
            throw new HttpServiceException("query param expected: " + name, 400);
        }
        return params.get(name);
    }

    protected <T> T getBody(HttpRequest req, Class<T> type) {
        try {
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!");
            var text = req.aggregate().join().contentUtf8();
            System.out.println("??????????????????????");
            return jsonMapper.readValue(text, type);
        } catch (Exception e) {
            throw new HttpServiceException(e.getMessage(), 400);
        }
    }
}
