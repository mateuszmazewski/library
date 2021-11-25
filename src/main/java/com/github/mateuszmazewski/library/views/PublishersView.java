package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Publisher;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.PublisherForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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

@PageTitle("Wydawnictwa | Biblioteka")
@Route(value = "publishers", layout = MainLayout.class)
public class PublishersView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Publisher> grid = new Grid<>(Publisher.class);
    TextField filterName = new TextField("Nazwa");
    PublisherForm form;
    private final DataService service;
    private final HttpServletRequest req;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!(req.isUserInRole("ADMIN") || req.isUserInRole("USER"))) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public PublishersView(DataService service, HttpServletRequest req) {
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
        form.setPublisher(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findPublishers(filterName.getValue()));
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
        form = new PublisherForm();
        form.setWidth("25em");

        form.addListener(PublisherForm.SaveEvent.class, this::savePublisher);
        form.addListener(PublisherForm.DeleteEvent.class, this::deletePublisher);
        form.addListener(PublisherForm.CloseEvent.class, e -> closeEditor());
    }

    private void savePublisher(PublisherForm.SaveEvent event) {
        service.savePublisher((Publisher) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deletePublisher(PublisherForm.DeleteEvent event) {
        service.deletePublisher((Publisher) event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterName.setClearButtonVisible(true);
        filterName.setValueChangeMode(ValueChangeMode.LAZY);
        filterName.addValueChangeListener(e -> updateList());

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addPublisherButton = new Button("Dodaj wydawnictwo");
        addPublisherButton.addClickListener(e -> addPublisher());

        HorizontalLayout toolbar = new HorizontalLayout(filterName, clearFiltersButton, addPublisherButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterName.clear();
    }

    private void addPublisher() {
        grid.asSingleSelect().clear();
        editPublisher(new Publisher());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Publisher::getName).setHeader("Nazwa").setSortable(true);
        grid.addColumn(Publisher::getEmail).setHeader("E-mail").setSortable(false);
        grid.addColumn(Publisher::getPhoneNumber).setHeader("Numer telefonu").setSortable(false);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editPublisher(e.getValue()));
    }

    private void editPublisher(Publisher publisher) {
        if (publisher == null) {
            closeEditor();
        } else {
            form.setPublisher(publisher);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
