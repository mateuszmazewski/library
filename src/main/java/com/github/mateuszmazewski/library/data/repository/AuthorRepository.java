package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("select a from Author a " +
            "where lower(a.name) like lower(concat('%', :searchTerm, '%')) " +
            "or lower(a.surname) like lower(concat('%', :searchTerm, '%'))")
    List<Author> search(@Param("searchTerm") String searchTerm);
}
