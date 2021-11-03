package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private AuthorRepository authorRepository;

    public DataService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> findAuthors(String filterText) {
        if(filterText == null || filterText.isEmpty()) {
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
        if(author == null) {
            System.err.println("Author is null");
            return;
        }

        authorRepository.save(author);
    }
}
