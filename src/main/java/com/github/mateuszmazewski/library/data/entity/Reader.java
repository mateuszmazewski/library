package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;
import com.github.mateuszmazewski.library.data.Messages;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Reader extends AbstractEntity {
    @NotBlank(message = Messages.NOT_EMPTY)
    private String name;

    @NotBlank(message = Messages.NOT_EMPTY)
    private String surname;

    @Email
    @NotBlank(message = Messages.NOT_EMPTY)
    private String email;

    @NotBlank(message = Messages.NOT_EMPTY)
    private String phoneNumber;

    @NotNull(message = Messages.NOT_EMPTY)
    private LocalDate birthdate;

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

    @Override
    public String toString() {
        return name + " " + surname;
    }

}