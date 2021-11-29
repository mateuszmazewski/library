package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;
import com.github.mateuszmazewski.library.data.Messages;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Book extends AbstractEntity {

    @NotBlank(message = Messages.NOT_EMPTY)
    @Column(unique = true)
    private String bookCode;

    @ManyToOne
    @NotNull(message = Messages.NOT_EMPTY)
    private BookDefinition bookDefinition;

    @OneToOne
    private Borrow borrow;

    private String notes;

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBookCode() {
        return bookCode;
    }

    public void setBookCode(String bookCode) {
        this.bookCode = bookCode;
    }

    public BookDefinition getBookDefinition() {
        return bookDefinition;
    }

    public void setBookDefinition(BookDefinition bookDefinition) {
        this.bookDefinition = bookDefinition;
    }

    public String getNotes() {
        return notes;
    }

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    @Override
    public String toString() {
        return "[" + bookCode + "] " + bookDefinition.getTitle();
    }
}
