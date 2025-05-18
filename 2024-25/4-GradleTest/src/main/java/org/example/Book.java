package org.example;

import lombok.*;

@Data
@Builder(toBuilder = true)
public class Book {
    private final long id;
    private final String title;
    private final int year;
}