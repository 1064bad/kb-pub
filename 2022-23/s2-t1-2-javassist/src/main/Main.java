package main;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Dolly!");
        printDeclaredMethods();

    }

    private static void printDeclaredMethods() {
        System.out.println("================");
        System.out.println("Declared methods: ");
        for (var m : Main.class.getDeclaredMethods()) {
            System.out.println(m);
        }
        System.out.println("================");
    }
}
