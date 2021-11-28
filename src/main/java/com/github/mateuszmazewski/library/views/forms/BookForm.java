package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.Calendar;
import java.util.List;

public class BookForm extends EntityForm {
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    TextField libraryBookId = new TextField("Identyfikator");
    TextField title = new TextField("Tytuł");
    ComboBox<Author> author = new ComboBox<>("Autor");
    ComboBox<Publisher> publisher = new ComboBox<>("Wydawnictwo");
    IntegerField publicationYear = new IntegerField("Rok wydania");
    ComboBox<Category> category = new ComboBox<>("Gatunek literacki");
    TextField isbn = new TextField("ISBN");
    private Book book;

    public BookForm(List<Author> authors, List<Publisher> publishers, List<Category> categories, DataService service) {
        super();
        binder.bindInstanceFields(this);

        libraryBookId.setClearButtonVisible(true);

        author.setItems(authors);
        author.setItemLabelGenerator(Author::toString);
        publisher.setItems(publishers);
        publisher.setItemLabelGenerator(Publisher::getName);

        category.setItems(categories);
        category.setItemLabelGenerator(Category::getName);

        publicationYear.setClearButtonVisible(true);
        publicationYear.setMin(1000);
        publicationYear.setMax(Calendar.getInstance().get(Calendar.YEAR));

        isbn.setClearButtonVisible(true);
        binder.forField(libraryBookId)
                .withValidator(
                        libraryBookId -> libraryBookId != null && !libraryBookId.isEmpty(),
                        Messages.NOT_EMPTY)
                .withValidator(
                        libraryBookId -> service.findBookByLibraryBookId(libraryBookId).isEmpty(),
                        Messages.UNIQUE)
                .bind(Book::getLibraryBookId, Book::setLibraryBookId);
        binder.forField(isbn)
                .withValidator(
                        isbn -> isbn == null || isbn.isEmpty() || isbn.matches("[0-9]+"),
                        "ISBN składa się z samych cyfr")
                .withValidator(
                        isbn -> isbn == null || isbn.isEmpty() || isbn.length() == 10 || isbn.length() == 13,
                        "ISBN ma 13 lub 10 cyfr")
                .bind(Book::getIsbn, Book::setIsbn);

        add(libraryBookId, title, author, publisher, publicationYear, category, isbn, createButtonLayout());
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
