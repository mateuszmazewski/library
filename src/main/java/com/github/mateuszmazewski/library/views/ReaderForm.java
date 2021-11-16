package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Reader;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class ReaderForm extends EntityForm {
    Binder<Reader> binder = new BeanValidationBinder<>(Reader.class);

    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");
    EmailField email = new EmailField("E-mail");
    TextField phoneNumber = new TextField("Numer telefonu");
    DatePicker birthdate = new DatePicker("Data urodzenia");
    private Reader reader;

    public ReaderForm() {
        super();
        binder.bindInstanceFields(this);

        add(name, surname, email, phoneNumber, birthdate, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, reader)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setReader(Reader reader) {
        this.reader = reader;
        binder.readBean(reader);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(reader);
            fireEvent(new SaveEvent(this, reader));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
