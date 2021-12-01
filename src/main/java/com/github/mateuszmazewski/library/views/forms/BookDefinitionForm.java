package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.BookDefinition;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Publisher;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.Calendar;

public class BookDefinitionForm extends EntityForm {
    Binder<BookDefinition> binder = new BeanValidationBinder<>(BookDefinition.class);
    private final DataService service;

    TextField title = new TextField("Tytuł");
    ComboBox<Author> author = new ComboBox<>("Autor");
    ComboBox<Publisher> publisher = new ComboBox<>("Wydawnictwo");
    IntegerField publicationYear = new IntegerField("Rok wydania");
    ComboBox<Category> category = new ComboBox<>("Gatunek literacki");
    TextField isbn = new TextField("ISBN");
    private BookDefinition bookDefinition;

    public BookDefinitionForm(DataService service) {
        super();
        this.service = service;
        binder.bindInstanceFields(this);

        binder.forField(publicationYear)
                .withValidator(
                        publicationYear -> publicationYear == null || (publicationYear >= 1000 && publicationYear <= Calendar.getInstance().get(Calendar.YEAR)),
                        Messages.PUBLICATION_YEAR_RANGE)
                .bind(BookDefinition::getPublicationYear, BookDefinition::setPublicationYear);

        author.setItemLabelGenerator(Author::toString);
        publisher.setItemLabelGenerator(Publisher::getName);

        category.setItemLabelGenerator(Category::getName);

        publicationYear.setClearButtonVisible(true);
        publicationYear.setMin(1000);
        publicationYear.setMax(Calendar.getInstance().get(Calendar.YEAR));

        isbn.setClearButtonVisible(true);
        binder.forField(isbn)
                .withValidator(
                        isbn -> isbn == null || isbn.isEmpty() || isbn.matches("[0-9]+"),
                        "ISBN składa się z samych cyfr")
                .withValidator(
                        isbn -> isbn == null || isbn.isEmpty() || isbn.length() == 10 || isbn.length() == 13,
                        "ISBN ma 13 lub 10 cyfr")
                .bind(BookDefinition::getIsbn, BookDefinition::setIsbn);

        add(title, author, publisher, publicationYear, category, isbn, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, bookDefinition)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void refreshLists() {
        author.setItems(service.findAllAuthors());
        publisher.setItems(service.findAllPublishers());
        category.setItems(service.findAllCategories());
    }

    public void setBookDefinition(BookDefinition bookDefinition) {
        this.bookDefinition = bookDefinition;
        binder.readBean(bookDefinition);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(bookDefinition);
            fireEvent(new SaveEvent(this, bookDefinition));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
