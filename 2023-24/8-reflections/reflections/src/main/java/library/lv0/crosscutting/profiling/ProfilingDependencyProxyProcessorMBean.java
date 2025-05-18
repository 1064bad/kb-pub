package library.lv0.crosscutting.profiling;

import java.util.List;

public interface ProfilingDependencyProxyProcessorMBean {
    List<String> getCandidates();
    void addProfiling(String simpleClassName);
    void removeProfiling(String simpleClassName);
}
