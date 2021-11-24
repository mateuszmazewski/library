package com.github.mateuszmazewski.library.data.repository;

import com.github.mateuszmazewski.library.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    @Query("select u from User u " +
            "where lower(u.username) like lower(concat('%', :searchUsername, '%')) " +
            "and (:searchEmployeeId is null or u.employee.id=:searchEmployeeId) " +
            "and (:searchIsActive is null or u.active=:searchIsActive)")
    List<User> search(@Param("searchUsername") String searchUsername, @Param("searchEmployeeId") Integer searchEmployeeId, @Param("searchIsActive") Boolean searchIsActive);
}