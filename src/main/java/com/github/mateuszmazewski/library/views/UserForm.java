package com.github.mateuszmazewski.library.views;

import com.github.mateuszmazewski.library.data.entity.Employee;
import com.github.mateuszmazewski.library.data.entity.User;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

public class UserForm extends EntityForm {
    Binder<User> binder = new BeanValidationBinder<>(User.class);
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    TextField username = new TextField("Nazwa użytkownika");
    PasswordField rawPassword = new PasswordField("Hasło");
    Checkbox active = new Checkbox("Aktywny");
    ComboBox<Employee> employee = new ComboBox<>("Pracownik");
    MultiSelectListBox<String> rolesList = new MultiSelectListBox<>();
    TextField roles = new TextField(); // definition only for binder to bind properly
    private User user;

    public UserForm(List<Employee> employees) {
        super();
        binder.bindInstanceFields(this);

        rawPassword.setHelperText("Jeśli użytkownik ma już ustawione hasło i nie chcesz go zmieniać, pozostaw to pole puste");

        employee.setItems(employees);
        employee.setItemLabelGenerator(Employee::toString);

        rolesList.setItems("ROLE_USER", "ROLE_ADMIN");
        Label rolesLabel = new Label("Role użytkownika");

        add(username, rawPassword, employee, active, rolesLabel, rolesList, createButtonLayout());
        saveButton.addClickListener(e -> validateAndSave());
        deleteButton.addClickListener(e -> fireEvent(new DeleteEvent(this, user)));
        cancelButton.addClickListener(e -> fireEvent(new CloseEvent(this)));
    }

    public void setUser(User user) {
        this.user = user;
        binder.readBean(user);
        adjustRolesList(user);
    }

    private void adjustRolesList(User user) {
        if (user != null && user.getRoles() != null) {
            if (user.getRoles().contains("ROLE_ADMIN")) {
                rolesList.select("ROLE_ADMIN");
            } else {
                rolesList.deselect("ROLE_ADMIN");
            }
            if (user.getRoles().contains("ROLE_USER")) {
                rolesList.select("ROLE_USER");
            } else {
                rolesList.deselect("ROLE_USER");
            }
        } else {
            rolesList.deselectAll();
        }
    }

    private void validateAndSave() {
        try {
            StringBuilder sb = new StringBuilder();
            for (String role : rolesList.getSelectedItems()) {
                sb.append(role).append(",");
            }
            roles.setValue(sb.toString());

            //If password is not set, then it has to be set - otherwise changing it is optional
            if (user.getPassword() == null && (rawPassword.getValue() == null || rawPassword.getValue().isEmpty())) {
                Notification.show("Hasło nie może być puste").addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            binder.writeBean(user);
            if (user.getPassword() == null && (rawPassword.getValue() == null || rawPassword.getValue().isEmpty())) {
                return;
            }
            if (rawPassword.getValue() != null && !rawPassword.getValue().isEmpty()) {
                user.setPassword(passwordEncoder.encode(rawPassword.getValue()));
            }
            fireEvent(new SaveEvent(this, user));
            rawPassword.clear();
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }
}
