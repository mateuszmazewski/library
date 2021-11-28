package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.entity.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.Calendar;
import java.util.List;

//TODO: Improve selecting genre/category in form

public class BookForm extends EntityForm {
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    TextField libraryBookId = new TextField("Identyfikator");
    TextField title = new TextField("Tytuł");
    ComboBox<Author> author = new ComboBox<>("Autor");
    ComboBox<Publisher> publisher = new ComboBox<>("Wydawnictwo");
    IntegerField publicationYear = new IntegerField("Rok wydania");
    //ComboBox<Genre> genre = new ComboBox<>("Rodzaj literacki");
    ComboBox<Category> category = new ComboBox<>("Gatunek literacki");
    TextField isbn = new TextField("ISBN");
    private Book book;

    public BookForm(List<Author> authors, List<Publisher> publishers, List<Genre> genres, List<Category> categories) {
        super();
        binder.bindInstanceFields(this);

        libraryBookId.setClearButtonVisible(true);

        author.setItems(authors);
        author.setItemLabelGenerator(Author::toString);
        publisher.setItems(publishers);
        publisher.setItemLabelGenerator(Publisher::getName);
        //genre.setItems(genres);
        //genre.setItemLabelGenerator(Genre::getName);
        category.setItems(categories);
        category.setItemLabelGenerator(Category::getName);

        publicationYear.setClearButtonVisible(true);
        publicationYear.setMin(1000);
        publicationYear.setMax(Calendar.getInstance().get(Calendar.YEAR));

        isbn.setClearButtonVisible(true);
        binder.forField(isbn)
                .withValidator(
                        isbn -> isbn.length() == 10 || isbn.length() == 13,
                        "ISBN ma 13 cyfr (lub 10  - do 31 grudnia 2006)")
                .withValidator(
                        isbn -> isbn.matches("[0-9]+"),
                        "ISBN składa się z samych cyfr")
                .bind(Book::getIsbn, Book::setIsbn);

        //genre.addValueChangeListener(e -> filterCategoriesByGenre(categories));

        add(libraryBookId, title, author, publisher, publicationYear, category, isbn, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, book)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    /*
    private void filterCategoriesByGenre(List<Category> allCategories) {
        Genre selectedGenre = genre.getValue();
        Integer selectedGenreId = selectedGenre != null ? selectedGenre.getId() : null;
        Category selectedCategory = category.getValue();

        if (selectedGenre != null) {
            category.setEnabled(true);
            List<Category> categoriesFilteredByGenre = allCategories.stream()
                    .filter(category -> category.getGenre().getId().equals(selectedGenreId))
                    .collect(Collectors.toList());
            category.setItems(categoriesFilteredByGenre);

            if (selectedCategory.getGenre().getId().equals(selectedGenreId)) {
                category.setValue(selectedCategory);
            }
        } else {
            category.setEnabled(false);
        }
    }
     */

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
