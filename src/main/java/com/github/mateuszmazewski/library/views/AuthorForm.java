package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class AuthorForm extends FormLayout {
    Binder<Author> binder = new BeanValidationBinder<>(Author.class);

    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");

    Button saveButton = new Button("Zapisz");
    Button deleteButton = new Button("Usuń");
    Button cancelButton = new Button("Anuluj");
    private Author author;

    public AuthorForm() {
        addClassName("author-form");
        binder.bindInstanceFields(this);

        add(name, surname, createButtonLayout());
    }

    public void setAuthor(Author author) {
        this.author = author;
        binder.readBean(author);
    }

    private Component createButtonLayout() {
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> new DeleteEvent(this, author));
        cancelButton.addClickListener(e -> new CloseEvent(this));

        saveButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(author);
            fireEvent(new SaveEvent(this, author));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class AuthorFormEvent extends ComponentEvent<AuthorForm> {
        private Author author;

        protected AuthorFormEvent(AuthorForm source, Author author) {
            super(source, false);
            this.author = author;
        }

        public Author getAuthor() {
            return author;
        }
    }

    public static class SaveEvent extends AuthorFormEvent {
        SaveEvent(AuthorForm source, Author author) {
            super(source, author);
        }
    }

    public static class DeleteEvent extends AuthorFormEvent {
        DeleteEvent(AuthorForm source, Author author) {
            super(source, author);
        }

    }

    public static class CloseEvent extends AuthorFormEvent {
        CloseEvent(AuthorForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
