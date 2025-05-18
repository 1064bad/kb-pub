package main;

import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws Exception {
        var settings = new Settings();

        ManagementFactory.getPlatformMBeanServer().registerMBean(
                settings,
                new ObjectName("SettingsAgent:name=params")
        );

        while (true) {
            System.out.println("[" + LocalDateTime.now() + "] Hello, " + settings.getName() + "!");
            Thread.sleep(2000);
        }
    }
}
