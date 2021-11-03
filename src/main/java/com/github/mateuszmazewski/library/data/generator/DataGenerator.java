package com.github.mateuszmazewski.library.data.generator;

import com.github.mateuszmazewski.library.data.entity.Author;
import com.github.mateuszmazewski.library.data.repository.AuthorRepository;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringComponent
public class DataGenerator {

    @Bean
    public CommandLineRunner loadData(AuthorRepository authorRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (authorRepository.count() != 0L) {
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
        };
    }

}
