package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.Reader;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.ReaderForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;

@PageTitle("Czytelnicy | Biblioteka")
@Route(value = "readers", layout = MainLayout.class)
public class ReadersView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Reader> grid = new Grid<>(Reader.class);
    IntegerField filterId = new IntegerField("ID czytelnika");
    TextField filterName = new TextField("Imię");
    TextField filterSurname = new TextField("Nazwisko");
    ReaderForm form;
    private final DataService service;
    private final HttpServletRequest req;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!(req.isUserInRole("ADMIN") || req.isUserInRole("USER"))) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public ReadersView(DataService service, HttpServletRequest req) {
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
        form.setReader(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findReaders(filterId.getValue(), filterName.getValue(), filterSurname.getValue()));
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
        try {
            service.deleteReader((Reader) event.getEntity());
            updateList();
            closeEditor();
        } catch (DataIntegrityViolationException e) {
            Notification.show(Messages.INTEGRITY_READER).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private Component getToolbar() {
        filterId.addValueChangeListener(e -> updateList());
        filterId.setValueChangeMode(ValueChangeMode.LAZY);
        filterId.setClearButtonVisible(true);

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

        HorizontalLayout toolbar = new HorizontalLayout(filterId, filterName, filterSurname, clearFiltersButton, addReaderButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterId.clear();
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
        grid.addColumn(Reader::getId).setHeader("ID").setSortable(true);
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
