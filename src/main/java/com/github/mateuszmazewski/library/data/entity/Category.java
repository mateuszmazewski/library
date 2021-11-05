package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Category extends AbstractEntity {
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
