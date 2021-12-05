package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReaderRepository extends JpaRepository<Reader, Integer> {

    @Query("select r from Reader r " +
            "where (:searchId is null or r.id=:searchId) " +
            "and lower(r.name) like lower(concat('%', :searchName, '%')) " +
            "and lower(r.surname) like lower(concat('%', :searchSurname, '%'))")
    List<Reader> search(@Param("searchId") Integer searchId,
                        @Param("searchName") String searchName,
                        @Param("searchSurname") String searchSurname);
}
