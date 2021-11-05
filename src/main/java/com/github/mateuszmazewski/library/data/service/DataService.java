package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.repository.AuthorRepository;
import com.github.mateuszmazewski.library.data.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public DataService(AuthorRepository authorRepository,
                       CategoryRepository categoryRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
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
}
