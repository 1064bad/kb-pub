package server.controller.render;

import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import server.controller.ContentType;
import server.controller.Response;
import server.controller.Model;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class HtmlRender implements Render {
    @Override
    @SneakyThrows
    public Response render(Model model) {
        var template = getTemplate(model.getTemplate());

        var result = process(template, model.getVariables());

        return new Response(ContentType.HTML,
                Unpooled.wrappedBuffer(result.getBytes()));
    }

    @SneakyThrows
    private String getTemplate(String templateName) {
        var htmlStream = getClass().getClassLoader()
                .getResourceAsStream(templateName + ".html");
        if (htmlStream == null)
            throw new RenderException("template " + templateName + " not found");
        return new String(htmlStream.readAllBytes());
    }

    private String process(String content, Map<String, Object> vars) {
        content = processEach(content,vars);
        return processVar(content, vars);
    }

    String processEach(String template, Map<String, Object> vars) {
        var patternEach = Pattern.compile("##each:(?<collection>[a-z.]+)(?<content>.*?)###",
                Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        var builder = new StringBuilder();
        var lastMatchEnd = new int[1];
        patternEach.matcher(template).results()
                .forEach(matchResult -> {
                    builder.append(template, lastMatchEnd[0], matchResult.start());
                    lastMatchEnd[0] = matchResult.end();
                    var collection = matchResult.group(1);
                    var content = matchResult.group(2);
                    if (vars.get(collection) != null) {
                        for (var obj : (Iterable<?>) vars.get(collection)) {
                            builder.append( processIter(content, obj) );
                        }
                    }
                });
        builder.append(template.substring(lastMatchEnd[0]));
        return builder.toString();
    }

    String processIter(String content, Object obj) {
        var patternVar = Pattern.compile("##iter(?:\\.(?<path>[a-z.]+))?",
                Pattern.CASE_INSENSITIVE);
        var builder = new StringBuilder();
        int [] lastMatchEnd = new int[1];
        patternVar.matcher(content).results()
                .forEach(matchResult -> {
                    builder.append(content, lastMatchEnd[0], matchResult.start());
                    lastMatchEnd[0] = matchResult.end();
                    var path = matchResult.group(1) != null ? matchResult.group(1) : "";
                    builder.append(getValueFromObject(obj, path));
                });
        builder.append(content.substring(lastMatchEnd[0]));
        return builder.toString();
    }

    String processVar(String content, Map<String, Object> vars) {
        var patternVar = Pattern.compile("##var:(?<var>[a-z.]+)",
                Pattern.CASE_INSENSITIVE);
        var builder = new StringBuilder();
        var lastMatchEnd = new int[1];
        patternVar.matcher(content).results()
                .forEach(matchResult -> {
                    builder.append(content, lastMatchEnd[0], matchResult.start());
                    lastMatchEnd[0] = matchResult.end();
                    var path = matchResult.group(1);
                    builder.append(getValueFromModel(vars, path));
                });
        builder.append(content.substring(lastMatchEnd[0]));

        return builder.toString();
    }

    String getValueFromModel(Map<String, Object> vars, String path) {
        assert ! "".equals(path);
        try {
            var pieces = path.split("\\.");
            var varName = pieces[0];
            pieces = Arrays.copyOfRange(pieces, 1, pieces.length);
            var obj = vars.get(varName);
            if (obj == null)
                throw new RenderException("no variable in model: " + varName);
            return getValueFromObject(obj, pieces);
        }
        catch (Throwable t) {
            throw new RenderException("cannot find value for path " + path, t);
        }
    }

    @SneakyThrows
    String getValueFromObject(Object obj, String path) {
        return getValueFromObject(obj, path.split("\\."));
    }

    @SneakyThrows
    String getValueFromObject(Object obj, String[] pieces) {
        assert obj != null;
        if (pieces.length > 0) {
            for (var part : pieces) {
                String getterName;
                if (part.length() == 0)
                    throw new RenderException("empty path part found");
                else {
                    getterName = "get" + part.substring(0, 1).toUpperCase() +
                            part.substring(1);
                }
                obj = obj.getClass().getMethod(getterName).invoke(obj);
            }
        }
        return StringEscapeUtils.escapeHtml4(obj.toString());
    }
}
