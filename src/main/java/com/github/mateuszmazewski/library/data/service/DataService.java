package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Genre;
import com.github.mateuszmazewski.library.data.entity.Publisher;
import com.github.mateuszmazewski.library.data.repository.AuthorRepository;
import com.github.mateuszmazewski.library.data.repository.CategoryRepository;
import com.github.mateuszmazewski.library.data.repository.GenreRepository;
import com.github.mateuszmazewski.library.data.repository.PublisherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;

    public DataService(AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       GenreRepository genreRepository,
                       PublisherRepository publisherRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
    }

    // ----- Authors -----

    public List<Author> findAuthors(String filterName, String filterSurname) {
        if ((filterName == null || filterName.isEmpty()) && (filterSurname == null || filterSurname.isEmpty())) {
            return authorRepository.findAll();
        } else {
            return authorRepository.search(filterName, filterSurname);
        }
    }

    public long countAuthors() {
        return authorRepository.count();
    }

    public void deleteAuthor(Author author) {
        authorRepository.delete(author);
    }

    public void saveAuthor(Author author) {
        if (author == null) {
            System.err.println("Author is null");
            return;
        }

        authorRepository.save(author);
    }

    // ----- Categories -----

    public List<Category> findCategories(String filterName, Integer filterGenreId) {
        if ((filterName == null || filterName.isEmpty()) && (filterGenreId == null)) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.search(filterName, filterGenreId);
        }
    }

    public long countCategories() {
        return categoryRepository.count();
    }

    public void deleteCategory(Category category) {
        categoryRepository.delete(category);
    }

    public void saveCategory(Category category) {
        if (category == null) {
            System.err.println("Category is null");
            return;
        }

        categoryRepository.save(category);
    }

    // ----- Genres -----

    public List<Genre> findGenres(String filterName) {
        if (filterName == null || filterName.isEmpty()) {
            return genreRepository.findAll();
        } else {
            return genreRepository.search(filterName);
        }
    }

    public long countGenres() {
        return genreRepository.count();
    }

    public void deleteGenre(Genre genre) {
        genreRepository.delete(genre);
    }

    public void saveGenre(Genre genre) {
        if (genre == null) {
            System.err.println("Genre is null");
            return;
        }

        genreRepository.save(genre);
    }

    // ----- Publishers -----

    public List<Publisher> findPublishers(String filterName) {
        if (filterName == null || filterName.isEmpty()) {
            return publisherRepository.findAll();
        } else {
            return publisherRepository.search(filterName);
        }
    }

    public long countPublishers() {
        return publisherRepository.count();
    }

    public void deletePublisher(Publisher publisher) {
        publisherRepository.delete(publisher);
    }

    public void savePublisher(Publisher publisher) {
        if (publisher == null) {
            System.err.println("Publisher is null");
            return;
        }

        publisherRepository.save(publisher);
    }
}
