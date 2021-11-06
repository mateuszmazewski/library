package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PublisherRepository extends JpaRepository<Publisher, Integer> {

    @Query("select p from Publisher p " +
            "where lower(p.name) like lower(concat('%', :searchName, '%')) ")
    List<Publisher> search(@Param("searchName") String searchName);
}