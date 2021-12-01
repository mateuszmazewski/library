package com.github.mateuszmazewski.library.views.forms;

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

        add(reader, book, borrowDate, giveBackDate, borrowEmployee, giveBackEmployee, notes, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, borrow)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
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
