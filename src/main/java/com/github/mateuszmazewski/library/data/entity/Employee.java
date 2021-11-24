package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Employee extends AbstractEntity {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String position;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private LocalDate birthdate;

    @NotNull
    private LocalDate employedSinceDate;

    private LocalDate employedToDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public LocalDate getEmployedSinceDate() {
        return employedSinceDate;
    }

    public void setEmployedSinceDate(LocalDate employedSinceDate) {
        this.employedSinceDate = employedSinceDate;
    }

    public LocalDate getEmployedToDate() {
        return employedToDate;
    }

    public void setEmployedToDate(LocalDate employedToDate) {
        this.employedToDate = employedToDate;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
