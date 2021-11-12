package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("select b from Book b " +
            "where lower(b.title) like lower(concat('%', :searchTitle, '%')) " +
            "and (:searchAuthorId is null or b.author.id=:searchAuthorId) " +
            "and (:searchPublisherId is null or b.publisher.id=:searchPublisherId) " +
            "and (:searchGenreId is null or b.category.genre.id=:searchGenreId) " +
            "and (:searchCategoryId is null or b.category.id=:searchCategoryId) ")
    List<Book> search(@Param("searchTitle") String searchTitle,
                      @Param("searchAuthorId") Integer searchAuthorId,
                      @Param("searchPublisherId") Integer searchPublisherId,
                      @Param("searchGenreId") Integer searchGenreId,
                      @Param("searchCategoryId") Integer searchCategoryId);
}
