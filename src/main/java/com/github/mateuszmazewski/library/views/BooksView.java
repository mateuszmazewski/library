package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.BookForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

//TODO: improve filtering by genre/category

@PageTitle("Książki | Biblioteka")
@Route(value = "books", layout = MainLayout.class)
public class BooksView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Book> grid = new Grid<>(Book.class);
    TextField filterLibraryBookId = new TextField("Identyfikator");
    TextField filterTitle = new TextField("Tytuł");
    ComboBox<Author> filterAuthor = new ComboBox<>("Autor");
    ComboBox<Publisher> filterPublisher = new ComboBox<>("Wydawnictwo");
    IntegerField filterPublicationYear = new IntegerField("Rok wydania");
    ComboBox<Genre> filterGenre = new ComboBox<>("Rodzaj literacki");
    ComboBox<Category> filterCategory = new ComboBox<>("Gatunek literacki");
    TextField filterIsbn = new TextField("ISBN");
    BookForm form;
    private final DataService service;
    private final HttpServletRequest req;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!(req.isUserInRole("ADMIN") || req.isUserInRole("USER"))) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public BooksView(DataService service, HttpServletRequest req) {
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
        form.setBook(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        Integer authorId = filterAuthor.getValue() != null ? filterAuthor.getValue().getId() : null;
        Integer publisherId = filterPublisher.getValue() != null ? filterPublisher.getValue().getId() : null;
        Integer publicationYear = filterPublicationYear.getValue();
        Integer genreId = filterGenre.getValue() != null ? filterGenre.getValue().getId() : null;
        Integer categoryId = filterCategory.getValue() != null ? filterCategory.getValue().getId() : null;
        grid.setItems(service.findBooks(
                filterLibraryBookId.getValue(),
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
        Integer genreId = filterGenre.getValue() != null ? filterGenre.getValue().getId() : null;
        form = new BookForm(service.findAuthors(null, null),
                service.findPublishers(null),
                service.findGenres(null),
                service.findCategories(null, genreId));
        form.setWidth("25em");

        form.addListener(BookForm.SaveEvent.class, this::saveBook);
        form.addListener(BookForm.DeleteEvent.class, this::deleteBook);
        form.addListener(BookForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveBook(BookForm.SaveEvent event) {
        service.saveBook((Book) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteBook(BookForm.DeleteEvent event) {
        service.deleteBook((Book) event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterLibraryBookId.setClearButtonVisible(true);
        filterLibraryBookId.addValueChangeListener(e -> updateList());
        filterLibraryBookId.setValueChangeMode(ValueChangeMode.LAZY);

        filterTitle.setClearButtonVisible(true);
        filterTitle.setValueChangeMode(ValueChangeMode.LAZY);
        filterTitle.addValueChangeListener(e -> updateList());

        filterAuthor.setItems(service.findAuthors(null, null));
        filterAuthor.setItemLabelGenerator(Author::toString);
        filterAuthor.setClearButtonVisible(true);
        filterAuthor.addValueChangeListener(e -> updateList());

        filterPublisher.setItems(service.findPublishers(null));
        filterPublisher.setItemLabelGenerator(Publisher::getName);
        filterPublisher.setClearButtonVisible(true);
        filterPublisher.addValueChangeListener(e -> updateList());

        filterPublicationYear.addValueChangeListener(e -> updateList());
        filterPublicationYear.setValueChangeMode(ValueChangeMode.LAZY);
        filterPublicationYear.setMin(1000);
        filterPublicationYear.setMax(Calendar.getInstance().get(Calendar.YEAR));

        filterGenre.setItems(service.findGenres(null));
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

        Button addBookButton = new Button("Dodaj książkę");
        addBookButton.addClickListener(e -> addBook());

        HorizontalLayout filters = new HorizontalLayout(
                filterLibraryBookId,
                filterTitle,
                filterAuthor,
                filterPublisher,
                filterPublicationYear,
                filterGenre,
                filterCategory,
                filterIsbn);
        HorizontalLayout buttons = new HorizontalLayout(clearFiltersButton, addBookButton);
        filters.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        buttons.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return new VerticalLayout(filters, buttons);
    }

    private void clearFilters() {
        filterLibraryBookId.clear();
        filterTitle.clear();
        filterAuthor.clear();
        filterPublisher.clear();
        filterPublicationYear.clear();
        filterGenre.clear();
        filterCategory.clear();
        filterIsbn.clear();
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editBook(new Book());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Book::getLibraryBookId).setHeader("Identyfikator").setSortable(true);
        grid.addColumn(Book::getTitle).setHeader("Tytuł").setSortable(true);
        grid.addColumn(book -> book.getAuthor().toString()).setHeader("Autor").setSortable(true);
        grid.addColumn(book -> book.getPublisher() != null ? book.getPublisher().getName() : "").setHeader("Wydawnictwo").setSortable(true);
        grid.addColumn(book -> book.getPublicationYear() != null ? book.getPublicationYear().intValue() : "").setHeader("Rok wydania").setSortable(true);
        grid.addColumn(book -> book.getCategory().getGenre().getName()).setHeader("Rodzaj literacki").setSortable(true);
        grid.addColumn(book -> book.getCategory().getName()).setHeader("Gatunek literacki").setSortable(true);
        grid.addColumn(Book::getIsbn).setHeader("ISBN").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editBook(e.getValue()));
    }

    private void editBook(Book book) {
        if (book == null) {
            closeEditor();
        } else {
            form.setBook(book);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
