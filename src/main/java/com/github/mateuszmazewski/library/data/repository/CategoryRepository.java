package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    @Query("select c from Category c " +
            "where lower(c.name) like lower(concat('%', :searchName, '%')) " +
            "and (:searchGenreId is null or c.genre.id=:searchGenreId)")
    List<Category> search(@Param("searchName") String searchName, @Param("searchGenreId") Integer searchGenreId);
}
