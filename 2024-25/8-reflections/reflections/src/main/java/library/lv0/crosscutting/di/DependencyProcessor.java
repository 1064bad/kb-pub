package library.lv0.crosscutting.di;

import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static java.util.function.Predicate.not;

public class DependencyProcessor {
    private final Map<Class<?>, Object> dependencies = new HashMap<>();
    private List<? extends DependencyProxyProcessor> proxyProcessors;
    private final Reflections reflect;

    public DependencyProcessor(String packageName) {
        this.reflect = new Reflections(packageName);
    }

    public <T> T getDependency(Class<T> type) {
        if (proxyProcessors == null) {
            collectProxyProcessors();
        }
        var dep = dependencies.get(type);
        if (dep == null) {
            createDependency(type);
            dep = dependencies.get(type);
        }
        return type.cast(dep);
    }

    private void collectProxyProcessors() {
        proxyProcessors = reflect.getSubTypesOf(DependencyProxyProcessor.class).stream()
                .map(type -> {
                    try {
                        return type.getConstructor().newInstance();
                    } catch (NoSuchMethodException e) {
                        throw new DIException("no default constructor for type " + type);
                    } catch (Exception e) {
                        throw new DIException("constructor failed for type " + type);
                    }
                })
                .toList();
    }

    private void createDependency(Class<?> type) {
        var concreteType = getConcreteClass(type);
        var constructor = getConstructor(concreteType);
        var dependency = createInstance(constructor);
        dependency = processProxyProcessors(concreteType, dependency);
        putInDependencies(concreteType, dependency);
    }

    private Class<?> getConcreteClass(Class<?> type) {
        if (type.isInterface()) {
            return reflect.getTypesAnnotatedWith(Dependency.class).stream()
                    .filter(not(Class::isInterface))
                    .filter(c -> Arrays.asList(c.getInterfaces()).contains(type))
                    .max(Comparator.comparing(c -> c.getAnnotation(Dependency.class).primary()))
                    .orElseThrow(() -> new DIException("no concrete class for type " + type));
        } else {
            return type;
        }
    }

    private <T> Constructor<T> getConstructor(Class<T> concreteType) {
        Constructor<?>[] constructors = concreteType.getConstructors();
        if (constructors.length == 0) {
            throw new DIException("no constructor for type " + concreteType);
        }
        return (Constructor<T>) constructors[0];
    }

    private <T> T createInstance(Constructor<T> constructor) {
        try {
            if (constructor.getParameterCount() == 0) {
                return constructor.newInstance();
            } else {
                var params = new Object[constructor.getParameterCount()];
                for (int i = 0; i < params.length; i++) {
                    var paramType = constructor.getParameterTypes()[i];
                    var param = getDependency(paramType);
                    params[i] = param;
                }
                return constructor.newInstance(params);
            }
        } catch (Exception e) {
            throw new DIException(e.getMessage());
        }
    }

    private Object processProxyProcessors(Class<?> originalType, Object dependency) {
        for (var pp : proxyProcessors) {
            dependency = pp.process(originalType, dependency);
        }
        return dependency;
    }

    private void putInDependencies(Class<?> originalType, Object dependency) {
        dependencies.put(originalType, dependency);
        for (var i : originalType.getInterfaces()) {
            dependencies.put(i, dependency);
        }
    }
}
