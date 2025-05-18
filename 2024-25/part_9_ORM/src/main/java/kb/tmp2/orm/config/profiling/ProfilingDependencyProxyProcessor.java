package kb.tmp2.orm.config.profiling;

import kb.tmp2.orm.config.di.DependencyProxyProcessor;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ProfilingDependencyProxyProcessor implements DependencyProxyProcessor, ProfilingDependencyProxyProcessorMBean {
    private final Set<Class<?>> candidates = ConcurrentHashMap.newKeySet();
    private final Set<Class<?>> currentProfiling = ConcurrentHashMap.newKeySet();

    public ProfilingDependencyProxyProcessor() throws Exception {
        ManagementFactory.getPlatformMBeanServer()
                .registerMBean(
                        this,
                        new ObjectName("profiling:type=Profile"));
    }

    @Override
    public List<String> getCandidates() {
        return candidates.stream().map(Class::getSimpleName).toList();
    }

    @Override
    public void addProfiling(String simpleClassName) {
        var type = candidates.stream().filter(c -> c.getSimpleName().equals(simpleClassName))
                .findFirst().orElseThrow();
        currentProfiling.add(type);
    }

    @Override
    public void removeProfiling(String simpleClassName) {
        currentProfiling.removeIf(c -> c.getSimpleName().equals(simpleClassName));
    }

    @Override
    public Object process(Class<?> originalType, Object dependency) {
        var annot = originalType.getAnnotation(Profiling.class);
        if (annot == null) {
            return dependency;
        }

        candidates.add(originalType);
        if (annot.enabled()) {
            currentProfiling.add(originalType);
        }

        return Proxy.newProxyInstance(
                originalType.getClassLoader(),
                originalType.getInterfaces(),
                new ProfilingProxy(dependency, originalType)
        );
    }

    private class ProfilingProxy implements InvocationHandler {
        private final Object target;
        private final Class<?> originalType;

        public ProfilingProxy(Object target, Class<?> originalType) {
            this.target = target;
            this.originalType = originalType;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("proxy invoked with method" + method);
            try {
                if (currentProfiling.contains(originalType)) {
                    var start = System.nanoTime();
                    var result = method.invoke(target, args);
                    var stop = System.nanoTime();
                    System.out.printf("method %s elapsed time: %.3f ms\n",
                            method,
                            (stop - start) / 1000000.0);
                    return result;
                } else {
                    return method.invoke(target, args);
                }
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        }
    }
}
