package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    public DataService(AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       GenreRepository genreRepository,
                       PublisherRepository publisherRepository,
                       BookRepository bookRepository,
                       ReaderRepository readerRepository,
                       EmployeeRepository employeeRepository,
                       UserRepository userRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
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

    // ----- Books -----

    public List<Book> findBooks(String filterTitle,
                                Integer filterAuthorId,
                                Integer filterPublisherId,
                                Integer filterGenreId,
                                Integer filterCategoryId) {
        if ((filterTitle == null || filterTitle.isEmpty())
                && filterAuthorId == null
                && filterPublisherId == null
                && filterGenreId == null
                && filterCategoryId == null) {
            return bookRepository.findAll();
        } else {
            return bookRepository.search(filterTitle,
                    filterAuthorId,
                    filterPublisherId,
                    filterGenreId,
                    filterCategoryId);
        }
    }

    public long countBooks() {
        return bookRepository.count();
    }

    public void deleteBook(Book book) {
        bookRepository.delete(book);
    }

    public void saveBook(Book book) {
        if (book == null) {
            System.err.println("Book is null");
            return;
        }

        bookRepository.save(book);
    }

    // ----- Readers -----

    public List<Reader> findReaders(String filterName, String filterSurname) {
        if ((filterName == null || filterName.isEmpty()) && (filterSurname == null || filterSurname.isEmpty())) {
            return readerRepository.findAll();
        } else {
            return readerRepository.search(filterName, filterSurname);
        }
    }

    public long countReaders() {
        return readerRepository.count();
    }

    public void deleteReader(Reader reader) {
        readerRepository.delete(reader);
    }

    public void saveReader(Reader reader) {
        if (reader == null) {
            System.err.println("Reader is null");
            return;
        }

        readerRepository.save(reader);
    }

    // ----- Employees -----

    public List<Employee> findEmployees(String filterName, String filterSurname, String filterPosition) {
        if ((filterName == null || filterName.isEmpty()) && (filterSurname == null || filterSurname.isEmpty()) && (filterPosition == null || filterPosition.isEmpty())) {
            return employeeRepository.findAll();
        } else {
            return employeeRepository.search(filterName, filterSurname, filterPosition);
        }
    }

    public long countEmployees() {
        return employeeRepository.count();
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    public void saveEmployee(Employee employee) {
        if (employee == null) {
            System.err.println("Employee is null");
            return;
        }

        employeeRepository.save(employee);
    }

    // ----- Users -----

    public List<User> findUsers(String filterUsername, Integer filterEmployeeId, Boolean filterIsActive) {
        if ((filterUsername == null || filterUsername.isEmpty()) && filterEmployeeId == null && filterIsActive == null) {
            return userRepository.findAll();
        } else {
            return userRepository.search(filterUsername, filterEmployeeId, filterIsActive);
        }
    }

    public long countUsers() {
        return userRepository.count();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public void saveUser(User user) {
        if (user == null) {
            System.err.println("User is null");
            return;
        }

        userRepository.save(user);
    }
}
