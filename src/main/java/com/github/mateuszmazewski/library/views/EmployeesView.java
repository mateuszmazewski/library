package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Employee;
import com.github.mateuszmazewski.library.data.service.DataService;
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

@PageTitle("Pracownicy | Biblioteka")
@Route(value = "employees", layout = MainLayout.class)
public class EmployeesView extends VerticalLayout implements BeforeEnterObserver {
    Grid<Employee> grid = new Grid<>(Employee.class);
    TextField filterName = new TextField("Imię");
    TextField filterSurname = new TextField("Nazwisko");
    TextField filterPosition = new TextField("Stanowisko");
    EmployeeForm form;
    private final DataService service;
    private final HttpServletRequest req;

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!req.isUserInRole("ADMIN")) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public EmployeesView(DataService service, HttpServletRequest req) {
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
        form.setEmployee(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        grid.setItems(service.findEmployees(filterName.getValue(), filterSurname.getValue(), filterPosition.getValue()));
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
        form = new EmployeeForm();
        form.setWidth("25em");

        form.addListener(EmployeeForm.SaveEvent.class, this::saveEmployee);
        form.addListener(EmployeeForm.DeleteEvent.class, this::deleteEmployee);
        form.addListener(EmployeeForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveEmployee(EmployeeForm.SaveEvent event) {
        service.saveEmployee((Employee) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        service.deleteEmployee((Employee) event.getEntity());
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

        filterPosition.setClearButtonVisible(true);
        filterPosition.setValueChangeMode(ValueChangeMode.LAZY);
        filterPosition.addValueChangeListener(e -> updateList());

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addEmployeeButton = new Button("Dodaj pracownika");
        addEmployeeButton.addClickListener(e -> addEmployee());

        HorizontalLayout toolbar = new HorizontalLayout(filterName, filterSurname, filterPosition, clearFiltersButton, addEmployeeButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterName.clear();
        filterSurname.clear();
        filterPosition.clear();
    }

    private void addEmployee() {
        grid.asSingleSelect().clear();
        editEmployee(new Employee());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(Employee::getName).setHeader("Imię").setSortable(true);
        grid.addColumn(Employee::getSurname).setHeader("Nazwisko").setSortable(true);
        grid.addColumn(Employee::getPosition).setHeader("Stanowisko").setSortable(true);
        grid.addColumn(Employee::getEmail).setHeader("E-mail").setSortable(false);
        grid.addColumn(Employee::getPhoneNumber).setHeader("Numer telefonu").setSortable(false);
        grid.addColumn(Employee::getBirthdate).setHeader("Data urodzenia").setSortable(false);
        grid.addColumn(Employee::getEmployedSinceDate).setHeader("Zatrudniony od").setSortable(true);
        grid.addColumn(Employee::getEmployedToDate).setHeader("Zatrudniony do").setSortable(true);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editEmployee(e.getValue()));
    }

    private void editEmployee(Employee employee) {
        if (employee == null) {
            closeEditor();
        } else {
            form.setEmployee(employee);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
