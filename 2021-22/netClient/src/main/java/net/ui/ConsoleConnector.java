package net.ui;

import java.io.PrintWriter;
import java.util.Scanner;

public class ConsoleConnector {
    private final Scanner scanner = new Scanner(System.in);
    private final PrintWriter writer = new PrintWriter(System.out);

    public String read() {
        return scanner.nextLine();
    }

    public void write(String line) {
        writer.println(line);
    }
}
