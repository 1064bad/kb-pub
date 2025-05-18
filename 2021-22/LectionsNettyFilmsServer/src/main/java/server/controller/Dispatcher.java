package server.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import server.controller.render.HtmlRender;
import server.net.exception.NetException;
import server.controller.exception.UnsupportedOperationException;
import server.controller.exception.UnsupportedPathException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Dispatcher {
    private final static Path PROJECT_ROOT = Path.of(
            "/home/kb/projs/java/univer/tmp2/LectionsNettyFilmsServer/build/classes/java/main"
    );

    private final Map<Class<?>, Object> controllers = new HashMap<>();
    private final Map<String, Method> methods = new HashMap<>();

    public Response handle(Request request) {
        var path = request.getPath();

        var method = methods.keySet()
                .stream()
                .filter( pattern -> request.getPath().matches(pattern))
                .map(methods::get)
                .findAny()
                .orElse(null);
        if (method == null)
            throw new UnsupportedPathException(path);
        Model model;
        try {
            var controller = controllers.get(method.getDeclaringClass());
            model = (Model) method.invoke(controller, request);
        } catch (Exception e) {
            throw new NetException(e);
        }

        return render(request.getResponseType(), model);
    }

    private Response render(ContentType contentType, Model model) {
        return switch (contentType) {
            case JSON -> renderJson(model);
            case XML -> renderXml(model);
            case HTML -> renderHtml(model);
        };
    }

    private Response renderJson(Model model) {
        throw new UnsupportedOperationException();
    }

    private Response renderXml(Model model) {
        throw new UnsupportedOperationException();
    }

    @SneakyThrows
    private Response renderHtml(Model model) {
        return new HtmlRender().render(model);
    }

    @SneakyThrows
    public Dispatcher() {
        Files.walk(PROJECT_ROOT)
                .filter(path -> path.getFileName().toString().endsWith(".class"))
                .map(Object::toString)
                .map(fname -> fname.substring(PROJECT_ROOT.toString().length() + 1, fname.length() - ".class".length()))
                .map(fname -> fname.replaceAll("/", "."))
                .map(this::loadClass)
                .filter(this::isHtmlController)
                .peek(this::createControllerSingleton)
                .forEach(this::collectPathMethods);
    }

    @SneakyThrows
    private Class<?> loadClass(String classname) {
        return getClass().getClassLoader().loadClass(classname);
    }

    @SneakyThrows
    private boolean isHtmlController(Class<?> clazz) {
        for (var a : clazz.getAnnotations()) {
            if (a.annotationType() == HttpController.class)
                return true;
        }
        return false;
    }

    @SneakyThrows
    private void createControllerSingleton(Class<?> clazz) {
        Constructor<?> noArgConstructor;
        try {
            noArgConstructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("class " + clazz.getCanonicalName() +
                    " has no no-arg constructor");
        }
        Object controller = noArgConstructor.newInstance();
        controllers.put(clazz, controller);
        log.info("controller found: " + clazz);
    }

    @SneakyThrows
    private void collectPathMethods(Class<?> clazz) {
        for (var method : clazz.getMethods()) {
            var pathAnnotation = method.getAnnotation(UrlPath.class);
            if (pathAnnotation != null) {
                if (method.getParameterCount() != 1 ||
                        method.getParameterTypes()[0] != Request.class ||
                        method.getReturnType() != Model.class) {
                    throw new RuntimeException(clazz + " method " + method +
                            " should have one parameter of type " + Request.class +
                            " and return type of " + Model.class);
                }
                methods.put(pathAnnotation.value(), method);
                log.info("for path " + pathAnnotation.value() + " found method: " + method);
            }
        }
    }
}
