package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Reader;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Czytelnicy | Biblioteka")
@Route(value = "readers", layout = MainLayout.class)
public class ReadersView extends VerticalLayout {
    Grid<Reader> grid = new Grid<>(Reader.class);
    TextField filterName = new TextField("Imię");
    TextField filterSurname = new TextField("Nazwisko");
    ReaderForm form;
    private final DataService service;

    public ReadersView(DataService service) {
        this.service = service;
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setReader(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findReaders(filterName.getValue(), filterSurname.getValue()));
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
        form = new ReaderForm();
        form.setWidth("25em");

        form.addListener(ReaderForm.SaveEvent.class, this::saveReader);
        form.addListener(ReaderForm.DeleteEvent.class, this::deleteReader);
        form.addListener(ReaderForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveReader(ReaderForm.SaveEvent event) {
        service.saveReader((Reader) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteReader(ReaderForm.DeleteEvent event) {
        service.deleteReader((Reader) event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterName.setClearButtonVisible(true);
        filterName.setValueChangeMode(ValueChangeMode.LAZY);
        filterName.addValueChangeListener(e -> updateList());

        filterSurname.setClearButtonVisible(true);
        filterSurname.setValueChangeMode(ValueChangeMode.LAZY);
        filterSurname.addValueChangeListener(e -> updateList());

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addReaderButton = new Button("Dodaj czytelnika");
        addReaderButton.addClickListener(e -> addReader());

        HorizontalLayout toolbar = new HorizontalLayout(filterName, filterSurname, clearFiltersButton, addReaderButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterName.clear();
        filterSurname.clear();
    }

    private void addReader() {
        grid.asSingleSelect().clear();
        editReader(new Reader());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Reader::getName).setHeader("Imię").setSortable(true);
        grid.addColumn(Reader::getSurname).setHeader("Nazwisko").setSortable(true);
        grid.addColumn(Reader::getEmail).setHeader("E-mail").setSortable(false);
        grid.addColumn(Reader::getPhoneNumber).setHeader("Numer telefonu").setSortable(false);
        grid.addColumn(Reader::getBirthdate).setHeader("Data urodzenia").setSortable(false);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editReader(e.getValue()));
    }

    private void editReader(Reader reader) {
        if (reader == null) {
            closeEditor();
        } else {
            form.setReader(reader);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}