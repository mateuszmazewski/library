package com.github.mateuszmazewski.library.data.service;

import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DataService {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;
    private final GenreRepository genreRepository;
    private final PublisherRepository publisherRepository;
    private final BookDefinitionRepository bookDefinitionRepository;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final BorrowRepository borrowRepository;

    public DataService(AuthorRepository authorRepository,
                       CategoryRepository categoryRepository,
                       GenreRepository genreRepository,
                       PublisherRepository publisherRepository,
                       BookDefinitionRepository bookDefinitionRepository,
                       BookRepository bookRepository,
                       ReaderRepository readerRepository,
                       EmployeeRepository employeeRepository,
                       UserRepository userRepository,
                       BorrowRepository borrowRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.genreRepository = genreRepository;
        this.publisherRepository = publisherRepository;
        this.bookDefinitionRepository = bookDefinitionRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.borrowRepository = borrowRepository;
    }

    // ----- Authors -----

    public List<Author> findAuthors(String filterName, String filterSurname) {
        if ((filterName == null || filterName.isEmpty()) && (filterSurname == null || filterSurname.isEmpty())) {
            return authorRepository.findAll();
        } else {
            return authorRepository.search(filterName, filterSurname);
        }
    }

    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
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

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
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

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
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

    public List<Publisher> findAllPublishers() {
        return publisherRepository.findAll();
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

    // ----- Book definitions -----

    public List<BookDefinition> findBookDefinitions(String filterTitle,
                                                    Integer filterAuthorId,
                                                    Integer filterPublisherId,
                                                    Integer filterPublicationYear,
                                                    Integer filterGenreId,
                                                    Integer filterCategoryId,
                                                    String filterIsbn) {
        if ((filterTitle == null || filterTitle.isEmpty())
                && filterAuthorId == null
                && filterPublisherId == null
                && filterGenreId == null
                && filterCategoryId == null
                && filterPublicationYear == null
                && (filterIsbn == null || filterIsbn.isEmpty())) {
            return bookDefinitionRepository.findAll();
        } else {
            return bookDefinitionRepository.search(
                    filterTitle,
                    filterAuthorId,
                    filterPublisherId,
                    filterPublicationYear,
                    filterGenreId,
                    filterCategoryId,
                    filterIsbn);
        }
    }

    public List<BookDefinition> findAllBookDefinitions() {
        return bookDefinitionRepository.findAll();
    }

    public long countBookDefinitions() {
        return bookDefinitionRepository.count();
    }

    public void deleteBookDefinition(BookDefinition bookDefinition) {
        bookDefinitionRepository.delete(bookDefinition);
    }

    public void saveBookDefinition(BookDefinition bookDefinition) {
        if (bookDefinition == null) {
            System.err.println("BookDefinition is null");
            return;
        }

        bookDefinitionRepository.save(bookDefinition);
    }

    // ----- Books -----

    public List<Book> findBooks(Boolean filterIsBorrowed, String filterBookCode, Integer filterBookDefinitionId) {
        if (filterIsBorrowed == null && (filterBookCode == null || filterBookCode.isEmpty()) && filterBookDefinitionId == null) {
            return bookRepository.findAll();
        } else {
            return bookRepository.search(filterIsBorrowed, filterBookCode, filterBookDefinitionId);
        }
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> findBookByBookCode(String filterBookCode) {
        return bookRepository.searchByBookCode(filterBookCode);
    }

    public List<Book> findOnlyNotBorrowedBooks() {
        return bookRepository.searchOnlyNotBorrowed();
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

    public List<Reader> findReaders(Integer filterId, String filterName, String filterSurname) {
        if (filterId == null && (filterName == null || filterName.isEmpty()) && (filterSurname == null || filterSurname.isEmpty())) {
            return readerRepository.findAll();
        } else {
            return readerRepository.search(filterId, filterName, filterSurname);
        }
    }

    public List<Reader> findAllReaders() {
        return readerRepository.findAll();
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

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
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

    public User findUserByExactUsername(String filterUsername) {
        if (filterUsername == null || filterUsername.isEmpty()) {
            return null;
        } else {
            Optional<User> u = userRepository.findByUsername(filterUsername);
            return u.orElse(null);
        }
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
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

    // ----- Borrows -----

    public List<Borrow> findBorrows(Boolean filterIsActive, Integer filterBorrowId, Integer filterReaderId, String filterBookCode, Integer filterBookDefinitionId,
                                    Integer filterBorrowEmployeeId, Integer filterGiveBackEmployeeId) {
        if (filterIsActive == null
                && filterBorrowId == null
                && filterReaderId == null
                && (filterBookCode == null || filterBookCode.isEmpty())
                && filterBookDefinitionId == null
                && filterBorrowEmployeeId == null
                && filterGiveBackEmployeeId == null) {
            return borrowRepository.findAll();
        } else {
            return borrowRepository.search(filterIsActive, filterBorrowId, filterReaderId, filterBookCode, filterBookDefinitionId, filterBorrowEmployeeId, filterGiveBackEmployeeId);
        }
    }

    public List<Borrow> findAllBorrows() {
        return borrowRepository.findAll();
    }

    public long countBorrows() {
        return borrowRepository.count();
    }

    public void deleteBorrow(Borrow borrow) {
        borrowRepository.delete(borrow);
    }

    public Borrow saveBorrow(Borrow borrow) {
        if (borrow == null) {
            System.err.println("Borrow is null");
            return null;
        }

        return borrowRepository.save(borrow);
    }
}
