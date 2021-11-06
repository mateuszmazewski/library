package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Publisher;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class PublisherForm extends EntityForm {
    Binder<Publisher> binder = new BeanValidationBinder<>(Publisher.class);

    TextField name = new TextField("Nazwa");
    EmailField email = new EmailField("E-mail");
    TextField phoneNumber = new TextField("Numer telefonu");
    private Publisher publisher;

    public PublisherForm() {
        super();
        binder.bindInstanceFields(this);

        add(name, email, phoneNumber, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, publisher)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
        binder.readBean(publisher);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(publisher);
            fireEvent(new SaveEvent(this, publisher));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}