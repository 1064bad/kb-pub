package library.lv5.impl.infrastructure;

import library.AppException;
import library.lv0.crosscutting.di.Dependency;
import library.lv4.controller.console.IO;

import java.io.PrintStream;
import java.util.Scanner;

@Dependency
public class ConsoleIO implements IO {
    private final Scanner in = new Scanner(System.in);
    private final PrintStream out = System.out;

    @Override
    public String read() {
        return in.nextLine();
    }

    @Override
    public int readInt() {
        try {
            return Integer.parseInt(read());
        }
        catch (NumberFormatException e) {
            throw new AppException("input must be integer number");
        }
    }

    @Override
    public void print(Object o) {
        out.print(o);
    }

    @Override
    public void println(Object o) {
        out.println(o);
    }
}
