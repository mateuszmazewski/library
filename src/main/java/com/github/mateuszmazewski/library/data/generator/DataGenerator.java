package com.github.mateuszmazewski.library.data.generator;

import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.repository.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(AuthorRepository authorRepository,
                                      CategoryRepository categoryRepository,
                                      GenreRepository genreRepository,
                                      PublisherRepository publisherRepository,
                                      BookDefinitionRepository bookDefinitionRepository,
                                      ReaderRepository readerRepository,
                                      EmployeeRepository employeeRepository,
                                      UserRepository userRepository,
                                      BookRepository bookRepository,
                                      BorrowRepository borrowRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());

            // ----- EMPLOYEES -----

            Employee boss = new Employee();
            boss.setName("Grzegorz");
            boss.setSurname("Brzęczyszczykiewicz");
            boss.setPosition("Dyrektor");
            boss.setBirthdate(LocalDate.of(1965, 2, 8));
            boss.setEmail("szef@gmail.com");
            boss.setPhoneNumber("917 538 943");
            boss.setEmployedSinceDate(LocalDate.of(2001, 6, 14));

            Employee librarian1 = new Employee();
            librarian1.setName("Jakub");
            librarian1.setSurname("Puchatek");
            librarian1.setPosition("Bibliotekarz");
            librarian1.setBirthdate(LocalDate.of(1988, 12, 18));
            librarian1.setEmail("jpuchatek@o2.pl");
            librarian1.setPhoneNumber("846 759 301");
            librarian1.setEmployedSinceDate(LocalDate.of(2020, 8, 11));

            Employee librarian2 = new Employee();
            librarian2.setName("Merida");
            librarian2.setSurname("Waleczna");
            librarian2.setPosition("Bibliotekarz");
            librarian2.setBirthdate(LocalDate.of(1992, 5, 6));
            librarian2.setEmail("meridawaleczna@gmail.com");
            librarian2.setPhoneNumber("105 206 834");
            librarian2.setEmployedSinceDate(LocalDate.of(2019, 3, 22));
            librarian2.setEmployedSinceDate(LocalDate.of(2021, 11, 24));

            employeeRepository.save(boss);
            employeeRepository.save(librarian1);
            employeeRepository.save(librarian2);

            // ----- USERS -----

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles("ROLE_ADMIN,ROLE_USER");
            admin.setActive(true);
            admin.setEmployee(boss);

            User userLibrarian1 = new User();
            userLibrarian1.setUsername("kubus");
            userLibrarian1.setPassword(passwordEncoder.encode("puchatek"));
            userLibrarian1.setRoles("ROLE_USER");
            userLibrarian1.setActive(true);
            userLibrarian1.setEmployee(librarian1);

            User userLibrarian2 = new User();
            userLibrarian2.setUsername("merida");
            userLibrarian2.setPassword(passwordEncoder.encode("waleczna"));
            userLibrarian2.setRoles("ROLE_USER");
            userLibrarian2.setActive(false);
            userLibrarian2.setEmployee(librarian2);

            userRepository.save(admin);
            userRepository.save(userLibrarian1);
            userRepository.save(userLibrarian2);

            // ----- GENRES -----

            Genre epic = new Genre("Epika");
            Genre poetry = new Genre("Liryka");
            Genre drama = new Genre("Dramat");

            genreRepository.save(epic);
            genreRepository.save(poetry);
            genreRepository.save(drama);

            if (authorRepository.count() != 0L || categoryRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }

            logger.info("Generating demo data");

            // ----- AUTHORS -----

            Author mickiewicz = new Author();
            mickiewicz.setName("Adam");
            mickiewicz.setSurname("Mickiewicz");

            Author sienkiewicz = new Author();
            sienkiewicz.setName("Henryk");
            sienkiewicz.setSurname("Sienkiewicz");

            Author sapkowski = new Author();
            sapkowski.setName("Andrzej");
            sapkowski.setSurname("Sapkowski");

            Author shakespeare = new Author();
            shakespeare.setName("William");
            shakespeare.setSurname("Shakespeare");

            authorRepository.save(mickiewicz);
            authorRepository.save(sienkiewicz);
            authorRepository.save(sapkowski);
            authorRepository.save(shakespeare);

            // ----- CATEGORIES -----

            Category novel = new Category();
            novel.setGenre(epic);
            novel.setName("Powieść");

            Category tale = new Category();
            tale.setGenre(epic);
            tale.setName("Opowiadanie");

            Category epigram = new Category();
            epigram.setGenre(poetry);
            epigram.setName("Fraszka");

            Category epos = new Category();
            epos.setGenre(epic);
            epos.setName("Epos");

            Category tragedy = new Category();
            tragedy.setGenre(drama);
            tragedy.setName("Tragedia");

            Category comedy = new Category();
            comedy.setGenre(drama);
            comedy.setName("Komedia");

            categoryRepository.save(novel);
            categoryRepository.save(tale);
            categoryRepository.save(epigram);
            categoryRepository.save(epos);
            categoryRepository.save(tragedy);
            categoryRepository.save(comedy);

            // ----- PUBLISHERS -----

            Publisher wsip = new Publisher();
            wsip.setName("WSiP - Wydawnictwa Szkolne i Pedagogiczne");
            wsip.setEmail("wsip@wsip.com.pl");
            wsip.setPhoneNumber("801 220 555");

            Publisher supernova = new Publisher();
            supernova.setName("superNOWA");
            supernova.setEmail("redakcja@supernowa.pl");
            supernova.setPhoneNumber("22 825 61 12");

            Publisher greg = new Publisher();
            greg.setName("Wydawnictwo Greg");
            greg.setEmail("greg@greg.pl");
            greg.setPhoneNumber("12 680 15 50");

            publisherRepository.save(wsip);
            publisherRepository.save(supernova);
            publisherRepository.save(greg);

            // ----- BOOK DEFINITIONS -----

            BookDefinition witcher1 = new BookDefinition();
            witcher1.setTitle("Wiedźmin. Tom 1. Ostatnie życzenie");
            witcher1.setAuthor(sapkowski);
            witcher1.setPublisher(supernova);
            witcher1.setPublicationYear(2014);
            witcher1.setCategory(novel);
            witcher1.setIsbn("9780316029186");

            BookDefinition witcher2 = new BookDefinition();
            witcher2.setTitle("Miecz przeznaczenia. Wiedźmin. Tom 2");
            witcher2.setAuthor(sapkowski);
            witcher2.setPublisher(supernova);
            witcher2.setPublicationYear(2019);
            witcher2.setCategory(novel);
            witcher2.setIsbn("9780316389709");

            BookDefinition mrThaddeus = new BookDefinition();
            mrThaddeus.setTitle("Pan Tadeusz");
            mrThaddeus.setAuthor(mickiewicz);
            mrThaddeus.setPublisher(greg);
            mrThaddeus.setPublicationYear(2021);
            mrThaddeus.setCategory(epos);
            mrThaddeus.setIsbn("9788373271920");

            BookDefinition teutonicKnights = new BookDefinition();
            teutonicKnights.setTitle("Krzyżacy");
            teutonicKnights.setAuthor(sienkiewicz);
            teutonicKnights.setPublisher(wsip);
            teutonicKnights.setPublicationYear(2018);
            teutonicKnights.setCategory(novel);
            teutonicKnights.setIsbn("9788373271821");

            BookDefinition romeoAndJuliet = new BookDefinition();
            romeoAndJuliet.setTitle("Romeo i Julia");
            romeoAndJuliet.setAuthor(shakespeare);
            romeoAndJuliet.setPublisher(greg);
            romeoAndJuliet.setPublicationYear(2021);
            romeoAndJuliet.setCategory(tragedy);
            romeoAndJuliet.setIsbn("9788373270282");


            bookDefinitionRepository.save(witcher1);
            bookDefinitionRepository.save(witcher2);
            bookDefinitionRepository.save(mrThaddeus);
            bookDefinitionRepository.save(teutonicKnights);
            bookDefinitionRepository.save(romeoAndJuliet);

            // ----- BOOKS -----

            Book book1 = new Book();
            book1.setBookCode("E-00284");
            book1.setBookDefinition(witcher1);
            book1.setNotes("Porwana okładka na rogach");

            Book book2 = new Book();
            book2.setBookCode("E-00072");
            book2.setBookDefinition(witcher1);
            book2.setNotes("Jak nowa");

            Book book3 = new Book();
            book3.setBookCode("E-00121");
            book3.setBookDefinition(teutonicKnights);
            book3.setNotes("Porwana okładka na rogach");

            Book book4 = new Book();
            book4.setBookCode("E-00090");
            book4.setBookDefinition(teutonicKnights);

            Book book5 = new Book();
            book5.setBookCode("D-00001");
            book5.setBookDefinition(romeoAndJuliet);

            Book book6 = new Book();
            book6.setBookCode("E-00736");
            book6.setBookDefinition(mrThaddeus);

            bookRepository.save(book1);
            bookRepository.save(book2);
            bookRepository.save(book3);
            bookRepository.save(book4);
            bookRepository.save(book5);
            bookRepository.save(book6);

            // ----- READERS -----

            Reader kowalski = new Reader();
            kowalski.setName("Jan");
            kowalski.setSurname("Kowalski");
            kowalski.setEmail("jan.kowalski@wp.pl");
            kowalski.setPhoneNumber("927 584 173");
            kowalski.setBirthdate(LocalDate.of(1991, 10, 15));

            Reader nowak = new Reader();
            nowak.setName("Paweł");
            nowak.setSurname("Nowak");
            nowak.setEmail("pawelnowak@gmail.com");
            nowak.setPhoneNumber("583 559 210");
            nowak.setBirthdate(LocalDate.of(2000, 5, 30));

            Reader ostrowski = new Reader();
            ostrowski.setName("Marek");
            ostrowski.setSurname("Ostrowski");
            ostrowski.setEmail("marek.ostrowski@o2.pl");
            ostrowski.setPhoneNumber("199 373 271");
            ostrowski.setBirthdate(LocalDate.of(1999, 1, 5));

            readerRepository.save(kowalski);
            readerRepository.save(nowak);
            readerRepository.save(ostrowski);

            // ----- BORROWS -----

            Borrow borrow1 = new Borrow();
            borrow1.setBook(book1);
            borrow1.setBorrowDate(LocalDate.of(2021, 10, 27));
            borrow1.setBorrowEmployee(librarian1);
            borrow1.setGiveBackDate(LocalDate.of(2021, 11, 10));
            borrow1.setGiveBackEmployee(librarian1);
            borrow1.setReader(ostrowski);

            Borrow borrow2 = new Borrow();
            borrow2.setBook(book2);
            borrow2.setBorrowDate(LocalDate.of(2021, 12, 2));
            borrow2.setBorrowEmployee(librarian2);
            borrow2.setReader(kowalski);

            Borrow borrow3 = new Borrow();
            borrow3.setBook(book3);
            borrow3.setBorrowDate(LocalDate.of(2021, 11, 8));
            borrow3.setBorrowEmployee(boss);
            borrow3.setGiveBackDate(LocalDate.of(2021, 11, 19));
            borrow3.setGiveBackEmployee(librarian1);
            borrow3.setReader(nowak);

            Borrow borrow4 = new Borrow();
            borrow4.setBook(book4);
            borrow4.setBorrowDate(LocalDate.of(2021, 12, 8));
            borrow4.setBorrowEmployee(librarian1);
            borrow4.setReader(nowak);

            Borrow borrow5 = new Borrow();
            borrow5.setBook(book3);
            borrow5.setBorrowDate(LocalDate.of(2021, 11, 23));
            borrow5.setBorrowEmployee(librarian2);
            borrow5.setGiveBackDate(LocalDate.of(2021, 11, 30));
            borrow5.setGiveBackEmployee(boss);
            borrow5.setReader(ostrowski);

            Borrow borrow6 = new Borrow();
            borrow6.setBook(book1);
            borrow6.setBorrowDate(LocalDate.of(2021, 11, 13));
            borrow6.setBorrowEmployee(boss);
            borrow6.setReader(kowalski);

            Borrow borrow7 = new Borrow();
            borrow7.setBook(book5);
            borrow7.setBorrowDate(LocalDate.of(2021, 12, 1));
            borrow7.setBorrowEmployee(boss);
            borrow7.setGiveBackDate(LocalDate.of(2021, 12, 3));
            borrow7.setGiveBackEmployee(librarian2);
            borrow7.setReader(kowalski);

            borrowRepository.save(borrow1);
            borrowRepository.save(borrow2);
            borrowRepository.save(borrow3);
            borrowRepository.save(borrow4);
            borrowRepository.save(borrow5);
            borrowRepository.save(borrow6);
            borrowRepository.save(borrow7);

            book2.setBorrow(borrow2);
            book4.setBorrow(borrow4);
            book1.setBorrow(borrow6);

            bookRepository.save(book2);
            bookRepository.save(book4);
            bookRepository.save(book1);
        };
    }

}
