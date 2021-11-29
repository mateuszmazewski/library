package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;
import com.github.mateuszmazewski.library.data.Messages;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Borrow extends AbstractEntity {

    @ManyToOne
    @NotNull(message = Messages.NOT_EMPTY)
    private Reader reader;

    @ManyToOne
    @NotNull(message = Messages.NOT_EMPTY)
    private Book book;

    @NotNull(message = Messages.NOT_EMPTY)
    private LocalDate borrowDate;

    private LocalDate giveBackDate;

    @ManyToOne
    @NotNull(message = Messages.NOT_EMPTY)
    private Employee borrowEmployee;

    @ManyToOne
    private Employee giveBackEmployee;

    private String notes;

    public Reader getReader() {
        return reader;
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getGiveBackDate() {
        return giveBackDate;
    }

    public void setGiveBackDate(LocalDate giveBackDate) {
        this.giveBackDate = giveBackDate;
    }

    public Employee getBorrowEmployee() {
        return borrowEmployee;
    }

    public void setBorrowEmployee(Employee borrowEmployee) {
        this.borrowEmployee = borrowEmployee;
    }

    public Employee getGiveBackEmployee() {
        return giveBackEmployee;
    }

    public void setGiveBackEmployee(Employee giveBackEmployee) {
        this.giveBackEmployee = giveBackEmployee;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
