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
@Route(value = "")
public class AuthorsView extends VerticalLayout {
    Grid<Author> grid = new Grid<>(Author.class);
    TextField filterText = new TextField();
    AuthorForm form;
    private DataService service;

    public AuthorsView(DataService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull(); // this view size == browser window size

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
        service.saveAuthor(event.getAuthor());
        updateList();
        closeEditor();
    }

    private void deleteAuthor(AuthorForm.DeleteEvent event) {
        service.deleteAuthor(event.getAuthor());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Imię lub nazwisko...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // wait a while before querying database
        filterText.addValueChangeListener(e -> updateList());

        Button addAuthorButton = new Button("Dodaj autora");
        addAuthorButton.addClickListener(e -> addAuthor());

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addAuthorButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void addAuthor() {
        grid.asSingleSelect().clear();
        editAuthor(new Author());
    }

    private void configureGrid() {
        grid.addClassName("authors-grid");
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
