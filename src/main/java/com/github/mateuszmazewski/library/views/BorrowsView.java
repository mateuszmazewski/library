package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.Messages;
import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.BorrowForm;
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
import java.util.List;

@PageTitle("Wypożyczenia | Biblioteka")
@Route(value = "borrows", layout = MainLayout.class)
public class BorrowsView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Borrow> grid = new Grid<>(Borrow.class);
    ComboBox<String> filterBorrowStatus = new ComboBox<>("Status wypoż.");
    IntegerField filterId = new IntegerField("ID wypożyczenia");
    ComboBox<Reader> filterReader = new ComboBox<>("Czytelnik");
    TextField filterBookCode = new TextField("Kod książki");
    ComboBox<BookDefinition> filterBookDefinition = new ComboBox<>("Książka");
    ComboBox<Employee> filterBorrowEmployee = new ComboBox<>("Pracownik wypożyczający");
    ComboBox<Employee> filterGiveBackEmployee = new ComboBox<>("Pracownik przyjmujący");
    BorrowForm form;
    private final DataService service;
    private final HttpServletRequest req;
    public final static String ALL_BORROWS = "Wszystkie";
    public final static String ACTIVE_BORROWS = "Trwające";
    public final static String ENDED_BORROWS = "Zakończone";

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!(req.isUserInRole("ADMIN") || req.isUserInRole("USER"))) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public BorrowsView(DataService service, HttpServletRequest req) {
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
        form.setBorrow(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        Boolean searchIsActive = null;

        if (filterBorrowStatus.getValue() != null) {
            switch (filterBorrowStatus.getValue()) {
                case ACTIVE_BORROWS: {
                    searchIsActive = true;
                    break;
                }
                case ENDED_BORROWS: {
                    searchIsActive = false;
                    break;
                }
            }
        }

        Integer readerId = filterReader.getValue() != null ? filterReader.getValue().getId() : null;
        String bookCode = filterBookCode.getValue();
        Integer bookDefinitionId = filterBookDefinition.getValue() != null ? filterBookDefinition.getValue().getId() : null;
        Integer borrowEmployeeId = filterBorrowEmployee.getValue() != null ? filterBorrowEmployee.getValue().getId() : null;
        Integer giveBackEmployeeId = filterGiveBackEmployee.getValue() != null ? filterGiveBackEmployee.getValue().getId() : null;

        grid.setItems(service.findBorrows(
                searchIsActive,
                filterId.getValue(),
                readerId,
                bookCode,
                bookDefinitionId,
                borrowEmployeeId,
                giveBackEmployeeId
        ));
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
        form = new BorrowForm(service);
        form.setWidth("25em");

        form.addListener(BorrowForm.SaveEvent.class, this::saveBorrow);
        form.addListener(BorrowForm.DeleteEvent.class, this::deleteBorrow);
        form.addListener(BorrowForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveBorrow(BorrowForm.SaveEvent event) {
        // Save borrow
        Borrow borrow = (Borrow) event.getEntity(); // Without ID
        if (borrow.getBook().getBorrow() == null // If book is not borrowed
                || borrow.getBook().getBorrow().equals(borrow)) { // or user is editing existing borrow
            borrow = service.saveBorrow(borrow); // After saving - has ID
        } else {
            Notification.show(Messages.BOOK_ALREADY_BORROWED).addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        // Update borrow in borrowed book
        Book book = borrow.getBook();
        if (borrow.getBook().getBorrow() == null) { // If book is not borrowed
            book.setBorrow(borrow); // Book is now being borrowed
            service.saveBook(book);
        } else if (borrow.getBook().getBorrow().equals(borrow) // User is editing existing borrow
                && borrow.getGiveBackDate() != null) { // and user specified giveBackDate == took back the book
            book.setBorrow(null); // The book is no longer borrowed
            service.saveBook(book);
        }

        updateList();
        closeEditor();
    }

    private void deleteBorrow(BorrowForm.DeleteEvent event) {
        try {
            service.deleteBorrow((Borrow) event.getEntity());
            updateList();
            closeEditor();
        } catch (DataIntegrityViolationException e) {
            Notification.show(Messages.INTEGRITY_BORROW).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private Component getToolbar() {
        filterBorrowStatus.setItems(ALL_BORROWS, ACTIVE_BORROWS, ENDED_BORROWS);
        filterBorrowStatus.setValue(ALL_BORROWS);
        filterBorrowStatus.addValueChangeListener(e -> updateList());

        filterId.addValueChangeListener(e -> updateList());
        filterId.setValueChangeMode(ValueChangeMode.LAZY);
        filterId.setClearButtonVisible(true);

        filterReader.setItems(service.findAllReaders());
        filterReader.setItemLabelGenerator(Reader::toString);
        filterReader.setClearButtonVisible(true);
        filterReader.addValueChangeListener(e -> updateList());

        filterBookCode.setClearButtonVisible(true);
        filterBookCode.addValueChangeListener(e -> updateList());
        filterBookCode.setValueChangeMode(ValueChangeMode.LAZY);

        filterBookDefinition.setItems(service.findAllBookDefinitions());
        filterBookDefinition.setItemLabelGenerator(BookDefinition::toString);
        filterBookDefinition.setClearButtonVisible(true);
        filterBookDefinition.addValueChangeListener(e -> updateList());

        List<Employee> employees = service.findAllEmployees();

        filterBorrowEmployee.setItems(employees);
        filterBorrowEmployee.setItemLabelGenerator(Employee::toString);
        filterBorrowEmployee.setClearButtonVisible(true);
        filterBorrowEmployee.addValueChangeListener(e -> updateList());

        filterGiveBackEmployee.setItems(employees);
        filterGiveBackEmployee.setItemLabelGenerator(Employee::toString);
        filterGiveBackEmployee.setClearButtonVisible(true);
        filterGiveBackEmployee.addValueChangeListener(e -> updateList());

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addBorrowButton = new Button("Dodaj wypożyczenie");
        addBorrowButton.addClickListener(e -> addBorrow());

        HorizontalLayout filters1 = new HorizontalLayout(
                filterBorrowStatus,
                filterId,
                filterReader,
                filterBookCode,
                filterBookDefinition
        );
        HorizontalLayout filters2AndButtons = new HorizontalLayout(
                filterBorrowEmployee,
                filterGiveBackEmployee,
                clearFiltersButton,
                addBorrowButton
        );
        filters1.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        filters2AndButtons.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        VerticalLayout toolbar = new VerticalLayout(filters1, filters2AndButtons);
        toolbar.setSpacing(false);

        return toolbar;
    }

    private void clearFilters() {
        filterBorrowStatus.setValue(ALL_BORROWS);
        filterId.clear();
        filterReader.clear();
        filterBookCode.clear();
        filterBookDefinition.clear();
        filterBorrowEmployee.clear();
        filterGiveBackEmployee.clear();
    }

    private void addBorrow() {
        form.refreshLists();
        grid.asSingleSelect().clear();
        editBorrow(new Borrow());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Borrow::getId).setHeader("ID wypoż.").setSortable(true);
        grid.addColumn(borrow -> borrow.getReader().toString()).setHeader("Czytelnik").setSortable(true);
        grid.addColumn(borrow -> borrow.getBook().getBookCode()).setHeader("Kod książki").setSortable(true);
        grid.addColumn(borrow -> borrow.getBook().getBookDefinition().getTitle()).setHeader("Tytuł").setSortable(true);
        grid.addColumn(borrow -> borrow.getBorrowEmployee().toString()).setHeader("Pracownik wypożyczający").setSortable(true);
        grid.addColumn(borrow -> borrow.getGiveBackEmployee() != null ? borrow.getGiveBackEmployee().toString() : "").setHeader("Pracownik przyjmujący").setSortable(true);
        grid.addColumn(Borrow::getBorrowDate).setHeader("Data wypożyczenia").setSortable(true);
        grid.addColumn(borrow -> borrow.getGiveBackDate() != null ? borrow.getGiveBackDate() : "").setHeader("Data zwrotu").setSortable(true);
        grid.addColumn(Borrow::getNotes).setHeader("Uwagi").setSortable(false);

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editBorrow(e.getValue()));
    }

    private void editBorrow(Borrow borrow) {
        if (borrow == null) {
            closeEditor();
        } else {
            form.refreshLists();
            form.setBorrow(borrow);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
