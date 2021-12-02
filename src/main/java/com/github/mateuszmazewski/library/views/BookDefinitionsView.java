package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.BookDefinitionForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import java.util.Calendar;

@PageTitle("Definicje książek | Biblioteka")
@Route(value = "book-definitions", layout = MainLayout.class)
public class BookDefinitionsView extends VerticalLayout implements BeforeEnterObserver {
    Grid<BookDefinition> grid = new Grid<>(BookDefinition.class);
    TextField filterTitle = new TextField("Tytuł");
    ComboBox<Author> filterAuthor = new ComboBox<>("Autor");
    ComboBox<Publisher> filterPublisher = new ComboBox<>("Wydawnictwo");
    IntegerField filterPublicationYear = new IntegerField("Rok wydania");
    ComboBox<Genre> filterGenre = new ComboBox<>("Rodzaj literacki");
    ComboBox<Category> filterCategory = new ComboBox<>("Gatunek literacki");
    TextField filterIsbn = new TextField("ISBN");
    BookDefinitionForm form;
    private final DataService service;
    private final HttpServletRequest req;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!(req.isUserInRole("ADMIN") || req.isUserInRole("USER"))) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public BookDefinitionsView(DataService service, HttpServletRequest req) {
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
        form.setBookDefinition(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        Integer authorId = filterAuthor.getValue() != null ? filterAuthor.getValue().getId() : null;
        Integer publisherId = filterPublisher.getValue() != null ? filterPublisher.getValue().getId() : null;
        Integer publicationYear = filterPublicationYear.getValue();
        Integer genreId = filterGenre.getValue() != null ? filterGenre.getValue().getId() : null;
        Integer categoryId = filterCategory.getValue() != null ? filterCategory.getValue().getId() : null;
        grid.setItems(service.findBookDefinitions(
                filterTitle.getValue(),
                authorId,
                publisherId,
                publicationYear,
                genreId,
                categoryId,
                filterIsbn.getValue()));
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
        form = new BookDefinitionForm(service);
        form.setWidth("25em");

        form.addListener(BookDefinitionForm.SaveEvent.class, this::saveBookDefinition);
        form.addListener(BookDefinitionForm.DeleteEvent.class, this::deleteBookDefinition);
        form.addListener(BookDefinitionForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveBookDefinition(BookDefinitionForm.SaveEvent event) {
        service.saveBookDefinition((BookDefinition) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteBookDefinition(BookDefinitionForm.DeleteEvent event) {
        try {
            service.deleteBookDefinition((BookDefinition) event.getEntity());
            updateList();
            closeEditor();
        } catch (DataIntegrityViolationException e) {
            Notification.show(Messages.INTEGRITY_BOOK_DEFINITION).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private Component getToolbar() {
        filterTitle.setClearButtonVisible(true);
        filterTitle.setValueChangeMode(ValueChangeMode.LAZY);
        filterTitle.addValueChangeListener(e -> updateList());

        filterAuthor.setItems(service.findAllAuthors());
        filterAuthor.setItemLabelGenerator(Author::toString);
        filterAuthor.setClearButtonVisible(true);
        filterAuthor.addValueChangeListener(e -> updateList());

        filterPublisher.setItems(service.findAllPublishers());
        filterPublisher.setItemLabelGenerator(Publisher::getName);
        filterPublisher.setClearButtonVisible(true);
        filterPublisher.addValueChangeListener(e -> updateList());

        filterPublicationYear.addValueChangeListener(e -> updateList());
        filterPublicationYear.setValueChangeMode(ValueChangeMode.LAZY);
        filterPublicationYear.setMin(1000);
        filterPublicationYear.setMax(Calendar.getInstance().get(Calendar.YEAR));

        filterGenre.setItems(service.findAllGenres());
        filterGenre.setItemLabelGenerator(Genre::getName);
        filterGenre.setClearButtonVisible(true);
        filterGenre.addValueChangeListener(e -> updateList());

        Integer genreId = filterGenre.getValue() != null ? filterGenre.getValue().getId() : null;
        filterCategory.setItems(service.findCategories(null, genreId));
        filterCategory.setItemLabelGenerator(Category::getName);
        filterCategory.setClearButtonVisible(true);
        filterCategory.addValueChangeListener(e -> updateList());

        filterIsbn.setClearButtonVisible(true);
        filterIsbn.addValueChangeListener(e -> updateList());
        filterIsbn.setValueChangeMode(ValueChangeMode.LAZY);

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addBookDefinitionButton = new Button("Dodaj definicję książki");
        addBookDefinitionButton.addClickListener(e -> addBookDefinition());

        HorizontalLayout filters = new HorizontalLayout(
                filterTitle,
                filterAuthor,
                filterPublisher,
                filterPublicationYear,
                filterGenre,
                filterCategory,
                filterIsbn);
        HorizontalLayout buttons = new HorizontalLayout(clearFiltersButton, addBookDefinitionButton);
        filters.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        buttons.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return new VerticalLayout(filters, buttons);
    }

    private void clearFilters() {
        filterTitle.clear();
        filterAuthor.clear();
        filterPublisher.clear();
        filterPublicationYear.clear();
        filterGenre.clear();
        filterCategory.clear();
        filterIsbn.clear();
    }

    private void addBookDefinition() {
        form.refreshLists();
        grid.asSingleSelect().clear();
        editBookDefinition(new BookDefinition());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(BookDefinition::getTitle).setHeader("Tytuł").setSortable(true);
        grid.addColumn(bookDefinition -> bookDefinition.getAuthor().toString()).setHeader("Autor").setSortable(true);
        grid.addColumn(bookDefinition -> bookDefinition.getPublisher() != null ? bookDefinition.getPublisher().getName() : "").setHeader("Wydawnictwo").setSortable(true);
        grid.addColumn(bookDefinition -> bookDefinition.getPublicationYear() != null ? bookDefinition.getPublicationYear().intValue() : "").setHeader("Rok wydania").setSortable(true);
        grid.addColumn(bookDefinition -> bookDefinition.getCategory().getGenre().getName()).setHeader("Rodzaj literacki").setSortable(true);
        grid.addColumn(bookDefinition -> bookDefinition.getCategory().getName()).setHeader("Gatunek literacki").setSortable(true);
        grid.addColumn(BookDefinition::getIsbn).setHeader("ISBN").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editBookDefinition(e.getValue()));
    }

    private void editBookDefinition(BookDefinition bookDefinition) {
        if (bookDefinition == null) {
            closeEditor();
        } else {
            form.refreshLists();
            form.setBookDefinition(bookDefinition);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
