package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select b from Book b " +
            "where (:searchIsBorrowed is null or (:searchIsBorrowed=true and b.borrow is not null) or (:searchIsBorrowed=false and b.borrow is null))" +
            "and lower(b.bookCode) like lower(concat('%', :searchBookCode, '%')) " +
            "and (:searchBookDefinitionId is null or b.bookDefinition.id=:searchBookDefinitionId)")
    List<Book> search(@Param("searchIsBorrowed") Boolean searchIsBorrowed,
                      @Param("searchBookCode") String searchBookCode,
                      @Param("searchBookDefinitionId") Integer searchBookDefinitionId);

    @Query("select b from Book b " +
            "where lower(b.bookCode) like lower(concat('%', :searchBookCode, '%')) ")
    List<Book> searchByBookCode(@Param("searchBookCode") String searchBookCode);

    @Query("select b from Book b " +
            "where b.borrow is null")
    List<Book> searchOnlyNotBorrowed();

}
