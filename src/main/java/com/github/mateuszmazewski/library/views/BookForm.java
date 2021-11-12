package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.*;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.List;

//TODO: Improve selecting genre/category in form

public class BookForm extends EntityForm {
    Binder<Book> binder = new BeanValidationBinder<>(Book.class);

    TextField title = new TextField("Tytuł");
    ComboBox<Author> author = new ComboBox<>("Autor");
    ComboBox<Publisher> publisher = new ComboBox<>("Wydawnictwo");
    //ComboBox<Genre> genre = new ComboBox<>("Rodzaj literacki");
    ComboBox<Category> category = new ComboBox<>("Gatunek literacki");
    private Book book;

    public BookForm(List<Author> authors, List<Publisher> publishers, List<Genre> genres, List<Category> categories) {
        super();
        binder.bindInstanceFields(this);

        author.setItems(authors);
        author.setItemLabelGenerator(Author::toString);
        publisher.setItems(publishers);
        publisher.setItemLabelGenerator(Publisher::getName);
        //genre.setItems(genres);
        //genre.setItemLabelGenerator(Genre::getName);
        category.setItems(categories);
        category.setItemLabelGenerator(Category::getName);

        //genre.addValueChangeListener(e -> filterCategoriesByGenre(categories));

        add(title, author, publisher, category, createButtonLayout());
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
