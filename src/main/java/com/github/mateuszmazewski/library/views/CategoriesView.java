package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Genre;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.servlet.http.HttpServletRequest;

@PageTitle("Gatunki literackie | Biblioteka")
@Route(value = "categories", layout = MainLayout.class)
public class CategoriesView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Category> grid = new Grid<>(Category.class);
    TextField filterName = new TextField("Gatunek literacki");
    ComboBox<Genre> filterGenre = new ComboBox<>("Rodzaj literacki");
    CategoryForm form;
    private final DataService service;
    private final HttpServletRequest req;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!(req.isUserInRole("ADMIN") || req.isUserInRole("USER"))) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public CategoriesView(DataService service, HttpServletRequest req) {
        this.service = service;
        this.req = req;
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
        if (filterGenre.getValue() != null) {
            grid.setItems(service.findCategories(filterName.getValue(), filterGenre.getValue().getId()));
        } else {
            grid.setItems(service.findCategories(filterName.getValue(), null));
        }
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
        form = new CategoryForm(service.findGenres(null));
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
        filterName.setClearButtonVisible(true);
        filterName.setValueChangeMode(ValueChangeMode.LAZY);
        filterName.addValueChangeListener(e -> updateList());

        filterGenre.setItems(service.findGenres(null));
        filterGenre.setItemLabelGenerator(Genre::getName);
        filterGenre.setClearButtonVisible(true);
        filterGenre.addValueChangeListener(e -> updateList());

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addCategoryButton = new Button("Dodaj gatunek");
        addCategoryButton.addClickListener(e -> addCategory());

        HorizontalLayout toolbar = new HorizontalLayout(filterName, filterGenre, clearFiltersButton, addCategoryButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterName.clear();
        filterGenre.clear();
    }

    private void addCategory() {
        grid.asSingleSelect().clear();
        editCategory(new Category());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Category::getName).setHeader("Gatunek literacki").setSortable(true);
        grid.addColumn(category -> category.getGenre().getName()).setHeader("Rodzaj literacki").setSortable(true);
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
