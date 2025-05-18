package loaders;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

public class Main {
    private static LoaderFactory loaderFactory;

    public static void main(String[] args) throws Exception {
        loaderFactory = new LoaderFactory("/home/kb/test/animals/v1");

        ManagementFactory.getPlatformMBeanServer().registerMBean(
                loaderFactory,
                new ObjectName("LoaderFactoryAgent:name=factory")
        );

//        exec();
        execInSeparatedThread();
    }

    private static void exec() throws Exception {
        while (true) {
            System.out.println("[" + LocalDateTime.now() + "] =>");

            var loader = loaderFactory.getLoader();
            var clazz = loader.loadClass("animals.Main");
            clazz.getMethod("main", String[].class)
                    .invoke(null, (Object) new String[]{});

            Thread.sleep(2000);
        }
    }

    private static void execInSeparatedThread() throws Exception {
        while (true) {
            var thread = new Thread(() -> {
                System.out.println("[" + LocalDateTime.now() + "] =>");
                try {
                    var clazz = Thread.currentThread().getContextClassLoader()
                            .loadClass("animals.Main");
                    clazz.getMethod("main", String[].class).invoke(null, (Object) new String[]{});
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.setContextClassLoader(loaderFactory.newLoader("/home/kb/test/animals/v1"));
            thread.start();

            Thread.sleep(10);
        }
    }
}
