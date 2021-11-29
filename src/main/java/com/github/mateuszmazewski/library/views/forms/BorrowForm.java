package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.entity.Book;
import com.github.mateuszmazewski.library.data.entity.Borrow;
import com.github.mateuszmazewski.library.data.entity.Employee;
import com.github.mateuszmazewski.library.data.entity.Reader;
import com.github.mateuszmazewski.library.data.service.DataService;
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

        refreshLists();
        reader.setItemLabelGenerator(Reader::toString);
        book.setItemLabelGenerator(Book::toString);
        borrowEmployee.setItemLabelGenerator(Employee::toString);
        giveBackEmployee.setItemLabelGenerator(Employee::toString);

        //borrowEmployee.setValue(); TODO: actually logged-in employee

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
        reader.setItems(service.findReaders(null, null));
        book.setItems(service.findOnlyNotBorrowedBooks());
        borrowEmployee.setItems(service.findEmployees(null, null, null));
        giveBackEmployee.setItems(service.findEmployees(null, null, null));
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
