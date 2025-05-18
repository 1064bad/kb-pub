package loaders;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoaderFactory implements LoaderFactoryMBean {
    private ClassLoader loader;
    private String dir;

    public LoaderFactory(String dir) throws Exception {
        this.dir = dir;
        loader = newLoader(dir);
    }

    public void reload(String dir) throws Exception {
        this.dir = dir;
        loader = newLoader(dir);
    }

    public ClassLoader newLoader(String dir) throws Exception {
        var path = Path.of(dir);
        if (!Files.isDirectory(path))
            throw new RuntimeException("loader creation failed: incorrect directory path '" + path + "'");
        System.out.println("info: class loader url=" + path.toUri().toURL());
        return new URLClassLoader(new URL[] { path.toUri().toURL() });
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public String getDir() {
        return dir;
    }
}
