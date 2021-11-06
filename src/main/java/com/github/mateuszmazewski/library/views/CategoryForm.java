package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Genre;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.util.List;

public class CategoryForm extends EntityForm {
    Binder<Category> binder = new BeanValidationBinder<>(Category.class);

    ComboBox<Genre> genre = new ComboBox<>("Rodzaj literacki");
    TextField name = new TextField("Gatunek literacki");
    private Category category;

    public CategoryForm(List<Genre> genres) {
        super();
        binder.bindInstanceFields(this);

        genre.setItems(genres);
        genre.setItemLabelGenerator(Genre::getName);

        add(genre, name, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, category)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setCategory(Category category) {
        this.category = category;
        binder.readBean(category);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(category);
            fireEvent(new SaveEvent(this, category));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
