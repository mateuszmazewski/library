package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Author;
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

@PageTitle("Autorzy | Biblioteka")
@Route(value = "", layout = MainLayout.class)
public class AuthorsView extends VerticalLayout {
    Grid<Author> grid = new Grid<>(Author.class);
    TextField filterText = new TextField();
    AuthorForm form;
    private final DataService service;

    public AuthorsView(DataService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setAuthor(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findAuthors(filterText.getValue()));
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
        form = new AuthorForm();
        form.setWidth("25em");

        form.addListener(AuthorForm.SaveEvent.class, this::saveAuthor);
        form.addListener(AuthorForm.DeleteEvent.class, this::deleteAuthor);
        form.addListener(AuthorForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveAuthor(AuthorForm.SaveEvent event) {
        service.saveAuthor((Author) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteAuthor(AuthorForm.DeleteEvent event) {
        service.deleteAuthor((Author) event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Imię lub nazwisko...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addAuthorButton = new Button("Dodaj autora");
        addAuthorButton.addClickListener(e -> addAuthor());

        return new HorizontalLayout(filterText, addAuthorButton);
    }

    private void addAuthor() {
        grid.asSingleSelect().clear();
        editAuthor(new Author());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Author::getName).setHeader("Imię").setSortable(true);
        grid.addColumn(Author::getSurname).setHeader("Nazwisko").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editAuthor(e.getValue()));
    }

    private void editAuthor(Author author) {
        if (author == null) {
            closeEditor();
        } else {
            form.setAuthor(author);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
