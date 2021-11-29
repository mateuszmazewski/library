package com.github.mateuszmazewski.library.data.entity;

import com.github.mateuszmazewski.library.data.AbstractEntity;
import com.github.mateuszmazewski.library.data.Messages;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class BookDefinition extends AbstractEntity {

    @NotBlank(message = Messages.NOT_EMPTY)
    private String title;

    @ManyToOne
    @NotNull(message = Messages.NOT_EMPTY)
    private Author author;

    @ManyToOne
    @NotNull(message = Messages.NOT_EMPTY)
    private Category category;

    @ManyToOne
    private Publisher publisher;

    private String isbn;
    private Integer publicationYear;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Integer getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(Integer publicationYear) {
        this.publicationYear = publicationYear;
    }

    @Override
    public String toString() {
        return title;
    }
}
