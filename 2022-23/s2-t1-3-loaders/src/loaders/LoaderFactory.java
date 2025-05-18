package loaders;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoaderFactory implements LoaderFactoryMBean {
    private ClassLoader loader;

    public LoaderFactory(String dir) throws Exception {
        loader = newLoader(dir);
    }

    public void reload(String dir) throws Exception {
        loader = newLoader(dir);
    }

    private ClassLoader newLoader(String dir) throws Exception {
        var path = Path.of(dir);
        if (!Files.isDirectory(path))
            throw new RuntimeException("loader creation failed: incorrect directory path '" + path + "'");
        System.out.println("info: class loader url=" + path.toUri().toURL());
        return new URLClassLoader(new URL[] { path.toUri().toURL() });
    }

    public ClassLoader getLoader() {
        return loader;
    }
}
