package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Author;
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

    public AuthorsView() {
        addClassName("list-view");
        setSizeFull(); // this view size == browser window size

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
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
    }

    private Component getToolbar() {
        filterText.setPlaceholder("Imię lub nazwisko...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY); // wait a while before querying database

        Button addAuthorButton = new Button("Dodaj autora");
        HorizontalLayout toolbar = new HorizontalLayout(filterText, addAuthorButton);
        toolbar.addClassName("toolbar");

        return toolbar;
    }

    private void configureGrid() {
        grid.addClassName("authors-grid");
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Author::getName).setHeader("Imię").setSortable(true);
        grid.addColumn(Author::getSurname).setHeader("Nazwisko").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

}
