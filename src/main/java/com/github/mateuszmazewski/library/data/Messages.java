package com.github.mateuszmazewski.library.data;

import java.util.Calendar;

public class Messages {
    public static final String NOT_EMPTY = "Nie może być puste";
    public static final String UNIQUE = "Musi być unikalne";

    public static final String BOOK_ALREADY_BORROWED = "Ta książka jest aktualnie wypożyczona";
    public static final String PUBLICATION_YEAR_RANGE = "Musi być między 1000 a " + Calendar.getInstance().get(Calendar.YEAR);
    public static final String NOT_EMPTY_GIVE_BACK_EMPLOYEE = "Nie może być puste, jeśli ustawiono datę zwrotu";
    public static final String CANNOT_DELETE_LOGGED_IN_USER = "Nie można usunąć aktualnie zalogowanego użytkownika";
    public static final String CANNOT_DEACTIVATE_LOGGED_IN_USER = "Nie można zdezaktywować aktualnie zalogowanego użytkownika";

    public static final String INTEGRITY_AUTHOR = "Nie można usunąć autora, który posiada przypisania do definicji książek.";
    public static final String INTEGRITY_BOOK_DEFINITION = "Nie można usunąć definicji książki, która posiada przypisania do książek.";
    public static final String INTEGRITY_BOOK = "Nie można usunąć książki, która posiada przypisania do wypożyczeń.";
    public static final String INTEGRITY_BORROW = "Nie można usunąć aktywnego wypożyczenia.";
    public static final String INTEGRITY_CATEGORY = "Nie można usunąć gatunku, który posiada przypisania do definicji książek.";
    public static final String INTEGRITY_EMPLOYEE = "Nie można usunąć pracownika, który posiada przypisania do użytkowników lub wypożyczeń.";
    public static final String INTEGRITY_PUBLISHER = "Nie można usunąć wydawnictwa, które posiada przypisania do definicji książek.";
    public static final String INTEGRITY_READER = "Nie można usunąć czytelnika, który posiada przypisania do wypożyczeń.";
}
