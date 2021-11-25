package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Employee;
import com.github.mateuszmazewski.library.data.entity.User;
import com.github.mateuszmazewski.library.data.service.DataService;
import com.github.mateuszmazewski.library.views.forms.UserForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.servlet.http.HttpServletRequest;

@PageTitle("Użytkownicy | Biblioteka")
@Route(value = "users", layout = MainLayout.class)
public class UsersView extends VerticalLayout implements BeforeEnterObserver {
    Grid<User> grid = new Grid<>(User.class);
    TextField filterUsername = new TextField("Nazwa użytkownika");
    ComboBox<Employee> filterEmployee = new ComboBox<>("Pracownik");
    ComboBox<String> filterActive = new ComboBox<>("Aktywny");
    UserForm form;
    private final DataService service;
    private final HttpServletRequest req;
    public final static String ALL_USERS = "Wszyscy";
    public final static String ACTIVE_USERS = "Aktywni";
    public final static String INACTIVE_USERS = "Nieaktywni";

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!req.isUserInRole("ADMIN")) {
            beforeEnterEvent.rerouteTo(AccessDeniedView.class);
        }
    }

    public UsersView(DataService service, HttpServletRequest req) {
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
        form.setUser(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void updateList() {
        Boolean searchIsActive = null;

        if (filterActive.getValue() != null) {
            switch (filterActive.getValue()) {
                case ACTIVE_USERS: {
                    searchIsActive = true;
                    break;
                }
                case INACTIVE_USERS: {
                    searchIsActive = false;
                    break;
                }
            }
        }

        if (filterEmployee.getValue() != null) {
            grid.setItems(service.findUsers(filterUsername.getValue(), filterEmployee.getValue().getId(), searchIsActive));
        } else {
            grid.setItems(service.findUsers(filterUsername.getValue(), null, searchIsActive));
        }
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
        form = new UserForm(service.findEmployees(null, null, null));
        form.setWidth("25em");

        form.addListener(UserForm.SaveEvent.class, this::saveUser);
        form.addListener(UserForm.DeleteEvent.class, this::deleteUser);
        form.addListener(UserForm.CloseEvent.class, e -> closeEditor());
    }

    private void saveUser(UserForm.SaveEvent event) {
        service.saveUser((User) event.getEntity());
        updateList();
        closeEditor();
    }

    private void deleteUser(UserForm.DeleteEvent event) {
        service.deleteUser((User) event.getEntity());
        updateList();
        closeEditor();
    }

    private Component getToolbar() {
        filterUsername.setClearButtonVisible(true);
        filterUsername.setValueChangeMode(ValueChangeMode.LAZY);
        filterUsername.addValueChangeListener(e -> updateList());

        filterEmployee.setItems(service.findEmployees(null, null, null));
        filterEmployee.setItemLabelGenerator(Employee::toString);
        filterEmployee.setClearButtonVisible(true);
        filterEmployee.addValueChangeListener(e -> updateList());

        filterActive.setItems(ALL_USERS, ACTIVE_USERS, INACTIVE_USERS);
        filterActive.setValue(ALL_USERS);
        filterActive.addValueChangeListener(e -> updateList());

        Button clearFiltersButton = new Button("Wyczyść filtry");
        clearFiltersButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        clearFiltersButton.addClickListener(e -> clearFilters());

        Button addUserButton = new Button("Dodaj użytkownika");
        addUserButton.addClickListener(e -> addUser());

        HorizontalLayout toolbar = new HorizontalLayout(filterUsername, filterEmployee, filterActive, clearFiltersButton, addUserButton);
        toolbar.setDefaultVerticalComponentAlignment(Alignment.BASELINE);

        return toolbar;
    }

    private void clearFilters() {
        filterUsername.clear();
        filterEmployee.clear();
        filterActive.setValue(ALL_USERS);
    }

    private void addUser() {
        grid.asSingleSelect().clear();
        editUser(new User());
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.removeAllColumns();
        grid.addColumn(User::getUsername).setHeader("Nazwa użytkownika").setSortable(true);
        grid.addColumn(
                new ComponentRenderer<>(
                        user -> {
                            Checkbox checkbox = new Checkbox();
                            checkbox.setReadOnly(true);
                            checkbox.setValue(user.isActive());
                            return checkbox;
                        }
                )
        ).setHeader("Aktywny");
        grid.addColumn(User::getEmployee).setHeader("Pracownik").setSortable(true);
        grid.addColumn(User::getRoles).setHeader("Role").setSortable(false);
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(e -> editUser(e.getValue()));
    }

    private void editUser(User user) {
        if (user == null) {
            closeEditor();
        } else {
            form.setUser(user);
            form.setVisible(true);
            addClassName("editing");
        }
    }
}
