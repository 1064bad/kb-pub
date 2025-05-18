package org.example;

public class Main {
    public static void main(String[] args) {
        var db = new DB();
        System.out.println(db.insert(new Book(0, "new book", 2025)));
        System.out.println(db.findAll());
    }
}