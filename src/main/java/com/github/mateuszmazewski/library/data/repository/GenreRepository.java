package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Integer> {

    @Query("select g from Genre g " +
            "where lower(g.name) like lower(concat('%', :searchName, '%')) ")
    List<Genre> search(@Param("searchName") String searchName);
}