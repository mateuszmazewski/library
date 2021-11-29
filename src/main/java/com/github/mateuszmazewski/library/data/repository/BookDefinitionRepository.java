package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.BookDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookDefinitionRepository extends JpaRepository<BookDefinition, Integer> {

    @Query("select b from BookDefinition b " +
            "where lower(b.title) like lower(concat('%', :searchTitle, '%')) " +
            "and (:searchAuthorId is null or b.author.id=:searchAuthorId) " +
            "and (:searchPublisherId is null or b.publisher.id=:searchPublisherId) " +
            "and (:searchPublicationYear is null or b.publicationYear=:searchPublicationYear) " +
            "and (:searchGenreId is null or b.category.genre.id=:searchGenreId) " +
            "and (:searchCategoryId is null or b.category.id=:searchCategoryId) " +
            "and lower(b.isbn) like lower(concat('%', :searchIsbn, '%')) ")
    List<BookDefinition> search(@Param("searchTitle") String searchTitle,
                                @Param("searchAuthorId") Integer searchAuthorId,
                                @Param("searchPublisherId") Integer searchPublisherId,
                                @Param("searchPublicationYear") Integer searchPublicationYear,
                                @Param("searchGenreId") Integer searchGenreId,
                                @Param("searchCategoryId") Integer searchCategoryId,
                                @Param("searchIsbn") String searchIsbn);

}
