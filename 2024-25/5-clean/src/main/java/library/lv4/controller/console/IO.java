package library.lv4.controller.console;

public interface IO {
    String read();
    int readInt();

    void print(Object o);

    void println(Object o);
}
