package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Integer> {

    @Query("select a from Author a " +
            "where lower(a.name) like lower(concat('%', :searchName, '%')) " +
            "and lower(a.surname) like lower(concat('%', :searchSurname, '%'))")
    List<Author> search(@Param("searchName") String searchName, @Param("searchSurname") String searchSurname);
}
