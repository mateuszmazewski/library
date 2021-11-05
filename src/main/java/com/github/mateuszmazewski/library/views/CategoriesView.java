package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Kategorie | Biblioteka")
@Route(value = "categories", layout = MainLayout.class)
public class CategoriesView extends VerticalLayout {
    Grid<Category> grid = new Grid<>(Category.class);
    TextField filterText = new TextField();
    CategoryForm form;
    private final DataService service;

    public CategoriesView(DataService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setCategory(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findCategories(filterText.getValue()));
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        form = new CategoryForm();
        form.setWidth("25em");

        form.addListener(AuthorForm.SaveEvent.class, this::saveCategory);
        form.addListener(AuthorForm.DeleteEvent.class, this::deleteCategory);
        form.addListener(AuthorForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveCategory(CategoryForm.SaveEvent event) {
        service.saveCategory((Category) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteCategory(CategoryForm.DeleteEvent event) {
        service.deleteCategory((Category) event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Nazwa kategorii ...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addCategoryButton = new Button("Dodaj kategorię");
        addCategoryButton.addClickListener(e -> addCategory());

        return new HorizontalLayout(filterText, addCategoryButton);
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Category());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Category::getName).setHeader("Nazwa").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editCategory(e.getValue()));
    }

    private void editCategory(Category category) {
        if (category == null) {
            closeEditor();
        } else {
            form.setCategory(category);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
