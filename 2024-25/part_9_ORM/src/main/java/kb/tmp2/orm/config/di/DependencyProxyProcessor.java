package kb.tmp2.orm.config.di;

public interface DependencyProxyProcessor {
    Object process(Class<?> originalType, Object dependency);
}
