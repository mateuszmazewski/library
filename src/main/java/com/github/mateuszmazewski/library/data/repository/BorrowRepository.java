package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BorrowRepository extends JpaRepository<Borrow, Integer> {

    @Query("select b from Borrow b " +
            "where (:searchIsActive is null or (:searchIsActive=true and b.giveBackDate is null) or (:searchIsActive=false and b.giveBackDate is not null))" +
            "and (:searchBorrowId is null or b.id=:searchBorrowId) " +
            "and (:searchReaderId is null or b.reader.id=:searchReaderId) " +
            "and lower(b.book.bookCode) like lower(concat('%', :searchBookCode, '%')) " +
            "and (:searchBookDefinitionId is null or b.book.bookDefinition.id=:searchBookDefinitionId) " +
            "and (:searchBorrowEmployeeId is null or b.borrowEmployee.id=:searchBorrowEmployeeId) " +
            "and (:searchGiveBackEmployeeId is null or b.giveBackEmployee.id=:searchGiveBackEmployeeId) ")
    List<Borrow> search(@Param("searchIsActive") Boolean searchIsActive,
                        @Param("searchBorrowId") Integer searchBorrowId,
                        @Param("searchReaderId") Integer searchReaderId,
                        @Param("searchBookCode") String searchBookCode,
                        @Param("searchBookDefinitionId") Integer searchBookDefinitionId,
                        @Param("searchBorrowEmployeeId") Integer searchBorrowEmployeeId,
                        @Param("searchGiveBackEmployeeId") Integer searchGiveBackEmployeeId);

    @Query("select b from Borrow b " +
            "where (:searchBookCode is null or b.book.bookCode=:searchBookCode) ")
    List<Borrow> searchByBookCode(@Param("searchBookCode") Integer searchBookCode);

}
