package library.lv0.crosscutting.di;

public interface DependencyProxyProcessor {
    Object process(Class<?> originalType, Object dependency);
}
