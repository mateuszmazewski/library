package com.github.mateuszmazewski.library.data;

import java.util.Calendar;

public class Messages {
    public static final String NOT_EMPTY = "Nie może być puste";
    public static final String UNIQUE = "Musi być unikalne";

    public static final String BOOK_ALREADY_BORROWED = "Ta książka jest aktualnie wypożyczona";
    public static final String PUBLICATION_YEAR_RANGE = "Musi być między 1000 a " + Calendar.getInstance().get(Calendar.YEAR);
}
