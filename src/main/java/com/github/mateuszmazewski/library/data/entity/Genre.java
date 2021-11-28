package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;
import com.github.mateuszmazewski.library.data.Messages;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Genre extends AbstractEntity {
    @NotBlank(message = Messages.NOT_EMPTY)
    private String name;

    public Genre() {

    }

    public Genre(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
