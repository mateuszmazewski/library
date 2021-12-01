package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.Calendar;
import java.util.List;

public class BookDefinitionForm extends EntityForm {
    Binder<BookDefinition> binder = new BeanValidationBinder<>(BookDefinition.class);

    TextField title = new TextField("Tytuł");
    ComboBox<Author> author = new ComboBox<>("Autor");
    ComboBox<Publisher> publisher = new ComboBox<>("Wydawnictwo");
    IntegerField publicationYear = new IntegerField("Rok wydania");
    ComboBox<Category> category = new ComboBox<>("Gatunek literacki");
    TextField isbn = new TextField("ISBN");
    private BookDefinition bookDefinition;

    public BookDefinitionForm(List<Author> authors, List<Publisher> publishers, List<Category> categories) {
        super();
        binder.bindInstanceFields(this);

        binder.forField(publicationYear)
                .withValidator(
                        publicationYear -> publicationYear >= 1000 && publicationYear <= Calendar.getInstance().get(Calendar.YEAR),
                        Messages.PUBLICATION_YEAR_RANGE)
                .bind(BookDefinition::getPublicationYear, BookDefinition::setPublicationYear);

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
