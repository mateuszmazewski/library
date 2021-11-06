package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Genre;
import com.github.mateuszmazewski.library.data.repository.AuthorRepository;
import com.github.mateuszmazewski.library.data.repository.CategoryRepository;
import com.github.mateuszmazewski.library.data.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;

    public DataService(AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       GenreRepository genreRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.genreRepository = genreRepository;
    }

    // ----- Authors -----

    public List<Author> findAuthors(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return authorRepository.findAll();
        } else {
            return authorRepository.search(filterText);
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

    public List<Category> findCategories(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return categoryRepository.findAll();
        } else {
            return categoryRepository.search(filterText);
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

    public List<Genre> findGenres(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return genreRepository.findAll();
        } else {
            return genreRepository.search(filterText);
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
}
