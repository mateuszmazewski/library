package com.github.mateuszmazewski.library.data.generator;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.entity.Category;
import com.github.mateuszmazewski.library.data.entity.Genre;
import com.github.mateuszmazewski.library.data.repository.AuthorRepository;
import com.github.mateuszmazewski.library.data.repository.CategoryRepository;
import com.github.mateuszmazewski.library.data.repository.GenreRepository;
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
                                      GenreRepository genreRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());

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

            Author a1 = new Author();
            a1.setName("Adam");
            a1.setSurname("Mickiewicz");

            Author a2 = new Author();
            a2.setName("Henryk");
            a2.setSurname("Sienkiewicz");

            Author a3 = new Author();
            a3.setName("Andrzej");
            a3.setSurname("Sapkowski");

            authorRepository.save(a1);
            authorRepository.save(a2);
            authorRepository.save(a3);

            Category c1 = new Category();
            c1.setGenre(epic);
            c1.setName("Powieść");

            Category c2 = new Category();
            c2.setGenre(epic);
            c2.setName("Opowiadanie");

            Category c3 = new Category();
            c3.setGenre(poetry);
            c3.setName("Fraszka");

            categoryRepository.save(c1);
            categoryRepository.save(c2);
            categoryRepository.save(c3);
        };
    }

}
