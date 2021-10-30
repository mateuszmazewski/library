package com.github.mateuszmazewski.library.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class AuthorForm extends FormLayout {
    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");

    Button saveButton = new Button("Zapisz");
    Button deleteButton = new Button("Usuń");
    Button cancelButton = new Button("Anuluj");

    public AuthorForm() {
        addClassName("author-form");
        add(name, surname, createButtonLayout());
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }
}
