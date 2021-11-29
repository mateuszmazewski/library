package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.Book;
import com.github.mateuszmazewski.library.data.entity.BookDefinition;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.List;

public class BookForm extends EntityForm {
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    TextField bookCode = new TextField("Kod książki");
    ComboBox<BookDefinition> bookDefinition = new ComboBox<>("Definicja książki");
    TextArea notes = new TextArea("Uwagi");
    private Book book;

    public BookForm(List<BookDefinition> bookDefinitions, DataService service) {
        super();
        binder.bindInstanceFields(this);

        binder.forField(bookCode)
                .withValidator(
                        bookCode -> bookCode != null && !bookCode.isEmpty(),
                        Messages.NOT_EMPTY)
                .withValidator(
                        bookCode -> bookCode != null && service.findBookByBookCode(bookCode).isEmpty(),
                        Messages.UNIQUE)
                .bind(Book::getBookCode, Book::setBookCode);

        bookDefinition.setItems(bookDefinitions);
        bookDefinition.setItemLabelGenerator(BookDefinition::toString);

        add(bookCode, bookDefinition, notes, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, book)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setBook(Book book) {
        this.book = book;
        binder.readBean(book);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(book);
            fireEvent(new SaveEvent(this, book));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
