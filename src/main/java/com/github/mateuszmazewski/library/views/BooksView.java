package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Book;
import com.github.mateuszmazewski.library.data.entity.BookDefinition;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.BookForm;
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

@PageTitle("Książki | Biblioteka")
@Route(value = "books", layout = MainLayout.class)
public class BooksView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Book> grid = new Grid<>(Book.class);
    TextField filterBookCode = new TextField("Kod książki");
    ComboBox<BookDefinition> filterBookDefinition = new ComboBox<>("Definicja książki");
    ComboBox<String> filterBorrowStatus = new ComboBox<>("Status wypoż.");
    BookForm form;
    private final DataService service;
    private final HttpServletRequest req;
    public final static String ALL_BOOKS = "Wszystkie";
    public final static String BORROWED_BOOKS = "Wypożyczone";
    public final static String NOT_BORROWED_BOOKS = "Niewypożyczone";

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
        Boolean searchIsBorrowed = null;

        if (filterBorrowStatus.getValue() != null) {
            switch (filterBorrowStatus.getValue()) {
                case BORROWED_BOOKS: {
                    searchIsBorrowed = true;
                    break;
                }
                case NOT_BORROWED_BOOKS: {
                    searchIsBorrowed = false;
                    break;
                }
            }
        }

        Integer filterBookDefinitionId = filterBookDefinition.getValue() != null ? filterBookDefinition.getValue().getId() : null;
        grid.setItems(service.findBooks(searchIsBorrowed, filterBookCode.getValue(), filterBookDefinitionId));
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
        form = new BookForm(service.findAllBookDefinitions(), service);
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
        filterBorrowStatus.setItems(ALL_BOOKS, BORROWED_BOOKS, NOT_BORROWED_BOOKS);
        filterBorrowStatus.setValue(ALL_BOOKS);
        filterBorrowStatus.addValueChangeListener(e -> updateList());

        filterBookCode.setClearButtonVisible(true);
        filterBookCode.setValueChangeMode(ValueChangeMode.LAZY);
        filterBookCode.addValueChangeListener(e -> updateList());

        filterBookDefinition.setItems(service.findAllBookDefinitions());
        filterBookDefinition.addValueChangeListener(e -> updateList());
        filterBookDefinition.setClearButtonVisible(true);

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addBookButton = new Button("Dodaj książkę");
        addBookButton.addClickListener(e -> addBook());

        HorizontalLayout toolbar = new HorizontalLayout(filterBorrowStatus, filterBookCode, filterBookDefinition, clearFiltersButton, addBookButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterBorrowStatus.setValue(ALL_BOOKS);
        filterBookCode.clear();
        filterBookDefinition.clear();
    }

    private void addBook() {
        grid.asSingleSelect().clear();
        editBook(new Book());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Book::getBookCode).setHeader("Kod książki").setSortable(true);
        grid.addColumn(Book::getBookDefinition).setHeader("Definicja książki").setSortable(true);
        grid.addColumn(book -> book.getBorrow() != null ? book.getBorrow().getId() : "").setHeader("ID wypoż.").setSortable(true);
        grid.addColumn(Book::getNotes).setHeader("Uwagi").setSortable(false);
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
