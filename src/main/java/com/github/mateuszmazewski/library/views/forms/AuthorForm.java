package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class AuthorForm extends EntityForm {
    Binder<Author> binder = new BeanValidationBinder<>(Author.class);

    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");
    private Author author;

    public AuthorForm() {
        super();
        binder.bindInstanceFields(this);

        add(name, surname, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, author)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setAuthor(Author author) {
        this.author = author;
        binder.readBean(author);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(author);
            fireEvent(new SaveEvent(this, author));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
