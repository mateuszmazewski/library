package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Employee;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class EmployeeForm extends EntityForm {
    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    TextField name = new TextField("Imię");
    TextField surname = new TextField("Nazwisko");
    TextField position = new TextField("Stanowisko");
    EmailField email = new EmailField("E-mail");
    TextField phoneNumber = new TextField("Numer telefonu");
    DatePicker birthdate = new DatePicker("Data urodzenia");
    DatePicker employedSinceDate = new DatePicker("Zatrudniony od");
    DatePicker employedToDate = new DatePicker("Zatrudniony do");
    private Employee employee;

    public EmployeeForm() {
        super();
        binder.bindInstanceFields(this);

        add(name, surname, position, email, phoneNumber, birthdate, employedSinceDate, employedToDate, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, employee)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        binder.readBean(employee);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(employee);
            fireEvent(new SaveEvent(this, employee));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
