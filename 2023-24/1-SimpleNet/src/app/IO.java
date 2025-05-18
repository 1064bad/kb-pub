package app;

import java.io.PrintWriter;
import java.util.Scanner;

public class IO {
    private final PrintWriter writer = new PrintWriter(System.out);
    private final Scanner scanner = new Scanner(System.in);

    public String readln() {
        return scanner.nextLine();
    }

    public void print(Object o) {
        writer.print(o);
        writer.flush();
    }

    public void println(Object o) {
        writer.println(o);
        writer.flush();
    }

    public void log(Object o) {
        if (Settings.DEBUG_ON) {
            println("DEBUG " + o);
        }
    }

    public void log(String fmt, Object... o) {
        if (Settings.DEBUG_ON) {
            log(fmt.formatted(o));
        }
    }
}
