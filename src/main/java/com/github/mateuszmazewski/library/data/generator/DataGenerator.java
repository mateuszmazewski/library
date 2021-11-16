package com.github.mateuszmazewski.library.data.generator;

import com.github.mateuszmazewski.library.data.entity.*;
import com.github.mateuszmazewski.library.data.repository.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(AuthorRepository authorRepository,
                                      CategoryRepository categoryRepository,
                                      GenreRepository genreRepository,
                                      PublisherRepository publisherRepository,
                                      BookRepository bookRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());

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

            // ----- BOOKS -----

            Book witcher1 = new Book();
            witcher1.setTitle("Wiedźmin. Tom 1. Ostatnie życzenie");
            witcher1.setAuthor(sapkowski);
            witcher1.setPublisher(supernova);
            witcher1.setCategory(novel);

            Book witcher2 = new Book();
            witcher2.setTitle("Miecz przeznaczenia. Wiedźmin. Tom 2");
            witcher2.setAuthor(sapkowski);
            witcher2.setPublisher(supernova);
            witcher2.setCategory(novel);

            Book mrThaddeus = new Book();
            mrThaddeus.setTitle("Pan Tadeusz");
            mrThaddeus.setAuthor(mickiewicz);
            mrThaddeus.setPublisher(greg);
            mrThaddeus.setCategory(epos);

            Book teutonicKnights = new Book();
            teutonicKnights.setTitle("Krzyżacy");
            teutonicKnights.setAuthor(sienkiewicz);
            teutonicKnights.setPublisher(wsip);
            teutonicKnights.setCategory(novel);

            Book romeoAndJuliet = new Book();
            romeoAndJuliet.setTitle("Romeo i Julia");
            romeoAndJuliet.setAuthor(shakespeare);
            romeoAndJuliet.setPublisher(greg);
            romeoAndJuliet.setCategory(tragedy);

            bookRepository.save(witcher1);
            bookRepository.save(witcher2);
            bookRepository.save(mrThaddeus);
            bookRepository.save(teutonicKnights);
            bookRepository.save(romeoAndJuliet);
        };
    }

}
