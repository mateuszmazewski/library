package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;
import com.github.mateuszmazewski.library.data.Messages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class User extends AbstractEntity {

    @NotBlank(message = Messages.NOT_EMPTY)
    @Column(unique = true)
    private String username;

    @NotBlank(message = Messages.NOT_EMPTY)
    private String password;

    @NotNull(message = Messages.NOT_EMPTY)
    private boolean active;

    @NotNull(message = Messages.NOT_EMPTY)
    @OneToOne
    private Employee employee;

    private String roles; // comma-separated roles e.g. ROLE_ADMIN,ROLE_USER

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String isActiveString() {
        return active ? "Aktywny" : "Nieaktywny";
    }
}
