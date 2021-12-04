package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.security.SecurityService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.time.LocalDate;

public class BorrowForm extends EntityForm {
    Binder<Borrow> binder = new BeanValidationBinder<>(Borrow.class);
    private final DataService service;

    ComboBox<Reader> reader = new ComboBox<>("Czytelnik");
    ComboBox<Book> book = new ComboBox<>("Książka");
    DatePicker borrowDate = new DatePicker(LocalDate.now());
    DatePicker giveBackDate = new DatePicker();
    ComboBox<Employee> borrowEmployee = new ComboBox<>("Wypożyczający pracownik");
    ComboBox<Employee> giveBackEmployee = new ComboBox<>("Przyjmujący pracownik");
    TextArea notes = new TextArea("Uwagi");
    private Borrow borrow;

    public BorrowForm(DataService service) {
        super();
        this.service = service;
        binder.bindInstanceFields(this);

        reader.setItemLabelGenerator(Reader::toString);
        book.setItemLabelGenerator(Book::toString);
        borrowEmployee.setItemLabelGenerator(Employee::toString);
        giveBackEmployee.setItemLabelGenerator(Employee::toString);

        giveBackDate.setClearButtonVisible(true);
        giveBackEmployee.setClearButtonVisible(true);

        borrowDate.setLabel("Data wypożyczenia");
        giveBackDate.setLabel("Data zwrotu");
        giveBackDate.addValueChangeListener(e -> checkGiveBackEmployee());

        binder.forField(giveBackEmployee)
                .withValidator(
                        giveBackEmployee -> giveBackDate.getValue() == null && giveBackEmployee == null
                                || (giveBackDate.getValue() != null && giveBackEmployee != null),
                        Messages.NOT_EMPTY_GIVE_BACK_EMPLOYEE)
                .bind(Borrow::getGiveBackEmployee, Borrow::setGiveBackEmployee);

        checkGiveBackEmployee();

        add(reader, book, borrowDate, borrowEmployee, giveBackDate, giveBackEmployee, notes, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, borrow)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    private void checkGiveBackEmployee() {
        if (giveBackDate.getValue() == null) {
            giveBackEmployee.setErrorMessage(null);
            giveBackEmployee.setInvalid(false);
            giveBackEmployee.clear();
            giveBackEmployee.setEnabled(false);
        } else {
            giveBackEmployee.setEnabled(true);
        }
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
        binder.readBean(borrow);
    }

    public void refreshLists() {
        reader.setItems(service.findAllReaders());
        book.setItems(service.findOnlyNotBorrowedBooks());
        setEmployees();
    }

    private void setEmployees() {
        String username = SecurityService.getAuthenticatedUserUsername();
        User user = service.findUserByExactUsername(username);
        Employee employee = user != null ? user.getEmployee() : null;

        if (user != null) {
            if (user.getRoles().contains("ROLE_ADMIN")) { // Admin can set any employee
                borrowEmployee.setItems(service.findAllEmployees());
                giveBackEmployee.setItems(service.findAllEmployees());
            } else if (employee != null) { // Standard user can only set himself
                borrowEmployee.setItems(employee);
                borrowEmployee.setValue(employee);
                giveBackEmployee.setItems(employee);
                giveBackEmployee.setValue(employee);
            }
        }
    }

    private void validateAndSave() {
        try {
            binder.writeBean(borrow);
            fireEvent(new SaveEvent(this, borrow));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
