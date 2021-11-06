package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Category extends AbstractEntity {
    @NotBlank
    private String name;

    @ManyToOne
    @NotNull
    private Genre genre;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return name;
    }

}
