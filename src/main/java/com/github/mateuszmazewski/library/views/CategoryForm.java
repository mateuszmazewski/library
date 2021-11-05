package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class CategoryForm extends EntityForm {
    Binder<Category> binder = new BeanValidationBinder<>(Category.class);

    TextField name = new TextField("Nazwa");
    private Category category;

    public CategoryForm() {
        super();
        binder.bindInstanceFields(this);

        add(name, createButtonLayout());
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
