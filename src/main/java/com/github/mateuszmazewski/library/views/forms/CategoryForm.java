package com.github.mateuszmazewski.library.views.forms;

import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Genre;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class CategoryForm extends EntityForm {
    Binder<Category> binder = new BeanValidationBinder<>(Category.class);
    private final DataService service;

    ComboBox<Genre> genre = new ComboBox<>("Rodzaj literacki");
    TextField name = new TextField("Gatunek literacki");
    private Category category;

    public CategoryForm(DataService service) {
        super();
        this.service = service;
        binder.bindInstanceFields(this);

        genre.setItemLabelGenerator(Genre::getName);

        add(genre, name, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, category)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void refreshLists() {
        genre.setItems(service.findAllGenres());
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
