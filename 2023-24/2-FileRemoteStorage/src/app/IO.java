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

    public void debug(Object o) {
        if (Settings.DEBUG_ON) {
            println(STR."DEBUG \{o}");
        }
    }

    public void debug(String fmt, Object... params) {
        if (Settings.DEBUG_ON) {
            println(fmt.formatted(params));
        }
    }
}
